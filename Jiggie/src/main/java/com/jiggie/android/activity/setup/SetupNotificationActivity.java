package com.jiggie.android.activity.setup;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Switch;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.BitmapUtility;
import com.jiggie.android.component.activity.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by rangg on 14/01/2016.
 */
public class SetupNotificationActivity extends BaseActivity {
    public static final String PARAM_NOTIFICATION = "param-notification";

    @Bind(R.id.switchView) Switch switchView;
    @Bind(R.id.root) View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_setup_notification);
        super.bindView();

        final Bitmap background = BitmapUtility.getBitmapResource(R.mipmap.signup1);
        final Bitmap blurBackground = BitmapUtility.blur(background);
        this.root.setBackground(new BitmapDrawable(super.getResources(), blurBackground));
    }

    @OnClick(R.id.btnNext)
    @SuppressWarnings("unused")
    void btnNextOnClick() {
        if(switchView.isChecked()){
            showNotifDialog();
        }else{
            actionNext(switchView.isChecked());
        }

    }

    @Override
    protected int getThemeResource() { return R.style.AppTheme_Setup; }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void showNotifDialog() {
        final AlertDialog dialogNotif = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
                .setTitle(R.string.title_dialog_notif).setMessage(R.string.msg_dialog_notif)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actionNext(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actionNext(false);
                    }
                }).create();
        dialogNotif.show();

    }

    private void actionNext(boolean isChecked){
        App.getInstance().trackMixPanelEvent("Walkthrough Push Notifications");
        final Intent intent = new Intent(SetupNotificationActivity.this,SetupLocationActivity.class);
        intent.putExtra(PARAM_NOTIFICATION, isChecked);
        intent.putExtras(super.getIntent());
        super.startActivity(intent);
    }
}
