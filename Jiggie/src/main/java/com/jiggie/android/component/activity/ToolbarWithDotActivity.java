package com.jiggie.android.component.activity;

import android.os.Bundle;

import com.jiggie.android.R;
import com.jiggie.android.view.CustomToolbar;

import butterknife.Bind;

/**
 * Created by Wandy on 2/25/2016.
 */
public abstract class ToolbarWithDotActivity extends ToolbarActivity{

    @Bind(R.id.customToolbar)
    CustomToolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate();
        super.bindView();
        setSupportActionBar(tb);
        tb.setTitle(getToolbarTitle());
        tb.setSelected(getCurrentStep());
    }

    protected abstract void onCreate();
    protected abstract String getToolbarTitle();
    protected abstract int getCurrentStep();
}
