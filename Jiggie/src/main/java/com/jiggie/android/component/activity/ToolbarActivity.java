package com.jiggie.android.component.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by rangg on 19/11/2015.
 */
public abstract class ToolbarActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Bind(R.id.img_help)
    ImageView imgHelp;

    @Override
    protected void bindView() {
        super.bindView();
        super.setSupportActionBar(this.toolbar);
    }

    protected void setToolbarTitle(String title, boolean backEnabled) {
        final ActionBar actionBar = super.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(backEnabled);
            actionBar.setTitle(title);
        }
    }

    protected void setBackEnabled(boolean backEnabled) {
        final ActionBar actionBar = super.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(backEnabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    protected void redirectToHome() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Nullable
    @OnClick(R.id.img_help)
    public void onHelpClick() {
        //go to support email
                /*final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", con.getString(R.string.support_email), null));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {con.getString(R.string.support_email)}); // hack for android 4.3
                intent.putExtra(Intent.EXTRA_SUBJECT, "Support");
                con.startActivity(Intent.createChooser(intent, con.getString(R.string.support)));*/

        Uri uri = Uri.parse("smsto:081218288317");
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        //it.putExtra("sms_body", "The SMS text");
        startActivity(it);
    }
}
