package com.jiggie.android.manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    }

    public static void deleteChatList(String roomId, String fb_id){
        getFirebaseDatabase().child("room_members").child(roomId).child(fb_id).setValue(false);
    }

    public static void updateLastMessage(HashMap<String, Object> updatedRoom, String key){
        getFirebaseDatabase().child("rooms").child(key).child("info").setValue(updatedRoom);
    }

}
