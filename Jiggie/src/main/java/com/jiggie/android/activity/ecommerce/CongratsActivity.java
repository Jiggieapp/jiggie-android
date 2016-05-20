package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.SucScreenCCModel;
import com.jiggie.android.view.DiscountView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by LTE on 3/1/2016.
 */
public class CongratsActivity extends ToolbarActivity {
    TextView txtCongrats, txtEventTitle, txtEventDate, txtTypeNumberFill, txtGuestNameFill, txtStatusFill, txtPaymentFill, txtSummaryDate, txtRegTicketTitle,
            txtRegTicketFill, txtAdFeeFill, txtTaxFill, txtTotalFill, txtInstrucFill, txtEventTitle2,
            txtEventDate2, txtVenueTitle, txtVenueDate, lblGuestCount, lblSummaryTitle
            , lblEstimatedBalance, lblPaidDeposit, lblEstimatedTotal, lblTotalTitle, txt_type_number_title, txtInstruc, txtPaymentTitle, txtStatusTitle,
    txtCreditFill;
    LinearLayout linSummaryFooter, linDiscount;
    RelativeLayout relViewTicket, containerTableGuest, relCredit;
    RelativeLayout scrollView;
    ProgressBar progressBar;
    View divider, divider8, divider4;

    long orderId;
    boolean fromOrderList;
    private final static String TAG = CongratsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticketreserve);

        Intent a = getIntent();
        orderId = a.getLongExtra(Common.FIELD_ORDER_ID, 0);
        fromOrderList = a.getBooleanExtra(Common.FIELD_FROM_ORDER_LIST, false);
        /*orderId = 1459229470982L;
        fromOrderList = true;*/
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
        txtStatusTitle = (TextView)findViewById(R.id.txt_status_title);
        txtPaymentFill = (TextView)findViewById(R.id.txt_payment_fill);
        txtPaymentTitle = (TextView)findViewById(R.id.txt_payment_title);
        txtSummaryDate = (TextView)findViewById(R.id.txt_summary_date);
        txtRegTicketTitle = (TextView)findViewById(R.id.txt_reg_ticket_title);
        txtRegTicketFill = (TextView)findViewById(R.id.txt_reg_ticket_fill);
        txtAdFeeFill = (TextView)findViewById(R.id.txt_ad_fee_fill);
        txtTaxFill = (TextView)findViewById(R.id.txt_tax_fill);
        txtTotalFill = (TextView)findViewById(R.id.txt_total_fill);
        txtInstrucFill = (TextView)findViewById(R.id.txt_instruc_fill);
        txtEventTitle2 = (TextView)findViewById(R.id.txt_event_title2);
        txtEventDate2 = (TextView)findViewById(R.id.txt_event_date2);
        txtVenueTitle = (TextView)findViewById(R.id.txt_venue_title);
        txtVenueDate = (TextView)findViewById(R.id.txt_venue_date);
        relViewTicket = (RelativeLayout)findViewById(R.id.rel_view_ticket);
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
        divider8 = (View) this.findViewById(R.id.divider8);
        divider4 = (View) this.findViewById(R.id.divider4);
        linSummaryFooter = (LinearLayout)findViewById(R.id.lin_summary_footer);
        txt_type_number_title = (TextView)findViewById(R.id.txt_type_number_title);
        txtInstruc = (TextView)findViewById(R.id.txt_instruc);

        txtCreditFill = (TextView)findViewById(R.id.txt_credit_fill);
        relCredit = (RelativeLayout) findViewById(R.id.rel_credit);
        linDiscount = (LinearLayout)findViewById(R.id.lin_discount);

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
        Utils.d(TAG, "orderId " + orderId);
        CommerceManager.loaderSucScreenCC(orderId, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                scrollView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                SucScreenCCModel sucScreenCCModel = (SucScreenCCModel)object;
                if(sucScreenCCModel!=null){
                    sendMixpanel(sucScreenCCModel);
                    SucScreenCCModel.Data.Success_screen.Event event = sucScreenCCModel.getData().getSuccess_screen().getEvent();
                    SucScreenCCModel.Data.Success_screen.Summary summary = sucScreenCCModel.getData().getSuccess_screen().getSummary();
                    SucScreenCCModel.Data.Success_screen.Summary.Vt_response vt_response = summary.getVt_response();
                    //txtCongrats.setText("Congratulations "+ AccountManager.loadLogin().getUser_first_name()+"!");

                    txtCongrats.setText("Congratulations " + summary.getGuest_detail().getName() + "!");
                    txtEventTitle.setText(event.getTitle());
                    try {
                        final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(event.getStart_datetime());
                        txtEventDate.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate));
                    }catch (ParseException e){
                        throw new RuntimeException(App.getErrorMessage(e), e);
                    }

                    txtTypeNumberFill.setText(String.valueOf(sucScreenCCModel.getData().getSuccess_screen().getOrder_number()));
                    txtGuestNameFill.setText(summary.getGuest_detail().getName());
                    txtStatusFill.setText(sucScreenCCModel.getData().getSuccess_screen().getPayment_status());
                    String paymentType = sucScreenCCModel.getData().getSuccess_screen().getPayment_type();
                    if(paymentType.equals(Utils.TYPE_CC)){
                        txtPaymentFill.setText(getString(R.string.section_credit_card));
                    }else if(paymentType.equals(Utils.TYPE_BP)){
                        txtPaymentFill.setText(getString(R.string.va_mandiri));
                    }else if(paymentType.equals(Utils.TYPE_VA)){
                        txtPaymentFill.setText(getString(R.string.bank_transfer));
                    }else if(paymentType.equals(Utils.TYPE_BCA)){
                        txtPaymentFill.setText(getString(R.string.va_bca));
                    }
                    if(vt_response != null)
                    {
                        try {
                            final Date orderDate = Common.FORMAT_COMM_TRANSACTION.parse(vt_response.getTransaction_time());
                            txtSummaryDate.setText(Common.FORMAT_COMM_TICKET.format(orderDate));
                        }catch (ParseException e){
                            throw new RuntimeException(App.getErrorMessage(e), e);
                        }
                    }
                    else //free payment
                    {
                        //txtSummaryDate.setVisibility(View.GONE);
                        /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        format.setTimeZone(TimeZone.getTimeZone("UTC"));
                        format.p
                        String simpleDate = App.getInstance().getResources().getString(R.string.event_date_format
                                , Common.SERVER_DATE_FORMAT_ALT.format(startDate), Common.SIMPLE_12_HOUR_FORMAT.format(endDate));

                        */
                        try {
                            String start_dt = summary.getCreated_at();
                            Date date = Common.ISO8601_DATE_FORMAT_UTC.parse(start_dt);
                            SimpleDateFormat newFormat = new SimpleDateFormat("dd MMMM yyyy - HH:mm:ss", Locale.getDefault());
                            //newFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            String finalString = newFormat.format(date);
                            txtSummaryDate.setText(finalString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    SucScreenCCModel.Data.Success_screen.Summary.Product_list product_list = summary.getProduct_list().get(0);

                    if(product_list.getTotal_price().equals(Utils.NOL_RUPIAH)){
                        txtRegTicketFill.setText(getString(R.string.free));
                    }else{
                        txtRegTicketFill.setText(StringUtility.getRupiahFormat(product_list.getTotal_price()));
                    }

                    if(product_list.getAdmin_fee().equals(Utils.NOL_RUPIAH)){
                        txtAdFeeFill.setText(getString(R.string.free));
                    }else{
                        txtAdFeeFill.setText(StringUtility.getRupiahFormat(product_list.getAdmin_fee()));
                    }

                    if(product_list.getTax_amount().equals(Utils.NOL_RUPIAH)){
                        txtTaxFill.setText(getString(R.string.free));
                    }else{
                        //txtTaxFill.setText(StringUtility.getRupiahFormat(product_list.getTax_amount()));
                        txtTaxFill.setText(StringUtility.getRupiahFormat(summary.getTotal_tax_amount()));
                    }

                    if(summary.getTotal_price().equals(Utils.NOL_RUPIAH)){
                        txtTotalFill.setText(getString(R.string.free));
                    }else{
                        txtTotalFill.setText(StringUtility.getRupiahFormat(summary.getTotal_price()));
                    }

                    txtEventTitle2.setText(event.getTitle());

                    if (product_list.getTicket_type().equalsIgnoreCase(Common.TYPE_RESERVATION))
                    {
                        containerTableGuest.setVisibility(View.VISIBLE);
                        txt_type_number_title.setText(getString(R.string.vor_reservation_number));
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

                        if(summary.getPay_deposit().equals(Utils.NOL_RUPIAH)){
                            txtStatusTitle.setVisibility(View.GONE);
                            txtStatusFill.setVisibility(View.GONE);
                            txtPaymentTitle.setVisibility(View.GONE);
                            txtPaymentFill.setVisibility(View.GONE);
                        }
                    }else{
                        txt_type_number_title.setText(getString(R.string.vor_order_number));
                        lblSummaryTitle.setText(
                                getResources().getString(R.string.vor_order_summary));
                        txtRegTicketTitle.setText(product_list.getName() + " Ticket (" + product_list.getNum_buy() + "x)");
                        linSummaryFooter.setVisibility(View.GONE);
                        divider8.setVisibility(View.GONE);
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

                    //txtInstrucFill.setText(sucScreenCCModel.getData().getSuccess_screen().getInstructions());
                    if(!sucScreenCCModel.getData().getSuccess_screen().getInstructions().equals(Utils.BLANK)){
                        txtInstrucFill.setText(Html.fromHtml(sucScreenCCModel.getData().getSuccess_screen().getInstructions()));
                    }else{
                        txtInstruc.setVisibility(View.GONE);
                        txtInstrucFill.setVisibility(View.GONE);
                        divider4.setVisibility(View.GONE);
                    }

                    //CREDIT AND DISCOUNT PART===========================
                    /*for(int i=0;i<4;i++){
                        DiscountView discountView = new DiscountView(CongratsActivity.this, "Discount test", "10000", true, 0,
                                getResources().getColor(R.color.purple));
                        linDiscount.addView(discountView);
                    }
                    linDiscount.setVisibility(View.VISIBLE);*/

                    SucScreenCCModel.Data.Success_screen.Credit credit= sucScreenCCModel.getData().getSuccess_screen().getCredit();
                    if (Integer.parseInt(credit.getCredit_used()) != 0) {
                        relCredit.setVisibility(View.VISIBLE);
                        txtCreditFill.setText("- " + StringUtility.getRupiahFormat(credit.getCredit_used()));
                    }

                    SucScreenCCModel.Data.Success_screen.Discount discount = sucScreenCCModel.getData().getSuccess_screen().getDiscount();
                    //float textSize = getResources().getDimension(R.dimen.normal_text_size) / getResources().getDisplayMetrics().density;
                    float textSize = 12;
                    if (discount.getData().size() > 0) {
                        for (int i = 0; i < discount.getData().size(); i++) {
                            String title = discount.getData().get(i).getName();
                            String value = String.valueOf(discount.getData().get(i).getAmount_used());
                            DiscountView discountView = new DiscountView(CongratsActivity.this, title, value, true, 0, getResources().getColor(R.color.purple), textSize);
                            linDiscount.addView(discountView);
                        }
                        linDiscount.setVisibility(View.VISIBLE);
                    }
                    //END OF CREDIT AND DISCOUNT PART===========================

                }else{
                    Toast.makeText(CongratsActivity.this, getString(R.string.msg_wrong), Toast.LENGTH_LONG).show();
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

        if (productList.getTicket_type().equalsIgnoreCase(Common.TYPE_RESERVATION))
        {
            commEventMixpanelModel = new CommEventMixpanelModel(event.getTitle(), event.getVenue_name(), event.getLocation(), event.getStart_datetime_str(),
                    event.getEnd_datetime_str(), event.getTags(), event.getDescription(), productList.getName(), productList.getTicket_type(),
                    successScreen.getSummary().getTotal_price(), productList.getMax_buy(), successScreen.getSummary().getCreated_at(), productList.getNum_buy(),
                    successScreen.getSummary().getTotal_price(), "0", successScreen.getPayment_type(), Utils.BLANK, false);
        }else{
            commEventMixpanelModel = new CommEventMixpanelModel(event.getTitle(), event.getVenue_name(), event.getLocation(), event.getStart_datetime_str(),
                    event.getEnd_datetime_str(), event.getTags(), event.getDescription(), productList.getName(), productList.getTicket_type(),
                    successScreen.getSummary().getTotal_price(), productList.getMax_buy(), successScreen.getSummary().getCreated_at(), Utils.BLANK,
                    successScreen.getSummary().getTotal_price(), "0", successScreen.getPayment_type(), productList.getNum_buy(), true);
        }


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
