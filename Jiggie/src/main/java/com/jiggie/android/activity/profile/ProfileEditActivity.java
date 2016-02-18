package com.jiggie.android.activity.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.facebook.AccessToken;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.AboutModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SuccessModel;

/**
 * Created by rangg on 17/11/2015.
 */
public class ProfileEditActivity extends ToolbarActivity {
    @Bind(R.id.textView) TextView textView;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile_edit);
        super.bindView();
        super.setBackEnabled(true);

        EventBus.getDefault().register(this);

        final String value = super.getIntent().getStringExtra(Common.FIELD_ABOUT);
        this.textView.setText(value);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save)
            this.save();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.root)
    void rootOnClick() { this.textView.requestFocus(); }

    private void save() {
        dialog = App.showProgressDialog(this);

        final AboutModel aboutModel = new AboutModel();
        aboutModel.setFb_id(AccessToken.getCurrentAccessToken().getUserId());
        aboutModel.setAbout(this.textView.getText().toString());

        String sd = String.valueOf(new Gson().toJson(aboutModel));

        AccountManager.loaderEditAbout(aboutModel);
    }

    public void onEvent(Success2Model message){
        if(message.getResponse()==1){
            LoginModel login = AccountManager.loadLogin();
            login.setAbout(this.textView.getText().toString());
            AccountManager.saveLogin(login);
            App.getInstance().trackMixPanelEvent("MyProfile Update");

            setResult(RESULT_OK, new Intent().putExtra(Common.FIELD_ABOUT, this.textView.getText().toString()));

            dialog.dismiss();
            finish();
        }else{
            Toast.makeText(ProfileEditActivity.this, "edit about failed", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void onEvent(ExceptionModel message){
        if(message.getFrom().equals(Utils.FROM_PROFILE_EDIT)){
            Toast.makeText(ProfileEditActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
