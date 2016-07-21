package com.jiggie.android.activity.player;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.SoundcloudModel;

public class PlayerActivity extends AppCompatActivity  {

    private final String TAG = PlayerActivity.class.getSimpleName();
    PlayerPresenterImplementation implementation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
    }
}
