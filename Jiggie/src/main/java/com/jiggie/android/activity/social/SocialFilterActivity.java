package com.jiggie.android.activity.social;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.google.gson.Gson;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.MemberSettingResultModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_filter);
        super.bindView();
        super.setToolbarTitle("Test", true);

        presenter = new SocialFilterImplementation(this);
        sliderLocation.setMax(160);
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

    MemberSettingResultModel.Data.MemberSettings memberSettings;
    MemberSettingResultModel memberSettingResultModel;

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

        if(distance == 0)
            sliderLocation.setProgress(160);
        else sliderLocation.setProgress(distance);

        if(fromAge == 0)
            sliderAgeDouble.setLeft(12);
        else sliderAgeDouble.setLeft(fromAge);
        if(toAge == 0)
            sliderAgeDouble.setRight(50);
        else sliderAgeDouble.setRight(toAge);

    }

    @Override
    public void saveSocialSetting(MemberSettingResultModel memberSettingResultModel) {

    }

    @OnClick(R.id.btnApply)
    public void onBtnApplyClick()
    {
        memberSettings.getNotifications().setFeed(switchSocialize.isChecked());
        memberSettings.setFrom_age(Integer.parseInt(sliderAgeDouble.getLeftPinValue()));
        memberSettings.setTo_age(Integer.parseInt(sliderAgeDouble.getRightPinValue()));
        memberSettings.setArea_event("jakarta");

        //MemberSettingModel temp = new MemberSettingModel(new MemberSettingResultModel());
        memberSettingResultModel.getData().setMembersettings(memberSettings);
        MemberSettingModel temp = new MemberSettingModel(memberSettingResultModel);
        Utils.d("Account social", "social " +new Gson().toJson(temp));
        AccountManager.loaderMemberSetting(temp);
    }

    @OnClick(R.id.lbl_male)
    public void onLblMaleClick()
    {
        if(memberSettings.getGender_interest().equalsIgnoreCase("female"))
        {
            memberSettings.setGender_interest("both");
            lblMale.setTextColor(getResources()
                    .getColor(R.color.blue_selector));
        }
        else if(memberSettings.getGender_interest().equalsIgnoreCase("male"))
        {
            //do nothing
        }
        else if(memberSettings.getGender_interest().equalsIgnoreCase("both"))
        {
            memberSettings.setGender_interest("female");
            lblMale.setTextColor(getResources()
                    .getColor(android.R.color.primary_text_light));
        }
    }

    @OnClick(R.id.lbl_female)
    public void onLblFemaleClick()
    {
        if(memberSettings.getGender_interest().equalsIgnoreCase("female"))
        {
            //do nothing
        }
        else if(memberSettings.getGender_interest().equalsIgnoreCase("male"))
        {
            //do nothing
            memberSettings.setGender_interest("both");
            lblFemale.setTextColor(getResources()
                    .getColor(R.color.blue_selector));
        }
        else if(memberSettings.getGender_interest().equalsIgnoreCase("both"))
        {
            memberSettings.setGender_interest("male");
            lblFemale.setTextColor(getResources()
                    .getColor(android.R.color.primary_text_light));
        }
    }
}
