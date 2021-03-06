package com.jiggie.android.activity.ecommerce.ticket;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.PostSummaryModel;
import com.jiggie.android.presenter.GuestPresenter;

import butterknife.Bind;

public abstract class AbstractTicketDetailActivity extends /*ToolbarWithDotActivity*/ ToolbarActivity {

    /*@Bind(R.id.lblTypeCaption)
    TextView lblEventCaption;
    @Bind(R.id.lblTypePriceCaption) TextView lblTypePriceCaption;*/
    @Bind(R.id.lblEstimatedCostCaption)
    TextView lblEstimatedCostCaption;
    @Bind(R.id.lblTicketCaption)
    TextView lblTicketCaption;
    @Bind(R.id.rel_guest_detail)
    RelativeLayout relGuestDetail;
    GuestPresenter guestPresenter;
    @Bind(R.id.lblFillYourContactInfo)
    TextView lblFillYourContactInfo;

    String guestName, guestEmail, dialCode, guestPhone, identity_id;
    @Bind(R.id.txt_guest_name)
    TextView txtGuestName;
    @Bind(R.id.txt_guest_email)
    TextView txtGuestEmail;
    @Bind(R.id.txt_guest_phone)
    TextView txtGuestPhone;
    @Bind(R.id.rel_guest)
    RelativeLayout relGuest;


    private static final String TAG = AbstractTicketDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ButterKnife.bind(this);
        //tb.setTitle(getResources().getString(R.string.ticket_detail));
        guestPresenter = new GuestPresenter();
        onCreate();
        super.bindView();
        //super.setToolbarTitle(getToolbarTitle(), true);
        getSupportActionBar().setTitle(getToolbarTitle());
        //lblEventCaption.setText(getEventCaption());
        //lblTypePriceCaption.setText(getTypePriceCaption());
        lblEstimatedCostCaption.setText(getEstimatedCostCaption());
        lblTicketCaption.setText(getTicketCaption());
        //lblInfo.setText(getPageInfo() + "grrr");
    }

    /*public abstract String getEventCaption();
    public abstract String getTypePriceCaption();*/
    public abstract String getEstimatedCostCaption();
    public abstract String getTicketCaption();
    protected abstract void onCreate();

    protected abstract String getToolbarTitle();
    protected abstract String getPageInfo();

    protected void initGuest()
    {
        initGuest(false);
    }

    protected void initGuest(boolean isLoket) {
        //wandy 20-04-2016
        /*
        LoginModel loginModel = AccountManager.loadLogin();
        guestName = loginModel.getUser_first_name() + " " + loginModel.getUser_last_name();
        guestEmail = loginModel.getEmail();
        guestPhone = AccountManager.loadSetting().getData().getPhone();*/
        PostSummaryModel.Guest_detail guestDetail = guestPresenter.loadGuest();
        if (!guestDetail.dial_code.isEmpty()
                || !guestDetail.phone.isEmpty()
                || !guestDetail.name.isEmpty()
                || !guestDetail.email.isEmpty()
                ) {
            lblFillYourContactInfo.setVisibility(View.GONE);
            relGuestDetail.setVisibility(View.VISIBLE);
            guestName = guestDetail.name;
            guestEmail = guestDetail.email;
            guestPhone = guestDetail.phone;
            dialCode = guestDetail.dial_code;
        } else {
            /*lblFillYourContactInfo.setVisibility(View.VISIBLE);
            relGuestDetail.setVisibility(View.GONE);*/

            LoginModel loginModel = AccountManager.loadLogin();
            guestName = loginModel.getUser_first_name() + " " + loginModel.getUser_last_name();
            guestEmail = loginModel.getEmail();
            guestPhone = AccountManager.loadSetting().getData().getPhone();

            lblFillYourContactInfo.setVisibility(View.GONE);
            relGuestDetail.setVisibility(View.VISIBLE);
            /*guestName = "";
            guestEmail = "";
            guestPhone = Utils.BLANK;
            dialCode = "";*/
        }

        if (guestPhone.equals(Utils.BLANK)) {
            guestPhone = getString(R.string.phone_number);
            txtGuestPhone.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            relGuest.setSelected(true);
        }
        else if(isLoket)
        {
            if(guestDetail == null || guestDetail.identity_id == null || guestDetail.identity_id.isEmpty())
            {
                identity_id = "";
                relGuest.setSelected(true);
            }
            else
            {
                identity_id = guestDetail.identity_id;
            }
        }

        txtGuestName.setText(guestName);
        txtGuestEmail.setText(guestEmail + " | ");
        if (guestPhone.equals(getString(R.string.phone_number))) {
            txtGuestPhone.setText(guestPhone);
        } else {
            txtGuestPhone.setText("+" + dialCode + guestPhone);
        }
    }
}

