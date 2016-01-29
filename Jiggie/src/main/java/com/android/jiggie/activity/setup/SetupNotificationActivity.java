package com.android.jiggie.activity.setup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.android.jiggie.App;
import com.android.jiggie.R;
import com.android.jiggie.activity.MainActivity;
import com.android.jiggie.component.BitmapUtility;
import com.android.jiggie.component.activity.BaseActivity;

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
        App.getInstance().trackMixPanelEvent("Walkthrough Push Notifications");
        final Intent intent = new Intent(this,SetupLocationActivity.class);
        intent.putExtra(PARAM_NOTIFICATION, this.switchView.isChecked());
        intent.putExtras(super.getIntent());
        super.startActivity(intent);
    }

    @Override
    protected int getThemeResource() { return R.style.AppTheme_Setup; }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
