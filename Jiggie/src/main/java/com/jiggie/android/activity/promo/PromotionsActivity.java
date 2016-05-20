package com.jiggie.android.activity.promo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.invite.InviteFriendsActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.RedeemCodeManager;
import com.jiggie.android.model.PostRedeemCodeModel;
import com.jiggie.android.model.SuccessRedeemCodeModel;

import butterknife.Bind;

/**
 * Created by LTE on 5/11/2016.
 */
public class PromotionsActivity extends ToolbarActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btnApply)
    Button btnApply;
    @Bind(R.id.edt_code)
    EditText edtCode;
    @Bind(R.id.btn_invite)
    Button btnInvite;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_promotions);
        super.bindView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Promotions");

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtCode.getWindowToken(), 0);

                /*progressDialog = App.showProgressDialog(PromotionsActivity.this);
                RedeemCodeManager.loaderRedeemCode(new PostRedeemCodeModel(AccessToken.getCurrentAccessToken().getUserId(), edtCode.getText().toString()), new RedeemCodeManager.OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {
                        try {
                            SuccessRedeemCodeModel successRedeemCodeModel = (SuccessRedeemCodeModel) object;
                            if(successRedeemCodeModel.getResponse()== Utils.CODE_FAILED){
                                showFailedPromoDialog(successRedeemCodeModel.getData().getRedeem_code().getMsg());
                            }else{
                                tesShowPromoDialog(successRedeemCodeModel.getData().getRedeem_code().getMsg());
                            }

                            hideProgressDialog();
                        } catch (Exception e) {
                            Log.d("Redeem Code", e.toString());
                            hideProgressDialog();
                        }
                    }

                    @Override
                    public void onFailure(int responseCode, String message) {
                        Log.d("Redeem Code", message);
                        hideProgressDialog();
                    }
                });*/
            }
        });

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PromotionsActivity.this, InviteFriendsActivity.class));
            }
        });

        edtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = s.toString();
                String b = a.toUpperCase();
                if(!a.equals(b)){
                    s.replace(0, a.length(), b);
                }

                checkEnability();
            }
        });

        checkEnability();
    }

    private void hideProgressDialog()
    {
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    private void tesShowPromoDialog(String text) {
        final Dialog dialog = new Dialog(PromotionsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_promo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        Button btnUseNow = (Button) dialog.findViewById(R.id.btn_use_now);
        Button btnLater = (Button) dialog.findViewById(R.id.btn_later);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        TextView txt_desc = (TextView)dialog.findViewById(R.id.txt_desc);

        txt_desc.setText(text);

        btnUseNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    AlertDialog alertDialog = null;
    private void showFailedPromoDialog(String text){
        alertDialog = new AlertDialog.Builder(PromotionsActivity.this).setMessage(text)
                .setMessage(text)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (alertDialog != null && alertDialog.isShowing())
                            alertDialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void checkEnability() {
        String code = edtCode.getText().toString();
        boolean isItEnable = true;

        if (code.equals(Utils.BLANK)) {
            isItEnable = false;
        }

        if (isItEnable) {
            btnApply.setEnabled(true);
        } else {
            btnApply.setEnabled(false);
        }
    }
}
