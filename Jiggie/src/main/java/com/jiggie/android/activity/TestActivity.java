package com.jiggie.android.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import com.jiggie.android.component.activity.ToolbarActivity;

import id.co.veritrans.android.api.VTDirect;
import id.co.veritrans.android.api.VTInterface.ITokenCallback;
import id.co.veritrans.android.api.VTModel.VTCardDetails;
import id.co.veritrans.android.api.VTModel.VTToken;
import id.co.veritrans.android.api.VTUtil.VTConfig;

/**
 * Created by LTE on 2/1/2016.
 */
public class TestActivity extends ToolbarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set environment
        VTConfig.VT_IsProduction = false;

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
        cardDetails.setCard_exp_month(1);
        cardDetails.setCard_exp_year(2020);

        //set true or false to enable or disable 3dsecure
        cardDetails.setSecure(true);
        cardDetails.setGross_amount("10000");

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
                    webView.loadUrl(token.getRedirect_url());
                }
                //print or send token
                String a = token.toString();
                Log.d("token", token.getToken_id());
            }

            @Override
            public void onError(Exception e) {

                //Something is wrong, get details message by print e.getMessage()
            }
        });
    }
}
