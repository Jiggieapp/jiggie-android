package com.jiggie.android.activity.setup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.component.BitmapUtility;
import com.jiggie.android.component.activity.BaseActivity;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.LoginSetting;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.Setting;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by rangg on 15/01/2016.
 */
public class SetupLocationActivity extends BaseActivity {
    @Bind(R.id.switchView) Switch switchView;
    @Bind(R.id.root) View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_setup_location);
        super.bindView();

        final Bitmap background = BitmapUtility.getBitmapResource(R.mipmap.signup1);
        final Bitmap blurBackground = BitmapUtility.blur(background);
        this.root.setBackground(new BitmapDrawable(super.getResources(), blurBackground));
    }

    @Override
    protected int getThemeResource() { return R.style.AppTheme_Setup; }

    @OnClick(R.id.btnDone)
    @SuppressWarnings("unused")
    void btnDoneOnClick() {
        final ProgressDialog dialog = App.showProgressDialog(this);
        final LoginSetting loginSetting = new LoginSetting();
        final Setting currentSetting = Setting.getCurrentSetting();
        final Intent intent = super.getIntent();

        loginSetting.setAccountType(currentSetting.getAccountType());
        loginSetting.setLocation(this.switchView.isChecked());
        loginSetting.setGender(currentSetting.getGenderString());
        loginSetting.setGenderInterest(currentSetting.getGenderInterestString());
        loginSetting.setFacebookId(AccessToken.getCurrentAccessToken().getUserId());
        loginSetting.setChat(intent.getBooleanExtra(SetupNotificationActivity.PARAM_NOTIFICATION, true));
        loginSetting.setFeed(intent.getBooleanExtra(SetupNotificationActivity.PARAM_NOTIFICATION, true));
        loginSetting.setExperiences(TextUtils.join(",", intent.getStringArrayExtra(SetupTagsActivity.PARAM_EXPERIENCES)));

        final MemberSettingModel memberSettingModel = new MemberSettingModel();
        //memberSettingModel.setGender();

        VolleyHandler.getInstance().createVolleyRequest("membersettings", loginSetting, new VolleyRequestListener<Boolean, JSONObject>() {
            @Override
            public Boolean onResponseAsync(JSONObject jsonObject) { return jsonObject.optBoolean(Common.FIELD_STATUS, false); }

            @Override
            public void onResponseCompleted(Boolean value) {
                if (isActive()) {
                    dialog.dismiss();
                    finish();
                }
                // Start new activity from app context instead of current activity. This prevent crash when activity has been destroyed.
                final App app = App.getInstance();
                app.trackMixPanelEvent("Walkthrough Location");
                app.trackMixPanelEvent("Completed Walkthrough");
                App.getSharedPreferences().edit().putBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, true).apply();
                app.startActivity(new Intent(App.getInstance(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isActive()) {
                    Toast.makeText(SetupLocationActivity.this, App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
