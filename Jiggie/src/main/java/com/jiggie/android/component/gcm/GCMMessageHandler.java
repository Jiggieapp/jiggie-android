package com.jiggie.android.component.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.google.android.gms.gcm.GcmListenerService;
import com.jiggie.android.activity.chat.ChatActivity;
import com.jiggie.android.manager.ChatManager;
import com.jiggie.android.model.Conversation;

/**
 * Created by rangg on 09/09/2015.
 */
public class GCMMessageHandler extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String name = "Jiggie";
        String message = "";
        boolean chat = false;

        String fromId = "";
        String toId = "";

        for (String key : data.keySet()) {
            if (key.equalsIgnoreCase("Jiggie")) {
                final String content = data.getString(key);
                if (content != null) {
                    final String[] values = content.split(":");
                    message = values.length > 1 ? values[1].trim() : content;
                    name = values.length > 1 ? values[0].trim() : getString(R.string.app_name);
                    chat = values.length > 1;
                }
            }
        }

        for (String key : data.keySet()) {
            if (key.equalsIgnoreCase("fromId")) {
                final String content = data.getString(key);
                fromId = content;
            }
        }

        for (String key : data.keySet()) {
            if (key.equalsIgnoreCase("toId")) {
                final String content = data.getString(key);
                toId = content;
            }
        }

        final NotificationManager notificationManager = (NotificationManager) super.getSystemService(NOTIFICATION_SERVICE);
        //final Intent intent = new Intent(App.getInstance(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("chat", chat);

        /*String profil_image="";
        for(int i=0;i< ChatManager.dataChatList.size();i++){
            if(ChatManager.dataChatList.get(i).getFb_id().equals(fromId)){
                profil_image = ChatManager.dataChatList.get(i).getProfile_image();
            }
        }

        final Intent intent = new Intent(App.getInstance(), ChatActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Conversation.FIELD_PROFILE_IMAGE, profil_image);
        intent.putExtra(Conversation.FIELD_FROM_NAME, name);
        intent.putExtra(Conversation.FIELD_FACEBOOK_ID, fromId);
        intent.putExtra("chat", chat);*/

        Intent intent;
        if(chat){
            String profil_image="";
            for(int i=0;i< ChatManager.dataChatList.size();i++){
                if(ChatManager.dataChatList.get(i).getFb_id().equals(fromId)){
                    profil_image = ChatManager.dataChatList.get(i).getProfile_image();
                }
            }

            intent = new Intent(App.getInstance(), ChatActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Conversation.FIELD_PROFILE_IMAGE, profil_image);
            intent.putExtra(Conversation.FIELD_FROM_NAME, name);
            intent.putExtra(Conversation.FIELD_FACEBOOK_ID, fromId);
            intent.putExtra("chat", chat);
        }else{
            intent = new Intent(App.getInstance(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("chat", chat);
        }

        final PendingIntent pendingIntent = PendingIntent.getActivity(App.getInstance(), Integer.MIN_VALUE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        final Notification notif = new NotificationCompat.BigTextStyle(new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(super.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_notification)
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .setContentTitle(name)
                .setAutoCancel(true))
                .bigText(message)
                .build();
        notificationManager.notify(0, notif);
        super.sendBroadcast(new Intent(super.getString(R.string.broadcast_notification)));
    }
}
