package com.jiggie.android.activity.ticket;

import android.os.Bundle;

import com.jiggie.android.component.activity.ToolbarWithDotActivity;

public abstract class AbstractTicketDetailActivity extends ToolbarWithDotActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ButterKnife.bind(this);
        //tb.setTitle(getResources().getString(R.string.ticket_detail));
    }
}
