package com.jiggie.android.activity.ecommerce;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.R;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CCModel;
import com.jiggie.android.model.CCScreenModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.PostCCModel;

import java.lang.reflect.Field;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import id.co.veritrans.android.api.VTDirect;
import id.co.veritrans.android.api.VTInterface.ITokenCallback;
import id.co.veritrans.android.api.VTModel.VTCardDetails;
import id.co.veritrans.android.api.VTModel.VTToken;
import id.co.veritrans.android.api.VTUtil.VTConfig;

/**
 * Created by LTE on 2/26/2016.
 */
public class AddCreditCardActivity extends ToolbarActivity {

    EditText edt_cvv, edt_date, edt_cc_number, edt_cc_name;
    TextView txt_cancel;
    RelativeLayout rel_save;

    String totalPrice;
    private int month, year;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cc);

        Intent a = getIntent();
        totalPrice = a.getStringExtra(Common.FIELD_PRICE);

        initView();
    }

    private void initView(){
        edt_cc_name = (EditText)findViewById(R.id.edt_cc_name);
        edt_cc_number = (EditText)findViewById(R.id.edt_cc_number);
        edt_cvv = (EditText)findViewById(R.id.edt_cc_cvv);
        edt_date = (EditText)findViewById(R.id.edt_cc_date);
        txt_cancel = (TextView)findViewById(R.id.txt_cancel);
        rel_save = (RelativeLayout)findViewById(R.id.rel_save);

        datePickerDialog = createDialogWithoutDateField();

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edt_cc_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_cc_number.setTextColor(getResources().getColor(R.color.textDarkGray));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_cvv.setTextColor(getResources().getColor(R.color.textDarkGray));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!datePickerDialog.isShowing()) {
                    datePickerDialog.show();
                }
            }
        });

        edt_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !datePickerDialog.isShowing()) {
                    datePickerDialog.show();
                }
            }
        });

        edt_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_date.setTextColor(getResources().getColor(R.color.textDarkGray));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNumber = edt_cc_number.getText().toString();
                String cvv = edt_cvv.getText().toString();
                String date = edt_date.getText().toString();
                if (!isFieldError(cardNumber, cvv, date)) {
                    saveCC(cardNumber, cvv, month, year, totalPrice);
                }

                //saveCC(cardNumber, "123", 01, 2020, totalPrice);
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth) {
            String months = String.format("%02d", monthOfYear);
            month = monthOfYear;
            year = years;
            edt_date.setText(months+"/"+String.valueOf(year));
        }
    };

    private boolean isFieldError(String cardNumber, String cvv, String date){
        boolean isError = false;
        if(cardNumber.isEmpty()){
            isError = true;
            edt_cc_number.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edt_cc_number.setError(Utils.BLANK);
        }if(cvv.isEmpty()){
            isError = true;
            edt_cvv.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edt_cvv.setError(Utils.BLANK);
        }if(date.isEmpty()){
            isError = true;
            edt_date.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edt_date.setError(Utils.BLANK);
        }
        return isError;
    }

    private void saveCC(final String cardNumber, String cvv, int expMonth, int expYear, final String price){
        String maskedCard = cardNumber.substring(0, cardNumber.length()-4)+"-"+cardNumber.substring(cardNumber.length()-4, cardNumber.length());

        CCScreenModel.CardDetails cardDetails = new CCScreenModel.CardDetails(cardNumber, cvv, expMonth, expYear, price);

        CommerceManager.arrCCScreen.add(new CCScreenModel(new CCModel.Data.Creditcard_information(maskedCard, Utils.BLANK, Utils.BLANK, Utils.TYPE_CC),
                cardDetails, edt_cc_name.getText().toString()));
        setResult(RESULT_OK, new Intent());
        finish();


    }

    private DatePickerDialog createDialogWithoutDateField() {

        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddCreditCardActivity.this, dateSetListener, year, month, day);

        try {
            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {

                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField
                            .get(dpd);
                    Field datePickerFields[] = datePickerDialogField.getType()
                            .getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName())
                                || "mDaySpinner".equals(datePickerField
                                .getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;

    }


}
