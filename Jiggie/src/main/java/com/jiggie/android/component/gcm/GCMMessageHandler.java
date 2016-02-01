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

        final NotificationManager notificationManager = (NotificationManager) super.getSystemService(NOTIFICATION_SERVICE);
        final Intent intent = new Intent(App.getInstance(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("chat", chat);
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
