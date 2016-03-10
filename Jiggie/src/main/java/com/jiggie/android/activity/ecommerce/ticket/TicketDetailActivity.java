package com.jiggie.android.activity.ecommerce.ticket;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.AddGuestActivity;
import com.jiggie.android.activity.ecommerce.PurchaseInfoActivity;

import butterknife.Bind;

public class TicketDetailActivity extends AbstractTicketDetailActivity {

    public static final String TAG = TicketDetailActivity.class.getSimpleName();
    @Bind(R.id.rel_guest)
    RelativeLayout relGuest;
    @Bind(R.id.btnDone)
    Button btnDone;


    @Override
    protected void onCreate() {
        super.setContentView(R.layout.activity_ticket_detail);
        super.bindView();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TicketDetailActivity.this, PurchaseInfoActivity.class));
            }
        });

        relGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TicketDetailActivity.this, AddGuestActivity.class));
            }
        });
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
        return "Information about regular class";
    }

    @Override
    public String getTypePriceCaption() {
        return "1 PER PERSON";
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
