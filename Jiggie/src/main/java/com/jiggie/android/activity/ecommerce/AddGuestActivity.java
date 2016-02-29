package com.jiggie.android.activity.ecommerce;

import android.os.Bundle;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;

/**
 * Created by LTE on 2/29/2016.
 */
public class AddGuestActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guest);
    }
}
