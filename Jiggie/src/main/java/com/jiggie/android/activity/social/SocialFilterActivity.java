package com.jiggie.android.activity.social;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.fragment.SocialTabFragment;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.MemberSettingResultModel;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.OnClick;

public class SocialFilterActivity extends ToolbarActivity implements SocialView {
    @Bind(R.id.progressBar)
    View progressBar;

    @Bind(R.id.layoutError)
    View layoutError;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.btnApply)
    TextView lblApply;
    @Bind(R.id.lbl_reset_filter)
    TextView lblResetFilter;
    @Bind(R.id.switchSocialize)
    Switch switchSocialize;
    @Bind(R.id.lbl_male)
    TextView lblMale;
    @Bind(R.id.lbl_female)
    TextView lblFemale;
    /*@Bind(R.id.slider_age)
    SeekBar sliderAge;*/
    @Bind(R.id.slider_age_double)
    RangeBar sliderAgeDouble;
    @Bind(R.id.slider_distance)
    SeekBar sliderLocation;

    SocialFilterPresenter presenter;
    MemberSettingResultModel.Data.MemberSettings memberSettings;
    MemberSettingResultModel memberSettingResultModel;

    private static final String TAG = SocialFilterActivity.class.getSimpleName();
    private final int MIN_AGE = 18;
    private final int MAX_AGE = 60;
    private final int MAX_LOCATION = 160;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_filter);
        super.bindView();
        super.setBackEnabled(true);


        presenter = new SocialFilterImplementation(this);
        sliderLocation.setMax(MAX_LOCATION);
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
        this.lblApply.setVisibility(View.GONE);
        this.lblResetFilter.setVisibility(View.GONE);
    }

    @Override
    public void dismissProgressDialog() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.layoutError.setVisibility(View.GONE);
        this.scrollView.setVisibility(View.GONE);
        this.lblApply.setVisibility(View.GONE);
        this.lblResetFilter.setVisibility(View.GONE);
    }

    @Override
    public void showErrorLayout() {
        this.progressBar.setVisibility(View.GONE);
        this.layoutError.setVisibility(View.VISIBLE);
        this.scrollView.setVisibility(View.GONE);
        this.lblApply.setVisibility(View.GONE);
        this.lblResetFilter.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorLayout() {
        this.progressBar.setVisibility(View.GONE);
        this.layoutError.setVisibility(View.GONE);
        this.scrollView.setVisibility(View.VISIBLE);
        this.lblApply.setVisibility(View.VISIBLE);
        this.lblResetFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateUI(MemberSettingResultModel memberSettingResultModel) {
        this.memberSettingResultModel = memberSettingResultModel;
        memberSettings
                = memberSettingResultModel.getData().getMembersettings();
        MemberSettingResultModel.Data.MemberSettings.Notifications notifications
                = memberSettings.getNotifications();
        final boolean isSocialize = notifications.isFeed();
        switchSocialize.setChecked(isSocialize);

        final String interestedIn = memberSettings.getGender_interest();
        if (interestedIn.equalsIgnoreCase("female")) {
            lblFemale.setTextColor(this.getResources().getColor(R.color.blue_selector));
        } else if (interestedIn.equalsIgnoreCase("male")) {
            lblMale.setTextColor(this.getResources().getColor(R.color.blue_selector));
        } else if (interestedIn.equalsIgnoreCase("both")) {
            lblFemale.setTextColor(this.getResources().getColor(R.color.blue_selector));
            lblMale.setTextColor(this.getResources().getColor(R.color.blue_selector));
        }

        int fromAge = memberSettings.getFrom_age();
        int toAge = memberSettings.getTo_age();
        int distance = memberSettings.getDistance();
        String area_event = memberSettings.getArea_event();

        if (distance == 0 || distance > MAX_LOCATION)
            sliderLocation.setProgress(MAX_LOCATION);
        else sliderLocation.setProgress(distance);

        if (fromAge > 0 || toAge > 0) {
            if (fromAge < 12) fromAge = 12;
            if (toAge > 60) toAge = 60;
            sliderAgeDouble.setRangePinsByValue(fromAge, toAge);
        }

    }

    @Override
    public void saveSocialSetting(MemberSettingResultModel memberSettingResultModel) {

    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, getResources().getString(R.string.preferred_experience), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(SocialTabFragment.TAG);
        i.putExtra(Utils.IS_ON, switchSocialize.isChecked());
        sendBroadcast(i);
        dismissPleaseWaitDialog();
    }

    @Override
    public void onFailure() {
        dismissPleaseWaitDialog();
    }

    ProgressDialog progressDialog;

    private void showPleaseWaitDialog() {
        progressDialog = ProgressDialog.show(this
                , ""
                , getResources().getString(R.string.wait));
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void dismissPleaseWaitDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @OnClick(R.id.btnApply)
    public void onBtnApplyClick() {
        showPleaseWaitDialog();
        memberSettings.getNotifications().setFeed(switchSocialize.isChecked());
        memberSettings.setFrom_age(Integer.parseInt(sliderAgeDouble.getLeftPinValue()));
        memberSettings.setTo_age(Integer.parseInt(sliderAgeDouble.getRightPinValue()));
        memberSettings.setArea_event("jakarta");
        memberSettings.setDistance(sliderLocation.getProgress());

        //MemberSettingModel temp = new MemberSettingModel(new MemberSettingResultModel());
        memberSettingResultModel.getData().setMembersettings(memberSettings);
        MemberSettingModel temp = new MemberSettingModel(memberSettingResultModel);
        presenter.updateSetting(temp);
    }

    @OnClick(R.id.lbl_male)
    public void onLblMaleClick() {
        if (memberSettings.getGender_interest().equalsIgnoreCase("female")) {
            memberSettings.setGender_interest("both");
            lblMale.setTextColor(getResources()
                    .getColor(R.color.blue_selector));
        } else if (memberSettings.getGender_interest().equalsIgnoreCase("male")) {
            //do nothing
        } else if (memberSettings.getGender_interest().equalsIgnoreCase("both")) {
            memberSettings.setGender_interest("female");
            lblMale.setTextColor(getResources()
                    .getColor(android.R.color.primary_text_light));
        }
    }

    @OnClick(R.id.lbl_female)
    public void onLblFemaleClick() {
        if (memberSettings.getGender_interest().equalsIgnoreCase("female")) {
            //do nothing
        } else if (memberSettings.getGender_interest().equalsIgnoreCase("male")) {
            //do nothing
            memberSettings.setGender_interest("both");
            lblFemale.setTextColor(getResources()
                    .getColor(R.color.blue_selector));
        } else if (memberSettings.getGender_interest().equalsIgnoreCase("both")) {
            memberSettings.setGender_interest("male");
            lblFemale.setTextColor(getResources()
                    .getColor(android.R.color.primary_text_light));
        }
    }

    @OnClick(R.id.lbl_reset_filter)
    public void resetfilter() {
        switchSocialize.setChecked(true);
        sliderLocation.setProgress(MAX_LOCATION);
        if (memberSettings.getGender().equalsIgnoreCase("male")) {
            memberSettings.setGender_interest("female");
            lblMale.setTextColor(getResources()
                    .getColor(android.R.color.primary_text_light));
            lblFemale.setTextColor(getResources()
                    .getColor(R.color.blue_selector));
        } else {
            memberSettings.setGender_interest("male");
            lblMale.setTextColor(getResources()
                    .getColor(R.color.blue_selector));
            lblFemale.setTextColor(getResources()
                    .getColor(android.R.color.primary_text_light));
        }

        sliderAgeDouble.setRangePinsByValue(MIN_AGE, MAX_AGE);
    }
}
