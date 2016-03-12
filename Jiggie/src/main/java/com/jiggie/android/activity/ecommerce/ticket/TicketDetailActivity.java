package com.jiggie.android.activity.ecommerce.ticket;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.AddGuestActivity;
import com.jiggie.android.activity.ecommerce.PurchaseInfoActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.ProductListModel;

import java.text.ParseException;
import java.util.Date;

import butterknife.Bind;

public class TicketDetailActivity extends AbstractTicketDetailActivity {

    public static final String TAG = TicketDetailActivity.class.getSimpleName();
    @Bind(R.id.rel_guest)
    RelativeLayout relGuest;
    @Bind(R.id.btnDone)
    Button btnDone;
    @Bind(R.id.minus_button)
    View minusButton;
    @Bind(R.id.plus_button)
    View plusButton;
    int quantity = 0;
    @Bind(R.id.lblQuantity)
    TextView lblQuantity;

    ProductListModel.Data.ProductList.Purchase detailPurchase = null;
    ProductListModel.Data.ProductList.Reservation detailReservation = null;
    boolean isTicketTransaction;
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
    @Bind(R.id.lblTypePriceCaption)
    TextView lblTypePriceCaption;
    @Bind(R.id.txt_guest_name)
    TextView txtGuestName;
    @Bind(R.id.txt_guest_email)
    TextView txtGuestEmail;
    @Bind(R.id.txt_guest_phone)
    TextView txtGuestPhone;
    @Bind(R.id.lblEstimatedCost)
    TextView lblEstimatedCost;
    @Bind(R.id.lblTicketCaption)
    TextView lblTicketCaption;


    String eventId, eventName, venueName, startTime, guestName, guestEmail, guestPhone;
    int max = 0, price;

    @Override
    protected void onCreate() {
        super.setContentView(R.layout.activity_ticket_detail);
        super.bindView();

        preDefined();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TicketDetailActivity.this, PurchaseInfoActivity.class));
            }
        });

        relGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TicketDetailActivity.this, AddGuestActivity.class);
                i.putExtra(Common.FIELD_GUEST_NAME, guestName);
                i.putExtra(Common.FIELD_GUEST_EMAIL, guestEmail);
                i.putExtra(Common.FIELD_GUEST_PHONE, guestPhone);
                startActivityForResult(i, 0);
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    lblQuantity.setText(String.valueOf(quantity));
                    lblEstimatedCost.setText(getRupiahFormat(String.valueOf(quantity * price)));
                }

            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0 && quantity<max) {
                    quantity++;
                    lblQuantity.setText(String.valueOf(quantity));
                    lblEstimatedCost.setText(getRupiahFormat(String.valueOf(quantity * price)));
                }
            }
        });
    }

    public static String getRupiahFormat(String number) {
        String displayedString = "";

        if (number.length() == 0) {
            displayedString = "Rp0K";
        } else {
            if (number.length() > 3) {
                int length = number.length();

                for (int i = length; i > 0; i -= 3) {
                    if (i > 3) {
                        String myStringPrt1 = number.substring(0, i - 3);
                        String myStringPrt2 = number.substring(i - 3);

                        String combinedString;

                        combinedString = myStringPrt1 + ".";

                        combinedString += myStringPrt2;
                        number = combinedString;

                        displayedString = "Rp" + combinedString+"K";
                    }
                }
            } else {
                displayedString = "Rp" + number+"K";
            }
        }
        return displayedString;
    }

    private void preDefined() {
        Intent a = getIntent();
        eventId = a.getStringExtra(Common.FIELD_EVENT_ID);
        eventName = a.getStringExtra(Common.FIELD_EVENT_NAME);
        venueName = a.getStringExtra(Common.FIELD_VENUE_NAME);
        startTime = a.getStringExtra(Common.FIELD_STARTTIME);
        String type_transaction = a.getStringExtra(Common.FIELD_TRANS_TYPE);
        if (type_transaction.equals(Common.TYPE_PURCHASE)) {
            isTicketTransaction = true;
            detailPurchase = a.getParcelableExtra(ProductListModel.Data.ProductList.Purchase.class.getName());
        } else if (type_transaction.equals(Common.TYPE_RESERVATION)) {
            isTicketTransaction = false;
            detailReservation = a.getParcelableExtra(ProductListModel.Data.ProductList.Reservation.class.getName());
        }

        lblEventName.setText(eventName);
        try {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(startTime);
            lblEventLocation.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate)+" - "+venueName);
        }catch (ParseException e){
            throw new RuntimeException(App.getErrorMessage(e), e);
        }
        lblQuantity.setText(String.valueOf(max));

        if (isTicketTransaction) {
            max = Integer.parseInt(detailPurchase.getMax_purchase());
            price = Integer.parseInt(detailPurchase.getPrice());

            lblType.setText(detailPurchase.getName());
            lblTypeCaption.setText(detailPurchase.getDescription());
            lblTypePrice.setText(detailPurchase.getPrice());
            lblTypePriceCaption.setText(getString(R.string.pr_max_purchase)+" "+max);
            lblEstimatedCost.setText(getRupiahFormat(String.valueOf(price)));
        } else {
            max = Integer.parseInt(detailReservation.getMax_guests());
            price = Integer.parseInt(detailReservation.getPrice());

            lblType.setText(detailReservation.getName());
            lblTypeCaption.setText(detailReservation.getDescription());
            lblTypePrice.setText(detailReservation.getPrice());
            lblTypePriceCaption.setText(getString(R.string.pr_max_guest)+" "+max);
            lblEstimatedCost.setText(getRupiahFormat(String.valueOf(price)));
        }

        LoginModel loginModel = AccountManager.loadLogin();


        guestName = App.getSharedPreferences().getString(Common.FIELD_GUEST_NAME, Utils.BLANK);
        if(guestName.equals(Utils.BLANK)){
            guestName = loginModel.getUser_first_name()+" "+loginModel.getUser_last_name();
        }
        guestEmail = App.getSharedPreferences().getString(Common.FIELD_GUEST_EMAIL, Utils.BLANK);
        if(guestEmail.equals(Utils.BLANK)){
            guestEmail = loginModel.getEmail();
        }
        guestPhone = App.getSharedPreferences().getString(Common.FIELD_GUEST_PHONE, Utils.BLANK);
        if(guestPhone.equals(Utils.BLANK)){
            guestPhone = AccountManager.loadSetting().getData().getPhone();
            if(guestPhone.equals(Utils.BLANK)){
                guestPhone = "Phone Number";
                txtGuestPhone.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }

        }

        txtGuestName.setText(guestName);
        txtGuestEmail.setText(guestEmail+" | ");
        txtGuestPhone.setText(guestPhone);

    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.ticket_detail);
    }

    @Override
    protected int getCurrentStep() {
        return 2;
    }

    @Override
    public String getEstimatedCostCaption() {
        return "Estimated Costs";
    }

    @Override
    public String getTicketCaption() {
        return "Tickets";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            txtGuestName.setText(data.getStringExtra(Common.FIELD_GUEST_NAME));
            txtGuestEmail.setText(data.getStringExtra(Common.FIELD_GUEST_EMAIL)+" | ");
            txtGuestPhone.setText(data.getStringExtra(Common.FIELD_GUEST_PHONE));
            txtGuestPhone.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }
}
