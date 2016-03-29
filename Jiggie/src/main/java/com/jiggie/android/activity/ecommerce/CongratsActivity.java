package com.jiggie.android.activity.ecommerce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
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
import com.jiggie.android.view.InstructionItemView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/1/2016.
 */
public class CongratsActivity extends ToolbarActivity {
    TextView txtCongrats, txtEventTitle, txtEventDate, txtTypeNumberFill, txtGuestNameFill, txtStatusFill, txtPaymentFill, txtSummaryDate, txtRegTicketTitle,
            txtRegTicketFill, txtAdFeeFill, txtTaxFill, txtTotalFill, txtInstrucFill, txtInclude, txtFineprint, txtEventTitle2,
            txtEventDate2, txtVenueTitle, txtVenueDate, lblGuestCount, lblSummaryTitle
            , lblEstimatedBalance, lblPaidDeposit, lblEstimatedTotal, lblTotalTitle;
    LinearLayout linInclude, lineFineprint;
    RelativeLayout relViewTicket, containerTableGuest;
    RelativeLayout scrollView;
    ProgressBar progressBar;
    View divider;

    long orderId;
    boolean fromOrderList;
    private final static String TAG = CongratsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticketreserve);

        Intent a = getIntent();
        //orderId = a.getLongExtra(Common.FIELD_ORDER_ID, 0);
        //fromOrderList = a.getBooleanExtra(Common.FIELD_FROM_ORDER_LIST, false);
        orderId = 1459229470982L;
        fromOrderList = true;
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
        txtFineprint = (TextView)findViewById(R.id.txt_fineprint);
        txtEventTitle2 = (TextView)findViewById(R.id.txt_event_title2);
        txtEventDate2 = (TextView)findViewById(R.id.txt_event_date2);
        txtVenueTitle = (TextView)findViewById(R.id.txt_venue_title);
        txtVenueDate = (TextView)findViewById(R.id.txt_venue_date);
        relViewTicket = (RelativeLayout)findViewById(R.id.rel_view_ticket);
        linInclude = (LinearLayout)findViewById(R.id.lin_include);
        lineFineprint = (LinearLayout)findViewById(R.id.lin_fineprint);
        containerTableGuest = (RelativeLayout) findViewById(R.id.container_table_guest);
        lblGuestCount = (TextView) findViewById(R.id.txt_guest_count_fill);
        lblSummaryTitle = (TextView) findViewById(R.id.txt_summary_title);
        scrollView = (RelativeLayout) this.findViewById(R.id.container);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        lblEstimatedBalance = (TextView) this.findViewById(R.id.lbl_estimated_balance_fill);
        lblPaidDeposit = (TextView) this.findViewById(R.id.lbl_required_deposit_fill);
        lblEstimatedTotal = (TextView) this.findViewById(R.id.lbl_estimate_ttal_fill);
        lblTotalTitle = (TextView) this.findViewById(R.id.txt_total);
        divider = (View) this.findViewById(R.id.divider3);

        scrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        relViewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fromOrderList){
                    Intent i = new Intent(CongratsActivity.this, PurchaseHistoryActivity.class);
                    i.putExtra(Common.FIELD_FROM_HOWTOPAY, true);
                    startActivity(i);
                }else{
                    finish();
                }
            }
        });
    }

    private void preDefined(final String orderId){
        CommerceManager.loaderSucScreenCC(orderId, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                scrollView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                SucScreenCCModel sucScreenCCModel = (SucScreenCCModel)object;
                sendMixpanel(sucScreenCCModel);
                SucScreenCCModel.Data.Success_screen.Event event = sucScreenCCModel.getData().getSuccess_screen().getEvent();
                SucScreenCCModel.Data.Success_screen.Summary summary = sucScreenCCModel.getData().getSuccess_screen().getSummary();
                SucScreenCCModel.Data.Success_screen.Summary.Vt_response vt_response = summary.getVt_response();
                //txtCongrats.setText("Congratulations "+ AccountManager.loadLogin().getUser_first_name()+"!");

                txtCongrats.setText("Congratulations " + summary.getGuest_detail().getName() + "!");
                txtEventTitle.setText(event.getTitle());
                try {
                    final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(event.getStart_datetime());
                    txtEventDate.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate)+" - "+event.getVenue_name());
                }catch (ParseException e){
                    throw new RuntimeException(App.getErrorMessage(e), e);
                }
                txtTypeNumberFill.setText(String.valueOf(sucScreenCCModel.getData().getSuccess_screen().getOrder_number()));
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
                txtRegTicketFill.setText(StringUtility.getRupiahFormat(product_list.getTotal_price()));
                txtAdFeeFill.setText(StringUtility.getRupiahFormat(product_list.getAdmin_fee()));
                txtTaxFill.setText(StringUtility.getRupiahFormat(product_list.getTax_amount()));
                txtTotalFill.setText(StringUtility.getRupiahFormat(summary.getTotal_price()));

                txtEventTitle2.setText(event.getTitle());

                if (product_list.getTicket_type().equalsIgnoreCase("booking"))
                {
                    containerTableGuest.setVisibility(View.VISIBLE);
                    lblSummaryTitle.setText(
                            getResources().getString(R.string.vor_reservation_summary));
                    lblGuestCount.setText(product_list.getNum_buy());
                    txtRegTicketTitle.setText(product_list.getName().toUpperCase() + " "
                            + getResources().getString(R.string.estimate));

                    lblEstimatedTotal.setText(StringUtility.getRupiahFormat(summary.getTotal_price()));
                    lblPaidDeposit.setText(StringUtility.getRupiahFormat(summary.getPay_deposit()));
                    long estimatedBalance = Long.parseLong(summary.getTotal_price())
                            - Long.parseLong(summary.getPay_deposit());
                    lblEstimatedBalance.setText(StringUtility.getRupiahFormat(estimatedBalance + ""));

                    txtTotalFill.setVisibility(View.GONE);
                    lblTotalTitle.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                }
                else
                {
                    lblSummaryTitle.setText(
                            getResources().getString(R.string.vor_order_summary));
                    txtRegTicketTitle.setText(product_list.getName() + " Ticket (" + product_list.getNum_buy() + "x)");
                }

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

                txtInstrucFill.setText(sucScreenCCModel.getData().getSuccess_screen().getInstructions());
                ArrayList<String> arrInclude = sucScreenCCModel.getData().getSuccess_screen().getTicket_include();
                ArrayList<String> arrFineprint = sucScreenCCModel.getData().getSuccess_screen().getFine_print();
                for(int i=0;i<arrInclude.size();i++){
                    InstructionItemView instructionItemView = new InstructionItemView(CongratsActivity.this, String.valueOf(i+1)+".", arrInclude.get(i));
                    linInclude.addView(instructionItemView);
                }
                for(int i=0;i<arrFineprint.size();i++){
                    InstructionItemView instructionItemView = new InstructionItemView(CongratsActivity.this, String.valueOf(i+1)+".", arrFineprint.get(i));
                    lineFineprint.addView(instructionItemView);
                }
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Toast.makeText(CongratsActivity.this, message, Toast.LENGTH_LONG);
                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!fromOrderList){
            Intent i = new Intent(CongratsActivity.this, MainActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }else {
            finish();
        }
    }
}
