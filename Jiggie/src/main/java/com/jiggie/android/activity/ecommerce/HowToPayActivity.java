package com.jiggie.android.activity.ecommerce;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.activity.ToolbarActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by LTE on 3/6/2016.
 */
public class HowToPayActivity extends ToolbarActivity{

    TextView txt_t_limit_fill;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtopay);

        txt_t_limit_fill = (TextView)findViewById(R.id.txt_t_limit_fill);
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txt_t_limit_fill.setText(StringUtility.getTimeFormat(millisUntilFinished));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }



    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }
}
