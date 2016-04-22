package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.RelativeLayout;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.setup.country.CountryActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.PostSummaryModel;
import com.jiggie.android.model.ProductListModel;
import com.jiggie.android.presenter.GuestPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LTE on 2/29/2016.
 */
public class AddGuestActivity extends ToolbarActivity {

    /*ImageView img_close;
    EditText edt_name, edt_email, edt_62, edt_phone;
    RelativeLayout rel_save;*/
    ProductListModel.Data.ProductList.Purchase detailPurchase = null;
    ProductListModel.Data.ProductList.Reservation detailReservation = null;

    boolean textWatchEdited = false;
    @Bind(R.id.edt_name)
    MaterialEditText edtName;
    @Bind(R.id.edt_email)
    MaterialEditText edtEmail;
    @Bind(R.id.edt_62)
    MaterialEditText edt62;
    @Bind(R.id.edt_phone)
    MaterialEditText edtPhone;
    @Bind(R.id.rel_save)
    RelativeLayout relSave;

    GuestPresenter guestPresenter;
    private static final String TAG = AddGuestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guest);
        ButterKnife.bind(this);
        super.bindView();

        initView();
        preDefined();
        guestPresenter = new GuestPresenter();
    }

    private void initView() {
        /*img_close = (ImageView)findViewById(R.id.img_close);
        edt_name = (EditText)findViewById(R.id.edt_name);
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_62 = (EditText)findViewById(R.id.edt_62);
        edt_phone = (EditText)findViewById(R.id.edt_phone);
        rel_save = (RelativeLayout)findViewById(R.id.rel_save);*/
        super.setToolbarTitle(getString(R.string.title_guest), true);

        relSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guestName = edtName.getText().toString();
                String guestEmail = edtEmail.getText().toString();
                String guest62 = edt62.getText().toString();
                if (guest62.contains("+")) {
                    guest62 = guest62.substring(1, guest62.length());
                }
                String guestPhoneN = edtPhone.getText().toString();
                String guestPhone = /*guest62 +*/ guestPhoneN;

                if (!isFieldError(guestName, guestEmail, guest62, guestPhoneN)) {
                    if (!guestName.isEmpty() && !guestEmail.isEmpty() && !guest62.isEmpty() && !guestPhoneN.isEmpty()) {
                        Intent result = new Intent();
                        result.putExtra(Common.FIELD_GUEST_NAME, guestName);
                        result.putExtra(Common.FIELD_GUEST_EMAIL, guestEmail);
                        result.putExtra(Common.FIELD_GUEST_PHONE, guestPhone);
                        result.putExtra("dial_code", guest62);
                        PostSummaryModel.Guest_detail gDetail = new PostSummaryModel.Guest_detail
                                (guestName, guestEmail, guestPhone, guest62);
                        guestPresenter.saveGuest(gDetail);
                        setResult(RESULT_OK, result);
                        finish();
                    } else {
                        //error handling
                    }
                }
            }
        });

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtName.setTextColor(getResources().getColor(R.color.textDarkGray));
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEnability();
            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtEmail.setTextColor(getResources().getColor(R.color.textDarkGray));
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEnability();
            }
        });


        edt62.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt62.setTextColor(getResources().getColor(R.color.textDarkGray));

            }

            @Override
            public void afterTextChanged(Editable s) {
                //edt_62.setText("+"+edt_62.getText().toString());

                String str = s.toString();

                if (textWatchEdited) {
                    textWatchEdited = false;
                    edt62.setSelection(str.length(), str.length());
                    return;
                }

                // do something
                textWatchEdited = true;
                if (!str.contains("+") && !str.isEmpty()) {
                    str = "+" + str;
                }
                edt62.setText(str);
                checkEnability();
            }
        });

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtPhone.setTextColor(getResources().getColor(R.color.textDarkGray));
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEnability();
            }
        });
    }

    private void preDefined() {
        Intent a = getIntent();
        String type_transaction = a.getStringExtra(Common.FIELD_TRANS_TYPE);

        EventDetailModel.Data.EventDetail eventDetail = a.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        edtName.setText(a.getStringExtra(Common.FIELD_GUEST_NAME));
        edtEmail.setText(a.getStringExtra(Common.FIELD_GUEST_EMAIL));
        String phone = a.getStringExtra(Common.FIELD_GUEST_PHONE);
        if (!phone.equals(Utils.BLANK)) {
            //String s62 = phone.substring(0, 2);
            //String phoneN = phone.substring(2, (phone.length()));
            String s62 = a.getStringExtra("dial_code");
            //s62 = null;
            if(s62 == null)
                s62 = "";
            if(!s62.isEmpty())
            {
                edt62.setText("+" + s62);
            }
            else
            {
                edt62.setText(s62);
            }

            edtPhone.setText(phone);
        }

        edt62.setFocusable(false);
        //edt62.setClickable(true);

        sendMixpanel(a, type_transaction, eventDetail);
        checkEnability();
    }

    private void sendMixpanel(Intent a, String type_transaction, EventDetailModel.Data.EventDetail eventDetail) {
        CommEventMixpanelModel commEventMixpanelModel = null;
        if (type_transaction.equals(Common.TYPE_PURCHASE)) {
            detailPurchase = a.getParcelableExtra(ProductListModel.Data.ProductList.Purchase.class.getName());
            commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                    eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), detailPurchase.getName(), detailPurchase.getTicket_type(), detailPurchase.getTotal_price(), detailPurchase.getMax_purchase());
        } else if (type_transaction.equals(Common.TYPE_RESERVATION)) {
            detailReservation = a.getParcelableExtra(ProductListModel.Data.ProductList.Reservation.class.getName());
            commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                    eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), detailReservation.getName(), detailReservation.getTicket_type(), detailReservation.getTotal_price(), detailReservation.getMax_guests());
        }

        App.getInstance().trackMixPanelCommerce(Utils.COMM_GUEST_INFO, commEventMixpanelModel);
    }

    private boolean isFieldError(String name, String email, String str62, String phone) {
        boolean isError = false;
        if (name.isEmpty()) {
            isError = true;
            edtName.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edtName.setError(Utils.BLANK);
        }
        if (email.isEmpty()) {
            isError = true;
            edtEmail.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edtEmail.setError(Utils.BLANK);
        }
        if (str62.isEmpty()) {
            isError = true;
            edt62.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edt62.setError(Utils.BLANK);
        }
        if (phone.isEmpty()) {
            isError = true;
            edtPhone.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            edtPhone.setError(Utils.BLANK);
        }
        if (!email.isEmpty()) {
            if (!isValidEmail(email)) {
                isError = true;
                edtEmail.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                edtEmail.setError(Utils.BLANK);
            }
        }
        return isError;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void checkEnability() {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String s_62 = edt62.getText().toString();
        String s_phone = edtPhone.getText().toString();
        boolean isItEnable = true;

        if (name.equals(Utils.BLANK)) {
            isItEnable = false;
        }
        if (email.equals(Utils.BLANK)) {
            isItEnable = false;
        }
        if (s_62.equals(Utils.BLANK)) {
            isItEnable = false;
        }
        if (s_phone.equals(Utils.BLANK)) {
            isItEnable = false;
        }

        if (isItEnable) {
            relSave.setEnabled(true);
        } else {
            relSave.setEnabled(false);
        }
    }

    @OnClick(R.id.edt_62)
    public void onEdt62Click()
    {
        Intent i = new Intent(this, CountryActivity.class);
        startActivityForResult(i ,Utils.REQUEST_CODE_CHOOSE_COUNTRY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Utils.REQUEST_CODE_CHOOSE_COUNTRY)
        {
            if(resultCode == RESULT_OK)
            {
                edt62.setText(
                        data.getStringExtra("dial_code").replace(" ", ""));
            }
        }
    }
}
