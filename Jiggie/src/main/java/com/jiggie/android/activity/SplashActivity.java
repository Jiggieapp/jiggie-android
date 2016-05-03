package com.jiggie.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jiggie.android.App;
import com.jiggie.android.activity.ecommerce.CongratsActivity;
import com.jiggie.android.activity.ecommerce.HowToPayActivity;
import com.jiggie.android.activity.ecommerce.TermsConditionActivity;
import com.jiggie.android.activity.profile.NewProfileDetailActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.Common;

/**
 * Created by Wandy on 3/23/2016.
 */
public class SplashActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //App.getSharedPreferences().edit().putBoolean(Utils.PREFERENCE_GPS, false).commit();
        App.runningActivity = this;

        Intent i = new Intent(this, MainActivity.class);
        //Intent i = new Intent(this, NewProfileDetailActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }


}
