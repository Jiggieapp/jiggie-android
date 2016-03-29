package com.jiggie.android.activity.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.Success2Model;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class ConfirmationCodeActivity extends ToolbarActivity {

    @Bind(R.id.lblPhoneNumber)
    TextView lblPhoneNumber;
    @Bind(R.id.lblSubmitValidation)
    TextView lblSubmitValidation;
    @Bind(R.id.txtValidationCode)
    EditText txtValidationCode;

    private ProgressDialog progressDialog;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_code);

        super.bindView();
        super.setBackEnabled(true);

        phoneNumber = getIntent().getStringExtra(Common.PHONE_NUMBER);
        lblPhoneNumber.setText(phoneNumber);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.lblSubmitValidation)
    public void onLblSubmitValidationClick()
    {
        showProgressDialog();
        AccountManager.verifyVerificationCode(txtValidationCode.getText().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(Success2Model success2Model)
    {
        dismissProgressDialog();
        final AlertDialog builder = new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.verification_success))
                .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SettingModel settingModel = AccountManager.loadSetting();
                        settingModel.getData().setPhone(phoneNumber);
                        AccountManager.saveSetting(settingModel);
                        dialog.dismiss();
                        redirect();
                    }
                })
                .setCancelable(false)
                .create();
        builder.show();
    }

    public void onEvent(ExceptionModel exceptionModel)
    {
        if(exceptionModel.getFrom().equalsIgnoreCase(Utils.FROM_VERIFY_VERIFICATION_CODE))
        {
            dismissProgressDialog();
            final AlertDialog builder = new AlertDialog.Builder(this)
                    //.setTitle(getAct)
                    .setMessage(getResources().getString(R.string.verification_failed))
                    .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .create();
            builder.show();
        }
    }

    private void showProgressDialog()
    {
        if(progressDialog == null)
            progressDialog = ProgressDialog.show(this
                    , "", App.getInstance().getApplicationContext()
                    .getResources().getString(R.string.wait))
                    ;
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog()
    {
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void redirect()
    {
        Intent i = new Intent(this, MainActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
