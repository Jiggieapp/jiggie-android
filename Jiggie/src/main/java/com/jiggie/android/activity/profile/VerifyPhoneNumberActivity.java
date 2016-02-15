package com.jiggie.android.activity.profile;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class VerifyPhoneNumberActivity extends ToolbarActivity {

    @Bind(R.id.lblCountryCode)
    TextView lblCountryCode;
    @Bind(R.id.txtCountryCode)
    EditText txtCountryCode;

    private int state = 0;

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

}
