package com.jiggie.android.activity.ecommerce;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

    AlertDialog dialog3ds;
    String tokens;
    String totalPrice;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cc);

        Intent a = getIntent();
        totalPrice = a.getStringExtra(Common.FIELD_PRICE);

        /*edtCcDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = createDialogWithoutDateField();
                datePickerDialog.show();
            }
        });

        edtCcCvv.setError("error message");*/
        edt_cc_name = (EditText)findViewById(R.id.edt_cc_name);
        edt_cc_number = (EditText)findViewById(R.id.edt_cc_number);
        edt_cvv = (EditText)findViewById(R.id.edt_cc_cvv);
        edt_date = (EditText)findViewById(R.id.edt_cc_date);
        txt_cancel = (TextView)findViewById(R.id.txt_cancel);
        rel_save = (RelativeLayout)findViewById(R.id.rel_save);
        //edt_cvv.setError("error message");

        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = createDialogWithoutDateField();
                datePickerDialog.show();
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNumber = edt_cc_number.getText().toString();
                String cvv = edt_cvv.getText().toString();
                saveCC(cardNumber, "123", 01, 2020, totalPrice);
            }
        });
    }

    private void saveCC(final String cardNumber, String cvv, int expMonth, int expYear, final String price){
        initLoadingDialog();
        String maskedCard = cardNumber.substring(0, cardNumber.length()-4)+"-"+cardNumber.substring(cardNumber.length()-4, cardNumber.length());

        CCScreenModel.CardDetails cardDetails = new CCScreenModel.CardDetails(cardNumber, cvv, expMonth, expYear, price);

        CommerceManager.arrCCScreen.add(new CCScreenModel(new CCModel.Data.Creditcard_information(maskedCard, Utils.BLANK, Utils.BLANK, Utils.TYPE_CC),
                cardDetails, edt_cc_name.getText().toString()));

        dismissLoadingDialog();
        setResult(RESULT_OK, new Intent());
        finish();


    }

    private void initLoadingDialog(){
        if(progressDialog==null){
            progressDialog = new ProgressDialog(AddCreditCardActivity.this);
            progressDialog.setMessage(getString(R.string.loading));
        }

        progressDialog.show();
    }

    private void dismissLoadingDialog(){
        if(progressDialog!=null&progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private DatePickerDialog createDialogWithoutDateField() {

        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(AddCreditCardActivity.this, null, year, month, day);

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
