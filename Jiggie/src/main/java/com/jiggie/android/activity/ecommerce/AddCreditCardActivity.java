package com.jiggie.android.activity.ecommerce;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CCModel;
import com.jiggie.android.model.CCScreenModel;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.SummaryModel;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Field;
import java.util.Calendar;

import butterknife.Bind;

/**
 * Created by LTE on 2/26/2016.
 */
public class AddCreditCardActivity extends ToolbarActivity {
    @Bind(R.id.edt_cc_name)
    MaterialEditText edtCcName;
    @Bind(R.id.edt_cc_number)
    MaterialEditText edtCcNumber;
    @Bind(R.id.edt_cc_date)
    MaterialEditText edtCcDate;
    @Bind(R.id.edt_cc_cvv)
    MaterialEditText edtCcCvv;
    @Bind(R.id.rel_save)
    RelativeLayout relSave;

    /*EditText edt_cvv, edt_date, edt_cc_number, edt_cc_name;
    ImageView img_close;
    RelativeLayout rel_save;*/


    private int month, year;
    //DatePickerDialog datePickerDialog;
    String totalPrice;
    EventDetailModel.Data.EventDetail eventDetail;
    SummaryModel.Data.Product_summary productSummary;
    Dialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cc);
        super.bindView();

        Intent a = getIntent();
        totalPrice = a.getStringExtra(Common.FIELD_PRICE);
        productSummary = a.getParcelableExtra(SummaryModel.Data.Product_summary.class.getName());
        eventDetail = a.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        sendMixpanel(productSummary, eventDetail);
        initView();
    }

    private void sendMixpanel(SummaryModel.Data.Product_summary productSummary, EventDetailModel.Data.EventDetail eventDetail) {
        CommEventMixpanelModel commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), productSummary.getProduct_list().get(0).getName(), productSummary.getProduct_list().get(0).getTicket_type(),
                productSummary.getTotal_price(), productSummary.getProduct_list().get(0).getMax_buy());
        App.getInstance().trackMixPanelCommerce(Utils.COMM_CREDIT_CARD, commEventMixpanelModel);
    }

    private void initView() {

        //datePickerDialog = createDialogWithoutDateField();
        super.setToolbarTitle(getString(R.string.title_cc), true);
        initDateDialog();

        edtCcNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtCcNumber.setTextColor(getResources().getColor(R.color.textDarkGray));
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

        edtCcCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtCcCvv.setTextColor(getResources().getColor(R.color.textDarkGray));
            }

            @Override
            public void afterTextChanged(Editable s) {

                checkEnability();
            }
        });

        edtCcDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!datePickerDialog.isShowing()) {
                    datePickerDialog.show();
                }
            }
        });

        edtCcDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !datePickerDialog.isShowing()) {
                    datePickerDialog.show();
                }
            }
        });

        edtCcDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtCcDate.setTextColor(getResources().getColor(R.color.textDarkGray));

            }

            @Override
            public void afterTextChanged(Editable s) {

                checkEnability();
            }
        });

        relSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNumber = edtCcNumber.getText().toString().replaceAll("\\D", "");
                String cvv = edtCcCvv.getText().toString();
                String date = edtCcDate.getText().toString();
                if (!isFieldError(cardNumber, cvv, date)) {
                    saveCC(cardNumber, cvv, month, year, totalPrice);
                }

                //saveCC(cardNumber, "123", 01, 2020, totalPrice);
            }
        });


        checkEnability();
    }

    private boolean isFieldError(String cardNumber, String cvv, String date) {
        boolean isError = false;
        if (cardNumber.isEmpty()) {
            isError = true;
            edtCcNumber.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edtCcNumber.setError(Utils.BLANK);
        } else {
            if (cardNumber.length() < 16) {
                isError = true;
                edtCcNumber.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                edtCcNumber.setError(Utils.BLANK);
            } else {
                if (cardNumber.startsWith("4") || cardNumber.startsWith("5")) {
                    //do nothing
                } else {
                    isError = true;
                    edtCcNumber.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    edtCcNumber.setError(Utils.BLANK);
                }
            }
        }

        if (cvv.isEmpty()) {
            isError = true;
            edtCcCvv.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edtCcCvv.setError(Utils.BLANK);
        }

        if (date.isEmpty()) {
            isError = true;
            edtCcDate.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edtCcDate.setError(Utils.BLANK);
        } else {
            Calendar c1 = Calendar.getInstance();
            c1.set(year, month - 1, 1);

            Calendar c2 = Calendar.getInstance();
            c2.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), 1);

            if (c1.before(c2)) {
                isError = true;
                edtCcDate.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                edtCcDate.setError(Utils.BLANK);
            }
        }
        return isError;
    }

    private void saveCC(final String cardNumber, String cvv, int expMonth, int expYear, final String price) {
        String maskedCard = cardNumber.substring(0, cardNumber.length() - 4) + "-" + cardNumber.substring(cardNumber.length() - 4, cardNumber.length());

        CCScreenModel.CardDetails cardDetails = new CCScreenModel.CardDetails(cardNumber, cvv, expMonth, expYear, price);

        CommerceManager.arrCCScreen.add(new CCScreenModel(new CCModel.Data.Creditcard_information(maskedCard, Utils.BLANK, Utils.BLANK, Utils.TYPE_CC),
                cardDetails, edtCcName.getText().toString()));
        //data yg di local
        CommerceManager.arrCCLocal.add(new CCScreenModel(new CCModel.Data.Creditcard_information(maskedCard, Utils.BLANK, Utils.BLANK, Utils.TYPE_CC),
                cardDetails, edtCcName.getText().toString()));
        //-------------
        setResult(RESULT_OK, new Intent());
        finish();

    }

    private void checkEnability() {
        String cardNumber = edtCcNumber.getText().toString();
        String cvv = edtCcCvv.getText().toString();
        String date = edtCcDate.getText().toString();
        boolean isItEnable = true;

        if (cardNumber.equals(Utils.BLANK)) {
            isItEnable = false;
        }
        if (cvv.equals(Utils.BLANK)) {
            isItEnable = false;
        }
        if (date.equals(Utils.BLANK)) {
            isItEnable = false;
        }

        if (isItEnable) {
            relSave.setEnabled(true);
        } else {
            relSave.setEnabled(false);
        }
    }

    private void initDateDialog() {
        datePickerDialog = new Dialog(AddCreditCardActivity.this);
        datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datePickerDialog.setContentView(R.layout.dialog_datepicker);

        final DatePicker datePicker = (DatePicker) datePickerDialog.findViewById(R.id.datepicker);

        Button btn_ok = (Button) datePickerDialog.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) datePickerDialog.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.dismiss();
                String months = String.format("%02d", datePicker.getMonth() + 1);
                month = datePicker.getMonth() + 1;
                year = datePicker.getYear();
                edtCcDate.setText(months + "/" + String.valueOf(year));

                checkEnability();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
            if (daySpinnerId != 0) {
                View daySpinner = datePicker.findViewById(daySpinnerId);
                if (daySpinner != null) {
                    daySpinner.setVisibility(View.GONE);
                }
            }

            int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
            if (monthSpinnerId != 0) {
                View monthSpinner = datePicker.findViewById(monthSpinnerId);
                if (monthSpinner != null) {
                    monthSpinner.setVisibility(View.VISIBLE);
                }
            }

            int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
            if (yearSpinnerId != 0) {
                View yearSpinner = datePicker.findViewById(yearSpinnerId);
                if (yearSpinner != null) {
                    yearSpinner.setVisibility(View.VISIBLE);
                }
            }
        } else { //Older SDK versions
            Field f[] = datePicker.getClass().getDeclaredFields();
            for (Field field : f) {
                if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner")) {
                    field.setAccessible(true);
                    Object dayPicker = null;
                    try {
                        dayPicker = field.get(datePicker);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((View) dayPicker).setVisibility(View.GONE);
                }

                if (field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner")) {
                    field.setAccessible(true);
                    Object monthPicker = null;
                    try {
                        monthPicker = field.get(datePicker);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((View) monthPicker).setVisibility(View.VISIBLE);
                }

                if (field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner")) {
                    field.setAccessible(true);
                    Object yearPicker = null;
                    try {
                        yearPicker = field.get(datePicker);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((View) yearPicker).setVisibility(View.VISIBLE);
                }
            }
        }

        Calendar minCalendar = Calendar.getInstance();
        minCalendar.set(minCalendar.get(Calendar.YEAR), 1, 0);
        datePicker.setMinDate(minCalendar.getTimeInMillis());

        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(maxCalendar.get(Calendar.YEAR)+10, 12, 0);
        datePicker.setMaxDate(maxCalendar.getTimeInMillis());

    }


}
