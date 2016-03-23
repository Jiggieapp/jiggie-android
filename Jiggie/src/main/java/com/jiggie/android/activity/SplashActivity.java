package com.jiggie.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Wandy on 3/23/2016.
 */
public class SplashActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
