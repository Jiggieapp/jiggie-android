package com.jiggie.android.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.FirebaseChatListAdapter;
import com.jiggie.android.manager.FirebaseChatManager;
import com.jiggie.android.model.RoomMembersModel;
import com.jiggie.android.model.RoomModel;
import com.jiggie.android.model.UserModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by LTE on 6/14/2016.
 */
public class TesFirebaseChatListActivity extends ToolbarActivity implements FirebaseChatListAdapter.RoomSelectedListener, FirebaseChatListAdapter.RoomLongClickListener{

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;


    ArrayList<RoomModel> arrAllRoom = new ArrayList<>();
    ArrayList<RoomModel> arrSelectedRoom = new ArrayList<>();
    /*ArrayList<RoomMembersModel> arrAllRoomMembers = new ArrayList<>();
    ArrayList<RoomMembersModel> arrSelectedRoomMembers = new ArrayList<>();*/
    ArrayList<String> arrAllRoomMembers = new ArrayList<>();

    FirebaseChatListAdapter adapter;

    public static final int TYPE_GROUP = 2;
    public static final int TYPE_PRIVATE = 1;

    String fb_id = "444555666";

    ValueEventListener userEvent, roomEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_chatlist);
        ButterKnife.bind(this);
        super.bindView();
        super.setToolbarTitle("Firebase chat list", true);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        /*mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);*/
        recyclerView.setLayoutManager(mManager);

        /*Query roomQuery = getQuery(mDatabase);
        roomQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //RoomModel roomModel = dataSnapshot.getValue(RoomModel.class);
                //String sd = String.valueOf(new Gson().toJson(roomModel));
                //String sd = String.valueOf(dataSnapshot.getValue());

                //ArrayList<RoomModel.Room> data = new ArrayList<RoomModel.Room>();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    //RoomModel.Room message = messageSnapshot.getValue(RoomModel.Room.class);
                    //data.add(message);

                    String key = (String) messageSnapshot.getKey();

                    //String name = (String) messageSnapshot.child("name").getValue();
                    StringBuilder sb = new StringBuilder();
                    for (DataSnapshot childSnapshot: messageSnapshot.getChildren()) {
                        sb.append((String)childSnapshot.getKey());
                        sb.append(",");
                    }
                    String id = sb.toString();
                    Log.d("w", key+" "+id);
                }

                //String sd = String.valueOf(new Gson().toJson(data));
                //Log.d("w", String.valueOf(data.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("e", "yes");
            }
        });*/

        getUsers();
        getRoomMembers();
        //getAllRooms();
    }

    private void getUsers(){
        Query queryUsers = FirebaseChatManager.getQueryUsers();
        userEvent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String key = (String) messageSnapshot.getKey();
                    String fb_id = String.valueOf(messageSnapshot.child("fb_id").getValue());
                    String name = (String) messageSnapshot.child("name").getValue();
                    String avatar = (String) messageSnapshot.child("avatar").getValue();

                    UserModel userModel = new UserModel(key, fb_id, name, avatar);
                    FirebaseChatManager.arrUser.add(userModel);
                }

                String sd = String.valueOf(new Gson().toJson(FirebaseChatManager.arrUser));
                Log.d("sd","sd");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        queryUsers.addValueEventListener(userEvent);
    }

    /*private void getAllRooms(){
        Query queryUsers = getQueryRoom(mDatabase);
        queryUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String key = (String) messageSnapshot.getKey();
                    long type = (long) messageSnapshot.child("type").getValue();

                    RoomModel.Info info = null;
                    if(type==TYPE_GROUP){
                        String name = (String) messageSnapshot.child("info").child("name").getValue();
                        String avatar = (String) messageSnapshot.child("info").child("avatar").getValue();
                        String last_message = (String) messageSnapshot.child("info").child("last_message").getValue();
                        long created_at = (long) messageSnapshot.child("info").child("created_at").getValue();
                        long updated_at = (long) messageSnapshot.child("info").child("updated_at").getValue();

                        info = new RoomModel.Info(name, avatar, name, last_message, created_at, updated_at);
                    }else{
                        String event = (String) messageSnapshot.child("info").child("event").getValue();
                        String identifier = (String) messageSnapshot.child("info").child("identifier").getValue();
                        String last_message = (String) messageSnapshot.child("info").child("last_message").getValue();
                        long created_at = (long) messageSnapshot.child("info").child("created_at").getValue();
                        long updated_at = (long) messageSnapshot.child("info").child("updated_at").getValue();

                        String id1 = identifier.substring(0, identifier.indexOf("_"));
                        String id2 = identifier.substring(identifier.indexOf("_")+1, identifier.length()-1);

                        String name = Utils.BLANK;
                        String avatar = Utils.BLANK;
                        String idFriend = Utils.BLANK;
                        if(fb_id.equals(id1)){
                            idFriend = id1;
                        }else {
                            idFriend = id2;
                        }

                        for(int i=0;i<arrUser.size();i++){
                            String idUser = arrUser.get(i).getFb_id();
                            if(idFriend.equals(idUser)){
                                name = arrUser.get(i).getName();
                                avatar = arrUser.get(i).getAvatar();
                                break;
                            }else{
                                //do nothing
                            }
                        }

                        info = new RoomModel.Info(name, avatar, event, last_message, created_at, updated_at);
                    }

                    RoomModel roomModel = new RoomModel(key, info, type);
                    arrAllRoom.add(roomModel);
                }

                String sd = String.valueOf(new Gson().toJson(arrAllRoom));
                Log.d("sd","sd");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    private void getRoomMembers(){
        Query queryRoomMembers = FirebaseChatManager.getQueryRoomMembers(fb_id);
        roomEvent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    String key = (String)messageSnapshot.getKey();

                    arrAllRoomMembers.add(key);

                    getRoomDetail(key);

                }

                sizeRoom = arrAllRoomMembers.size();

                Log.d("sd",String.valueOf(sizeRoom));
                String sd = String.valueOf(new Gson().toJson(arrAllRoom));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        queryRoomMembers.addValueEventListener(roomEvent);


    }


    int sizeRoom = 0;
    private void getRoomDetail(String key){
        Query queryRoom = FirebaseChatManager.getQueryRoom(key);
        queryRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = (String) dataSnapshot.getKey();
                long type = (long) dataSnapshot.child("type").getValue();

                RoomModel.Info info = null;
                if(type==TYPE_GROUP){
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
                    String id2 = identifier.substring(identifier.indexOf("_")+1, identifier.length()-1);

                    String name = Utils.BLANK;
                    String avatar = Utils.BLANK;
                    String idFriend = Utils.BLANK;
                    if(fb_id.equals(id1)){
                        idFriend = id1;
                    }else {
                        idFriend = id2;
                    }

                    for(int i=0;i<FirebaseChatManager.arrUser.size();i++){
                        String idUser = FirebaseChatManager.arrUser.get(i).getFb_id();
                        if(idFriend.equals(idUser)){
                            name = FirebaseChatManager.arrUser.get(i).getName();
                            avatar = FirebaseChatManager.arrUser.get(i).getAvatar();
                            break;
                        }else{
                            //do nothing
                        }
                    }

                    info = new RoomModel.Info(name, avatar, event, last_message, created_at, updated_at);
                }

                RoomModel roomModel = new RoomModel(key, info, type);
                arrAllRoom.add(roomModel);

                String sds = String.valueOf(new Gson().toJson(arrAllRoom));
                Log.d("sd","sd");

                setAdapters();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapters(){
        if(arrAllRoom.size()==arrAllRoomMembers.size()){
            if(adapter==null){
                adapter = new FirebaseChatListAdapter(TesFirebaseChatListActivity.this, arrAllRoom, this, this);
                recyclerView.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRoomSelected(RoomModel roomModel) {
        startActivity(new Intent(TesFirebaseChatListActivity.this, TesFirebaseChatDetailActivity.class).putExtra(Utils.ROOM_ID, roomModel.getKey()));
    }

    @Override
    public void onRoomLongClick(RoomModel roomModel) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseChatManager.getFirebaseDatabase().removeEventListener(userEvent);
        FirebaseChatManager.getFirebaseDatabase().removeEventListener(roomEvent);
    }
}
