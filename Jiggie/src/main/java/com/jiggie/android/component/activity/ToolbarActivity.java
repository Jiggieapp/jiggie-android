package com.jiggie.android.component.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.SupportModel;

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

    ProgressDialog progressDialog;

    @Override
    protected void bindView() {
        super.bindView();
        super.setSupportActionBar(this.toolbar);

        App.runningActivity = this;
    }

    protected void setToolbarTitle(String title, boolean backEnabled) {
        final ActionBar actionBar = super.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(backEnabled);
            actionBar.setTitle(title);
        }
    }

    protected void setBackIcon(int res){
        final ActionBar actionBar = super.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(res);
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        if(CommerceManager.supportData==null){
            CommerceManager.loaderSupport(new CommerceManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    progressDialog.dismiss();
                    SupportModel supportModel = (SupportModel)object;
                    CommerceManager.supportData = supportModel.getData().getSupport();
                    openSMS(CommerceManager.supportData.getTelp());
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    progressDialog.dismiss();
                    Toast.makeText(ToolbarActivity.this, message, Toast.LENGTH_LONG).show();
                }
            });
        }else{
            progressDialog.dismiss();
            openSMS(CommerceManager.supportData.getTelp());
        }

    }

    private void openSMS(String telp){
        Uri uri = Uri.parse("smsto:"+telp);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        //it.putExtra("sms_body", "The SMS text");
        startActivity(it);
    }
}
