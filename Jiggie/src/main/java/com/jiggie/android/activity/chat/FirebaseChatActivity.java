package com.jiggie.android.activity.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.api.OnResponseListener;
import com.jiggie.android.component.SimpleTextWatcher;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.FirebaseChatAdapter;
import com.jiggie.android.manager.ChatManager;
import com.jiggie.android.manager.FirebaseChatManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.MessagesModel;
import com.jiggie.android.model.RoomModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    boolean fromNotif = false;

    public static final String TAG = ChatActivity.class.getSimpleName();

    ArrayList<MessagesModel> arrMessages = new ArrayList<>();
    ValueEventListener messageEvent;
    FirebaseChatAdapter adapter;

    boolean loaded = false;
    public static final int RESULT_BLOCKED = 10001;
    ProgressDialog dialog;

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
        roomId = intent.getStringExtra(Utils.ROOM_ID);
        fromNotif = intent.getBooleanExtra(Utils.LOAD_ROOM_DETAIL, false);

        if(fromNotif){
            final Query roomDetail = FirebaseChatManager.getQueryRoom(roomId);
            roomDetail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    boolean isRoomValueExist = dataSnapshot.exists();
                    boolean isTypeExist = dataSnapshot.child("type").exists();
                    boolean isInfoExist = dataSnapshot.child("info").exists();

                    if(isRoomValueExist&&isTypeExist&&isInfoExist){
                        String key = roomId;
                        //long type = (long) dataSnapshot.child("type").getValue();

                        long types = (long) dataSnapshot.child("type").getValue();
                        type = (int)types;
                        event = (String) dataSnapshot.child("info").child("event").getValue();

                        RoomModel.Info info = null;
                        if(type==FirebaseChatManager.TYPE_GROUP){
                            String event = (String) dataSnapshot.child("info").child("event").getValue();
                            String avatar = (String) dataSnapshot.child("info").child("avatar").getValue();
                            String last_message = (String) dataSnapshot.child("info").child("last_message").getValue();
                            long created_at = (long) dataSnapshot.child("info").child("created_at").getValue();
                            long updated_at = (long) dataSnapshot.child("info").child("updated_at").getValue();

                            info = new RoomModel.Info(event, avatar, event, last_message, created_at, updated_at);
                        }else{
                            String event = (String) dataSnapshot.child("info").child("event").getValue();
                            String identifier = (String) dataSnapshot.child("info").child("identifier").getValue();
                            String last_message = (String) dataSnapshot.child("info").child("last_message").getValue();
                            long created_at = (long) dataSnapshot.child("info").child("created_at").getValue();
                            long updated_at = (long) dataSnapshot.child("info").child("updated_at").getValue();

                            String id1 = identifier.substring(0, identifier.indexOf("_"));
                            String id2 = identifier.substring(identifier.indexOf("_")+1, identifier.length());

                            String name = Utils.BLANK;
                            String avatar = Utils.BLANK;
                            String idFriend = Utils.BLANK;
                            if(FirebaseChatManager.fb_id.equals(id1)){
                                idFriend = id2;
                            }else {
                                idFriend = id1;
                            }


                            for(int i=0;i<FirebaseChatManager.arrUser.size();i++){
                                String idUser = FirebaseChatManager.arrUser.get(i).getFb_id();
                                if(idFriend.equals(idUser)){
                                    name = FirebaseChatManager.arrUser.get(i).getName();
                                    avatar = FirebaseChatManager.arrUser.get(i).getAvatar();

                                    toName = name;
                                    toId = idFriend;
                                    break;
                                }else{
                                    //do nothing
                                }
                            }

                            info = new RoomModel.Info(name, avatar, event, last_message, created_at, updated_at);
                        }

                        ArrayList<RoomModel.Unread> arrUnread = new ArrayList<RoomModel.Unread>();
                        for (DataSnapshot unreadSnapshot: dataSnapshot.child("info").child("unread").getChildren()) {
                            String fb_id = (String)unreadSnapshot.getKey();
                            long counter = (long)unreadSnapshot.getValue();

                            RoomModel.Unread unread = new RoomModel.Unread(fb_id, counter);
                            arrUnread.add(unread);
                        }

                        RoomModel roomModel = new RoomModel(key, info, type, arrUnread);

                        //checker if already just update, if not then added---------------------
                        if(FirebaseChatManager.arrAllRoom.size()==0){
                            FirebaseChatManager.arrAllRoom.add(roomModel);
                        }else{
                            boolean isExist = false;
                            int existPosition = 0;
                            for(int i=0; i<FirebaseChatManager.arrAllRoom.size();i++){
                                String keyMatch = FirebaseChatManager.arrAllRoom.get(i).getKey();
                                if(key.equals(keyMatch)){
                                    existPosition = i;
                                    isExist = true;
                                    break;
                                }
                            }

                            if(isExist){
                                FirebaseChatManager.arrAllRoom.set(existPosition, roomModel);
                            }else{
                                FirebaseChatManager.arrAllRoom.add(roomModel);
                            }
                        }
                        //--------------------------------------------------------------------------

                        nextInit();

                        roomDetail.removeEventListener(this);
                        dismissProgressBar();
                    }else{
                        //keep looking
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //do nothing
                    //roomDetail.removeEventListener(this);
                    dismissProgressBar();
                }
            });
        }else{
            event = intent.getStringExtra(Utils.ROOM_EVENT);
            type = (int)intent.getLongExtra(Utils.ROOM_TYPE, 1);
            nextInit();
        }

    }

    private void dismissProgressBar(){
        if(progressBar!=null&&progressBar.getVisibility()==View.VISIBLE){
            progressBar.setVisibility(View.GONE);
        }
    }

    private void nextInit(){
        super.bindView();

        if(type==FirebaseChatManager.TYPE_GROUP){
            super.setToolbarTitle(event, true);
            App.getInstance().setIdChatActive(roomId);
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
        FirebaseChatManager.getCollectionRoomMembers(roomId);
        FirebaseChatManager.counterRead(roomId);
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

        HashMap<String, Object> result = new HashMap<>();
        if(type==FirebaseChatManager.TYPE_PRIVATE){
            result.put("toId", this.toId);
            result.put("toName", this.toName);
        }

        //FirebaseChatManager.sendMessage(new MessagesModel("aabbcc", FirebaseChatManager.fb_id, name, avatar, txtMessage.getText().toString(), System.currentTimeMillis()), roomId, type, result);
        HashMap<String, Object> chatModel = new HashMap<>();
        String message = txtMessage.getText().toString();
        chatModel.put("fb_id", FirebaseChatManager.fb_id);
        if(type==FirebaseChatManager.TYPE_GROUP){
            chatModel.put("member_fb_id", "");
        }else{
            chatModel.put("member_fb_id", toId);
        }
        chatModel.put("message", message);
        chatModel.put("room_id", roomId);
        chatModel.put("type", String.valueOf(type));
        ChatManager.loaderAddChatFirebase(chatModel, new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                //success
            }

            @Override
            public void onFailure(ExceptionModel exceptionModel) {
                //failure
            }
        });

        if(type==FirebaseChatManager.TYPE_PRIVATE){
            FirebaseChatManager.reActivatedDeletedChat(roomId);
        }

        txtMessage.setText(Utils.BLANK);
        this.recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void getMessages(final String roomId){
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

                    adapter = new FirebaseChatAdapter(FirebaseChatActivity.this, arrMessages, event, type);
                    recyclerView.setAdapter(adapter);

                    checkActive();
                }else{
                    adapter.notifyDataSetChanged();
                    checkActive();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dismissProgressBar();
            }
        };
        queryMessages.addValueEventListener(messageEvent);
    }

    private void checkActive(){
        if (isActive()) {
            loaded = true;
            viewChat.setVisibility(View.VISIBLE);
            dismissProgressBar();
            invalidateOptionsMenu();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            //setResult(RESULT_OK, new Intent().putExtra(Conversation.FIELD_FACEBOOK_ID, toId));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //FirebaseChatManager.getFirebaseDatabase().removeEventListener(messageEvent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.loaded) {
            //super.getMenuInflater().inflate(R.menu.menu_chat, menu);
            super.getMenuInflater().inflate(R.menu.menu_chat, menu);
            final MenuItem menuBlock = menu.findItem(R.id.action_block);
            final MenuItem menuProfile = menu.findItem(R.id.action_profile);

            if(type==FirebaseChatManager.TYPE_PRIVATE){
                menuBlock.setTitle(super.getString(R.string.user_chat_block, this.toName));
                menuProfile.setTitle(super.getString(R.string.user_chat_profile, this.toName));
            }else{
                menuBlock.setTitle("Exit Group");
                menuProfile.setTitle(this.event+" Detail");
            }

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(type==FirebaseChatManager.TYPE_PRIVATE){
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
                                FirebaseChatManager.blockChatList(roomId, FirebaseChatManager.fb_id);
                                blockUser();
                            }
                        }).show();
            }
            else if(item.getItemId() == R.id.home)
            {
                onBackPressed();
            }
        }else{
            if (item.getItemId() == R.id.action_profile) {
                final Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra(Common.FIELD_EVENT_ID, roomId);
                intent.putExtra(Common.FIELD_FROM, TAG);
                startActivity(intent);
            } else if (item.getItemId() == R.id.action_block) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.confirmation)
                        .setTitle("Exit Group")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                FirebaseChatManager.blockChatList(roomId, FirebaseChatManager.fb_id);
                                onBackPressed();
                            }
                        }).show();
            }
            else if(item.getItemId() == R.id.home)
            {
                onBackPressed();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void blockUser(){
        dialog = App.showProgressDialog(this);
        ChatManager.loaderBlockChatNew(FirebaseChatManager.fb_id, this.toId, new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                App.getInstance().trackMixPanelEvent("Block User");

                setResult(RESULT_BLOCKED, new Intent());
                onBackPressed();
            }

            @Override
            public void onFailure(ExceptionModel exceptionModel) {
                Toast.makeText(FirebaseChatActivity.this, getString(R.string.block_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent i = new Intent(this, MainActivity.class);
        i.putExtra(Common.TO_TAB_CHAT, true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();*/
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getInstance().setIdChatActive(Utils.BLANK);
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.getInstance().setIdChatActive(Utils.BLANK);
    }
}