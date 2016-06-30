package com.jiggie.android.manager;

import android.util.Log;

import com.facebook.AccessToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jiggie.android.api.OnResponseListener;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ChatAddModel;
import com.jiggie.android.model.CollectionRoomMemberModel;
import com.jiggie.android.model.ExceptionModel;
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
    //public static String fb_id = "444555666";
    public static String fb_id = AccessToken.getCurrentAccessToken().getUserId();
    public static String badgeChat = "0";

    public static DatabaseReference getFirebaseDatabase(){
        if(mDatabase==null){
            firebaseDatabase = FirebaseDatabase.getInstance();
            //firebaseDatabase.setPersistenceEnabled(true);
            mDatabase = firebaseDatabase.getReference();
        }

        return mDatabase;
    }

    public static Query getQueryUsers(){
        return getFirebaseDatabase().child("users");
    }

    public static Query getQueryRoomMembers(String fb_id){
        String a = "";
        try {
            return getFirebaseDatabase().child("room_members").orderByChild(fb_id).equalTo(true);
        }catch (Exception e){
            return null;
        }
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

    public static void deleteChatList(String roomId, String fb_id){
        getFirebaseDatabase().child("room_members").child(roomId).child(fb_id).setValue(false);
    }

    public static void updateLastMessage(HashMap<String, Object> updatedRoom, String key){
        getFirebaseDatabase().child("rooms").child(key).child("info").updateChildren(updatedRoom);
    }

    public static void blockChatList(String roomId, String fb_id){
        getFirebaseDatabase().child("room_members").child(roomId).child(fb_id).removeValue();
    }

    public static void counterRead(String roomId){
        getFirebaseDatabase().child("rooms").child(roomId).child("info").child("unread").child(fb_id).setValue(0);
    }

    public static void sendMessage(MessagesModel messagesModel, String roomId, int type, HashMap<String, Object> privateInfo){

        try {
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
            String message = messagesModel.getMessage();
            result.put("event", roomModel.getInfo().getEvent());
            if(type==TYPE_PRIVATE){
                result.put("identifier", roomModel.getKey());
            }
            result.put("last_message", message);
            result.put("created_at", roomModel.getInfo().getCreated_at());
            result.put("updated_at", messagesModel.getCreated_at());

            //unread part------------------------------------
            HashMap<String, Object> unread = new HashMap<>();
            ArrayList<RoomModel.Unread> dataUnread = roomModel.getUnreads();
            for(int i=0;i<dataUnread.size();i++){
                String fb_idMatch = dataUnread.get(i).getFb_id();
                if(fb_idMatch.equals(fb_id)){
                    unread.put(fb_idMatch, 0);
                }else{
                    unread.put(fb_idMatch, dataUnread.get(i).getCounter()+1);
                }
            }

            result.put("unread", unread);
            //end of unread part---------------------------------

            updateLastMessage(result, roomId);
            //end of update last message in room-----------

            //updateCounterUnread(roomId, roomModel);

            if(type==TYPE_GROUP){
                sendGroupChatJannes(roomId, message);
            }else{
                //send private
                reActivatedDeletedChat(roomId);
                sendPrivateChatJannes(key, message, privateInfo);
            }
        }catch (Exception e){
            Log.d("sendMessage", e.toString());
        }

    }

    public static void reActivatedDeletedChat(String roomId){

        try {
            HashMap<String, Object> result = new HashMap<>();
            for(int i=0;i<arrCollectRoomMembers.size();i++){
                result.put(arrCollectRoomMembers.get(i).getFb_id(), true);
            }

            getFirebaseDatabase().child("room_members").child(roomId).updateChildren(result);
        }catch (Exception e){
            Log.d("reActivatedDeletedChat", e.toString());
        }

    }

    private static void reActivatedGroupChat(String roomId){

        try {
            HashMap<String, Object> result = new HashMap<>();
            result.put(fb_id, true);
            getFirebaseDatabase().child("room_members").child(roomId).updateChildren(result);
        }catch (Exception e){
            Log.d("reActivatedGroupChat", e.toString());
        }

    }

    public static void getCollectionRoomMembers(String roomId){

        try {
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
        }catch (Exception e){
            Log.d("getCollectRoomMembers", e.toString());
        }


    }

    private static void updateCounterUnread(String roomId, RoomModel roomModel){
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<RoomModel.Unread> dataUnread = roomModel.getUnreads();
        for(int i=0;i<dataUnread.size();i++){
            String fb_idMatch = dataUnread.get(i).getFb_id();
            long counter = 0;
            if(fb_idMatch.equals(fb_id)){
                result.put(fb_idMatch, 0);
            }else{
                result.put(fb_idMatch, dataUnread.get(i).getCounter()+1);
                counter = dataUnread.get(i).getCounter()+1;
            }

            //getFirebaseDatabase().child("rooms").child(roomId).child("info").child("unread").child(fb_idMatch).updateChildren();
        }
        //getFirebaseDatabase().child("rooms").child(roomId).child("info").child("unread").setValue(result);
        getFirebaseDatabase().child("rooms").child(roomId).child("info").child("unread").updateChildren(result);
    }

    private static void sendGroupChatJannes(String key, String message){

        try {
            HashMap<String, Object> result = new HashMap<>();
            result.put("fb_id", fb_id);
            result.put("event_id", key);
            result.put("message", message);
            ChatManager.loaderGroupChatJannes(result, new OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    //do nothing
                }

                @Override
                public void onFailure(ExceptionModel exceptionModel) {
                    Log.d("group chat jannes", exceptionModel.toString());
                    //do nothing
                }
            });
        }catch (Exception e){
            Log.d("sendGroupChatJannes", e.toString());
        }

        reActivatedGroupChat(key);
    }

    private static void sendPrivateChatJannes(String key, String message, HashMap<String, Object> privateInfo){

        try {
            ChatAddModel chatAddModel = new ChatAddModel();
            chatAddModel.setFromId(AccessToken.getCurrentAccessToken().getUserId());
            chatAddModel.setHeader("");
            chatAddModel.setFromName(privateInfo.get("toName").toString());
            chatAddModel.setMessage(message);
            chatAddModel.setHosting_id("");
            //chatAddModel.setKey("kT7bgkacbx73i3yxma09su0u901nu209mnuu30akhkpHJJ");
            chatAddModel.setKey(key);
            chatAddModel.setToId(privateInfo.get("toId").toString());
            ChatManager.loaderAddChatJannes(chatAddModel, new OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    //do nothing
                }

                @Override
                public void onFailure(ExceptionModel exceptionModel) {
                    Log.d("private chat jannes", exceptionModel.toString());
                }
            });
        }catch (Exception e){
            Log.d("sendPrivateChatJannes", e.toString());
        }
    }

    public static void clearDataFirebase(){
        if(arrUser!=null&&arrUser.size()>0){
            arrUser.clear();
        }
        if(arrAllRoom!=null&&arrAllRoom.size()>0){
            arrAllRoom.clear();
        }
        if(arrAllRoomMembers!=null&&arrAllRoomMembers.size()>0){
            arrAllRoomMembers.clear();
        }
        if(arrCollectRoomMembers!=null&&arrCollectRoomMembers.size()>0){
            arrCollectRoomMembers.clear();
        }
        //fb_id = Utils.BLANK;
        badgeChat = "0";
    }
}
