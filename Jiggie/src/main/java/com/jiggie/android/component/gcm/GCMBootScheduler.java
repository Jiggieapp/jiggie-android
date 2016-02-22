package com.jiggie.android.component.gcm;

import android.content.Intent;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by rangg on 23/12/2015.
 */
public class GCMBootScheduler extends GcmTaskService {
    public static String TAG = GCMBootScheduler.class.getName();

    @Override
    public int onRunTask(TaskParams taskParams) {
        super.startService(new Intent(this, GCMRegistration.class));
        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
