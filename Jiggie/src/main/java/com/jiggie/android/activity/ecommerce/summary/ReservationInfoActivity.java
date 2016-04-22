package com.jiggie.android.activity.ecommerce.summary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.CongratsActivity;
import com.jiggie.android.activity.ecommerce.HowToPayActivity;
import com.jiggie.android.activity.ecommerce.PaymentMethodActivity;
import com.jiggie.android.activity.ecommerce.ProductListActivity;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.fragment.SlideFragment;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CCScreenModel;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.PostFreePaymentModel;
import com.jiggie.android.model.PostPaymentModel;
import com.jiggie.android.model.SummaryModel;
import com.jiggie.android.view.TermsItemView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.co.veritrans.android.api.VTDirect;
import id.co.veritrans.android.api.VTInterface.ITokenCallback;
import id.co.veritrans.android.api.VTModel.VTCardDetails;
import id.co.veritrans.android.api.VTModel.VTToken;
import id.co.veritrans.android.api.VTUtil.VTConfig;

/**
 * Created by LTE on 3/28/2016.
 */
public class ReservationInfoActivity extends AbstractPurchaseSumaryActivity {

    @Bind(R.id.txt_event_name)
    TextView txtEventName;
    @Bind(R.id.txt_event_info)
    TextView txtEventInfo;
    @Bind(R.id.txt_dft_title)
    TextView txtDftTitle;
    @Bind(R.id.txt_dft_fill)
    TextView txtDftFill;
    @Bind(R.id.txt_taxx_title)
    TextView txtTaxxTitle;
    @Bind(R.id.txt_taxx_fill)
    TextView txtTaxxFill;
    @Bind(R.id.txt_ser_title)
    TextView txtSerTitle;
    @Bind(R.id.txt_ser_fill)
    TextView txtSerFill;
    @Bind(R.id.txt_est_tot_title)
    TextView txtEstTotTitle;
    @Bind(R.id.txt_est_tot_fill)
    TextView txtEstTotFill;
    @Bind(R.id.txt_require_title)
    TextView txtRequireTitle;
    @Bind(R.id.txt_require_fill)
    TextView txtRequireFill;
    @Bind(R.id.txt_est_bal_title)
    TextView txtEstBalTitle;
    @Bind(R.id.txt_est_bal_fill)
    TextView txtEstBalFill;

    @Bind(R.id.txt_total_fill)
    TextView txtTotalFill;
    @Bind(R.id.img_payment)
    ImageView imgPayment;
    @Bind(R.id.txt_payment)
    TextView txtPayment;
    @Bind(R.id.rel_payment)
    RelativeLayout relPayment;
    @Bind(R.id.pager_slide)
    ViewPager pagerSlide;
    @Bind(R.id.rel_disable)
    RelativeLayout relDisable;
    @Bind(R.id.txt_total_ticket_fill)
    TextView txtTotalTicketFill;
    @Bind(R.id.txt_event_info_date)
    TextView txtEventInfoDate;

    SummaryModel.Data.Product_summary productSummary;
    EventDetailModel.Data.EventDetail eventDetail;
    /*@Bind(R.id.lin_terms)
    LinearLayout linTerms;*/

    String eventId, eventName, venueName, startTime, totalPrice, minDeposit;
    ArrayList<TermsItemView> arrTermItemView = new ArrayList<>();
    String is_new_card, cc_token_id = Utils.BLANK, cc_card_id, paymentType = Utils.BLANK, name_cc = Utils.BLANK;
    boolean is_verified;
    long order_id;

    AlertDialog dialog3ds;
    ProgressDialog progressDialog;
    public final static String PAYMENT_API = "https://api.veritrans.co.id/v2/token";
    public final static String PAYMENT_API_SANDBOX = "https://api.sandbox.veritrans.co.id/v2/token";
    CCScreenModel.CardDetails cardDetails;
    @Bind(R.id.minus_button)
    RelativeLayout minusButton;
    @Bind(R.id.plus_button)
    RelativeLayout plusButton;
    @Bind(R.id.card_view)
    CardView cardView;
    private SlideAdapter slideAdapter;
    int payDeposit = 0, maxDeposit = 0, latestDeposit = 0;
    private final int INCREMENT_VALUE = 500000;
    boolean isPaying = false;
    private static final String TAG = ReservationInfoActivity.class.getSimpleName();

