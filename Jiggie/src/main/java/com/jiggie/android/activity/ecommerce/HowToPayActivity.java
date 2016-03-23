package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.PurchaseHistoryModel;
import com.jiggie.android.model.SucScreenVABPModel;
import com.jiggie.android.model.SucScreenWalkthroughModel;
import com.jiggie.android.model.SummaryModel;
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
    TextView txt_t_limit_fill, txt_t_amount_fill, txt_howtopay, txt_close, txt_to_fill;
    CountDownTimer countDownTimer;
    LinearLayout lin_con_step;
    long order_id;
    boolean isWalkthrough = false, fromOrderList;
    String payment_type;
    EventDetailModel.Data.EventDetail eventDetail;
    PurchaseHistoryModel.Data.Order_list.Event event;
    SummaryModel.Data.Product_summary productSummary;
    SucScreenVABPModel sucScreenVABPModel;
    PurchaseHistoryModel.Data.Order_list.Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtopay);

        Intent a = getIntent();
        isWalkthrough = a.getBooleanExtra(Common.FIELD_WALKTHROUGH_PAYMENT, false);
        fromOrderList = a.getBooleanExtra(Common.FIELD_FROM_ORDER_LIST, false);
        order_id = a.getLongExtra(Common.FIELD_ORDER_ID, 0);

        if(fromOrderList){
            event = a.getParcelableExtra(PurchaseHistoryModel.Data.Order_list.Event.class.getName());
            order = a.getParcelableExtra(PurchaseHistoryModel.Data.Order_list.Order.class.getName());
        }else {
            productSummary = a.getParcelableExtra(SummaryModel.Data.Product_summary.class.getName());
            eventDetail = a.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        }

        initView();

    }

    private void initView(){
        txt_t_amount_fill = (TextView) findViewById(R.id.txt_t_amount_fill);
        txt_t_limit_fill = (TextView) findViewById(R.id.txt_t_limit_fill);
        txt_howtopay = (TextView) findViewById(R.id.txt_howtopay);
        txt_to_fill = (TextView) findViewById(R.id.txt_to_fill);
        rel_view_orders = (RelativeLayout) findViewById(R.id.rel_view_orders);
        lin_con_step = (LinearLayout)findViewById(R.id.lin_con_step);
        txt_close = (TextView)findViewById(R.id.txt_close);

        rel_view_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HowToPayActivity.this, PurchaseHistoryActivity.class);
                i.putExtra(Common.FIELD_FROM_HOWTOPAY, true);
                startActivity(i);
            }
        });

        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isWalkthrough){
                    if(!fromOrderList){
                        Intent i = new Intent(HowToPayActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }else{
                        finish();
                    }
                }else{
                    finish();
                }

            }
        });

        if(isWalkthrough){
            sendMixpanel(productSummary, eventDetail);
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
            if(fromOrderList){
                rel_view_orders.setVisibility(View.GONE);
            }
            CommerceManager.loaderSucScreenVABP(String.valueOf(order_id), new CommerceManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    sucScreenVABPModel = (SucScreenVABPModel)object;
                    sendMixpanel(sucScreenVABPModel);
                    ArrayList<SucScreenVABPModel.Data.SuccessScreen.StepPayment> stepPayment = sucScreenVABPModel.getData().getSuccess_screen().getStep_payment();
                    txt_t_amount_fill.setText(StringUtility.getRupiahFormat(sucScreenVABPModel.getData().getSuccess_screen().getAmount()));

                    payment_type = sucScreenVABPModel.getData().getSuccess_screen().getPayment_type();
                    if(payment_type.equals(Utils.TYPE_BP)){
                        txt_howtopay.setText(getString(R.string.va_mandiri));
                    }else if(payment_type.equals(Utils.TYPE_VA)){
                        txt_howtopay.setText(getString(R.string.other_bank));
                    }

                    txt_to_fill.setText(sucScreenVABPModel.getData().getSuccess_screen().getTransfer_to());
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

    private void sendMixpanel(SummaryModel.Data.Product_summary productSummary, EventDetailModel.Data.EventDetail eventDetail){
        CommEventMixpanelModel commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), productSummary.getProduct_list().get(0).getName(), productSummary.getProduct_list().get(0).getTicket_type(),
                productSummary.getTotal_price(), productSummary.getProduct_list().get(0).getMax_buy());
        App.getInstance().trackMixPanelCommerce(Utils.COMM_VA_INSTRUCTION, commEventMixpanelModel);
    }

    private void sendMixpanel(SucScreenVABPModel sucScreenVABPModel){
        SucScreenVABPModel.Data.SuccessScreen successScreen = sucScreenVABPModel.getData().getSuccess_screen();
        //reservation type not handle yet
        CommEventMixpanelModel commEventMixpanelModel = null;
        if(fromOrderList){
            commEventMixpanelModel = new CommEventMixpanelModel(event.getTitle(), event.getVenue_name(), event.getLocation(), event.getStart_datetime_str(),
                    event.getEnd_datetime_str(), event.getTags(), event.getDescription(), order.getProduct_list().get(0).getName(), order.getProduct_list().get(0).getTicket_type(),
                    order.getTotal_price(), order.getProduct_list().get(0).getMax_buy(), successScreen.getCreated_at(), order.getProduct_list().get(0).getNum_buy(),
                    successScreen.getAmount(), "0", successScreen.getType(), Utils.BLANK, false);
        }else{
            commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                    eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), productSummary.getProduct_list().get(0).getName(), productSummary.getProduct_list().get(0).getTicket_type(),
                    productSummary.getTotal_price(), productSummary.getProduct_list().get(0).getMax_buy(), successScreen.getCreated_at(), productSummary.getProduct_list().get(0).getNum_buy(),
                    successScreen.getAmount(), "0", successScreen.getType(), Utils.BLANK, false);
        }

        App.getInstance().trackMixPanelCommerce(Utils.COMM_FINISH_VA, commEventMixpanelModel);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isWalkthrough){
            countDownTimer.cancel();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!isWalkthrough){
            if(!fromOrderList){
                Intent i = new Intent(HowToPayActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }else {
                finish();
            }
        }else {
            finish();
        }

    }
}
