package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.model.Common;

/**
 * Created by LTE on 2/29/2016.
 */
public class AddGuestActivity extends ToolbarActivity {

    TextView txt_cancel;
    EditText edt_name, edt_email, edt_62, edt_phone;
    RelativeLayout rel_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guest);

        initView();
        preDefined();

    }

    private void initView(){
        txt_cancel = (TextView)findViewById(R.id.txt_cancel);
        edt_name = (EditText)findViewById(R.id.edt_name);
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_62 = (EditText)findViewById(R.id.edt_62);
        edt_phone = (EditText)findViewById(R.id.edt_phone);
        rel_save = (RelativeLayout)findViewById(R.id.rel_save);


        rel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guestName = edt_name.getText().toString();
                String guestEmail = edt_email.getText().toString();
                String guest62 = edt_62.getText().toString();
                String guestPhoneN = edt_phone.getText().toString();
                String guestPhone = guest62+guestPhoneN;

                if(!isFieldError(guestName, guestEmail, guest62, guestPhoneN)){
                    if(!guestName.isEmpty() && !guestEmail.isEmpty() && !guest62.isEmpty() && !guestPhoneN.isEmpty()){
                        App.getSharedPreferences().edit().putString(Common.FIELD_GUEST_NAME, guestName).putString(Common.FIELD_GUEST_EMAIL, guestEmail).putString(Common.FIELD_GUEST_PHONE, guestPhone).commit();
                        setResult(RESULT_OK, new Intent().putExtra(Common.FIELD_GUEST_NAME, guestName).putExtra(Common.FIELD_GUEST_EMAIL, guestEmail).putExtra(Common.FIELD_GUEST_PHONE, guestPhone));
                        finish();
                    }else{
                        //error handling
                    }
                }
            }
        });

        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_name.setTextColor(getResources().getColor(R.color.textDarkGray));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_email.setTextColor(getResources().getColor(R.color.textDarkGray));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_62.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_62.setTextColor(getResources().getColor(R.color.textDarkGray));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_phone.setTextColor(getResources().getColor(R.color.textDarkGray));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void preDefined(){
        Intent a = getIntent();
        edt_name.setText(a.getStringExtra(Common.FIELD_GUEST_NAME));
        edt_email.setText(a.getStringExtra(Common.FIELD_GUEST_EMAIL));
        String phone = a.getStringExtra(Common.FIELD_GUEST_PHONE);
        if(!phone.equals(Utils.BLANK)){
           String s62 = phone.substring(0, 2);
           String phoneN = phone.substring(2, (phone.length()));
            edt_62.setText(s62);
            edt_phone.setText(phoneN);
        }

    }

    private boolean isFieldError(String name, String email, String str62, String phone){
        boolean isError = false;
        if(name.isEmpty()){
            isError = true;
            edt_name.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edt_name.setError(Utils.BLANK);
        }if(email.isEmpty()){
            isError = true;
            edt_email.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edt_email.setError(Utils.BLANK);
        }if(str62.isEmpty()){
            isError = true;
            edt_62.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edt_62.setError(Utils.BLANK);
        }if(phone.isEmpty()){
            isError = true;
            edt_phone.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edt_phone.setError(Utils.BLANK);
        }
        return isError;
    }

}
