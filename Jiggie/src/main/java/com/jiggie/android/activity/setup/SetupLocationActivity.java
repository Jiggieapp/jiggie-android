package com.jiggie.android.activity.setup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.activity.ecommerce.ProductListActivity;
import com.jiggie.android.component.BitmapUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.BaseActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.Success2Model;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by rangg on 15/01/2016.
 */
public class SetupLocationActivity extends BaseActivity {
    @Bind(R.id.switchView)
    Switch switchView;
    @Bind(R.id.root)
    View root;
    ProgressDialog dialog = null;
    public static final String TAG = SetupLocationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_setup_location);
        super.bindView();

        EventBus.getDefault().register(this);

        final Bitmap background = BitmapUtility.getBitmapResource(R.mipmap.signup1);
        final Bitmap blurBackground = BitmapUtility.blur(background);
        this.root.setBackground(new BitmapDrawable(super.getResources(), blurBackground));
    }

    @Override
    protected int getThemeResource() {
        return R.style.AppTheme_Setup;
    }

    @OnClick(R.id.btnDone)
    @SuppressWarnings("unused")
    void btnDoneOnClick() {
        final ProgressDialog dialog = App.showProgressDialog(this);
        final Intent intent = super.getIntent();

        final MemberSettingModel memberSettingModel = new MemberSettingModel();
        final SettingModel currentSettingModel = AccountManager.loadSetting();
        memberSettingModel.setAccount_type(currentSettingModel.getData().getAccount_type());
        memberSettingModel.setLocation(this.switchView.isChecked() ? 1 : 0);
        memberSettingModel.setGender(currentSettingModel.getData().getGender());
        memberSettingModel.setGender_interest(currentSettingModel.getData().getGender_interest());
        memberSettingModel.setFb_id(AccessToken.getCurrentAccessToken().getUserId());
        memberSettingModel.setChat(intent.getBooleanExtra(SetupNotificationActivity.PARAM_NOTIFICATION, true) ? 1 : 0);
        memberSettingModel.setFeed(intent.getBooleanExtra(SetupNotificationActivity.PARAM_NOTIFICATION, true) ? 1 : 0);
        memberSettingModel.setExperiences(TextUtils.join(",", intent.getStringArrayExtra(SetupTagsActivity.PARAM_EXPERIENCES)));

        AccountManager.loaderMemberSetting(memberSettingModel);
        if (this.switchView.isChecked()) {
            showLocationDialog();
        } else {
            actionDone(this.switchView.isChecked());
        }
    }

    public void onEvent(Success2Model message) {
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

    public void onEvent(ExceptionModel message) {
        if (message.getFrom().equals(Utils.FROM_MEMBER_SETTING)) {
            if (isActive()) {
                Toast.makeText(SetupLocationActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void showLocationDialog() {
        AlertDialog dialogNotif = new AlertDialog.Builder(this)
                .setTitle(R.string.title_dialog_location).setMessage(R.string.msg_dialog_location)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actionDone(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actionDone(false);
                    }
                }).create();
        dialogNotif.show();

    }

    private void actionDone(boolean isChecked) {
        dialog = App.showProgressDialog(this);
        final Intent intent = super.getIntent();

        final MemberSettingModel memberSettingModel = new MemberSettingModel();
        final SettingModel currentSettingModel = AccountManager.loadSetting();
        memberSettingModel.setAccount_type(currentSettingModel.getData().getAccount_type());
        memberSettingModel.setLocation(isChecked ? 1 : 0);
        memberSettingModel.setGender(currentSettingModel.getData().getGender());
        memberSettingModel.setGender_interest(currentSettingModel.getData().getGender_interest());
        memberSettingModel.setFb_id(AccessToken.getCurrentAccessToken().getUserId());
        memberSettingModel.setChat(intent.getBooleanExtra(SetupNotificationActivity.PARAM_NOTIFICATION, true) ? 1 : 0);
        memberSettingModel.setFeed(intent.getBooleanExtra(SetupNotificationActivity.PARAM_NOTIFICATION, true) ? 1 : 0);
        memberSettingModel.setExperiences(TextUtils.join(",", intent.getStringArrayExtra(SetupTagsActivity.PARAM_EXPERIENCES)));

        AccountManager.loaderMemberSetting(memberSettingModel);
        currentSettingModel.getData().getNotifications().setLocation(isChecked);
        currentSettingModel.getData().getNotifications().setChat(intent.getBooleanExtra(SetupNotificationActivity.PARAM_NOTIFICATION, true));
        currentSettingModel.getData().getNotifications().setFeed(intent.getBooleanExtra(SetupNotificationActivity.PARAM_NOTIFICATION, true));

        ArrayList<String> arrExperiences = new ArrayList<String>(Arrays.asList(intent.getStringArrayExtra(SetupTagsActivity.PARAM_EXPERIENCES)));
        currentSettingModel.getData().setExperiences(arrExperiences);

        AccountManager.saveSetting(currentSettingModel);
    }
}