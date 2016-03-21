package com.jiggie.android.activity.ecommerce.ticket;

import com.jiggie.android.R;

/**
 * Created by Wandy on 3/8/2016.
 */
public class ReservationActivity extends AbstractTicketDetailActivity{

    @Override
    public String getEstimatedCostCaption() {
        return "Minimum spend";
    }

    @Override
    public String getTicketCaption() {
        return "Number of guests";
    }

    @Override
    protected void onCreate() {
        super.setContentView(R.layout.activity_ticket_detail);
        super.bindView();
    }

    @Override
    protected String getToolbarTitle() {
        //return getResources().getString(R.string.ticket);
        return "";
    }

    @Override
    protected int getCurrentStep() {
        return 2;
    }
}
