package com.jiggie.android.activity.promo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.jiggie.android.R;
import com.jiggie.android.activity.invite.InviteFriendsActivity;
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
                RedeemCodeManager.loaderRedeemCode(new PostRedeemCodeModel(AccessToken.getCurrentAccessToken().getUserId(), edtCode.getText().toString()), new RedeemCodeManager.OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {
                        try {
                            SuccessRedeemCodeModel successRedeemCodeModel = (SuccessRedeemCodeModel) object;
                            tesShowPromoDialog(successRedeemCodeModel.getData().getRedeem_code().getMsg());
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(int responseCode, String message) {

                    }
                });
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

            }
        });
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
}
