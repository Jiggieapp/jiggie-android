package com.jiggie.android.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.SettingModel;

/**
 * Created by Wandy on 6/9/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String DEBUG_TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        SettingModel settingModel = AccountManager.loadSetting();
        settingModel.getData().getCityList().clear();
        AccountManager.saveSetting(settingModel);
    }

}
