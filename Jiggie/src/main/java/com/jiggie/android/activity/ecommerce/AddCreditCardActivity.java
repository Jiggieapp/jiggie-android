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
import com.jiggie.android.App;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.R;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CCModel;
import com.jiggie.android.model.CCScreenModel;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.PostCCModel;
import com.jiggie.android.model.SummaryModel;

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
    EventDetailModel.Data.EventDetail eventDetail;
    SummaryModel.Data.Product_summary productSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cc);

        Intent a = getIntent();
        totalPrice = a.getStringExtra(Common.FIELD_PRICE);
        productSummary = a.getParcelableExtra(SummaryModel.Data.Product_summary.class.getName());
        eventDetail = a.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        sendMixpanel(productSummary, eventDetail);
        initView();
    }

    private void sendMixpanel(SummaryModel.Data.Product_summary productSummary, EventDetailModel.Data.EventDetail eventDetail){
        CommEventMixpanelModel commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), productSummary.getProduct_list().get(0).getName(), productSummary.getProduct_list().get(0).getTicket_type(),
                productSummary.getTotal_price(), productSummary.getProduct_list().get(0).getMax_buy());
        App.getInstance().trackMixPanelCommerce(Utils.COMM_CREDIT_CARD, commEventMixpanelModel);
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

                String initial = s.toString();
                // remove all non-digits characters
                String processed = initial.replaceAll("\\D", "");
                // insert a space after all groups of 4 digits that are followed by another digit
                processed = StringUtility.getCCNumberFormat(processed);
                // to avoid stackoverflow errors, check that the processed is different from what's already
                //  there before setting
                if (!initial.equals(processed)) {
                    // set the value
                    s.replace(0, initial.length(), processed);
                }

                checkEnability();
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

                checkEnability();
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

                checkEnability();
            }
        });

        rel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNumber = edt_cc_number.getText().toString().replaceAll("\\D","");
                String cvv = edt_cvv.getText().toString();
                String date = edt_date.getText().toString();
                if (!isFieldError(cardNumber, cvv, date)) {
                    saveCC(cardNumber, cvv, month, year, totalPrice);
                }

                //saveCC(cardNumber, "123", 01, 2020, totalPrice);
            }
        });


        checkEnability();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth) {
            String months = String.format("%02d", monthOfYear+1);
            month = monthOfYear+1;
            year = years;
            edt_date.setText(months+"/"+String.valueOf(year));

            checkEnability();
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

    private void checkEnability(){
        String cardNumber = edt_cc_number.getText().toString();
        String cvv = edt_cvv.getText().toString();
        String date = edt_date.getText().toString();
        boolean isItEnable = true;

        if(cardNumber.equals(Utils.BLANK)){
            isItEnable = false;
        }
        if(cvv.equals(Utils.BLANK)){
            isItEnable = false;
        }
        if(date.equals(Utils.BLANK)){
            isItEnable = false;
        }

        if(isItEnable){
            rel_save.setEnabled(true);
        }else{
            rel_save.setEnabled(false);
        }
    }


}
