package com.jiggie.android.activity.ecommerce.ticket;

import com.jiggie.android.R;

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

    @Override
    public String getEventCaption() {
        //return getResources().getString("Information about regular classs");
        return "Information about regular classs";
    }

    @Override
    public String getTypePriceCaption() {
        return "1 Person";
    }

    @Override
    public String getEstimatedCostCaption() {
        return "Estimated Costs";
    }

    @Override
    public String getTicketCaption() {
        return "Tickets";
    }
}
