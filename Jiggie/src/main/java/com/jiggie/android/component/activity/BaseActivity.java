package com.jiggie.android.component.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;

import butterknife.ButterKnife;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by rangg on 14/01/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final int THEME = R.style.AppTheme;
    private boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        super.setTheme(this.getThemeResource());
        super.onCreate(savedInstanceState);
        this.active = true;
    }

    protected int getThemeResource() { return THEME; }
    protected void bindView() { ButterKnife.bind(this); }
    protected boolean isActive() { return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) ? !super.isDestroyed() : this.active; }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            super.unregisterReceiver(receiver);
        } catch (IllegalArgumentException unused) {
            //receiver already unregistered, let's ignore it
        }
    }

    @Override
    protected void onDestroy() {
        this.active = false;
        super.onDestroy();
    }


}