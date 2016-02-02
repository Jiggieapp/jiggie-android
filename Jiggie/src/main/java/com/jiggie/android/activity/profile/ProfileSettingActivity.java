package com.jiggie.android.activity.profile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.model.Setting;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;

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

    public final static String TAG = ProfileSettingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile_setting);
        super.bindView();
        super.setBackEnabled(true);
        this.btnRetryOnClick();
        App.getInstance().trackMixPanelEvent("View Settings");
    }

    @OnClick(R.id.btnRetry)
    void btnRetryOnClick() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.layoutError.setVisibility(View.GONE);
        this.scrollView.setVisibility(View.GONE);

        VolleyHandler.getInstance().createVolleyRequest("membersettings/" + AccessToken.getCurrentAccessToken().getUserId(), new VolleyRequestListener<Setting, JSONObject>() {
            @Override
            public Setting onResponseAsync(JSONObject jsonObject) { return new Setting(VolleyHandler.getData(jsonObject)); }

            @Override
            public void onResponseCompleted(Setting value) {
                if (isActive())
                    refreshView(value);
                value.save();
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isActive()) {
                    layoutError.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void refreshView(Setting setting) {
        final Resources res = super.getResources();
        final String[] genders = res.getStringArray(R.array.items_gender);
        final String[] genderInterests = res.getStringArray(R.array.items_gender_interest);

        switchSocial.setOnCheckedChangeListener(null);
        switchChat.setOnCheckedChangeListener(null);

        txtGender.setText(genders[setting.getGender()]);
        switchChat.setChecked(setting.getNotification().isChat());
        switchSocial.setChecked(setting.getNotification().isFeed());
        txtGenderInterest.setText(genderInterests[setting.getGenderInterest()]);

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
        final Setting setting = Setting.getCurrentSetting().duplicate();
        final ProgressDialog dialog = App.showProgressDialog(this);
        final String[] genders = super.getResources().getStringArray(R.array.items_gender);
        final String[] genderInterest = super.getResources().getStringArray(R.array.items_gender_interest);

        setting.getNotification().setChat(this.switchChat.isChecked());
        setting.getNotification().setFeed(this.switchSocial.isChecked());
        setting.setGender(Arrays.binarySearch(genders, this.txtGender.getText().toString(), String.CASE_INSENSITIVE_ORDER));
        setting.setGenderInterest(Arrays.binarySearch(genderInterest, this.txtGenderInterest.getText().toString(), String.CASE_INSENSITIVE_ORDER));

        VolleyHandler.getInstance().createVolleyRequest("membersettings", setting, new VolleyRequestListener<Void, JSONObject>() {
            @Override
            public Void onResponseAsync(JSONObject jsonObject) {
                if (!jsonObject.optBoolean("success", false))
                    throw new RuntimeException(getString(R.string.error_unknown));
                return null;
            }

            @Override
            public void onResponseCompleted(Void value) {
                if (isActive()) {
                    refreshView(setting);
                    dialog.dismiss();
                }
                setting.save();
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isActive()) {
                    Toast.makeText(ProfileSettingActivity.this, App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    refreshView(Setting.getCurrentSetting());
                    dialog.dismiss();
                }
            }
        });
    }
}
