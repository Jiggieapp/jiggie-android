package com.jiggie.android.activity.profile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.MemberSettingModel;
import com.facebook.AccessToken;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.SuccessModel;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by rangg on 17/11/2015.
 */
public class ProfileSettingActivity extends ToolbarActivity implements CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.txtGenderInterest) TextView txtGenderInterest;
    @Bind(R.id.scrollView) ScrollView scrollView;
    @Bind(R.id.switchSocial) Switch switchSocial;
    @Bind(R.id.progressBar) View progressBar;
    @Bind(R.id.txtGender) TextView txtGender;
    @Bind(R.id.switchChat) Switch switchChat;
    @Bind(R.id.layoutError) View layoutError;

    SettingModel setting;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile_setting);
        super.bindView();
        super.setBackEnabled(true);
        this.btnRetryOnClick();

        EventBus.getDefault().register(this);
        App.getInstance().trackMixPanelEvent("View Settings");
    }

    @OnClick(R.id.btnRetry)
    void btnRetryOnClick() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.layoutError.setVisibility(View.GONE);
        this.scrollView.setVisibility(View.GONE);

        AccountManager.loaderSetting(AccessToken.getCurrentAccessToken().getUserId());
    }

    private void refreshView(SettingModel setting) {
        switchSocial.setOnCheckedChangeListener(null);
        switchChat.setOnCheckedChangeListener(null);

        txtGender.setText(setting.getData().getGender());
        switchChat.setChecked(setting.getData().getNotifications().isChat());
        switchSocial.setChecked(setting.getData().getNotifications().isFeed());
        txtGenderInterest.setText(setting.getData().getGender_interest());

        switchSocial.setOnCheckedChangeListener(ProfileSettingActivity.this);
        switchChat.setOnCheckedChangeListener(ProfileSettingActivity.this);
        scrollView.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { this.sendServerSetting(); }

    @SuppressWarnings("unused")
    @OnClick(R.id.layoutGender)
    void layoutGenderOnClick() {
        new AlertDialog.Builder(this).setItems(R.array.items_gender, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String[] items = getResources().getStringArray(R.array.items_gender);
                txtGender.setText(items[which]);
                sendServerSetting();
            }
        }).show();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.layoutGenderInterest)
    void layoutGenderInterestOnClick() {
        new AlertDialog.Builder(this).setItems(R.array.items_gender_interest, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String[] items = getResources().getStringArray(R.array.items_gender_interest);
                txtGenderInterest.setText(items[which]);
                sendServerSetting();
            }
        }).show();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.layoutPrivacy)
    void layoutPrivacyOnClick() { super.startActivity(new Intent(this, PrivacyPolicyActivity.class)); }

    @SuppressWarnings("unused")
    @OnClick(R.id.layoutTerm)
    void layoutTermOnClick() { super.startActivity(new Intent(this, TermOfUseActivity.class)); }

    private void sendServerSetting() {
        //membersetting adalah object utk post, settingmodel hasil response
        final MemberSettingModel memberSettingModel = new MemberSettingModel();
        setting = AccountManager.loadSetting();
        dialog = App.showProgressDialog(this);
        final String[] genders = super.getResources().getStringArray(R.array.items_gender);
        final String[] genderInterest = super.getResources().getStringArray(R.array.items_gender_interest);
        memberSettingModel.setChat(this.switchChat.isChecked() ? 1 : 0);
        memberSettingModel.setFeed(this.switchSocial.isChecked() ? 1 : 0);

        int index_gender = Arrays.binarySearch(genders, this.txtGender.getText().toString(), String.CASE_INSENSITIVE_ORDER);
        if(index_gender<0){
            memberSettingModel.setGender(setting.getData().getGender());
        }else{
            memberSettingModel.setGender(genders[Arrays.binarySearch(genders, this.txtGender.getText().toString(), String.CASE_INSENSITIVE_ORDER)]);
        }

        int index_gender_interest = Arrays.binarySearch(genders, this.txtGender.getText().toString(), String.CASE_INSENSITIVE_ORDER);
        if(index_gender_interest<0){
            memberSettingModel.setGender_interest(setting.getData().getGender_interest());
        }else{
            memberSettingModel.setGender_interest(genderInterest[Arrays.binarySearch(genderInterest, this.txtGenderInterest.getText().toString(), String.CASE_INSENSITIVE_ORDER)]);
        }



        memberSettingModel.setAccount_type(setting.getData().getAccount_type());
        memberSettingModel.setLocation(setting.getData().getNotifications().isLocation() ? 1 : 0);
        memberSettingModel.setFb_id(AccessToken.getCurrentAccessToken().getUserId());
        memberSettingModel.setExperiences(TextUtils.join(",", setting.getData().getExperiences()));

        AccountManager.loaderMemberSetting(memberSettingModel);

        if(index_gender_interest>=0)
            setting.getData().setGender(genders[Arrays.binarySearch(genders, this.txtGender.getText().toString(), String.CASE_INSENSITIVE_ORDER)]);

        if(index_gender_interest>=0)
            setting.getData().setGender_interest(genderInterest[Arrays.binarySearch(genderInterest, this.txtGenderInterest.getText().toString(), String.CASE_INSENSITIVE_ORDER)]);


        setting.getData().getNotifications().setChat(this.switchChat.isChecked());
        setting.getData().getNotifications().setFeed(this.switchSocial.isChecked());

        AccountManager.saveSetting(setting);
    }

    public void onEvent(SuccessModel message){
        if (isActive()) {
            refreshView(setting);
            dialog.dismiss();
        }
    }

    public void onEvent(SettingModel message){
        if (isActive())
            refreshView(message);
        AccountManager.saveSetting(message);
    }

    public void onEvent(ExceptionModel message){
        if(message.getFrom().equals(Utils.FROM_PROFILE_SETTING)){
            if (isActive()) {
                layoutError.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }else{
            if (isActive()) {
                Toast.makeText(ProfileSettingActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                refreshView(AccountManager.loadSetting());
                dialog.dismiss();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
