package com.jiggie.android.activity.ticket;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jiggie.android.R;
import com.jiggie.android.view.CustomToolbar;

public class TicketDetailActivity extends AbstractTicketDetailActivity {

    public static final String TAG = TicketDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate() {
        super.setContentView(R.layout.activity_ticket_detail);
        super.bindView();
    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.ticket_detail);
    }

    @Override
    protected int getCurrentStep() {
        return 2;
    }
}
