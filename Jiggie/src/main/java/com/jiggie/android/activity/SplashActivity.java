package com.jiggie.android.activity;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.component.Utils;
import com.jiggie.android.listener.AlarmReceiver;
import com.jiggie.android.manager.AccountManager;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

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

        if(AccessToken.getCurrentAccessToken() != null
                && AccessToken.getCurrentAccessToken().getUserId() != null
                && !AccessToken.getCurrentAccessToken().getUserId().isEmpty())
        {
            if(!AccountManager.getHasSetAlarm())
            {
                AccountManager.setHasSetAlarm(true);
                setRecurringAlarm(this);
            }
            /*Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {

                }
            };
            timer.schedule(timerTask, getRemainingTimeUntilMidnight());*/
        }
        Intent i = new Intent(this, MainActivity.class);
        //Intent i = new Intent(this, EditProfileActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private long getRemainingTimeUntilMidnight() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTimeInMillis() - System.currentTimeMillis();
    }

    private void setRecurringAlarm(Context context) {
        // we know mobiletuts updates at right around 1130 GMT.
        // let's grab new stuff at around 11:45 GMT, inexactly

        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getDefault());
        updateTime.set(Calendar.HOUR_OF_DAY, 23);
        updateTime.set(Calendar.MINUTE, 59);

        Intent downloader = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
                0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) this.getSystemService(
                Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, recurringDownload);
    }

}