    public static String getPaymentApiUrl() {
        if (VTConfig.VT_IsProduction) {
            return PAYMENT_API;
        }
        return PAYMENT_API_SANDBOX;
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_purchase_info);
        ButterKnife.bind(this);

        preDefined();

        relPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReservationInfoActivity.this, PaymentMethodActivity.class);
                i.putExtra(Common.FIELD_ORDER_ID, order_id);
                i.putExtra(Common.FIELD_PAYMENT_TYPE, paymentType);
                i.putExtra(Common.FIELD_PRICE, String.valueOf(payDeposit));
                i.putExtra(productSummary.getClass().getName(), productSummary);
                i.putExtra(eventDetail.getClass().getName(), eventDetail);
                startActivityForResult(i, 0);
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrangeEstimateDeposit(false);
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrangeEstimateDeposit(true);
            }
        });
    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.purchase_info).toUpperCase();
    }

    private void preDefined() {
        slideAdapter = new SlideAdapter(getSupportFragmentManager(), pagerSlide);
        pagerSlide.setAdapter(slideAdapter);
        pagerSlide.setCurrentItem(1);

        Intent a = getIntent();
        eventId = a.getStringExtra(Common.FIELD_EVENT_ID);
        eventName = a.getStringExtra(Common.FIELD_EVENT_NAME);
        venueName = a.getStringExtra(Common.FIELD_VENUE_NAME);
        startTime = a.getStringExtra(Common.FIELD_STARTTIME);
        minDeposit = a.getStringExtra(Common.FIELD_MIN_DEPOSIT);
        productSummary = a.getParcelableExtra(SummaryModel.Data.Product_summary.class.getName());
        eventDetail = a.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        order_id = productSummary.getOrder_id();
        SummaryModel.Data.Product_summary.Product_list dataProduct = productSummary.getProduct_list().get(0);

        sendMixpanel(productSummary, eventDetail);

        relTableDet.setVisibility(View.VISIBLE);
        SummaryModel.Data.Product_summary.LastPayment lastPayment = productSummary.getLast_payment();
        if (!lastPayment.isEmpty()) {
            paymentType = productSummary.getLast_payment().getPayment_type();
            txtPayment.setTextColor(getResources().getColor(R.color.textDarkGray));
            if (paymentType.equals(Utils.TYPE_CC)) {
                is_verified = true;
                cc_token_id = productSummary.getLast_payment().getSaved_token_id();
                cc_card_id = productSummary.getLast_payment().getMasked_card();

                txtPayment.setText("• • • • " + cc_card_id.substring(cc_card_id.indexOf("-") + 1, cc_card_id.length()));
                imgPayment.setVisibility(View.VISIBLE);
                String headCC = cc_card_id.substring(0, 1);
                if (headCC.equals("4")) {
                    imgPayment.setVisibility(View.VISIBLE);
                    imgPayment.setImageResource(R.drawable.logo_visa2);
                } else if (headCC.equals("5")) {
                    imgPayment.setVisibility(View.VISIBLE);
                    imgPayment.setImageResource(R.drawable.logo_mastercard2);
                } else {
                    imgPayment.setVisibility(View.GONE);
                }
            } else if (paymentType.equals(Utils.TYPE_VA)) {
                imgPayment.setVisibility(View.GONE);
                txtPayment.setText(getString(R.string.other_bank));
                txtPayment.setTypeface(null, Typeface.NORMAL);
            } else if (paymentType.equals(Utils.TYPE_BP)) {
                imgPayment.setVisibility(View.VISIBLE);
                imgPayment.setImageResource(R.drawable.logo_mandiri);
                txtPayment.setText(getString(R.string.va_mandiri));
                txtPayment.setTypeface(null, Typeface.NORMAL);
            } else if (paymentType.equals(Utils.TYPE_BCA)) {
                imgPayment.setVisibility(View.VISIBLE);
                imgPayment.setImageResource(R.drawable.logo_bca2);
                txtPayment.setText(getString(R.string.va_bca));
                txtPayment.setTypeface(null, Typeface.NORMAL);
            }
        }else{
            relPayment.setSelected(true);
        }

        totalPrice = productSummary.getTotal_price();
        txtEventName.setText(eventName);
        try {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(startTime);
            txtEventInfo.setText(venueName);
            txtEventInfoDate.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate)
            );
        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }

        txtDftTitle.setText(dataProduct.getName());
        txtDftFill.setText(StringUtility.getRupiahFormat(dataProduct.getTotal_price()));
        txtTaxxFill.setText(StringUtility.getRupiahFormat(dataProduct.getTax_amount()));
        txtSerFill.setText(StringUtility.getRupiahFormat(dataProduct.getAdmin_fee()));
        maxDeposit = Integer.parseInt(productSummary.getTotal_price());
        txtEstTotFill.setText(StringUtility.getRupiahFormat(productSummary.getTotal_price()));
        payDeposit = (int) Double.parseDouble(minDeposit);
        latestDeposit = (int) Double.parseDouble(minDeposit);
        txtRequireFill.setText(StringUtility.getRupiahFormat(minDeposit));

        String estBalance = String.valueOf(Integer.parseInt(productSummary.getTotal_price()) - (int) Double.parseDouble(minDeposit));
        txtEstBalFill.setText(StringUtility.getRupiahFormat(estBalance));
        //txtTotalFill.setText(StringUtility.getRupiahFormat(productSummary.getTotal_price()));
        txtTotalFill.setText(StringUtility.getRupiahFormat(minDeposit));
        txtTotalTicketFill.setVisibility(View.GONE);

        //initTermView(dataProduct);
        checkEnability(txtPayment.getText().toString());
    }

    private void arrangeEstimateDeposit(boolean isIncrement) {
        if (isIncrement) {

            payDeposit = payDeposit + INCREMENT_VALUE;

            if (payDeposit >= Integer.parseInt(productSummary.getTotal_price())) {
                payDeposit = Integer.parseInt(productSummary.getTotal_price());
            } else {
                latestDeposit = payDeposit;
            }

            txtRequireFill.setText(StringUtility.getRupiahFormat(String.valueOf(payDeposit)));
            txtTotalFill.setText(StringUtility.getRupiahFormat(String.valueOf(payDeposit)));
            String estBalance = String.valueOf(Integer.parseInt(productSummary.getTotal_price()) - payDeposit);
            txtEstBalFill.setText(StringUtility.getRupiahFormat(estBalance));

        } else {
            if (payDeposit == Integer.parseInt(productSummary.getTotal_price())) {
                payDeposit = latestDeposit;
            } else {
                payDeposit = payDeposit - INCREMENT_VALUE;
            }

            if (payDeposit >= Integer.valueOf(minDeposit)) {
                txtRequireFill.setText(StringUtility.getRupiahFormat(String.valueOf(payDeposit)));
                txtTotalFill.setText(StringUtility.getRupiahFormat(String.valueOf(payDeposit)));
                String estBalance = String.valueOf(Integer.parseInt(productSummary.getTotal_price()) - payDeposit);
                txtEstBalFill.setText(StringUtility.getRupiahFormat(estBalance));
            } else {
                //rollback value payDeposit if lower than minimum deposit
                payDeposit = payDeposit + INCREMENT_VALUE;
            }
        }
    }

    private void sendMixpanel(SummaryModel.Data.Product_summary productSummary, EventDetailModel.Data.EventDetail eventDetail) {
        CommEventMixpanelModel commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), productSummary.getProduct_list().get(0).getName(), productSummary.getProduct_list().get(0).getTicket_type(),
                productSummary.getTotal_price(), productSummary.getProduct_list().get(0).getMax_buy());
        App.getInstance().trackMixPanelCommerce(Utils.COMM_PURCHASE_CONFIRMATION, commEventMixpanelModel);
    }

    /*private void initTermView(SummaryModel.Data.Product_summary.Product_list dataProduct) {
        int size = dataProduct.getTerms().size();
        for (int i = 0; i < size; i++) {
            TermsItemView termsItemView = new TermsItemView(ReservationInfoActivity.this, dataProduct.getTerms().get(i).getBody(), new TermsItemView.OnCheckTermsListener() {
                @Override
                public void onCheckTerms() {
                    checkEnability(arrTermItemView, txtPayment.getText().toString());
                }
            });
            linTerms.addView(termsItemView);
            arrTermItemView.add(termsItemView);
        }

        checkEnability(arrTermItemView, txtPayment.getText().toString());
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        pagerSlide.setCurrentItem(1);
    }

    public class SlideAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;

        public SlideAdapter(FragmentManager fm, ViewPager viewPager) {
            super(fm);
            this.fragments = new Fragment[2];
            viewPager.setAdapter(this);
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    //action
                    if (position == 0) {
                        if (canPay()) {
                            slidePay();
                        } else {
                            Log.d("Pay status", "cannot pay");
                        }
                    }

                    super.onPageSelected(position);
                }
            });
            viewPager.setOffscreenPageLimit(this.fragments.length);
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = this.fragments[position];

            if (fragment == null) {
                fragment = SlideFragment.newInstance();
                final Bundle arg = new Bundle();
                final App app = App.getInstance();

                if (position == 0) {
                    arg.putString(SlideFragment.ARG_TITLE, "");
                } else if (position == 1) {
                    arg.putString(SlideFragment.ARG_TITLE, app.getString(R.string.pci_slide));
                }

                fragment.setArguments(arg);
                this.fragments[position] = fragment;
            }

            return fragment;
        }
    }

    private boolean canPay() {
        boolean can = true;

        for (int i = 0; i < arrTermItemView.size(); i++) {
            ImageView img = arrTermItemView.get(i).getImgCheck();
            if (!img.isSelected()) {
                can = false;
                break;
            }
        }

        if (can) {
            if (paymentType.equals(Utils.TYPE_CC)) {
                if (cc_card_id.isEmpty()) {
                    can = false;
                }
            } else if (paymentType.equals(Utils.BLANK)) {
                can = false;
            }
        }
        return can;
    }

    private void slidePay() {
        if (Integer.parseInt(String.valueOf(payDeposit)) > 0) {
            if (paymentType.equals(Utils.TYPE_CC)) {
                if (is_verified) {
                    PostPaymentModel postPaymentModel = new PostPaymentModel(paymentType, "0", productSummary.getOrder_id(), cc_token_id, name_cc, String.valueOf(payDeposit));
                    String sd = String.valueOf(new Gson().toJson(postPaymentModel));
                    doPayment(postPaymentModel);
                } else {
                    access3dSecure();
                }
            } else {
                PostPaymentModel postPaymentModel = new PostPaymentModel(paymentType, Utils.BLANK, productSummary.getOrder_id(), Utils.BLANK, Utils.BLANK, String.valueOf(payDeposit));
                String sd = String.valueOf(new Gson().toJson(postPaymentModel));
                doPayment(postPaymentModel);
            }
        } else
        {
            //free payment
            PostFreePaymentModel postFreePaymentModel = new PostFreePaymentModel(String.valueOf(order_id), "0");
            doFreePayment(postFreePaymentModel);
        }
    }

    private void doFreePayment(PostFreePaymentModel postFreePaymentModel) {
        isPaying = true;
        CommerceManager.loaderFreePayment(postFreePaymentModel, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                isPaying = false;
                dismissLoadingDialog();
                Intent i = new Intent(ReservationInfoActivity.this, CongratsActivity.class);

                i.putExtra(Common.FIELD_ORDER_ID, order_id);
                i.putExtra(Common.FIELD_PAYMENT_TYPE, paymentType);
                i.putExtra(Common.FIELD_FROM_ORDER_LIST, false);
                startActivity(i);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                isPaying = false;
                dismissLoadingDialog();

                pagerSlide.setCurrentItem(1);
                if(message != null && (message.contains("left")|| message.contains("unavailable"))){
                    final AlertDialog dialog = new AlertDialog.Builder(ReservationInfoActivity.this)
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent i = new Intent(ReservationInfoActivity.this, ProductListActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.putExtra(Common.FIELD_EVENT_ID, eventDetail.get_id());
                                    i.putExtra(EventDetailModel.Data.EventDetail.class.getName(), eventDetail);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                } else if(message != null && (message.contains("Paid"))){
                    final AlertDialog dialog = new AlertDialog.Builder(ReservationInfoActivity.this)
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Intent i = new Intent(ReservationInfoActivity.this, CongratsActivity.class);

                                    i.putExtra(Common.FIELD_ORDER_ID, order_id);
                                    i.putExtra(Common.FIELD_PAYMENT_TYPE, paymentType);
                                    i.putExtra(Common.FIELD_FROM_ORDER_LIST, false);
                                    startActivity(i);

                                }
                            }).create();
                    dialog.show();
                }else {
                    /*if(responseCode==Utils.CODE_FAILED){

                    }else{

                    }*/
                    if(message != null)
                    {
                        Toast.makeText(ReservationInfoActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        initLoadingDialog();
    }
    
    private void access3dSecure() {
        //using 3d secure
        VTConfig.VT_IsProduction = false;
        VTConfig.CLIENT_KEY = "VT-client-gJRBbRZC0t_-JXUD";

        VTDirect vtDirect = new VTDirect();

        final VTCardDetails vtCardDetails = new VTCardDetails();
        //TODO: Set your card details based on user input.
        //this is a sample
        vtCardDetails.setCard_number(cardDetails.getCardNumber()); // 3DS Dummy CC
        vtCardDetails.setCard_cvv(cardDetails.getCvv());
        vtCardDetails.setCard_exp_month(cardDetails.getExpMonth());
        vtCardDetails.setCard_exp_year(cardDetails.getExpYear());

        //set true or false to enable or disable 3dsecure
        vtCardDetails.setSecure(true);
        vtCardDetails.setGross_amount(cardDetails.getGrossAmount());

        //Set VTCardDetails to VTDirect
        vtDirect.setCard_details(vtCardDetails);

        //Simply Call getToken function and put your callback to handle data
        vtDirect.getToken(new ITokenCallback() {
            @Override
            public void onSuccess(VTToken token) {

                //use token anyhow you want, maybe send it to your server. Example here to check whether you are using 3dsecure feature or not.
                if (token.getRedirect_url() != null) {

                    //using 3d secure
                    /*WebView webView = new
                            WebView(ReservationInfoActivity.this);*/
                    MyWebView webView = new MyWebView(ReservationInfoActivity.this);

                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                case MotionEvent.ACTION_UP:
                                    if (!v.hasFocus()) {
                                        v.requestFocus();
                                    }
                                    break;
                            }
                            return false;
                        }
                    });
                    webView.setWebChromeClient(new WebChromeClient());
                    webView.setWebViewClient(new VtWebViewClient(token.getToken_id(), totalPrice));
                    webView.loadUrl(token.getRedirect_url());

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ReservationInfoActivity.this);
                    dialog3ds = alertBuilder.create();


                    dialog3ds.setTitle("3D Secure Veritrans");
                    dialog3ds.setView(webView);
                    webView.requestFocus(View.FOCUS_DOWN);
                    alertBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    dialog3ds.show();
                }
                //print or send token
                Log.d("token", token.getToken_id());


            }

            @Override
            public void onError(Exception e) {
                pagerSlide.setCurrentItem(1);
                Toast.makeText(ReservationInfoActivity.this, getString(R.string.error_3dsecure), Toast.LENGTH_LONG).show();
                //Something is wrong, get details message by print e.getMessage()
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            paymentType = data.getStringExtra(Common.FIELD_PAYMENT_TYPE);
            txtPayment.setTextColor(getResources().getColor(R.color.textDarkGray));
            if (paymentType.equals(Utils.TYPE_CC)) {
                CCScreenModel creditcardInformation = data.getParcelableExtra(CCScreenModel.class.getName());
                name_cc = creditcardInformation.getName_cc();

                if (name_cc.equals(Utils.BLANK)) {
                    is_verified = true;
                } else {
                    is_verified = false;
                }

                if (is_verified) {
                    is_new_card = "0";
                    cc_token_id = creditcardInformation.getCreditcardInformation().getSaved_token_id();
                    cc_card_id = creditcardInformation.getCreditcardInformation().getMasked_card();

                    txtPayment.setText("• • • • " + cc_card_id.substring(cc_card_id.indexOf("-") + 1, cc_card_id.length()));
                } else {
                    is_new_card = "1";
                    cardDetails = creditcardInformation.getCardDetails();
                    cc_card_id = creditcardInformation.getCardDetails().getCardNumber();

                    txtPayment.setText("• • • • " + cc_card_id.substring(cc_card_id.length() - 4, cc_card_id.length()));
                }

                String headCC = cc_card_id.substring(0, 1);
                if (headCC.equals("4")) {
                    imgPayment.setVisibility(View.VISIBLE);
                    imgPayment.setImageResource(R.drawable.logo_visa2);
                } else if (headCC.equals("5")) {
                    imgPayment.setVisibility(View.VISIBLE);
                    imgPayment.setImageResource(R.drawable.logo_mastercard2);
                } else {
                    imgPayment.setVisibility(View.GONE);
                }
            } else if (paymentType.equals(Utils.TYPE_BCA)) {
                txtPayment.setText(getString(R.string.va_bca));
                imgPayment.setVisibility(View.VISIBLE);
                imgPayment.setImageResource(R.drawable.logo_bca2);
                txtPayment.setTypeface(null, Typeface.NORMAL);
            } else if (paymentType.equals(Utils.TYPE_BP)) {
                txtPayment.setText(getString(R.string.va_mandiri));
                imgPayment.setVisibility(View.VISIBLE);
                imgPayment.setImageResource(R.drawable.logo_mandiri);
                txtPayment.setTypeface(null, Typeface.NORMAL);
            } else if (paymentType.equals(Utils.TYPE_VA)) {
                txtPayment.setText(getString(R.string.other_bank));
                imgPayment.setVisibility(View.GONE);
                txtPayment.setTypeface(null, Typeface.NORMAL);
            }
            relPayment.setSelected(false);
            checkEnability(txtPayment.getText().toString());
        } else {
            SummaryModel.Data.Product_summary.LastPayment lastPayment = productSummary.getLast_payment();
            if (CommerceManager.arrCCScreen.size() == 0) {
                if (lastPayment.isEmpty()) {
                    txtPayment.setText(getString(R.string.pci_payment));
                    txtPayment.setTextColor(getResources().getColor(R.color.purple));
                    imgPayment.setImageResource(R.drawable.ic_plus);

                    relPayment.setSelected(true);
                } else {
                    paymentType = lastPayment.getPayment_type();
                    if (paymentType.equals(Utils.TYPE_CC)) {
                        String mask = lastPayment.getMasked_card();
                        boolean isAlreadyDelete = true;
                        for (int i = 0; i < CommerceManager.arrCCScreen.size(); i++) {
                            if (mask.equals(CommerceManager.arrCCScreen.get(i).getCreditcardInformation().getMasked_card())) {
                                isAlreadyDelete = false;
                                break;
                            }
                        }

                        if (isAlreadyDelete) {
                            txtPayment.setText(getString(R.string.pci_payment));
                            txtPayment.setTextColor(getResources().getColor(R.color.purple));
                            imgPayment.setImageResource(R.drawable.ic_plus);

                            relPayment.setSelected(true);
                        }else{
                            relPayment.setSelected(false);
                        }
                    }
                }
            } else {
                if (CommerceManager.arrCCScreen.size() == 0) {
                    if (lastPayment.isEmpty()) {
                        txtPayment.setText(getString(R.string.pci_payment));
                        txtPayment.setTextColor(getResources().getColor(R.color.purple));
                        imgPayment.setImageResource(R.drawable.ic_plus);

                        relPayment.setSelected(true);
                    }
                } else {
                    if (lastPayment.isEmpty()) {
                        txtPayment.setText(getString(R.string.pci_payment));
                        txtPayment.setTextColor(getResources().getColor(R.color.purple));
                        imgPayment.setImageResource(R.drawable.ic_plus);

                        relPayment.setSelected(true);
                    } else {
                        paymentType = lastPayment.getPayment_type();
                        if (paymentType.equals(Utils.TYPE_CC)) {
                            String mask = lastPayment.getMasked_card();
                            boolean isAlreadyDelete = true;
                            for (int i = 0; i < CommerceManager.arrCCScreen.size(); i++) {
                                String maskB = CommerceManager.arrCCScreen.get(i).getCreditcardInformation().getMasked_card();
                                if (mask.equals(maskB)) {
                                    isAlreadyDelete = false;
                                    break;
                                }
                            }

                            if (isAlreadyDelete) {
                                txtPayment.setText(getString(R.string.pci_payment));
                                txtPayment.setTextColor(getResources().getColor(R.color.purple));
                                imgPayment.setImageResource(R.drawable.ic_plus);

                                relPayment.setSelected(true);
                            }else{
                                relPayment.setSelected(false);
                            }
                        }
                    }
                }
            }
        }
    }

    private class VtWebViewClient extends WebViewClient {

        String token;
        String price;

        public VtWebViewClient(String token, String price) {
            this.token = token;
            this.price = price;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Log.d("VtLog", url);

            if (url.startsWith(getPaymentApiUrl() + "/callback/")) {
                PostPaymentModel postPaymentModel = new PostPaymentModel(paymentType, "1", productSummary.getOrder_id(), token, name_cc, String.valueOf(payDeposit));

                String sd = String.valueOf(new Gson().toJson(postPaymentModel));
                dialog3ds.dismiss();

                doPayment(postPaymentModel);

            } else if (url.startsWith(getPaymentApiUrl() + "/redirect/") || url.contains("3dsecure")) {
                //Do nothing
            } else {
                if (dialog3ds != null) {
                    dialog3ds.dismiss();
                }
            }
        }
    }

    private void doPayment(PostPaymentModel postPaymentModel) {
        isPaying = true;
        CommerceManager.loaderPayment(postPaymentModel, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                isPaying = false;
                dismissLoadingDialog();
                Intent i;
                if (paymentType.equals(Utils.TYPE_CC)) {
                    i = new Intent(ReservationInfoActivity.this, CongratsActivity.class);
                } else {
                    i = new Intent(ReservationInfoActivity.this, HowToPayActivity.class);
                    i.putExtra(Common.FIELD_WALKTHROUGH_PAYMENT, false);
                    i.putExtra(productSummary.getClass().getName(), productSummary);
                    i.putExtra(eventDetail.getClass().getName(), eventDetail);
                }
                i.putExtra(Common.FIELD_ORDER_ID, order_id);
                i.putExtra(Common.FIELD_PAYMENT_TYPE, paymentType);
                i.putExtra(Common.FIELD_FROM_ORDER_LIST, false);
                startActivity(i);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                isPaying = false;
                dismissLoadingDialog();

                pagerSlide.setCurrentItem(1);

                Utils.d(String.valueOf(responseCode), message);
                if (message.contains("left") || message.contains("unavailable")) {
                    final AlertDialog dialog = new AlertDialog.Builder(ReservationInfoActivity.this)
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent i = new Intent(ReservationInfoActivity.this, ProductListActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.putExtra(Common.FIELD_EVENT_ID, eventDetail.get_id());
                                    i.putExtra(EventDetailModel.Data.EventDetail.class.getName(), eventDetail);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                } else {
                    Toast.makeText(ReservationInfoActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });

        initLoadingDialog();
    }

    private void initLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ReservationInfoActivity.this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
        }

        progressDialog.show();
    }

    private void dismissLoadingDialog() {
        if (progressDialog != null & progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void checkEnability(String namePayment) {
        boolean isItEnable = true;
        if (namePayment.equals(Utils.BLANK) || namePayment.equals(getString(R.string.pci_payment))) {
            isItEnable = false;
        }

        if (isItEnable) {
            pagerSlide.setVisibility(View.VISIBLE);
            relDisable.setVisibility(View.GONE);
        } else {
            pagerSlide.setVisibility(View.GONE);
            relDisable.setVisibility(View.VISIBLE);
        }
    }

    class MyWebView extends WebView {
        public MyWebView(Context context) {
            super(context);
        }

        public MyWebView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
            super(context, attrs, defStyleAttr, privateBrowsing);
        }

        @Override
        public boolean onCheckIsTextEditor() {
            return true;
        }
    }

    @Override
    public String getTotalCaption() {
        return getString(R.string.pci_f7b);
    }

    @Override
    public String getTransactionType() {
        return Common.TYPE_RESERVATION;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isPaying) {
            //do nothing
        }
    }
}
