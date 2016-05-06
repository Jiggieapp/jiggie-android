package com.jiggie.android.component.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.google.android.gms.gcm.GcmListenerService;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.activity.chat.ChatActivity;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Conversation;

/**
 * Created by rangg on 09/09/2015.
 */
public class GCMMessageHandler extends GcmListenerService {

    public static final String TAG = GCMMessageHandler.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        //String name = "Jiggie";
        //String message = "";
        boolean chat = false;

        String fromId = "";

        //Utils.d(TAG, "gcm message handler " +  data.toString());
        //Bundle[{Jiggie=cui cui cui cui BARUU
        // , type=general, collapse_key=do_not_collapse}]

        //[{Jiggie=cui cui cui cui BARUU, type=event
        //        , event_id=56b996e8f474090300ff4f9b, collapse_key=do_not_collapse}]

        //[{Jiggie=cui cui cui cui BARUU, fromId=10153418311072858
        //        , toId=140679782985703, type=match, collapse_key=do_not_collapse}]

        //[{Jiggie=cui cui cui cui BARUU, fromId=10153418311072858,
        // toId=140679782985703, type=message, collapse_key=do_not_collapse}]

        //lama
        //[{Jiggie=wandy:Cukup, fromId=10153418311072858
        //      , toId=140679782985703, collapse_key=do_not_collapse}]

        /*for (String key : data.keySet()) {
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
        }*/

        final NotificationManager notificationManager = (NotificationManager) super.getSystemService(NOTIFICATION_SERVICE);

        Intent intent = null;
        //final String key = data.getString("", "Jiggie");
        /*data.putString("Jiggie", "mantap eh");
        data.putString("type", "match");
        data.putString("fromId", "10153418311072858");
        data.putString("toId", "140679782985703");*/

        /*utk tes sosial */
        /*data.putString("Jiggie", "Sosial");
        data.putString("type", Common.PUSH_NOTIFICATIONS_TYPE_CHAT);*/
        /*end to tes sosial*/

        String key = Common.KEY;
        final String type = data.getString(Common.KEY_TYPE);
        String message = data.getString(Common.KEY, "");
        String too = "";

        if (type.equalsIgnoreCase(Common.PUSH_NOTIFICATIONS_TYPE_GENERAL)) {
            intent = new Intent(App.getInstance(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else if (type.equalsIgnoreCase(Common.PUSH_NOTIFICATIONS_TYPE_EVENT)) {
            final String eventId = data.getString(Common.KEY_EVENT_ID, "");
            intent = new Intent(App.getInstance(), EventDetailActivity.class);
            intent.putExtra(Common.FIELD_EVENT_ID, eventId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (type.equalsIgnoreCase(Common.PUSH_NOTIFICATIONS_TYPE_MATCH)) {
            final String fromm = data.getString(Common.PUSH_NOTIFICATIONS_FROM_NAME);
            too = data.getString(Common.PUSH_NOTIFICATIONS_FROM_ID);
            intent = new Intent(App.getInstance(), ChatActivity.class);
            intent.putExtra(Conversation.FIELD_FROM_NAME, fromm);
            intent.putExtra(Conversation.FIELD_FACEBOOK_ID, too);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        //[{Jiggie=cui cui cui cui BARUU,
        //        fromId=10153418311072858, fromName=wandy
        // , toId=140679782985703, type=message, collapse_key=do_not_collapse}]
        else if (type.equalsIgnoreCase(Common.PUSH_NOTIFICATIONS_TYPE_MESSAGE)) {
            final String fromName = data.getString(Common.PUSH_NOTIFICATIONS_FROM_NAME);
            too = data.getString(Common.PUSH_NOTIFICATIONS_FROM_ID);
            intent = new Intent(App.getInstance(), ChatActivity.class);
            intent.putExtra(Conversation.FIELD_FROM_NAME, fromName);
            intent.putExtra(Conversation.FIELD_FACEBOOK_ID, too);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            //Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            );
            //set title and body text for push notifications
            final String[] values = message.split(":");
            message = values.length > 1 ? values[1].trim() : message;
            key = values.length > 1 ? values[0].trim() : getString(R.string.app_name);
        } else if (type.equalsIgnoreCase(Common.PUSH_NOTIFICATIONS_TYPE_SOCIAL)) {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Common.TO_TAB_SOCIAL, true);
        } else if (type.equalsIgnoreCase(Common.PUSH_NOTIFICATIONS_TYPE_CHAT)) {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Common.TO_TAB_CHAT, true);
        }
        //tidak dipake
        /*else if(chat){
            String profil_image="";
            for(int i=0;i< ChatManager.dataChatList.size();i++){
                if(ChatManager.dataChatList.get(i).getFb_id().equals(fromId)){
                    profil_image = ChatManager.dataChatList.get(i).getProfile_image();
                }
            }
            intent = new Intent(App.getInstance(), ChatActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Conversation.FIELD_PROFILE_IMAGE, profil_image);
            intent.putExtra(Conversation.FIELD_FROM_NAME, key);
            intent.putExtra(Conversation.FIELD_FACEBOOK_ID, fromId);
            intent.putExtra("chat", chat);
        }else{
            //match notification---------
            String fromName = "";
            try {
                fromName = message.substring(message.indexOf("with ")+5, message.length());
            }catch (Exception e){
                Utils.d("error String Arr", e.toString());
            }
            intent = new Intent(App.getInstance(), ChatActivity.class);
            intent.putExtra(Conversation.FIELD_FROM_NAME, fromName);
            intent.putExtra(Conversation.FIELD_FACEBOOK_ID, fromId);
            intent.putExtra("chat", chat);
        }*/

        if (intent != null) {
            if (!type.equalsIgnoreCase(Common.PUSH_NOTIFICATIONS_TYPE_MESSAGE) ||
                    (type.equalsIgnoreCase(Common.PUSH_NOTIFICATIONS_TYPE_MESSAGE)
                        && !App.getInstance().getIdChatActive().equalsIgnoreCase(too))) {
                final PendingIntent pendingIntent = PendingIntent.getActivity(App.getInstance(), Integer.MIN_VALUE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                final Notification notif = new NotificationCompat.BigTextStyle
                        (new NotificationCompat.Builder(this)
                                //.setCategory(Notification.CATEGORY_PROMO)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setLargeIcon(BitmapFactory.decodeResource(super.getResources(), R.mipmap.ic_launcher))
                                .setSmallIcon(R.mipmap.ic_notification)
                                .setContentIntent(pendingIntent)
                                .setContentText(message)
                                .setContentTitle(key)
                                .setAutoCancel(true)
                                .setVisibility(View.VISIBLE)
                                .setVibrate(new long[]{0, 0, 0, 0, 0}))
                        .bigText(message)
                        .build();
                notificationManager.notify(0, notif);
            }
            super.sendBroadcast(new Intent(super.getString(R.string.broadcast_notification)));
        }
    }
}
