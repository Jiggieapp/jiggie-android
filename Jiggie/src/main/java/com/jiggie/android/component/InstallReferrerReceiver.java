package com.jiggie.android.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jiggie.android.App;

/**
 * Created by rangg on 19/01/2016.
 */
public class InstallReferrerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String referrer = intent.getStringExtra("referrer");
        App.getInstance().trackMixPanelEvent("Install", new SimpleJSONObject("Referrer", referrer));
    }
}
