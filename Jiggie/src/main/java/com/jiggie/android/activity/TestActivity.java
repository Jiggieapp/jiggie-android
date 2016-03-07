package com.jiggie.android.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CCModel;
import com.jiggie.android.model.PostPaymentModel;

import java.net.URLEncoder;

import id.co.veritrans.android.api.VTDirect;
import id.co.veritrans.android.api.VTInterface.ITokenCallback;
import id.co.veritrans.android.api.VTModel.VTCardDetails;
import id.co.veritrans.android.api.VTModel.VTToken;
import id.co.veritrans.android.api.VTUtil.VTConfig;

/**
 * Created by LTE on 2/1/2016.
 */
public class TestActivity extends ToolbarActivity implements CommerceManager.LoadCCListener{

    AlertDialog dialog3ds;
    String tokens;

    public final static String PAYMENT_API = "https://api.veritrans.co.id/v2/token";

    public final static String PAYMENT_API_SANDBOX = "https://api.sandbox.veritrans.co.id/v2/token";

    public static String getPaymentApiUrl(){
        if(VTConfig.VT_IsProduction){
            return PAYMENT_API;
        }
        return PAYMENT_API_SANDBOX;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final TextView txt_token = (TextView)findViewById(R.id.txt_token);
        Button btn = (Button)findViewById(R.id.btn);

        //CommerceManager.loaderCCList("123456", this);

        PostPaymentModel postPaymentModel = new PostPaymentModel("va", "", Long.parseLong("1457346333975"), "");

        String sd = String.valueOf(new Gson().toJson(postPaymentModel));

        CommerceManager.loaderPayment(postPaymentModel);

        //VTPART--------------------------------------------
        //set environment
        /*VTConfig.VT_IsProduction = false;

        //set client key
        VTConfig.CLIENT_KEY = "VT-client-gJRBbRZC0t_-JXUD";


        //Create VTDirect Object. It’s config is Based on VTConfig Static Class
        VTDirect vtDirect = new VTDirect();

        //Create VTCardDetails Object to be set to VTDirect Object
        VTCardDetails cardDetails = new VTCardDetails();

        //TODO: Set your card details based on user input.
        //this is a sample
        cardDetails.setCard_number("4811111111111114"); // 3DS Dummy CC
        cardDetails.setCard_cvv("123");
        cardDetails.setCard_exp_month(01);
        cardDetails.setCard_exp_year(2020);

        //set true or false to enable or disable 3dsecure
        cardDetails.setSecure(true);
        final String price = "52";
        cardDetails.setGross_amount(price);

        //Set VTCardDetails to VTDirect
        vtDirect.setCard_details(cardDetails);

        //Simply Call getToken function and put your callback to handle data
        vtDirect.getToken(new ITokenCallback() {
            @Override
            public void onSuccess(VTToken token) {

                //use token anyhow you want, maybe send it to your server. Example here to check whether you are using 3dsecure feature or not.
                if (token.getRedirect_url() != null) {

                    //using 3d secure
                    WebView webView = new
                            WebView(TestActivity.this);

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
                    webView.setWebViewClient(new VtWebViewClient(token.getToken_id(), price));
                    webView.loadUrl(token.getRedirect_url());

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(TestActivity.this);
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
                tokens = token.toString();
                txt_token.setText(tokens);
                Log.d("token", token.getToken_id());



            }

            @Override
            public void onError(Exception e) {

                //Something is wrong, get details message by print e.getMessage()
            }
        });*/
        //END of VTPART--------------------------

    }

    @Override
    public void onLoadCC(int status, String message, CCModel ccModel) {
        String sd = String.valueOf(new Gson().toJson(ccModel));

        Log.d("fill", sd);
    }

    private class VtWebViewClient extends WebViewClient {

        String token;
        String price;

        public VtWebViewClient(String token, String price){
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
                //PostPaymentModel postPaymentModel = new PostPaymentModel("cc", "1", Long.parseLong("1457335721926"), tokens);
                PostPaymentModel postPaymentModel = new PostPaymentModel("cc", "1", Long.parseLong("1457335721926"), "481111b43bce6a-4913-42d5-8f21-5d1e701b265b");

                String sd = String.valueOf(new Gson().toJson(postPaymentModel));

                //CommerceManager.loaderPayment(postPaymentModel);

                //send token to server
                /*SendTokenAsync sendTokenAsync = new SendTokenAsync();
                sendTokenAsync.execute(token,price);*/
                //close web dialog
                dialog3ds.dismiss();
                //show loading dialog
                //ProgressDialog.show(TestActivity.this, "", "Sending Data to Server. Please Wait...", true);

            } else if (url.startsWith(getPaymentApiUrl() + "/redirect/") || url.contains("3dsecure")) {
                 //Do nothing
            } else {
                if(dialog3ds != null){
                    dialog3ds.dismiss();
                }
            }
        }

    }
}
