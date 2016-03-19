package com.jiggie.android.activity.ecommerce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.SucScreenCCModel;
import com.jiggie.android.model.SummaryModel;

import java.text.ParseException;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/1/2016.
 */
public class CongratsActivity extends ToolbarActivity {
    TextView txtCongrats, txtEventTitle, txtEventDate, txtTypeNumberFill, txtGuestNameFill, txtStatusFill, txtPaymentFill, txtSummaryDate, txtRegTicketTitle,
            txtRegTicketFill, txtAdFeeFill, txtTaxFill, txtTotalFill, txtInstrucFill, txtInclude, txtIncludeFill, txtFineprint, txtFineprintFill, txtEventTitle2,
            txtEventDate2, txtVenueTitle, txtVenueDate;
    RelativeLayout relViewTicket;

    long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticketreserve);

        Intent a = getIntent();
        orderId = a.getLongExtra(Common.FIELD_ORDER_ID, 0);
        initView();
        preDefined(String.valueOf(orderId));

    }

    private void initView(){
        txtCongrats = (TextView)findViewById(R.id.txt_congrats);
        txtEventTitle = (TextView)findViewById(R.id.txt_event_title);
        txtEventDate = (TextView)findViewById(R.id.txt_event_date);
        txtTypeNumberFill = (TextView)findViewById(R.id.txt_type_number_fill);
        txtGuestNameFill = (TextView)findViewById(R.id.txt_guest_name_fill);
        txtStatusFill = (TextView)findViewById(R.id.txt_status_fill);
        txtPaymentFill = (TextView)findViewById(R.id.txt_payment_fill);
        txtSummaryDate = (TextView)findViewById(R.id.txt_summary_date);
        txtRegTicketTitle = (TextView)findViewById(R.id.txt_reg_ticket_title);
        txtRegTicketFill = (TextView)findViewById(R.id.txt_reg_ticket_fill);
        txtAdFeeFill = (TextView)findViewById(R.id.txt_ad_fee_fill);
        txtTaxFill = (TextView)findViewById(R.id.txt_tax_fill);
        txtTotalFill = (TextView)findViewById(R.id.txt_total_fill);
        txtInstrucFill = (TextView)findViewById(R.id.txt_instruc_fill);
        txtInclude = (TextView)findViewById(R.id.txt_include);
        txtIncludeFill = (TextView)findViewById(R.id.txt_include_fill);
        txtFineprint = (TextView)findViewById(R.id.txt_fineprint);
        txtFineprintFill = (TextView)findViewById(R.id.txt_fineprint_fill);
        txtEventTitle2 = (TextView)findViewById(R.id.txt_event_title2);
        txtEventDate2 = (TextView)findViewById(R.id.txt_event_date2);
        txtVenueTitle = (TextView)findViewById(R.id.txt_venue_title);
        txtVenueDate = (TextView)findViewById(R.id.txt_venue_date);
        relViewTicket = (RelativeLayout)findViewById(R.id.rel_view_ticket);
    }

    private void preDefined(final String orderId){
        CommerceManager.loaderSucScreenCC(orderId, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                SucScreenCCModel sucScreenCCModel = (SucScreenCCModel)object;
                sendMixpanel(sucScreenCCModel);
                SucScreenCCModel.Data.Success_screen.Event event = sucScreenCCModel.getData().getSuccess_screen().getEvent();
                SucScreenCCModel.Data.Success_screen.Summary summary = sucScreenCCModel.getData().getSuccess_screen().getSummary();
                SucScreenCCModel.Data.Success_screen.Summary.Vt_response vt_response = summary.getVt_response();
                txtCongrats.setText("Congratulations "+ AccountManager.loadLogin().getUser_first_name()+"!");
                txtEventTitle.setText(event.getTitle());
                try {
                    final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(event.getStart_datetime());
                    txtEventDate.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate)+" - "+event.getVenue_name());
                }catch (ParseException e){
                    throw new RuntimeException(App.getErrorMessage(e), e);
                }
                txtTypeNumberFill.setText(String.valueOf(orderId));
                txtGuestNameFill.setText(summary.getGuest_detail().getName());
                txtStatusFill.setText(sucScreenCCModel.getData().getSuccess_screen().getPayment_status());
                String paymentType = sucScreenCCModel.getData().getSuccess_screen().getType();
                if(paymentType.equals(Utils.TYPE_CC)){
                    txtPaymentFill.setText(getString(R.string.section_credit_card));
                }else if(paymentType.equals(Utils.TYPE_BP)){
                    txtPaymentFill.setText(getString(R.string.va_mandiri));
                }else if(paymentType.equals(Utils.TYPE_VA)){
                    txtPaymentFill.setText(getString(R.string.other_bank));
                }
                try {
                    final Date orderDate = Common.FORMAT_COMM_TRANSACTION.parse(vt_response.getTransaction_time());
                    txtSummaryDate.setText(Common.FORMAT_COMM_TICKET.format(orderDate));
                }catch (ParseException e){
                    throw new RuntimeException(App.getErrorMessage(e), e);
                }
                SucScreenCCModel.Data.Success_screen.Summary.Product_list product_list = summary.getProduct_list().get(0);
                txtRegTicketTitle.setText(product_list.getName() + " Ticket (" + product_list.getNum_buy() + "x)");
                txtRegTicketFill.setText(StringUtility.getRupiahFormat(product_list.getTotal_price()));
                txtAdFeeFill.setText(StringUtility.getRupiahFormat(product_list.getAdmin_fee()));
                txtTaxFill.setText(StringUtility.getRupiahFormat(product_list.getTax_amount()));
                txtTotalFill.setText(StringUtility.getRupiahFormat(summary.getTotal_price()));
                txtEventTitle2.setText(event.getTitle());
                try {
                    final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(event.getStart_datetime());
                    final Date endDate = Common.ISO8601_DATE_FORMAT_UTC.parse(event.getEnd_datetime());
                    txtEventDate2.setText(Common.SIMPLE_24_HOUR_FORMAT.format(startDate)+"-"+Common.SIMPLE_24_HOUR_FORMAT.format(endDate));
                }catch (ParseException e){
                    throw new RuntimeException(App.getErrorMessage(e), e);
                }
                txtVenueTitle.setText(event.getVenue_name());
                try {
                    final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(event.getStart_datetime());
                    txtVenueDate.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate));
                }catch (ParseException e){
                    throw new RuntimeException(App.getErrorMessage(e), e);
                }
            }

            @Override
            public void onFailure(int responseCode, String message) {

            }
        });
    }

    private void sendMixpanel(SucScreenCCModel sucScreenCCModel){
        CommEventMixpanelModel commEventMixpanelModel = null;
        SucScreenCCModel.Data.Success_screen successScreen = sucScreenCCModel.getData().getSuccess_screen();
        SucScreenCCModel.Data.Success_screen.Event event = sucScreenCCModel.getData().getSuccess_screen().getEvent();
        SucScreenCCModel.Data.Success_screen.Summary.Product_list productList = successScreen.getSummary().getProduct_list().get(0);
        commEventMixpanelModel = new CommEventMixpanelModel(event.getTitle(), event.getVenue_name(), event.getLocation(), event.getStart_datetime_str(),
                event.getEnd_datetime_str(), event.getTags(), event.getDescription(), productList.getName(), productList.getTicket_type(),
                successScreen.getSummary().getTotal_price(), productList.getMax_buy(), successScreen.getSummary().getCreated_at(), productList.getNum_buy(),
                successScreen.getSummary().getTotal_price(), "0", successScreen.getType(), Utils.BLANK, false);
        App.getInstance().trackMixPanelCommerce(Utils.COMM_FINISH, commEventMixpanelModel);
    }
}
