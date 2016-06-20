package com.jiggie.android.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.SimpleTextWatcher;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.FirebaseChatAdapter;
import com.jiggie.android.component.adapter.FirebaseChatDetailAdapter;
import com.jiggie.android.manager.FirebaseChatManager;
import com.jiggie.android.model.Conversation;
import com.jiggie.android.model.MessagesModel;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LTE on 6/19/2016.
 */
public class FirebaseChatActivity extends ToolbarActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.txtMessage)
    EditText txtMessage;
    @Bind(R.id.viewFailed)
    View failedView;
    @Bind(R.id.btnSend)
    Button btnSend;
    @Bind(R.id.viewChat) View viewChat;
    String event, roomId;
    int type;
    private String toName;
    private String toId;
    private Handler handler;

    public static final String TAG = ChatActivity.class.getSimpleName();

    ArrayList<MessagesModel> arrMessages = new ArrayList<>();
    ValueEventListener messageEvent;
    FirebaseChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_chat);

        App.runningActivity = this;

        final Intent intent = super.getIntent();
        init(intent);

    }

    private void init(Intent intent)
    {

        //fb_id = AccessToken.getCurrentAccessToken().getUserId();
        roomId = intent.getStringExtra(Utils.ROOM_ID);
        event = intent.getStringExtra(Utils.ROOM_EVENT);
        type = (int)intent.getLongExtra(Utils.ROOM_TYPE, 1);

        super.bindView();

        if(type==FirebaseChatManager.TYPE_GROUP){
            super.setToolbarTitle(event, true);
        }else {
            /*this.toName = intent.getStringExtra(Conversation.FIELD_FROM_NAME);
            this.toId = intent.getStringExtra(Conversation.FIELD_FACEBOOK_ID);*/

            String id1 = roomId.substring(0, roomId.indexOf("_"));
            String id2 = roomId.substring(roomId.indexOf("_")+1, roomId.length());

            String idFriend = Utils.BLANK;
            if(FirebaseChatManager.fb_id.equals(id1)){
                idFriend = id2;
            }else {
                idFriend = id1;
            }


            for(int i=0;i<FirebaseChatManager.arrUser.size();i++){
                String idUser = FirebaseChatManager.arrUser.get(i).getFb_id();
                if(idFriend.equals(idUser)){
                    toName = FirebaseChatManager.arrUser.get(i).getName();
                    toId = FirebaseChatManager.arrUser.get(i).getFb_id();
                    break;
                }else{
                    //do nothing
                }
            }

            App.getInstance().setIdChatActive(toId);
            super.setToolbarTitle(this.toName, true);
        }


        this.btnSend.setEnabled(false);
        this.handler = new Handler();
        this.viewChat.setVisibility(View.GONE);
        this.failedView.setVisibility(View.GONE);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.recyclerView.setScrollContainer(false);
        this.recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        this.txtMessage.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                btnSend.setEnabled(s.toString().trim().length() > 0);
            }
        });

        onGlobalLayout();
    }

    @Override
    public void onGlobalLayout() {
        getMessages(roomId);
        this.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @OnClick(R.id.btnSend)
    @SuppressWarnings("unused")
    void btnSendOnClick() {
        String name = Utils.BLANK, avatar = Utils.BLANK;
        for(int i=0;i<FirebaseChatManager.arrUser.size();i++){
            String fb_idMatch = FirebaseChatManager.arrUser.get(i).getFb_id();
            if(FirebaseChatManager.fb_id.equals(fb_idMatch)){
                name = FirebaseChatManager.arrUser.get(i).getName();
                avatar = FirebaseChatManager.arrUser.get(i).getAvatar();
            }else{
                //do nothing
            }
        }
        FirebaseChatManager.sendMessage(new MessagesModel("aabbcc", FirebaseChatManager.fb_id, name, avatar, txtMessage.getText().toString(), Calendar.getInstance().getTimeInMillis()), roomId);
    }

    private void getMessages(String roomId){
        Query queryMessages = FirebaseChatManager.getQueryMessage(roomId);
        messageEvent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrMessages.clear();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String messageId = (String) messageSnapshot.getKey();
                    String fb_id = String.valueOf(messageSnapshot.child("fb_id").getValue());
                    String message = String.valueOf(messageSnapshot.child("message").getValue());
                    long created_at = (long)messageSnapshot.child("created_at").getValue();

                    String name = Utils.BLANK, avatar = Utils.BLANK;
                    for(int i=0;i<FirebaseChatManager.arrUser.size();i++){
                        String fb_idMatch = FirebaseChatManager.arrUser.get(i).getFb_id();
                        if(fb_id.equals(fb_idMatch)){
                            name = FirebaseChatManager.arrUser.get(i).getName();
                            avatar = FirebaseChatManager.arrUser.get(i).getAvatar();
                        }else{
                            //do nothing
                        }
                    }

                    MessagesModel messagesModel = new MessagesModel(messageId, fb_id, name, avatar, message, created_at);
                    arrMessages.add(messagesModel);
                }

                if(adapter==null){

                    adapter = new FirebaseChatAdapter(FirebaseChatActivity.this, arrMessages, event);
                    recyclerView.setAdapter(adapter);

                    checkActive();
                }else{
                    adapter.notifyDataSetChanged();
                    checkActive();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        queryMessages.addValueEventListener(messageEvent);
    }

    private void checkActive(){
        if (isActive()) {
            viewChat.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            /*invalidateOptionsMenu();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            setResult(RESULT_OK, new Intent().putExtra(Conversation.FIELD_FACEBOOK_ID, toId));*/
        }
    }

}
