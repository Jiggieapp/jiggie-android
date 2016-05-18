package com.jiggie.android.activity.ecommerce;

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
import com.jiggie.android.BuildConfig;
import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.summary.AbstractPurchaseSumaryActivity;
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
 * Created by LTE on 3/9/2016.
 */
public class PurchaseInfoActivity extends AbstractPurchaseSumaryActivity {

    @Bind(R.id.pager_slide)
    ViewPager pagerSlide;
    @Bind(R.id.rel_payment)
    RelativeLayout relPayment;

    SummaryModel.Data.Product_summary productSummary;
    EventDetailModel.Data.EventDetail eventDetail;
    @Bind(R.id.txt_event_name)
    TextView txtEventName;
    @Bind(R.id.txt_event_info)
    TextView txtEventInfo;
    @Bind(R.id.txt_tik_title)
    TextView txtTikTitle;
    @Bind(R.id.txt_tik_fill)
    TextView txtTikFill;
    @Bind(R.id.txt_fee_fill)
    TextView txtFeeFill;
    @Bind(R.id.txt_tax_fill)
    TextView txtTaxFill;
    @Bind(R.id.txt_total_fill)
    TextView txtTotalFill;
    @Bind(R.id.txt_total_ticket_fill)
    TextView txtTotalTicketFill;
    @Bind(R.id.img_payment)
    ImageView imgPayment;
    @Bind(R.id.txt_payment)
    TextView txtPayment;
    @Bind(R.id.txt_event_info_date)
    TextView txtEventInfoDate;
    @Bind(R.id.card_booking)
    CardView card_booking;
    @Bind(R.id.lblPayDeposit)
    TextView lblPayDeposit;

    String eventId, eventName, venueName, startTime, totalPrice;
    /*@Bind(R.id.lin_terms)
    LinearLayout linTerms;*/
    ArrayList<TermsItemView> arrTermItemView = new ArrayList<>();
    String is_new_card, cc_token_id = Utils.BLANK, cc_card_id, paymentType = Utils.BLANK, name_cc = Utils.BLANK;
    boolean is_verified;
    long order_id;

    AlertDialog dialog3ds;
    ProgressDialog progressDialog;
    public final static String PAYMENT_API = BuildConfig.PAYMENT_API;
    //public final static String PAYMENT_API_SANDBOX = "https://api.sandbox.veritrans.co.id/v2/token";
    CCScreenModel.CardDetails cardDetails;
    @Bind(R.id.rel_disable)
    RelativeLayout relDisable;
    @Bind(R.id.card_view)
    CardView cardView;
    boolean isPaying = false;
    @Bind(R.id.lblSelectPayment)
    TextView lblSelectPayment;
    @Bind(R.id.txt_credit_fill)
    TextView txtCreditFill;
    @Bind(R.id.txt_credit_title)
    TextView txtCreditTitle;
    @Bind(R.id.txt_discount_title)
    TextView txtDiscountTitle;
    @Bind(R.id.txt_discount_fill)
    TextView txtDiscountFill;


    private SlideAdapter slideAdapter;
    public final static String TAG = PurchaseInfoActivity.class.getSimpleName();

    public static String getPaymentApiUrl() {
        /*if (VTConfig.VT_IsProduction) {
            return PAYMENT_API;
        }
        return PAYMENT_API_SANDBOX;*/

        return PAYMENT_API;
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_purchase_info);
        ButterKnife.bind(this);

        preDefined();

        relPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PurchaseInfoActivity.this, PaymentMethodActivity.class);
                i.putExtra(Common.FIELD_ORDER_ID, order_id);
                i.putExtra(Common.FIELD_PAYMENT_TYPE, paymentType);
                i.putExtra(Common.FIELD_PRICE, totalPrice);
                i.putExtra(productSummary.getClass().getName(), productSummary);
                i.putExtra(eventDetail.getClass().getName(), eventDetail);
                startActivityForResult(i, 0);
            }
        });

    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.purchase_info).toUpperCase();
    }

    private void preDefined() {
        card_booking.setVisibility(View.GONE);
        lblPayDeposit.setVisibility(View.GONE);
        slideAdapter = new SlideAdapter(getSupportFragmentManager(), pagerSlide);
        pagerSlide.setAdapter(slideAdapter);
        pagerSlide.setCurrentItem(1);

        Intent a = getIntent();
        eventId = a.getStringExtra(Common.FIELD_EVENT_ID);
        eventName = a.getStringExtra(Common.FIELD_EVENT_NAME);
        venueName = a.getStringExtra(Common.FIELD_VENUE_NAME);
        startTime = a.getStringExtra(Common.FIELD_STARTTIME);
        productSummary = a.getParcelableExtra(SummaryModel.Data.Product_summary.class.getName());
        eventDetail = a.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        order_id = productSummary.getOrder_id();
        SummaryModel.Data.Product_summary.Product_list dataProduct = productSummary.getProduct_list().get(0);

        sendMixpanel(productSummary, eventDetail);

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
        } else {
            relPayment.setSelected(true);
        }

        totalPrice = productSummary.getTotal_price();
        txtEventName.setText(eventName);
        /*try {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(startTime);
            final Date endDate = Common.ISO8601_DATE_FORMAT_UTC.parse(e);
            txtEventInfo.setText(venueName);
            txtEventInfoDate.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate)
            );
        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }*/

        try {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse
                    (eventDetail.getStart_datetime());
            final Date endDate = Common.ISO8601_DATE_FORMAT_UTC.parse
                    (eventDetail.getEnd_datetime());
            String simpleDate = App.getInstance().getResources().getString(R.string.event_date_format
                    , Common.SERVER_DATE_FORMAT_ALT.format(startDate), Common.SIMPLE_12_HOUR_FORMAT.format(endDate));
            txtEventInfo.setText(venueName);
            txtEventInfoDate.setText(simpleDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtTikTitle.setText(dataProduct.getName() + " Ticket (" + dataProduct.getNum_buy() + "x)");

        if (dataProduct.getTotal_price().equals(Utils.NOL_RUPIAH)) {
            txtTikFill.setText(getString(R.string.free));
        } else {
            txtTikFill.setText(StringUtility.getRupiahFormat(dataProduct.getTotal_price()));
        }

        if (dataProduct.getAdmin_fee().equals(Utils.NOL_RUPIAH)) {
            txtFeeFill.setText(getString(R.string.free));
        } else {
            txtFeeFill.setText(StringUtility.getRupiahFormat(dataProduct.getAdmin_fee()));
        }

        if (dataProduct.getTax_amount().equals(Utils.NOL_RUPIAH)) {
            txtTaxFill.setText(getString(R.string.free));
        } else {
            txtTaxFill.setText(StringUtility.getRupiahFormat(dataProduct.getTax_amount()));
        }

        if (totalPrice.equals(Utils.NOL_RUPIAH)) {
            txtTotalTicketFill.setText(getString(R.string.free));
        } else {
            txtTotalTicketFill.setText(StringUtility.getRupiahFormat(totalPrice));
        }

        if (productSummary.getCredit().getCredit_used() != 0) {
            txtCreditTitle.setVisibility(View.VISIBLE);
            txtCreditFill.setVisibility(View.VISIBLE);
            txtCreditFill.setText("- " + StringUtility.getRupiahFormat(String.valueOf(productSummary.getCredit().getCredit_used())));
        }

        /*if (productSummary.getDiscount().getTotal_discount() != 0) {
            txtDiscountTitle.setVisibility(View.VISIBLE);
            txtDiscountFill.setVisibility(View.VISIBLE);
            txtDiscountFill.setText("- " + StringUtility.getRupiahFormat(String.valueOf(productSummary.getDiscount().getTotal_discount())));
        }*/

        txtTotalFill.setVisibility(View.GONE);
        //initTermView(dataProduct);

        checkEnability(txtPayment.getText().toString());
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
            TermsItemView termsItemView = new TermsItemView(PurchaseInfoActivity.this, dataProduct.getTerms().get(i).getBody(), new TermsItemView.OnCheckTermsListener() {
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
                            if (isPaying == false) {
                                slidePay();
                            }
                        } else {
                            Utils.d("Pay status", "cannot pay");
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
                    if (totalPrice.equals(Utils.NOL_RUPIAH)) {
                        arg.putString(SlideFragment.ARG_TITLE, app.getString(R.string.pci_slide_continue));
                    } else {
                        arg.putString(SlideFragment.ARG_TITLE, app.getString(R.string.pci_slide));
                    }

                }

                fragment.setArguments(arg);
                this.fragments[position] = fragment;
            }

            return fragment;
        }
    }

    private boolean canPay() {
        boolean can = true;

        if (!totalPrice.equals(Utils.NOL_RUPIAH)) {
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
        }

        return can;
    }

    private void slidePay() {
        isPaying = true;
        if (Integer.parseInt(totalPrice) > 0) {
            if (paymentType.equals(Utils.TYPE_CC)) {
                if (is_verified) {
                    PostPaymentModel postPaymentModel = new PostPaymentModel(paymentType, "0", productSummary.getOrder_id(), cc_token_id, name_cc, Utils.BLANK);
                    doPayment(postPaymentModel);
                } else {
                    try {
                        access3dSecure();
                    } catch (Exception e) {
                        pagerSlide.setCurrentItem(1);
                        Toast.makeText(PurchaseInfoActivity.this, getString(R.string.error_3dsecure), Toast.LENGTH_LONG).show();
                    }

                }
            } else {
                PostPaymentModel postPaymentModel = new PostPaymentModel(paymentType, Utils.BLANK, productSummary.getOrder_id(), Utils.BLANK, Utils.BLANK, Utils.BLANK);

                //String responses = new Gson().toJson(postPaymentModel);
                //Utils.d("res", responses);

                doPayment(postPaymentModel);
            }
        } else {
            //free payment
            PostFreePaymentModel postFreePaymentModel = new PostFreePaymentModel(String.valueOf(order_id), Utils.BLANK);
            doFreePayment(postFreePaymentModel);
        }

    }

    private void access3dSecure() {
        //using 3d secure
        VTConfig.VT_IsProduction = true;
        //VTConfig.CLIENT_KEY = "VT-client-gJRBbRZC0t_-JXUD";
        //VTConfig.CLIENT_KEY = "VT-client-tHEKcD0xJGsm6uwH";
        VTConfig.CLIENT_KEY = BuildConfig.CLIENT_KEY;

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
                            WebView(PurchaseInfoActivity.this);*/
                    MyWebView webView = new MyWebView(PurchaseInfoActivity.this);

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

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PurchaseInfoActivity.this);
                    dialog3ds = alertBuilder.create();


                    dialog3ds.setTitle("3D Secure Veritrans");
                    dialog3ds.setView(webView);
                    webView.requestFocus(View.FOCUS_DOWN);
                    alertBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            pagerSlide.setCurrentItem(1);
                            isPaying = false;
                        }
                    });
                    dialog3ds.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                            pagerSlide.setCurrentItem(1);
                            isPaying = false;
                        }
                    });

                    dialog3ds.show();
                }
                //print or send token
                //Utils.d("token", token.getToken_id());
            }

            @Override
            public void onError(Exception e) {

                //Something is wrong, get details message by print e.getMessage()

                pagerSlide.setCurrentItem(1);
                Toast.makeText(PurchaseInfoActivity.this, getString(R.string.error_3dsecure), Toast.LENGTH_LONG).show();
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
        } else if (resultCode != 284) { //klo 284 do nothing
            SummaryModel.Data.Product_summary.LastPayment lastPayment = productSummary.getLast_payment();
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
                        } else {
                            relPayment.setSelected(false);
                        }
                    }
                }
            }
        } else if (resultCode == 284) {
            Utils.d(TAG, "what the fuck?");
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

            if (url.startsWith(getPaymentApiUrl() + "/callback/")) {
                PostPaymentModel postPaymentModel = new PostPaymentModel(paymentType, "1", productSummary.getOrder_id(), token, name_cc, Utils.BLANK);

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

        CommerceManager.loaderPayment(postPaymentModel, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                isPaying = false;
                dismissLoadingDialog();
                Intent i;
                if (paymentType.equals(Utils.TYPE_CC)) {
                    i = new Intent(PurchaseInfoActivity.this, CongratsActivity.class);
                } else {
                    i = new Intent(PurchaseInfoActivity.this, HowToPayActivity.class);
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
            public void onFailure(int responseCode, final String message) {
                pagerSlide.setCurrentItem(1);
                isPaying = false;
                dismissLoadingDialog();
                if (message != null && (message.contains("left") || message.contains("unavailable"))) {
                    final AlertDialog dialog = new AlertDialog.Builder(PurchaseInfoActivity.this)
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent i = new Intent(PurchaseInfoActivity.this, ProductListActivity.class);
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
                } else if (message != null && (message.contains("Paid"))) {
                    final AlertDialog dialog = new AlertDialog.Builder(PurchaseInfoActivity.this)
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Intent i;
                                    if (CommerceManager.lastPaymentType.equals(Utils.TYPE_CC)) {
                                        i = new Intent(PurchaseInfoActivity.this, CongratsActivity.class);
                                    } else {
                                        i = new Intent(PurchaseInfoActivity.this, HowToPayActivity.class);
                                        i.putExtra(Common.FIELD_WALKTHROUGH_PAYMENT, false);
                                        i.putExtra(productSummary.getClass().getName(), productSummary);
                                        i.putExtra(eventDetail.getClass().getName(), eventDetail);
                                    }
                                    i.putExtra(Common.FIELD_ORDER_ID, order_id);
                                    i.putExtra(Common.FIELD_PAYMENT_TYPE, paymentType);
                                    i.putExtra(Common.FIELD_FROM_ORDER_LIST, false);
                                    startActivity(i);

                                }
                            }).create();
                    dialog.show();
                } else {
                    /*if(responseCode==Utils.CODE_FAILED){

                    }else{

                    }*/
                    if (message != null) {
                        Toast.makeText(PurchaseInfoActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        initLoadingDialog();
    }

    private void doFreePayment(PostFreePaymentModel postFreePaymentModel) {
        CommerceManager.loaderFreePayment(postFreePaymentModel, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                isPaying = false;
                dismissLoadingDialog();
                Intent i = new Intent(PurchaseInfoActivity.this, CongratsActivity.class);

                i.putExtra(Common.FIELD_ORDER_ID, order_id);
                i.putExtra(Common.FIELD_PAYMENT_TYPE, paymentType);
                i.putExtra(Common.FIELD_FROM_ORDER_LIST, false);
                startActivity(i);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                pagerSlide.setCurrentItem(1);
                isPaying = false;
                dismissLoadingDialog();
                if (message != null && (message.contains("left") || message.contains("unavailable"))) {
                    final AlertDialog dialog = new AlertDialog.Builder(PurchaseInfoActivity.this)
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent i = new Intent(PurchaseInfoActivity.this, ProductListActivity.class);
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
                } else if (message != null && (message.contains("Paid"))) {
                    final AlertDialog dialog = new AlertDialog.Builder(PurchaseInfoActivity.this)
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Intent i = new Intent(PurchaseInfoActivity.this, CongratsActivity.class);

                                    i.putExtra(Common.FIELD_ORDER_ID, order_id);
                                    i.putExtra(Common.FIELD_PAYMENT_TYPE, paymentType);
                                    i.putExtra(Common.FIELD_FROM_ORDER_LIST, false);
                                    startActivity(i);

                                }
                            }).create();
                    dialog.show();
                } else {
                    /*if(responseCode==Utils.CODE_FAILED){

                    }else{

                    }*/
                    if (message != null) {
                        Toast.makeText(PurchaseInfoActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        initLoadingDialog();
    }

    private void initLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(PurchaseInfoActivity.this);
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

        if (totalPrice.equals(Utils.NOL_RUPIAH)) {
            pagerSlide.setVisibility(View.VISIBLE);
            relDisable.setVisibility(View.GONE);

            lblSelectPayment.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        } else {
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
        return getString(R.string.pci_total);
    }

    @Override
    public String getTransactionType() {
        return Common.TYPE_PURCHASE;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isPaying) {
            //do nothing
        }
    }
}