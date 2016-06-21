package com.jiggie.android.manager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jiggie.android.model.CollectionRoomMemberModel;
import com.jiggie.android.model.MessagesModel;
import com.jiggie.android.model.RoomMembersModel;
import com.jiggie.android.model.RoomModel;
import com.jiggie.android.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LTE on 6/17/2016.
 */
public class FirebaseChatManager {

    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference mDatabase;
    public static ArrayList<UserModel> arrUser = new ArrayList<>();
    public static ArrayList<RoomModel> arrAllRoom = new ArrayList<>();
    public static ArrayList<String> arrAllRoomMembers = new ArrayList<>();
    public static ArrayList<CollectionRoomMemberModel> arrCollectRoomMembers = new ArrayList<>();
    public static final int TYPE_GROUP = 2;
    public static final int TYPE_PRIVATE = 1;
    public static String fb_id = "444555666";

    public static DatabaseReference getFirebaseDatabase(){
        if(mDatabase==null){
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
            mDatabase = firebaseDatabase.getReference();
        }

        return mDatabase;
    }

    public static Query getQueryUsers(){
        return getFirebaseDatabase().child("users");
    }

    public static Query getQueryRoomMembers(String fb_id){
        return getFirebaseDatabase().child("room_members").orderByChild(fb_id).equalTo(true);
    }

    public static Query getQueryRoom(String key){
        return getFirebaseDatabase().child("rooms").child(key);
    }

    public static Query getQueryMessage(String roomId){
        return getFirebaseDatabase().child("messages").child(roomId);
    }

    public static Query getQueryCollectionRoomMembers(String roomId){
        return getFirebaseDatabase().child("room_members").child(roomId);
    }

    public static void sendMessage(MessagesModel messagesModel, String roomId){
        String key = getFirebaseDatabase().child("messages").child(roomId).push().getKey();
        Map<String, Object> postValues = messagesModel.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/messages/" + roomId + "/"+key+"/", postValues);

        getFirebaseDatabase().updateChildren(childUpdates);

        //update last message in room-------------
        RoomModel roomModel = null;
        for(int i=0;i<arrAllRoom.size();i++){
            String keyMatch = arrAllRoom.get(i).getKey();
            if(roomId.equals(keyMatch)){
                roomModel = arrAllRoom.get(i);
                break;
            }
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("event", roomModel.getInfo().getEvent());
        result.put("identifier", roomModel.getKey());
        result.put("last_message", messagesModel.getMessage());
        result.put("created_at", roomModel.getInfo().getCreated_at());
        result.put("updated_at", messagesModel.getCreated_at());

        updateLastMessage(result, roomId);
        //end of update last message in room-----------

        reActivatedDeletedChat(roomId);

        updateCounterUnread(roomId, roomModel);
    }

    public static void deleteChatList(String roomId, String fb_id){
        getFirebaseDatabase().child("room_members").child(roomId).child(fb_id).setValue(false);
    }

    public static void updateLastMessage(HashMap<String, Object> updatedRoom, String key){
        getFirebaseDatabase().child("rooms").child(key).child("info").setValue(updatedRoom);
    }

    public static void blockChatList(String roomId, String fb_id){
        getFirebaseDatabase().child("room_members").child(roomId).child(fb_id).removeValue();
    }

    private static void reActivatedDeletedChat(String roomId){
        HashMap<String, Object> result = new HashMap<>();
        for(int i=0;i<arrCollectRoomMembers.size();i++){
            result.put(arrCollectRoomMembers.get(i).getFb_id(), true);
        }

        getFirebaseDatabase().child("room_members").child(roomId).setValue(result);
    }

    public static void getCollectionRoomMembers(String roomId){
        Query queryCollectionRoomMember = FirebaseChatManager.getQueryCollectionRoomMembers(roomId);
        queryCollectionRoomMember.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseChatManager.arrCollectRoomMembers.clear();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String fb_id = (String) messageSnapshot.getKey();
                    boolean isAvailable = (boolean)messageSnapshot.getValue();

                    CollectionRoomMemberModel collectionRoomMemberModel = new CollectionRoomMemberModel(fb_id, isAvailable);
                    FirebaseChatManager.arrCollectRoomMembers.add(collectionRoomMemberModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void updateCounterUnread(String roomId, RoomModel roomModel){
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<RoomModel.Unread> dataUnread = roomModel.getUnreads();
        for(int i=0;i<dataUnread.size();i++){
            String fb_idMatch = dataUnread.get(i).getFb_id();
            if(fb_idMatch.equals(fb_id)){
                result.put(fb_idMatch, 0);
            }else{
                result.put(fb_idMatch, dataUnread.get(i).getCounter()+1);
            }
        }
        getFirebaseDatabase().child("rooms").child(roomId).child("unread").setValue(result);
    }

    public static void counterRead(String roomId){
        getFirebaseDatabase().child("rooms").child(roomId).child("unread").child(fb_id).setValue(0);
    }
}
