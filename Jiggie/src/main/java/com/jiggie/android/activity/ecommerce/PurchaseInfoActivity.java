package com.jiggie.android.activity.ecommerce;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarWithDotActivity;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CCModel;
import com.jiggie.android.model.CCScreenModel;
import com.jiggie.android.model.Common;
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
public class PurchaseInfoActivity extends ToolbarWithDotActivity {

    @Bind(R.id.pager_slide)
    ViewPager pagerSlide;
    @Bind(R.id.rel_payment)
    RelativeLayout relPayment;

    SummaryModel.Data.Product_summary productSummary;
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
    @Bind(R.id.img_payment)
    ImageView imgPayment;
    @Bind(R.id.txt_payment)
    TextView txtPayment;

    String eventId, eventName, venueName, startTime, totalPrice;
    @Bind(R.id.lin_terms)
    LinearLayout linTerms;
    ArrayList<TermsItemView> arrTermItemView = new ArrayList<>();
    String is_new_card, cc_token_id = Utils.BLANK, cc_card_id, paymentType = Utils.BLANK, name_cc = Utils.BLANK;
    boolean is_verified;

    AlertDialog dialog3ds;
    ProgressDialog progressDialog;
    public final static String PAYMENT_API = "https://api.veritrans.co.id/v2/token";
    public final static String PAYMENT_API_SANDBOX = "https://api.sandbox.veritrans.co.id/v2/token";
    CCScreenModel.CardDetails cardDetails;

