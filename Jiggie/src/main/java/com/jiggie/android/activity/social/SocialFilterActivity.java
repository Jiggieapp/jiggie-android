package com.jiggie.android.activity.social;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.facebook.AccessToken;
import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.SettingModel;

import butterknife.Bind;
import butterknife.OnClick;

public class SocialFilterActivity extends ToolbarActivity implements SocialView  {
    @Bind(R.id.progressBar)
    View progressBar;

    @Bind(R.id.layoutError) View layoutError;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    SocialFilterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_filter);
        super.bindView();
        super.setToolbarTitle("Test", true);

        presenter = new SocialFilterImplementation(this);
        btnRetryOnClick();
    }

    @OnClick(R.id.btnRetry)
    void btnRetryOnClick() {
        presenter.fetchSetting();
    }

    @Override
    public void showProgressDialog() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.layoutError.setVisibility(View.GONE);
        this.scrollView.setVisibility(View.GONE);
    }

    @Override
    public void dismissProgressDialog() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.layoutError.setVisibility(View.GONE);
        this.scrollView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorLayout() {
        this.progressBar.setVisibility(View.GONE);
        this.layoutError.setVisibility(View.VISIBLE);
        this.scrollView.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorLayout() {
        this.progressBar.setVisibility(View.GONE);
        this.layoutError.setVisibility(View.GONE);
        this.scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateUI() {

    }
}
