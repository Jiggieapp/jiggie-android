package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
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

                if(!guestName.isEmpty() && !guestEmail.isEmpty() && !guest62.isEmpty() && !guestPhoneN.isEmpty()){
                    App.getSharedPreferences().edit().putString(Common.FIELD_GUEST_NAME, guestName).putString(Common.FIELD_GUEST_EMAIL, guestEmail).putString(Common.FIELD_GUEST_PHONE, guestPhone).commit();
                    setResult(RESULT_OK, new Intent().putExtra(Common.FIELD_GUEST_NAME, guestName).putExtra(Common.FIELD_GUEST_EMAIL, guestEmail).putExtra(Common.FIELD_GUEST_PHONE, guestPhone));
                    finish();
                }else{
                    //error handling
                }
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

}
