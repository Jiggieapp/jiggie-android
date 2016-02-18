package com.jiggie.android.activity.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Success2Model;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class VerifyPhoneNumberActivity extends ToolbarActivity {

    @Bind(R.id.lblCountryCode)
    TextView lblCountryCode;
    @Bind(R.id.txtCountryCode)
    EditText txtCountryCode;
    @Bind(R.id.lblDoVerify)
    TextView lblDoVerify;

    private int state = 0;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        super.bindView();
        super.setBackEnabled(true);

        //EventBus.getDefault().register(this);
        lblCountryCode.setPaintFlags(lblCountryCode.getPaintFlags() |
                Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent()
    {

    }

    @OnClick(R.id.lblCountryCode)
    public void onLblCountryCodeClick()
    {
        if(state == 0)
        {
            lblCountryCode.setText(getResources().getString(R.string.usa));
            state = 1;
        }
        else if(state == 1)
        {
            lblCountryCode.setText(getResources().getString(R.string.international));
            state = 0;
        }
        txtCountryCode.setText("");
    }

    @OnClick(R.id.txtCountryCode)
    public void onTxtCountryCodeClick()
    {

    }

    @OnClick(R.id.lblDoVerify)
    public void onLblDoVerifyClick()
    {
        phoneNumber = txtCountryCode.getText().toString();
        showConfirmationDialog(phoneNumber);
    }

    private void showConfirmationDialog(final String number) {
        final AlertDialog builder = new AlertDialog.Builder(VerifyPhoneNumberActivity.this)
                //.setTitle(getAct)
                .setMessage(getResources().getString(R.string.submit_validation_dialog) + " " + number)
                .setPositiveButton(VerifyPhoneNumberActivity.this.getResources().getString(R.string.send_code), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AccountManager.verifyPhoneNumber(number);
                        showProgressDialog();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

    public void onEvent(Success2Model success2Model)
    {
        dismissProgressDialog();
        Intent i = new Intent(VerifyPhoneNumberActivity.this, ConfirmationCodeActivity.class);
        i.putExtra(Common.PHONE_NUMBER, phoneNumber);
        startActivity(i);
    }

    ProgressDialog progressDialog;
    private void showProgressDialog()
    {
        if(progressDialog == null)
            progressDialog = ProgressDialog.show(this
                    ,"", App.getInstance().getApplicationContext()
                    .getResources().getString(R.string.wait));
        progressDialog.show();
    }

    private void dismissProgressDialog()
    {
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
