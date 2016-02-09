package com.jiggie.android.activity.chat;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.component.SimpleTextWatcher;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ChatAdapter;
import com.jiggie.android.component.database.ChatTable;
import com.jiggie.android.component.service.ChatSendService;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.ChatManager;
import com.jiggie.android.model.Chat;
import com.jiggie.android.model.ChatResponseModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Conversation;
import com.jiggie.android.model.ExceptionModel;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.jiggie.android.model.LoginModel;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by rangg on 21/12/2015.
 */
public class ChatActivity extends ToolbarActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    public static final int RESULT_REPLIED = 10000;
    public static final int RESULT_BLOCKED = 10001;
    public static final int RESULT_CLEARED = 10002;

    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.recycler) RecyclerView recyclerView;
    @Bind(R.id.txtMessage) EditText txtMessage;
    @Bind(R.id.viewFailed) View failedView;
    @Bind(R.id.btnSend) Button btnSend;
    @Bind(R.id.viewChat) View viewChat;

    private String lastMessageDate = "";
    private ChatAdapter adapter;
    private boolean isChecking;
    private Handler handler;
    private boolean loaded;
    private String toName;
    private String toId;

    boolean changed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_chat);

        final Intent intent = super.getIntent();
        final String profileImage = intent.getStringExtra(Conversation.FIELD_PROFILE_IMAGE);
        this.toName = intent.getStringExtra(Conversation.FIELD_FROM_NAME);
        this.toId = intent.getStringExtra(Conversation.FIELD_FACEBOOK_ID);

        super.bindView();
        super.setToolbarTitle(this.toName, true);

        EventBus.getDefault().register(this);

        this.btnSend.setEnabled(false);
        this.handler = new Handler();
        this.viewChat.setVisibility(View.GONE);
        this.failedView.setVisibility(View.GONE);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter = new ChatAdapter(this, profileImage));
        this.recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.txtMessage.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                btnSend.setEnabled(s.toString().trim().length() > 0);
            }
        });

        super.registerReceiver(this.notificationReceived, new IntentFilter(super.getString(R.string.broadcast_notification)));
    }

    @Override
    public void onGlobalLayout() {
        this.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        this.loadData();
    }

    @OnClick(R.id.btnSend)
    @SuppressWarnings("unused")
    void btnSendOnClick() {
        final Chat chat = new Chat();
        final LoginModel loginModel = AccountManager.loadLogin();

        chat.setFromId(AccessToken.getCurrentAccessToken().getUserId());
        chat.setCreatedAt(Common.ISO8601_DATE_FORMAT_UTC.format(new Date()));
        chat.setMessage(this.txtMessage.getText().toString().trim());
        chat.setFromName(loginModel.getUser_first_name());
        chat.setToId(this.toId);
        chat.setFromYou(true);

        this.adapter.add(chat);
        this.txtMessage.setText(null);
        this.adapter.notifyDataSetChanged();
        this.recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        final Intent resultIntent = new Intent();
        resultIntent.putExtra(Conversation.FIELD_FACEBOOK_ID, toId);
        resultIntent.putExtra(Conversation.FIELD_LAST_UPDATED, chat.getCreatedAt());

        super.setResult(RESULT_REPLIED, resultIntent);

        VolleyHandler.getInstance().createVolleyRequest("messages/add", chat, new VolleyRequestListener<Void, JSONObject>() {
            @Override
            public Void onResponseAsync(JSONObject jsonObject) { return null; }
            @Override
            public void onResponseCompleted(Void value) { }

            @Override
            public void onErrorResponse(VolleyError error) {
                ChatTable.addUnprocessed(App.getInstance().getDatabase(), chat);
                ChatSendService.registerSchedule();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.loaded) {
            super.getMenuInflater().inflate(R.menu.menu_chat, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            final Intent intent = new Intent(this, ProfileDetailActivity.class);
            intent.putExtra(Common.FIELD_FACEBOOK_ID, toId);
            super.startActivity(intent);
        } else if (item.getItemId() == R.id.action_block) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.confirmation)
                    .setTitle(super.getString(R.string.user_chat_block, this.toName))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            blockUser();
                        }
                    }).show();
        } else if (item.getItemId() == R.id.action_clear) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.confirmation)
                    .setTitle(super.getString(R.string.clear_conversation))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearConversation();
                            dialog.dismiss();
                        }
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void blockUser() {
        final ProgressDialog dialog = App.showProgressDialog(this);
        final String url = String.format("blockuserwithfbid?fromId=%s&toId=%s", AccessToken.getCurrentAccessToken().getUserId(), this.toId);
        VolleyHandler.getInstance().createVolleyRequest(url, new VolleyRequestListener<Boolean, JSONObject>() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isActive())
                    Toast.makeText(ChatActivity.this, App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public Boolean onResponseAsync(JSONObject jsonArray) { return jsonArray.optBoolean("success", false); }

            @Override
            public void onResponseCompleted(Boolean value) {
                if (value)
                    App.getInstance().trackMixPanelEvent("Block User");
                if ((isActive()) && (!value)) {
                    Toast.makeText(ChatActivity.this, getString(R.string.block_failed, toName), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else if (isActive()) {
                    setResult(RESULT_BLOCKED, new Intent().putExtra(Conversation.FIELD_FACEBOOK_ID, toId));
                    dialog.dismiss();
                    onBackPressed();
                }
            }
        });
    }

    private void clearConversation() {
        final ProgressDialog dialog = App.showProgressDialog(this);
        final String url = String.format("deletemessageswithfbid?fromId=%s&toId=%s", AccessToken.getCurrentAccessToken().getUserId(), this.toId);
        VolleyHandler.getInstance().createVolleyRequest(url, new VolleyRequestListener<Boolean, JSONObject>() {
            @Override
            public Boolean onResponseAsync(JSONObject jsonObject) { return jsonObject.optBoolean("success"); }
            @Override
            public void onResponseCompleted(Boolean value) {
                if (value)
                    App.getInstance().trackMixPanelEvent("Delete Messages");
                if ((isActive()) && (!value)) {
                    Toast.makeText(ChatActivity.this, getString(R.string.clear_conversation_failed), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else if (isActive()) {
                    final Intent resultIntent = new Intent();
                    resultIntent.putExtra(Conversation.FIELD_LAST_UPDATED, Common.ISO8601_DATE_FORMAT_UTC.format(new Date()));
                    resultIntent.putExtra(Conversation.FIELD_FACEBOOK_ID, toId);
                    setResult(RESULT_CLEARED, resultIntent);

                    lastMessageDate = "";
                    adapter.clear(true);
                    dialog.dismiss();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isActive()) {
                    Toast.makeText(ChatActivity.this, App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    private BroadcastReceiver notificationReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.removeCallbacks(checkNewMessageRunnable);
            handler.postDelayed(checkNewMessageRunnable, getResources().getInteger(R.integer.chat_notif_delay));
        }
    };

    private Runnable checkNewMessageRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isChecking)
                checkNewMessages();
            isChecking = true;
        }
    };

    @OnClick(R.id.btnRetry)
    void loadData() {
        this.failedView.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.VISIBLE);

        ChatManager.loaderChatConversations(AccessToken.getCurrentAccessToken().getUserId(), toId, ChatManager.FROM_LOAD);
    }

    private void checkNewMessages() {
        this.isChecking = true;

        changed = false;
        ChatManager.loaderChatConversations(AccessToken.getCurrentAccessToken().getUserId(), toId, ChatManager.FROM_CHECK_NEW);
    }

    public void onEvent(ChatResponseModel message){

        if(message.getFromFunction().equals(ChatManager.FROM_LOAD)){
            final List<Chat> failedItems = ChatTable.getUnProcessedItems(App.getInstance().getDatabase(), toId);
            final int length = message.getData().getMessages() == null ? 0 : message.getData().getMessages().size();
            final int failedLength = failedItems.size();

            adapter.clear();
            for (int i = 0; i < length; i++) {
                final Chat chat = new Chat(message.getData().getMessages().get(i), message.getData().getFromId());
                if ((!chat.isFromYou()) && (lastMessageDate.compareTo(chat.getCreatedAt()) < 0))
                    lastMessageDate = chat.getCreatedAt();
                adapter.add(chat);
            }

            for (int i = 0; i < failedLength; i++) {
                final Chat failedItem = failedItems.get(i);
                failedItem.setCreatedAt(Common.ISO8601_DATE_FORMAT.format(new Date()));
                failedItem.setFromYou(true);
                adapter.add(failedItem);
            }

            if (isActive()) {
                loaded = true;
                invalidateOptionsMenu();
                adapter.notifyDataSetChanged();
                viewChat.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                setResult(RESULT_OK, new Intent().putExtra(Conversation.FIELD_FACEBOOK_ID, toId));
            }
        }else if(message.getFromFunction().equals(ChatManager.FROM_CHECK_NEW)){
            final int length = message.getData().getMessages() == null ? 0 : message.getData().getMessages().size();

            for (int i = 0; i < length; i++) {
                final Chat chat = new Chat(message.getData().getMessages().get(i), message.getData().getFromId());
                if ((!chat.isFromYou()) && (lastMessageDate.compareTo(chat.getCreatedAt()) < 0)) {
                    adapter.add(chat);
                    changed = true;
                }
            }

            if ((changed) && (isActive())) {
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
            isChecking = false;
        }

    }

    public void onEvent(ExceptionModel message){
        if (isActive()) {
            Toast.makeText(ChatActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
            failedView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        isChecking = false;
    }

    @Override
    protected void onDestroy() {
        super.unregisterReceiver(this.notificationReceived);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
