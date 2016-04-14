package com.jiggie.android.activity.ecommerce.ticket;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.AddGuestActivity;
import com.jiggie.android.activity.ecommerce.ProductListActivity;
import com.jiggie.android.activity.ecommerce.PurchaseInfoActivity;
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
import com.jiggie.android.view.InstructionItemView;

import java.util.ArrayList;

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
    int quantity = 1;
    @Bind(R.id.lblQuantity)
    TextView lblQuantity;

    ProductListModel.Data.ProductList.Purchase detailPurchase = null;

    /*@Bind(R.id.lblEventName)
    TextView lblEventName;
    @Bind(R.id.lblEventLocation)
    TextView lblEventLocation;*/
    /*@Bind(R.id.lblType)
    TextView lblType;
    @Bind(R.id.lblTypeCaption)
    TextView lblTypeCaption;*/
    /*@Bind(R.id.lblTypePrice)
    TextView lblTypePrice;
    @Bind(R.id.lblTypePriceCaption)
    TextView lblTypePriceCaption;*/
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


    String eventId, eventName, venueName, startTime, guestName, guestEmail, guestPhone, ticketId;
    int max = 0;
    int price;
    EventDetailModel.Data.EventDetail eventDetail;
    SummaryModel.Data.Product_summary productSummary;
    ProgressDialog progressDialog;
    @Bind(R.id.txt_ticket_desc)
    TextView txtTicketDesc;
    boolean isSoldOut = false;
    @Bind(R.id.purchaseContainer)
    LinearLayout purchaseContainer;
    @Bind(R.id.txt_sold_out)
    TextView txtSoldOut;
    @Bind(R.id.rel_con_guest)
    RelativeLayout relConGuest;
    @Bind(R.id.card_view_guest)
    CardView cardViewGuest;

    private Dialog dialogTerms;

    @Override
    protected void onCreate() {
        super.setContentView(R.layout.activity_ticket_detail);
        super.bindView();
        super.setToolbarTitle(getResources().getString(R.string.ticket_detail), true);
        preDefined();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLoadingDialog();

                PostSummaryModel.Product_list product_list = new PostSummaryModel.Product_list(ticketId, quantity);
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
                        productSummary = dataTemp.getData().getProduct_summary();
                        if (productSummary != null) {
                            showTermsDialog(productSummary.getProduct_list().get(0));
                            //String responses = new Gson().toJson(dataTemp);
                            //Utils.d("res", responses);

                            /*Intent i = new Intent(TicketDetailActivity.this, PurchaseInfoActivity.class);
                            i.putExtra(Common.FIELD_EVENT_ID, eventId);
                            i.putExtra(Common.FIELD_EVENT_NAME, eventName);
                            i.putExtra(Common.FIELD_VENUE_NAME, venueName);
                            i.putExtra(Common.FIELD_STARTTIME, startTime);
                            i.putExtra(productSummary.getClass().getName(), productSummary);
                            i.putExtra(eventDetail.getClass().getName(), eventDetail);
                            startActivity(i);*/
                        } else {
                            Toast.makeText(TicketDetailActivity.this, getString(R.string.msg_wrong), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int responseCode, final String message) {
                        dismissLoadingDialog();
                        Utils.d(String.valueOf(responseCode), message);
                        if (message.contains("left") || message.contains("unavailable")) {
                            final AlertDialog dialog = new AlertDialog.Builder(TicketDetailActivity.this)
                                    .setMessage(message)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if (message.contains("unavailable")) {
                                                Intent i = new Intent(TicketDetailActivity.this, ProductListActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                i.putExtra(Common.FIELD_EVENT_ID, eventDetail.get_id());
                                                i.putExtra(EventDetailModel.Data.EventDetail.class.getName(), eventDetail);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    }
                });
            }
        });

        cardViewGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TicketDetailActivity.this, AddGuestActivity.class);
                i.putExtra(Common.FIELD_GUEST_NAME, guestName);
                i.putExtra(Common.FIELD_GUEST_EMAIL, guestEmail);
                if (guestPhone.equals(getString(R.string.phone_number))) {
                    i.putExtra(Common.FIELD_GUEST_PHONE, Utils.BLANK);
                } else {
                    i.putExtra(Common.FIELD_GUEST_PHONE, guestPhone);
                }

                i.putExtra(Common.FIELD_TRANS_TYPE, Common.TYPE_PURCHASE);
                i.putExtra(detailPurchase.getClass().getName(), detailPurchase);
                i.putExtra(eventDetail.getClass().getName(), eventDetail);
                startActivityForResult(i, 0);
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    lblQuantity.setText(String.valueOf(quantity));
                    lblEstimatedCost.setText(StringUtility.getRupiahFormat(String.valueOf(quantity * price)));
                }

            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0 && quantity < max) {
                    quantity++;
                    lblQuantity.setText(String.valueOf(quantity));
                    lblEstimatedCost.setText(StringUtility.getRupiahFormat(String.valueOf(quantity * price)));
                }
            }
        });
    }

    @Override
    protected String getToolbarTitle() {
        return detailPurchase.getName().toUpperCase();
    }

    private void preDefined() {
        Intent a = getIntent();
        eventId = a.getStringExtra(Common.FIELD_EVENT_ID);
        eventName = a.getStringExtra(Common.FIELD_EVENT_NAME);
        venueName = a.getStringExtra(Common.FIELD_VENUE_NAME);
        startTime = a.getStringExtra(Common.FIELD_STARTTIME);
        eventDetail = a.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        detailPurchase = a.getParcelableExtra(ProductListModel.Data.ProductList.Purchase.class.getName());
        sendMixpanel(eventDetail);

        /*lblEventName.setText(eventName);
        try {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(startTime);
            lblEventLocation.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate) + " - " + venueName);
        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }*/

        max = Integer.parseInt(detailPurchase.getMax_purchase());
        price = (int) Double.parseDouble(detailPurchase.getPrice());
        ticketId = detailPurchase.getTicket_id();

        /*lblType.setText(detailPurchase.getName());
        lblTypeCaption.setText(detailPurchase.getSummary());
        lblTypePrice.setText(StringUtility.getRupiahFormat(detailPurchase.getPrice()));
        lblTypePriceCaption.setText(getString(R.string.pr_max_purchase) + " " + max);*/

        if (detailPurchase.getStatus().equals(Common.FIELD_STATUS_SOLD_OUT) || detailPurchase.getQuantity() == 0) {
            purchaseContainer.setVisibility(View.GONE);
            txtSoldOut.setVisibility(View.VISIBLE);
            isSoldOut = true;
        } else {
            lblEstimatedCost.setText(StringUtility.getRupiahFormat(String.valueOf(price)));
            lblQuantity.setText(String.valueOf(quantity));
            isSoldOut = false;
        }

        txtTicketDesc.setText(detailPurchase.getDescription());

        LoginModel loginModel = AccountManager.loadLogin();

        guestName = loginModel.getUser_first_name() + " " + loginModel.getUser_last_name();
        guestEmail = loginModel.getEmail();
        guestPhone = AccountManager.loadSetting().getData().getPhone();
        if (guestPhone.equals(Utils.BLANK)) {
            guestPhone = getString(R.string.phone_number);
            txtGuestPhone.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            relConGuest.setSelected(true);
        }

        txtGuestName.setText(guestName);
        txtGuestEmail.setText(guestEmail + " | ");
        if (guestPhone.equals(getString(R.string.phone_number))) {
            txtGuestPhone.setText(guestPhone);
        } else {
            txtGuestPhone.setText("+" + guestPhone);
        }

        checkEnability(guestName, guestEmail, guestPhone);
    }

    private void sendMixpanel(EventDetailModel.Data.EventDetail eventDetail) {
        CommEventMixpanelModel commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), detailPurchase.getName(), detailPurchase.getTicket_type(), detailPurchase.getTotal_price(), detailPurchase.getMax_purchase());

        App.getInstance().trackMixPanelCommerce(Utils.COMM_PRODUCT_DETAIL, commEventMixpanelModel);
    }

   /* @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.ticket_detail);
    }

    @Override
    protected int getCurrentStep() {
        return 1;
    }*/

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
        if (resultCode == RESULT_OK) {
            guestName = data.getStringExtra(Common.FIELD_GUEST_NAME);
            guestEmail = data.getStringExtra(Common.FIELD_GUEST_EMAIL);
            guestPhone = data.getStringExtra(Common.FIELD_GUEST_PHONE);
            txtGuestName.setText(data.getStringExtra(Common.FIELD_GUEST_NAME));
            txtGuestEmail.setText(guestEmail + " | ");
            if (guestPhone.equals(getString(R.string.phone_number))) {
                txtGuestPhone.setText(guestPhone);
            } else {
                txtGuestPhone.setText("+" + guestPhone);
            }
            txtGuestPhone.setTextColor(getResources().getColor(R.color.textDarkGray));
            relConGuest.setSelected(false);

            checkEnability(guestName, guestEmail, guestPhone);
        }
    }

    private void initLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(TicketDetailActivity.this);
            progressDialog.setMessage(getString(R.string.loading));
        }

        progressDialog.show();
    }

    private void dismissLoadingDialog() {
        if (progressDialog != null & progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void checkEnability(String name, String email, String phoneNumber) {

        if (isSoldOut) {
            btnDone.setEnabled(false);
        } else {
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

    }

    private void showTermsDialog(SummaryModel.Data.Product_summary.Product_list dataProduct) {
        dialogTerms = new Dialog(TicketDetailActivity.this);
        dialogTerms.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTerms.setContentView(R.layout.activity_terms);
        dialogTerms.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogTerms.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ImageView img_close = (ImageView) dialogTerms.findViewById(R.id.img_close);
        final ImageView img_check = (ImageView) dialogTerms.findViewById(R.id.img_check);
        final RelativeLayout rel_continue = (RelativeLayout) dialogTerms.findViewById(R.id.rel_continue);
        LinearLayout lin_term = (LinearLayout) dialogTerms.findViewById(R.id.lin_term);

        int size = dataProduct.getTerms().size();
        for (int i = 0; i < size; i++) {

            String number = String.valueOf((i + 1) + ".");
            String text = dataProduct.getTerms().get(i).getBody();
            InstructionItemView textView = new InstructionItemView(TicketDetailActivity.this, number, text);
            textView.setTextSizes(14);

            lin_term.addView(textView);
        }

        rel_continue.setEnabled(false);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTerms.dismiss();
            }
        });

        img_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_check.isSelected()) {
                    img_check.setSelected(false);
                    rel_continue.setEnabled(false);
                } else {
                    img_check.setSelected(true);
                    rel_continue.setEnabled(true);
                }
            }
        });

        rel_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rel_continue.isEnabled()) {
                    dialogTerms.dismiss();

                    Intent i = new Intent(TicketDetailActivity.this, PurchaseInfoActivity.class);
                    i.putExtra(Common.FIELD_EVENT_ID, eventId);
                    i.putExtra(Common.FIELD_EVENT_NAME, eventName);
                    i.putExtra(Common.FIELD_VENUE_NAME, venueName);
                    i.putExtra(Common.FIELD_STARTTIME, startTime);
                    i.putExtra(productSummary.getClass().getName(), productSummary);
                    i.putExtra(eventDetail.getClass().getName(), eventDetail);

                    startActivity(i);
                }
            }
        });

        dialogTerms.setCanceledOnTouchOutside(true);
        dialogTerms.setCancelable(true);
        dialogTerms.show();
    }

}
