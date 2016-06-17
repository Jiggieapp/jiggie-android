package com.jiggie.android.manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jiggie.android.model.MessagesModel;
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
        return getFirebaseDatabase().child("room_members").orderByChild(fb_id);
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
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        getFirebaseDatabase().updateChildren(childUpdates);
    }

}
