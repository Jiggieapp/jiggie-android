package com.jiggie.android.activity.profile;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;

import butterknife.Bind;

/**
 * Created by rangg on 19/11/2015.
 */
public class PrivacyPolicyActivity extends ToolbarActivity {
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.webView) WebView webView;

    protected String getUrl() { return "http://www.partyhostapp.com/privacy/"; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_text);
        super.bindView();
        super.setBackEnabled(true);

        this.webView.setWebViewClient(this.client);
        this.webView.loadUrl(this.getUrl());
    }

    private WebViewClient client = new WebViewClient() {
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isActive())
                        webView.loadUrl(getUrl());
                }
            }, 5000);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    };
}
