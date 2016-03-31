package com.jiggie.android.activity.ecommerce.ticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.AddGuestActivity;
import com.jiggie.android.activity.ecommerce.PurchaseInfoActivity;
import com.jiggie.android.activity.ecommerce.summary.ReservationInfoActivity;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.PostSummaryModel;
import com.jiggie.android.model.ProductListModel;
import com.jiggie.android.model.SummaryModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;

/**
 * Created by Wandy on 3/8/2016.
 */
public class ReservationActivity extends AbstractTicketDetailActivity {

    @Bind(R.id.lblEventName)
    TextView lblEventName;
    @Bind(R.id.lblEventLocation)
    TextView lblEventLocation;
    @Bind(R.id.lblType)
    TextView lblType;
    @Bind(R.id.lblTypeCaption)
    TextView lblTypeCaption;
    @Bind(R.id.lblTypePrice)
    TextView lblTypePrice;
    @Bind(R.id.txt_ticket_desc)
    TextView txtTicketDesc;
    @Bind(R.id.txt_guest_name)
    TextView txtGuestName;
    @Bind(R.id.txt_guest_email)
    TextView txtGuestEmail;
    @Bind(R.id.txt_guest_phone)
    TextView txtGuestPhone;
    @Bind(R.id.lblEstimatedCostCaption)
    TextView lblEstimatedCostCaption;
    @Bind(R.id.lblEstimatedCost)
    TextView lblEstimatedCost;
    @Bind(R.id.lblTicketCaption)
    TextView lblTicketCaption;
    @Bind(R.id.minus_button)
    View minusButton;
    @Bind(R.id.lblQuantity)
    TextView lblQuantity;
    @Bind(R.id.plus_button)
    View plusButton;
    @Bind(R.id.btnDone)
    Button btnDone;
    @Bind(R.id.lblTypePriceCaption)
    TextView lblTypePriceCaption;

    int num_guest = 1;
    ProductListModel.Data.ProductList.Reservation detailReservation = null;
    String eventId, eventName, venueName, startTime, guestName, guestEmail, guestPhone, ticketId;
    int max = 0, price;
    EventDetailModel.Data.EventDetail eventDetail;
    SummaryModel.Data.Product_summary productSummary;
    ProgressDialog progressDialog;
    @Bind(R.id.rel_guest)
    RelativeLayout relGuest;

