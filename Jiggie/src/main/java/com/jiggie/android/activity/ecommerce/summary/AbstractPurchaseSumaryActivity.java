package com.jiggie.android.activity.ecommerce.summary;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarWithDotActivity;
import com.jiggie.android.model.Common;

import butterknife.Bind;

/**
 * Created by LTE on 3/28/2016.
 */
public abstract class AbstractPurchaseSumaryActivity extends ToolbarWithDotActivity {

    @Bind(R.id.rel_table_det)
    RelativeLayout relTableDet;
    @Bind(R.id.txt_total)
    TextView txtTotal;
    @Bind(R.id.rel_ticket_det)
    RelativeLayout relTicketDet;
    @Bind(R.id.minus_button)
    View minusButton;
    @Bind(R.id.plus_button)
    View plusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.bindView();

        if(getTransactionType().equals(Common.TYPE_PURCHASE)){
            relTicketDet.setVisibility(View.VISIBLE);
        }else if(getTransactionType().equals(Common.TYPE_RESERVATION)){
            relTableDet.setVisibility(View.VISIBLE);
            minusButton.setVisibility(View.VISIBLE);
            plusButton.setVisibility(View.VISIBLE);
        }

        txtTotal.setText(getTotalCaption());
    }

    public abstract String getTotalCaption();
    public abstract String getTransactionType();
}
