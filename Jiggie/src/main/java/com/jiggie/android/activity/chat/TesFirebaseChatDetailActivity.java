package com.jiggie.android.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.FirebaseChatDetailAdapter;
import com.jiggie.android.component.adapter.FirebaseChatListAdapter;
import com.jiggie.android.manager.FirebaseChatManager;
import com.jiggie.android.model.MessagesModel;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 6/14/2016.
 */
public class TesFirebaseChatDetailActivity extends ToolbarActivity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.btn_send)
    Button btnSend;
    @Bind(R.id.edt_message)
    EditText edtMessage;

    private DatabaseReference mDatabase;
    ArrayList<MessagesModel> arrMessages = new ArrayList<>();
    ValueEventListener messageEvent;
    FirebaseChatDetailAdapter adapter;
    String fb_id = "111222333";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_chatdetail);
        ButterKnife.bind(this);
        super.bindView();
        super.setToolbarTitle("Firebase chat detail", true);

        Intent a = getIntent();
        final String roomId = a.getStringExtra(Utils.ROOM_ID);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        //mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        getMessages(roomId);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                FirebaseChatManager.sendMessage(new MessagesModel("aabbcc", fb_id, name, avatar, edtMessage.getText().toString(), Calendar.getInstance().getTimeInMillis()), roomId);
            }
        });
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
                    adapter = new FirebaseChatDetailAdapter(TesFirebaseChatDetailActivity.this, arrMessages);
                    recyclerView.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        queryMessages.addValueEventListener(messageEvent);
    }

}
