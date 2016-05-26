package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/6/2016.
 */
public class HowToPayActivity extends ToolbarActivity {

    /*RelativeLayout rel_view_orders;
    TextView txt_t_amount_fill, txt_howtopay, txt_to_fill;
    TextView txt_t_limit_fill;
    ImageView img_close;*/
    CountDownTimer countDownTimer;
    //LinearLayout lin_con_step;
    long order_id;
    boolean isWalkthrough = false, fromOrderList;
    String payment_type;
    EventDetailModel.Data.EventDetail eventDetail;
    PurchaseHistoryModel.Data.Order_list.Event event;
    SummaryModel.Data.Product_summary productSummary;
    SucScreenVABPModel sucScreenVABPModel;
    PurchaseHistoryModel.Data.Order_list.Order order;
    private final static String TAG = HowToPayActivity.class.getSimpleName();
    @Bind(R.id.txt_t_limit_title)
    TextView txtTLimitTitle;
    @Bind(R.id.txt_t_limit_fill)
    TextView txtTLimitFill;
    @Bind(R.id.txt_t_amount_title)
    TextView txtTAmountTitle;
    @Bind(R.id.txt_t_amount_fill)
    TextView txtTAmountFill;
    @Bind(R.id.txt_to_title)
    TextView txtToTitle;
    @Bind(R.id.txt_to_fill)
    TextView txtToFill;
    @Bind(R.id.txt_jiggie)
    TextView txtJiggie;
    @Bind(R.id.lin_con_step)
    LinearLayout linConStep;
    @Bind(R.id.txt_view_orders)
    TextView txtViewOrders;
    @Bind(R.id.rel_view_orders)
    RelativeLayout relViewOrders;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.rel_content)
    RelativeLayout relContent;
    @Bind(R.id.appBar)
    AppBarLayout appBar;
    //RelativeLayout relContent;
    //ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtopay);
        ButterKnife.bind(this);
        super.bindView();

        Intent a = getIntent();
        isWalkthrough = a.getBooleanExtra(Common.FIELD_WALKTHROUGH_PAYMENT, false);
        fromOrderList = a.getBooleanExtra(Common.FIELD_FROM_ORDER_LIST, false);
        order_id = a.getLongExtra(Common.FIELD_ORDER_ID, 0);

        if (fromOrderList) {
            event = a.getParcelableExtra(PurchaseHistoryModel.Data.Order_list.Event.class.getName());
            order = a.getParcelableExtra(PurchaseHistoryModel.Data.Order_list.Order.class.getName());
        } else {
            productSummary = a.getParcelableExtra(SummaryModel.Data.Product_summary.class.getName());
            eventDetail = a.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        }

        initView();
    }

    private void setTitles(String title) {
        super.setToolbarTitle(title, true);
    }

    private void initView() {

        relViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HowToPayActivity.this, PurchaseHistoryActivity.class);
                i.putExtra(Common.FIELD_FROM_HOWTOPAY, true);
                startActivity(i);
            }
        });

        appBar.setVisibility(View.GONE);
        relContent.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        setTitles(getString(R.string.howtp_title));
        if (isWalkthrough) {
            sendMixpanel(productSummary, eventDetail);

            relViewOrders.setVisibility(View.GONE);
            CommerceManager.loaderSucScreenWalkthrough(new CommerceManager.OnResponseListener() {

                @Override
                public void onSuccess(Object object) {
                    SucScreenWalkthroughModel sucScreenVABPModel = (SucScreenWalkthroughModel) object;
                    if (sucScreenVABPModel != null) {
                        ArrayList<SucScreenWalkthroughModel.Data.WalkthroughPayment.BpStep.Step_payment> stepPaymentBP = sucScreenVABPModel.getData().getWalkthrough_payment().getBp_step().getStep_payment();
                        ArrayList<SucScreenWalkthroughModel.Data.WalkthroughPayment.VaStep.Step_payment> stepPaymentVA = sucScreenVABPModel.getData().getWalkthrough_payment().getVa_step().getStep_payment();
                        int sizeStepBP = stepPaymentBP.size();
                        int sizeStepVA = stepPaymentVA.size();

                        for (int i = 0; i < sizeStepBP; i++) {
                            ContainerStepView containerStepView;
                            String header = stepPaymentBP.get(i).getHeader();
                            containerStepView = new ContainerStepView(HowToPayActivity.this, header, stepPaymentBP.get(i).getStep());
                            linConStep.addView(containerStepView);
                        }

                        for (int i = 0; i < sizeStepVA; i++) {
                            ContainerStepView containerStepView;
                            String header = stepPaymentVA.get(i).getHeader();
                            containerStepView = new ContainerStepView(HowToPayActivity.this, header, stepPaymentVA.get(i).getStep());
                            linConStep.addView(containerStepView);
                        }
                    } else {
                        Toast.makeText(HowToPayActivity.this, getString(R.string.msg_wrong), Toast.LENGTH_LONG).show();
                    }
                    appBar.setVisibility(View.VISIBLE);
                    relContent.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    appBar.setVisibility(View.VISIBLE);
                    relContent.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            if (fromOrderList) {
                relViewOrders.setVisibility(View.GONE);
            }
            CommerceManager.loaderSucScreenVABP(String.valueOf(order_id), new CommerceManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    sucScreenVABPModel = (SucScreenVABPModel) object;
                    if (sucScreenVABPModel != null) {
                        sendMixpanel(sucScreenVABPModel);
                        ArrayList<SucScreenVABPModel.Data.SuccessScreen.StepPayment> stepPayment = sucScreenVABPModel.getData().getSuccess_screen().getStep_payment();
                        txtTAmountFill.setText(StringUtility.getRupiahFormat(sucScreenVABPModel.getData().getSuccess_screen().getAmount()));

                        payment_type = sucScreenVABPModel.getData().getSuccess_screen().getPayment_type();
                        if (payment_type.equals(Utils.TYPE_BP)) {
                            setTitles(getString(R.string.va_mandiri));
                        } else if (payment_type.equals(Utils.TYPE_VA)) {
                            setTitles(getString(R.string.other_bank));
                        } else if (payment_type.equals(Utils.TYPE_BCA)) {
                            setTitles(getString(R.string.va_bca));
                        }

                        txtToFill.setText(StringUtility.getCCNumberFormat(sucScreenVABPModel.getData().getSuccess_screen().getTransfer_to()));
                        int sizeStep = stepPayment.size();
                        for (int i = 0; i < sizeStep; i++) {
                            ContainerStepView containerStepView;
                            String header = stepPayment.get(i).getHeader();
                            containerStepView = new ContainerStepView(HowToPayActivity.this, header, stepPayment.get(i).getStep());
                            linConStep.addView(containerStepView);
                        }

                        countDownTimer = new CountDownTimer(StringUtility.getCountdownTime(sucScreenVABPModel.getData().getSuccess_screen().getTimelimit()), 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                txtTLimitFill.setText(StringUtility.getTimeFormat(millisUntilFinished));
                            }

                            @Override
                            public void onFinish() {
                                txtTLimitFill.setText("Expired");
                            }
                        }.start();

                    } else {
                        Toast.makeText(HowToPayActivity.this, getString(R.string.msg_wrong), Toast.LENGTH_LONG).show();
                    }
                    appBar.setVisibility(View.VISIBLE);
                    relContent.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    appBar.setVisibility(View.VISIBLE);
                    relContent.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!isWalkthrough) {
                    if (!fromOrderList) {
                        Intent i = new Intent(HowToPayActivity.this, MainActivity.class);
                        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMixpanel(SummaryModel.Data.Product_summary productSummary, EventDetailModel.Data.EventDetail eventDetail) {
        CommEventMixpanelModel commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), productSummary.getProduct_list().get(0).getName(), productSummary.getProduct_list().get(0).getTicket_type(),
                productSummary.getTotal_price(), productSummary.getProduct_list().get(0).getMax_buy());
        App.getInstance().trackMixPanelCommerce(Utils.COMM_VA_INSTRUCTION, commEventMixpanelModel);
    }

    private void sendMixpanel(SucScreenVABPModel sucScreenVABPModel) {
        SucScreenVABPModel.Data.SuccessScreen successScreen = sucScreenVABPModel.getData().getSuccess_screen();
        //reservation type not handle yet
        CommEventMixpanelModel commEventMixpanelModel = null;
        if (fromOrderList) {
            commEventMixpanelModel = new CommEventMixpanelModel(event.getTitle(), event.getVenue_name(), event.getLocation(), event.getStart_datetime_str(),
                    event.getEnd_datetime_str(), event.getTags(), event.getDescription(), order.getProduct_list().get(0).getName(), order.getProduct_list().get(0).getTicket_type(),
                    order.getTotal_price(), order.getProduct_list().get(0).getMax_buy(), successScreen.getCreated_at(), order.getProduct_list().get(0).getNum_buy(),
                    successScreen.getAmount(), "0", successScreen.getType(), Utils.BLANK, false);
        } else {
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
        if (!isWalkthrough) {
            if (countDownTimer != null)
                countDownTimer.cancel();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isWalkthrough) {
            if (!fromOrderList) {
                Intent i = new Intent(HowToPayActivity.this, MainActivity.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }
}
