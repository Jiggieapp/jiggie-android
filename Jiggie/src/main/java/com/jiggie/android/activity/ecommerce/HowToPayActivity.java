package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.activity.ToolbarActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/6/2016.
 */
public class HowToPayActivity extends ToolbarActivity {

    RelativeLayout rel_view_orders;
    TextView txt_t_limit_fill;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtopay);

        txt_t_limit_fill = (TextView) findViewById(R.id.txt_t_limit_fill);
        rel_view_orders = (RelativeLayout)findViewById(R.id.rel_view_orders);
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txt_t_limit_fill.setText(StringUtility.getTimeFormat(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                txt_t_limit_fill.setText("Expired");
            }
        }.start();

        rel_view_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HowToPayActivity.this, PurchaseHistoryActivity.class));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }
}
