package com.jiggie.android.activity.ecommerce.ticket;

import android.os.Bundle;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarWithDotActivity;

import butterknife.Bind;

public abstract class AbstractTicketDetailActivity extends ToolbarWithDotActivity {

    /*@Bind(R.id.lblTypeCaption)
    TextView lblEventCaption;
    @Bind(R.id.lblTypePriceCaption) TextView lblTypePriceCaption;*/
    @Bind(R.id.lblEstimatedCostCaption) TextView lblEstimatedCostCaption;
    @Bind(R.id.lblTicketCaption) TextView lblTicketCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ButterKnife.bind(this);
        //tb.setTitle(getResources().getString(R.string.ticket_detail));
        super.bindView();

        //lblEventCaption.setText(getEventCaption());
        //lblTypePriceCaption.setText(getTypePriceCaption());
        lblEstimatedCostCaption.setText(getEstimatedCostCaption());
        lblTicketCaption.setText(getTicketCaption());
    }

    /*public abstract String getEventCaption();
    public abstract String getTypePriceCaption();*/
    public abstract String getEstimatedCostCaption();
    public abstract String getTicketCaption();
}
