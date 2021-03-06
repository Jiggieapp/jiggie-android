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
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.component.SimpleTextWatcher;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ChatAdapter;
import com.jiggie.android.component.database.ChatTable;
import com.jiggie.android.component.service.ChatSendService;
import com.jiggie.android.fragment.ChatTabFragment;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.ChatManager;
import com.jiggie.android.model.Chat;
import com.jiggie.android.model.ChatActionModel;
import com.jiggie.android.model.ChatAddModel;
import com.jiggie.android.model.ChatResponseModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Conversation;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.LoginModel;

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
    Chat chat;
    ProgressDialog dialog;

    public static final String TAG = ChatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_chat);

        App.runningActivity = this;

        final Intent intent = super.getIntent();
        init(intent);

        /*getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    */
    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        Utils.d(TAG, "on new intent");
        init(intent);
    }*/

    Animation makeInAnimation, makeOutAnimation;

    private void init(Intent intent)
    {

        final String profileImage = intent.getStringExtra(Conversation.FIELD_PROFILE_IMAGE);
        final String eventNae = intent.getStringExtra(Conversation.FIELD_EVENT_NAME);
        this.toName = intent.getStringExtra(Conversation.FIELD_FROM_NAME);
        this.toId = intent.getStringExtra(Conversation.FIELD_FACEBOOK_ID);

        App.getInstance().setIdChatActive(toId);

        super.bindView();
        super.setToolbarTitle(this.toName, true);

        EventBus.getDefault().register(this);

        this.btnSend.setEnabled(false);
        this.handler = new Handler();
        this.viewChat.setVisibility(View.GONE);
        this.failedView.setVisibility(View.GONE);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter = new ChatAdapter(this, profileImage, toId));
        this.recyclerView.setScrollContainer(false);
        this.recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        /*makeOutAnimation = AnimationUtils.loadAnimation(this,
                R.anim.com_mixpanel_android_fade_in);
        makeInAnimation = AnimationUtils.loadAnimation(this,
                R.anim.com_mixpanel_android_fade_out);

        makeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                //fab.setVisibility(View.VISIBLE);
                lblChatHeader.setVisibility(View.GONE);
            }
        });
        makeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                //fab.setVisibility(View.GONE);
                lblChatHeader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });

        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // scrolling up
                    //lblChatHeader.setVisibility(View.GONE);
                    //makeInAnimation.start();
                    //lblChatHeader.setAnimation(makeInAnimation);
                    lblChatHeader.setVisibility(View.GONE);
                }
                else if(dy < 0)
                { // scrolling down }
                    //lblChatHeader.setVisibility(View.VISIBLE);
                    //makeOutAnimation.start();
                    //lblChatHeader.setAnimation(makeOutAnimation);
                    //lblChatHeader.startAnimation(makeOutAnimation);
                    lblChatHeader.startAnimation(makeInAnimation);
                }
            }

        });*/

        this.txtMessage.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                btnSend.setEnabled(s.toString().trim().length() > 0);
            }
        });

        Intent i = new Intent(ChatTabFragment.TAG);
        i.putExtra(Conversation.FIELD_FACEBOOK_ID, toId);
        sendBroadcast(i);

        super.registerReceiver(this.notificationReceived
                , new IntentFilter(super.getString(R.string.broadcast_notification)));
        super.registerReceiver(checkNewMessageReceiver
                , new IntentFilter(Utils.CHECK_NEW_MESSAGE_RECEIVER));

        onGlobalLayout();
    }

    @Override
    public void onGlobalLayout() {
        this.loadData();
        /*getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                //WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        );*/
        this.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

    }

    @OnClick(R.id.btnSend)
    @SuppressWarnings("unused")
    void btnSendOnClick() {
        chat = new Chat();
        final LoginModel loginModel = AccountManager.loadLogin();

        chat.setFromId(AccessToken.getCurrentAccessToken().getUserId());
        chat.setCreatedAt(Common.ISO8601_DATE_FORMAT_UTC.format(new Date()));
        chat.setMessage(this.txtMessage.getText().toString().trim());
        chat.setFromName(loginModel.getUser_first_name());
        chat.setToId(this.toId);
        chat.setFromYou(true);

        //Added by Aga------
        ChatAddModel chatAddModel = new ChatAddModel();
        chatAddModel.setFromId(AccessToken.getCurrentAccessToken().getUserId());
        chatAddModel.setHeader("");
        chatAddModel.setFromName(loginModel.getUser_first_name());
        chatAddModel.setMessage(this.txtMessage.getText().toString().trim());
        chatAddModel.setHosting_id("");
        chatAddModel.setKey("kT7bgkacbx73i3yxma09su0u901nu209mnuu30akhkpHJJ");
        chatAddModel.setToId(this.toId);
        //------------------

        this.adapter.add(chat);

        this.txtMessage.setText(null);
        this.adapter.notifyDataSetChanged();
        this.recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        final Intent resultIntent = new Intent();
        resultIntent.putExtra(Conversation.FIELD_FACEBOOK_ID, toId);
        resultIntent.putExtra(Conversation.FIELD_LAST_UPDATED, chat.getCreatedAt());
        super.setResult(RESULT_REPLIED, resultIntent);

        //Added by Aga-----
        ChatManager.loaderAddChat(chatAddModel);
        //-----------------

        /*VolleyHandler.getInstance().createVolleyRequest("messages/add", chat, new VolleyRequestListener<Void, JSONObject>() {
            @Override
            public Void onResponseAsync(JSONObject jsonObject) {
                return null;
            }

            @Override
            public void onResponseCompleted(Void value) {
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ChatTable.addUnprocessed(App.getInstance().getDatabase(), chat);
                ChatSendService.registerSchedule();
            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.loaded) {
            //super.getMenuInflater().inflate(R.menu.menu_chat, menu);
            super.getMenuInflater().inflate(R.menu.menu_chat, menu);
            final MenuItem menuBlock = menu.findItem(R.id.action_block);
            final MenuItem menuProfile = menu.findItem(R.id.action_profile);
            menuBlock.setTitle(super.getString(R.string.user_chat_block, this.toName));
            menuProfile.setTitle(super.getString(R.string.user_chat_profile, this.toName));
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
        }
        else if(item.getItemId() == R.id.home)
        {
            onBackPressed();
        }
        /*else if (item.getItemId() == R.id.action_clear) {
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
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void blockUser() {
        dialog = App.showProgressDialog(this);
        /*final String url = String.format("blockuserwithfbid?fromId=%s&toId=%s", AccessToken.getCurrentAccessToken().getUserId(), this.toId);
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
        });*/

        ChatManager.loaderBlockChat(AccessToken.getCurrentAccessToken().getUserId(), this.toId);
    }

    private void clearConversation() {
        dialog = App.showProgressDialog(this);
        /*final String url = String.format("deletemessageswithfbid?fromId=%s&toId=%s", AccessToken.getCurrentAccessToken().getUserId(), this.toId);
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
        });*/
        ChatManager.loaderDeleteChat(AccessToken.getCurrentAccessToken().getUserId(), this.toId);
    }

    private BroadcastReceiver notificationReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.removeCallbacks(checkNewMessageRunnable);
            handler.postDelayed(checkNewMessageRunnable, getResources().getInteger(R.integer.chat_notif_delay));
        }
    };

    private BroadcastReceiver checkNewMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fetchData();
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

        fetchData();
    }

    private void fetchData()
    {
        Utils.d(TAG, "fetch data");
        ChatManager.loaderChatConversations(AccessToken.getCurrentAccessToken().getUserId(), toId, ChatManager.FROM_LOAD);
    }

    private void checkNewMessages() {
        this.isChecking = true;

        changed = false;
        ChatManager.loaderChatConversations(AccessToken.getCurrentAccessToken().getUserId(), toId, ChatManager.FROM_CHECK_NEW);
    }

    public void onEvent(ChatResponseModel message){
        if(message.getFromFunction().equals(ChatManager.FROM_LOAD)){
            /*lblChatHeader.bringToFront();
            lblChatHeader.setText(message.getData().getEvent_name());
            lblChatHeader.setVisibility(View.VISIBLE);*/

            final List<Chat> failedItems = ChatTable.getUnProcessedItems(App.getInstance().getDatabase(), toId);
            final int length = message.getData().getMessages() == null ? 0 : message.getData().getMessages().size();
            final int failedLength = failedItems.size();

            adapter.clear();

            final Chat chatHeader = new Chat(null
                    , message.getData().getFromId(), message.getData().getEvent_name());
            adapter.add(chatHeader);

            if(length>0){
                for (int i = 0; i < length; i++) {
                    final Chat chat = new Chat(message.getData().getMessages().get(i), message.getData().getFromId());
                    if ((!chat.isFromYou()) && (lastMessageDate.compareTo(chat.getCreatedAt()) < 0))
                        lastMessageDate = chat.getCreatedAt();
                    adapter.add(chat);
                }
            }

            if(failedLength>0){
                for (int i = 0; i < failedLength; i++) {
                    final Chat failedItem = failedItems.get(i);
                    failedItem.setCreatedAt(Common.ISO8601_DATE_FORMAT.format(new Date()));
                    failedItem.setFromYou(true);
                    adapter.add(failedItem);
                }
            }

            if (isActive()) {
                loaded = true;
                viewChat.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
                adapter.notifyDataSetChanged();
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
            this.isChecking = false;
        }
    }

    public void onEvent(ChatActionModel message){
        String from = message.getFrom();
        if(from.equals(Utils.FROM_ADD_CHAT)){
            //doNothing
        }else if(from.equals(Utils.FROM_BLOCK_CHAT)){
            final boolean value = message.getSuccess2Model().getResponse()==1 ? true : false;
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
        }else if(from.equals(Utils.FROM_DELETE_CHAT)){
            final boolean value = message.getSuccess2Model().getResponse()==1 ? true : false;
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
    }

    public void onEvent(ExceptionModel message){
        String from = message.getFrom();
        if(from.equals(Utils.FROM_CHAT_CONVERSATION)){
            if (isActive()) {
                Toast.makeText(ChatActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                failedView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
            isChecking = false;
        }else if(from.equals(Utils.FROM_ADD_CHAT)){
            if(chat!=null){
                ChatTable.addUnprocessed(App.getInstance().getDatabase(), chat);
                ChatSendService.registerSchedule();
            }
        }else if(from.equals(Utils.FROM_BLOCK_CHAT)||from.equals(Utils.FROM_DELETE_CHAT)){
            if (isActive()){
                Toast.makeText(ChatActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                //Added by Aga 13-2-2016
                if(dialog.isShowing())
                    dialog.dismiss();
                //-----------------
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.unregisterReceiver(this.notificationReceived);
        super.unregisterReceiver(this.checkNewMessageReceiver);
        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        App.getInstance().setIdChatActive("");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(Common.TO_TAB_CHAT, true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

}
