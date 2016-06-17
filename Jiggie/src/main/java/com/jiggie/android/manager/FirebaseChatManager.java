package com.jiggie.android.manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jiggie.android.model.UserModel;

import java.util.ArrayList;

/**
 * Created by LTE on 6/17/2016.
 */
public class FirebaseChatManager {

    public static DatabaseReference mDatabase;
    public static ArrayList<UserModel> arrUser = new ArrayList<>();

    public static DatabaseReference getFirebaseDatabase(){
        if(mDatabase==null){
            mDatabase = FirebaseDatabase.getInstance().getReference();
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

}
