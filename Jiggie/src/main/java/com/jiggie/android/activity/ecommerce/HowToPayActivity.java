package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.SucScreenVABPModel;
import com.jiggie.android.model.SucScreenWalkthroughModel;
import com.jiggie.android.view.ContainerStepView;
import com.jiggie.android.view.StepView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/6/2016.
 */
public class HowToPayActivity extends ToolbarActivity{

    RelativeLayout rel_view_orders;
    TextView txt_t_limit_fill, txt_t_amount_fill, txt_howtopay;
    CountDownTimer countDownTimer;
    LinearLayout lin_con_step;
    long order_id;
    boolean isWalkthrough;
    String payment_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtopay);

        Intent a = getIntent();
        isWalkthrough = a.getBooleanExtra(Common.FIELD_WALKTHROUGH_PAYMENT, false);
        order_id = a.getLongExtra(Common.FIELD_ORDER_ID, 0);
        //payment_type = a.getStringExtra(Common.FIELD_PAYMENT_TYPE);

        initView();

    }

    private void initView(){
        txt_t_amount_fill = (TextView) findViewById(R.id.txt_t_amount_fill);
        txt_t_limit_fill = (TextView) findViewById(R.id.txt_t_limit_fill);
        txt_howtopay = (TextView) findViewById(R.id.txt_howtopay);
        rel_view_orders = (RelativeLayout) findViewById(R.id.rel_view_orders);
        lin_con_step = (LinearLayout)findViewById(R.id.lin_con_step);

        rel_view_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HowToPayActivity.this, PurchaseHistoryActivity.class));
            }
        });

        if(isWalkthrough){
            rel_view_orders.setVisibility(View.GONE);
            CommerceManager.loaderSucScreenWalkthrough(new CommerceManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    SucScreenWalkthroughModel sucScreenVABPModel = (SucScreenWalkthroughModel)object;
                    ArrayList<SucScreenWalkthroughModel.Data.WalkthroughPayment.BpStep.Step_payment> stepPaymentBP = sucScreenVABPModel.getData().getWalkthrough_payment().getBp_step().getStep_payment();
                    ArrayList<SucScreenWalkthroughModel.Data.WalkthroughPayment.VaStep.Step_payment> stepPaymentVA = sucScreenVABPModel.getData().getWalkthrough_payment().getVa_step().getStep_payment();
                    int sizeStepBP = stepPaymentBP.size();
                    int sizeStepVA = stepPaymentVA.size();

                    for(int i=0;i<sizeStepBP;i++){
                        ContainerStepView containerStepView;
                        String header = stepPaymentBP.get(i).getHeader();
                        containerStepView = new ContainerStepView(HowToPayActivity.this, header, stepPaymentBP.get(i).getStep());
                        lin_con_step.addView(containerStepView);
                    }

                    for(int i=0;i<sizeStepVA;i++){
                        ContainerStepView containerStepView;
                        String header = stepPaymentVA.get(i).getHeader();
                        containerStepView = new ContainerStepView(HowToPayActivity.this, header, stepPaymentVA.get(i).getStep());
                        lin_con_step.addView(containerStepView);
                    }

                }

                @Override
                public void onFailure(int responseCode, String message) {

                }
            });
        }else{

            CommerceManager.loaderSucScreenVABP(String.valueOf(order_id), new CommerceManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    SucScreenVABPModel sucScreenVABPModel = (SucScreenVABPModel)object;
                    ArrayList<SucScreenVABPModel.Data.SuccessScreen.StepPayment> stepPayment = sucScreenVABPModel.getData().getSuccess_screen().getStep_payment();
                    txt_t_amount_fill.setText(StringUtility.getRupiahFormat(sucScreenVABPModel.getData().getSuccess_screen().getAmount()));

                    payment_type = sucScreenVABPModel.getData().getSuccess_screen().getPayment_type();
                    if(payment_type.equals(Utils.TYPE_BP)){
                        txt_howtopay.setText(getString(R.string.va_mandiri));
                    }else if(payment_type.equals(Utils.TYPE_VA)){
                        txt_howtopay.setText(getString(R.string.other_bank));
                    }

                    int sizeStep = stepPayment.size();
                    for(int i=0;i<sizeStep;i++){
                        ContainerStepView containerStepView;
                        String header = stepPayment.get(i).getHeader();
                        containerStepView = new ContainerStepView(HowToPayActivity.this, header, stepPayment.get(i).getStep());
                        lin_con_step.addView(containerStepView);
                    }

                    countDownTimer = new CountDownTimer(StringUtility.getCountdownTime(sucScreenVABPModel.getData().getSuccess_screen().getTimelimit()), 1000) {

                        long hour, minute;
                        long second;
                        boolean isFirstTime = true;

                        @Override
                        public void onTick(long millisUntilFinished) {

                            if(isFirstTime){
                                hour = ((millisUntilFinished/1000)/60)/60;
                                minute = ((millisUntilFinished/1000)/60)/3;
                                second = TimeUnit.MILLISECONDS.toSeconds(minute);
                            }

                            if(minute==0){
                                minute = 59;
                                hour--;
                            }
                            if(second==0){
                                second = 60;
                                if(!isFirstTime)
                                    minute--;
                            }

                            if(isFirstTime)
                                isFirstTime = false;

                            second--;
                            txt_t_limit_fill.setText(StringUtility.getTimeFormat(hour, minute, second));
                        }

                        @Override
                        public void onFinish() {
                            txt_t_limit_fill.setText("Expired");
                        }
                    }.start();
                }

                @Override
                public void onFailure(int responseCode, String message) {

                }
            });
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isWalkthrough){
            countDownTimer.cancel();
        }

    }
}
