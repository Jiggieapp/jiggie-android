package com.jiggie.android.component.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.jiggie.android.App;

/**
 * Created by rangg on 23/12/2015.
 */
public class GCMBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final GcmNetworkManager gcmNetworkManager = GcmNetworkManager.getInstance(App.getInstance());
        final OneoffTask task = new OneoffTask.Builder()
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setService(GCMBootScheduler.class)
                .setExecutionWindow(0L, 1L)
                .setTag(GCMBootScheduler.TAG)
                .build();
        gcmNetworkManager.cancelTask(GCMBootScheduler.TAG, GCMBootScheduler.class);
        gcmNetworkManager.schedule(task);
    }
}