    public static String getPaymentApiUrl(){
        if(VTConfig.VT_IsProduction){
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
                Intent i = new Intent(PurchaseInfoActivity.this, PaymentMethodActivity.class);
                i.putExtra(Common.FIELD_PRICE, totalPrice);
                startActivityForResult(i, 0);
            }
        });
    }

    private void preDefined() {
        pagerSlide.setAdapter(new SlideAdapter(getSupportFragmentManager(), pagerSlide));
        pagerSlide.setCurrentItem(1);

        Intent a = getIntent();
        eventId = a.getStringExtra(Common.FIELD_EVENT_ID);
        eventName = a.getStringExtra(Common.FIELD_EVENT_NAME);
        venueName = a.getStringExtra(Common.FIELD_VENUE_NAME);
        startTime = a.getStringExtra(Common.FIELD_STARTTIME);
        productSummary = a.getParcelableExtra(SummaryModel.Data.Product_summary.class.getName());
        SummaryModel.Data.Product_summary.Product_list dataProduct = productSummary.getProduct_list().get(0);

        SummaryModel.Data.Product_summary.LastPayment lastPayment = productSummary.getLast_payment();
        if(!lastPayment.isEmpty()){
            paymentType = productSummary.getLast_payment().getPayment_type();
            txtPayment.setTextColor(getResources().getColor(R.color.textDarkGray));
            if(paymentType.equals(Utils.TYPE_CC)){
                is_verified = true;
                cc_token_id = productSummary.getLast_payment().getSaved_token_id();
                cc_card_id = productSummary.getLast_payment().getMasked_card();

                txtPayment.setText("• • • • " + cc_card_id.substring(cc_card_id.indexOf("-") + 1, cc_card_id.length()));
                imgPayment.setVisibility(View.VISIBLE);
                String headCC = cc_card_id.substring(0, 1);
                if(headCC.equals("4")){
                    imgPayment.setImageResource(R.drawable.logo_visa);
                }else{
                    imgPayment.setImageResource(R.drawable.logo_mastercard);
                }
            }else if(paymentType.equals(Utils.TYPE_VA)){
                imgPayment.setVisibility(View.GONE);
                txtPayment.setText(getString(R.string.other_bank));
                txtPayment.setTypeface(null, Typeface.NORMAL);
            }else if(paymentType.equals(Utils.TYPE_BP)){
                imgPayment.setVisibility(View.VISIBLE);
                imgPayment.setImageResource(R.drawable.logo_mandiri);
                txtPayment.setText(getString(R.string.va_mandiri));
                txtPayment.setTypeface(null, Typeface.NORMAL);
            }


        }

        totalPrice = dataProduct.getTotal_price_all();
        txtEventName.setText(eventName);
        try {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(startTime);
            txtEventInfo.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate) + " - " + venueName);
        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }
        txtTikTitle.setText(dataProduct.getName() + " Ticket (" + dataProduct.getNum_buy() + ")");
        txtTikFill.setText(StringUtility.getRupiahFormat(dataProduct.getTotal_price()));
        txtFeeFill.setText(StringUtility.getRupiahFormat(dataProduct.getAdmin_fee()));
        txtTaxFill.setText(StringUtility.getRupiahFormat(dataProduct.getTax_amount()));
        txtTotalFill.setText(StringUtility.getRupiahFormat(dataProduct.getTotal_price_all()));

        initTermView(dataProduct);
    }

    private void initTermView(SummaryModel.Data.Product_summary.Product_list dataProduct){
        int size = dataProduct.getTerms().size();
        for(int i=0; i<dataProduct.getTerms().size();i++){
            TermsItemView termsItemView = new TermsItemView(PurchaseInfoActivity.this, dataProduct.getTerms().get(i).getBody());
            linTerms.addView(termsItemView);
            arrTermItemView.add(termsItemView);
        }
    }

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
                        /*Intent i = new Intent(PurchaseInfoActivity.this, HowToPayActivity.class);
                        startActivity(i);*/

                        if(canPay()){
                            slidePay();
                        }else {
                            Log.d("Pay status","cannot pay");
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
                fragment = new SlideFragment();
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

    private boolean canPay(){
        boolean can = true;

        for(int i=0;i<arrTermItemView.size();i++){
            ImageView img = arrTermItemView.get(i).getImgCheck();
            if(!img.isSelected()){
                can = false;
                break;
            }
        }

        if(can){
            if(paymentType.equals(Utils.TYPE_CC)){
                if(cc_card_id.isEmpty()){
                    can = false;
                }
            }else if(paymentType.equals(Utils.BLANK)){
                can = false;
            }
        }

        return can;
    }

    private void slidePay(){
        if(paymentType.equals(Utils.TYPE_CC)){
            if(is_verified){
                PostPaymentModel postPaymentModel = new PostPaymentModel(paymentType, "0", productSummary.getOrder_id(), cc_token_id, name_cc);
                doPayment(postPaymentModel);
            }else{
                access3dSecure();
            }
        }else{
            PostPaymentModel postPaymentModel = new PostPaymentModel(paymentType, Utils.BLANK, productSummary.getOrder_id(), Utils.BLANK, Utils.BLANK);
            doPayment(postPaymentModel);
        }
    }

    private void access3dSecure(){
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
                    WebView webView = new
                            WebView(PurchaseInfoActivity.this);

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
                        }
                    });

                    dialog3ds.show();
                }
                //print or send token
                Log.d("token", token.getToken_id());


            }

            @Override
            public void onError(Exception e) {

                //Something is wrong, get details message by print e.getMessage()
            }
        });
    }

    public static class SlideFragment extends Fragment {
        public static final String ARG_TITLE = "arg-title";
        @Bind(R.id.txt_pay)
        TextView txtPay;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_slidepay, container, false);
            ButterKnife.bind(this, rootView);
            return rootView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            final Bundle arg = super.getArguments();
            this.txtPay.setText(arg.getString(ARG_TITLE));
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }
    }

    @Override
    protected int getCurrentStep() {
        return 2;
    }

    @Override
    protected String getToolbarTitle() {
        return "PURCHASE INFO";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            paymentType = data.getStringExtra(Common.FIELD_PAYMENT_TYPE);
            txtPayment.setTextColor(getResources().getColor(R.color.textDarkGray));
            if(paymentType.equals(Utils.TYPE_CC)){
                CCScreenModel creditcardInformation = data.getParcelableExtra(CCScreenModel.class.getName());
                name_cc = creditcardInformation.getName_cc();

                if(name_cc.equals(Utils.BLANK)){
                    is_verified = true;
                }else{
                    is_verified = false;
                }

                if(is_verified){
                    is_new_card = "0";
                    cc_token_id = creditcardInformation.getCreditcardInformation().getSaved_token_id();
                    cc_card_id = creditcardInformation.getCreditcardInformation().getMasked_card();

                    txtPayment.setText("• • • • "+cc_card_id.substring(cc_card_id.indexOf("-")+1, cc_card_id.length()));
                }else{
                    is_new_card = "1";
                    cardDetails = creditcardInformation.getCardDetails();
                    cc_card_id = creditcardInformation.getCardDetails().getCardNumber();

                    txtPayment.setText("• • • • " + cc_card_id.substring(cc_card_id.length() - 4, cc_card_id.length()));
                }

                String headCC = cc_card_id.substring(0, 1);
                imgPayment.setVisibility(View.VISIBLE);
                if(headCC.equals("4")){
                    imgPayment.setImageResource(R.drawable.logo_visa);
                }else{
                    imgPayment.setImageResource(R.drawable.logo_mastercard);
                }
            }else if(paymentType.equals(Utils.TYPE_BP)){
                txtPayment.setText(getString(R.string.va_mandiri));
                imgPayment.setVisibility(View.VISIBLE);
                imgPayment.setImageResource(R.drawable.logo_mandiri);
                txtPayment.setTypeface(null, Typeface.NORMAL);
            }else if(paymentType.equals(Utils.TYPE_VA)){
                txtPayment.setText(getString(R.string.other_bank));
                imgPayment.setVisibility(View.GONE);
                txtPayment.setTypeface(null, Typeface.NORMAL);
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
                PostPaymentModel postPaymentModel = new PostPaymentModel(paymentType, "1", productSummary.getOrder_id(), token, name_cc);

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

    private void doPayment(PostPaymentModel postPaymentModel){
        CommerceManager.loaderPayment(postPaymentModel, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                dismissLoadingDialog();
                startActivity(new Intent(PurchaseInfoActivity.this, HowToPayActivity.class));
            }

            @Override
            public void onFailure(int responseCode, String message) {

            }
        });

        initLoadingDialog();
    }

    private void initLoadingDialog(){
        if(progressDialog==null){
            progressDialog = new ProgressDialog(PurchaseInfoActivity.this);
            progressDialog.setMessage(getString(R.string.loading));
        }

        progressDialog.show();
    }

    private void dismissLoadingDialog(){
        if(progressDialog!=null&progressDialog.isShowing())
            progressDialog.dismiss();
    }
}