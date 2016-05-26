package com.jiggie.android.component.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.jiggie.android.App;

/**
 * Created by rangga on 08/08/2015.
 */
public class GCMInstanceIDListener extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        App.getSharedPreferences().edit().putBoolean(GCMRegistrationService.TAG_UPDATED, false).commit();
        final Intent intent = new Intent(this, GCMRegistrationService.class);
        super.startService(intent);
    }
}
