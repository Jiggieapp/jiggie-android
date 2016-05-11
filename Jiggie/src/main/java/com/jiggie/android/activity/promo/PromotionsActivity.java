package com.jiggie.android.activity.promo;

import android.os.Bundle;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;

/**
 * Created by LTE on 5/11/2016.
 */
public class PromotionsActivity extends ToolbarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_invite_friend_code);
        super.bindView();
    }
}