    @Override
    protected void onCreate() {
        super.setContentView(R.layout.activity_ticket_detail);
        super.bindView();

        preDefined();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLoadingDialog();

                PostSummaryModel.Product_list product_list = new PostSummaryModel.Product_list(ticketId, num_guest);
                ArrayList<PostSummaryModel.Product_list> arrProductList = new ArrayList<PostSummaryModel.Product_list>();
                arrProductList.add(product_list);
                PostSummaryModel.Guest_detail guest_detail = new PostSummaryModel.Guest_detail(guestName, guestEmail, guestPhone);
                PostSummaryModel postSummaryModel = new PostSummaryModel(AccountManager.loadLogin().getFb_id(), eventId, arrProductList, guest_detail);

                String sd = String.valueOf(new Gson().toJson(postSummaryModel));

                CommerceManager.loaderSummary(postSummaryModel, new CommerceManager.OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {
                        SummaryModel dataTemp = (SummaryModel) object;
                        dismissLoadingDialog();

                        if(dataTemp!=null){
                            productSummary = dataTemp.getData().getProduct_summary();

                            String responses = new Gson().toJson(dataTemp);
                            Utils.d("res", responses);

                            Intent i = new Intent(ReservationActivity.this, ReservationInfoActivity.class);
                            i.putExtra(Common.FIELD_EVENT_ID, eventId);
                            i.putExtra(Common.FIELD_EVENT_NAME, eventName);
                            i.putExtra(Common.FIELD_VENUE_NAME, venueName);
                            i.putExtra(Common.FIELD_STARTTIME, startTime);
                            i.putExtra(productSummary.getClass().getName(), productSummary);
                            i.putExtra(eventDetail.getClass().getName(), eventDetail);
                            i.putExtra(Common.FIELD_MIN_DEPOSIT, detailReservation.getMin_deposit_amount());

                            startActivity(i);
                        }else{
                            Toast.makeText(ReservationActivity.this, getString(R.string.msg_wrong), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int responseCode, String message) {
                        dismissLoadingDialog();
                        Utils.d(String.valueOf(responseCode), message);
                    }
                });
            }
        });

        relGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReservationActivity.this, AddGuestActivity.class);
                i.putExtra(Common.FIELD_GUEST_NAME, guestName);
                i.putExtra(Common.FIELD_GUEST_EMAIL, guestEmail);
                if (guestPhone.equals(getString(R.string.phone_number))) {
                    i.putExtra(Common.FIELD_GUEST_PHONE, Utils.BLANK);
                } else {
                    i.putExtra(Common.FIELD_GUEST_PHONE, guestPhone);
                }

                i.putExtra(Common.FIELD_TRANS_TYPE, Common.TYPE_RESERVATION);
                i.putExtra(detailReservation.getClass().getName(), detailReservation);
                i.putExtra(eventDetail.getClass().getName(), eventDetail);
                startActivityForResult(i, 0);
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num_guest > 1) {
                    num_guest--;
                    lblQuantity.setText(String.valueOf(num_guest));
                }

            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num_guest > 0 && num_guest < max) {
                    num_guest++;
                    lblQuantity.setText(String.valueOf(num_guest));
                }
            }
        });

    }

    private void preDefined() {
        Intent a = getIntent();
        eventId = a.getStringExtra(Common.FIELD_EVENT_ID);
        eventName = a.getStringExtra(Common.FIELD_EVENT_NAME);
        venueName = a.getStringExtra(Common.FIELD_VENUE_NAME);
        startTime = a.getStringExtra(Common.FIELD_STARTTIME);
        eventDetail = a.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        detailReservation = a.getParcelableExtra(ProductListModel.Data.ProductList.Reservation.class.getName());

        sendMixpanel(eventDetail);

        lblEventName.setText(eventName);
        try {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(startTime);
            lblEventLocation.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate) + " - " + venueName);
        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }
        lblQuantity.setText(String.valueOf(num_guest));

        try {
            max = Integer.parseInt(detailReservation.getMax_guests());
        }catch (Exception e){
            max = 0;
        }

        price = Integer.parseInt(detailReservation.getPrice());
        ticketId = detailReservation.getTicket_id();

        lblType.setText(detailReservation.getName());
        lblTypeCaption.setText(detailReservation.getSummary());
        lblTypePrice.setText(StringUtility.getRupiahFormat(detailReservation.getPrice()));
        lblTypePriceCaption.setText(getString(R.string.pr_max_guest) + " " + max);
        lblEstimatedCost.setText(StringUtility.getRupiahFormat(String.valueOf(price)));
        txtTicketDesc.setText(detailReservation.getDescription());

        LoginModel loginModel = AccountManager.loadLogin();


        guestName = App.getSharedPreferences().getString(Common.FIELD_GUEST_NAME, Utils.BLANK);
        if (guestName.equals(Utils.BLANK)) {
            guestName = loginModel.getUser_first_name() + " " + loginModel.getUser_last_name();
        }
        guestEmail = App.getSharedPreferences().getString(Common.FIELD_GUEST_EMAIL, Utils.BLANK);
        if (guestEmail.equals(Utils.BLANK)) {
            guestEmail = loginModel.getEmail();
        }
        guestPhone = App.getSharedPreferences().getString(Common.FIELD_GUEST_PHONE, Utils.BLANK);
        if (guestPhone.equals(Utils.BLANK)) {
            guestPhone = AccountManager.loadSetting().getData().getPhone();
            if (guestPhone.equals(Utils.BLANK)) {
                guestPhone = getString(R.string.phone_number);
                txtGuestPhone.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }

        }

        txtGuestName.setText(guestName);
        txtGuestEmail.setText(guestEmail + " | ");
        txtGuestPhone.setText(guestPhone);

        checkEnability(guestName, guestEmail, guestPhone);
    }

    private void sendMixpanel(EventDetailModel.Data.EventDetail eventDetail) {
        CommEventMixpanelModel commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), detailReservation.getName(), detailReservation.getTicket_type(), detailReservation.getTotal_price(), detailReservation.getMax_guests());

        App.getInstance().trackMixPanelCommerce(Utils.COMM_PRODUCT_DETAIL, commEventMixpanelModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            guestName = data.getStringExtra(Common.FIELD_GUEST_NAME);
            guestEmail = data.getStringExtra(Common.FIELD_GUEST_EMAIL);
            guestPhone = data.getStringExtra(Common.FIELD_GUEST_PHONE);
            txtGuestName.setText(data.getStringExtra(Common.FIELD_GUEST_NAME));
            txtGuestEmail.setText(guestEmail + " | ");
            txtGuestPhone.setText(guestPhone);
            txtGuestPhone.setTextColor(getResources().getColor(android.R.color.darker_gray));

            checkEnability(guestName, guestEmail, guestPhone);
        }
    }

    private void initLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ReservationActivity.this);
            progressDialog.setMessage(getString(R.string.loading));
        }

        progressDialog.show();
    }

    private void dismissLoadingDialog() {
        if (progressDialog != null & progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void checkEnability(String name, String email, String phoneNumber) {
        boolean isItEnable = true;
        if (name.equals(Utils.BLANK)) {
            isItEnable = false;
        }
        if (email.equals(Utils.BLANK)) {
            isItEnable = false;
        }
        if (phoneNumber.equals(Utils.BLANK) || phoneNumber.equals(getString(R.string.phone_number))) {
            isItEnable = false;
        }

        if (isItEnable) {
            btnDone.setEnabled(true);
        } else {
            btnDone.setEnabled(false);
        }
    }

    @Override
    public String getEstimatedCostCaption() {
        return "Minimum spend";
    }

    @Override
    public String getTicketCaption() {
        return "Number of guests";
    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.ticket_detail);
    }

    @Override
    protected int getCurrentStep() {
        return 2;
    }
}
