package com.jiggie.android.activity.ecommerce;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.PaymentMethodAdapter;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CCModel;
import com.jiggie.android.model.CCScreenModel;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.PaymentMethod;
import com.jiggie.android.model.PostDeleteCCModel;
import com.jiggie.android.model.SummaryModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by LTE on 2/25/2016.
 */
public class PaymentMethodActivity extends ToolbarActivity implements PaymentMethodAdapter.ViewSelectedListener, ViewTreeObserver.OnGlobalLayoutListener, SwipeRefreshLayout.OnRefreshListener, PaymentMethodAdapter.LongClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private int section2Start = 0;
    private PaymentMethodAdapter adapter;
    private boolean isLoading;
    private String totalPrice;
    private Dialog dialogLongClick;
    private final static String TAG = PaymentMethodActivity.class.getSimpleName();

    String payment_type;
    long order_id;
    EventDetailModel.Data.EventDetail eventDetail;
    SummaryModel.Data.Product_summary productSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_payment_method);
        super.bindView();
        super.setToolbarTitle(getResources().getString(R.string.select_payment), true);

        Intent a = getIntent();
        totalPrice = a.getStringExtra(Common.FIELD_PRICE);
        order_id = a.getLongExtra(Common.FIELD_ORDER_ID, 0);
        payment_type = a.getStringExtra(Common.FIELD_PAYMENT_TYPE);
        productSummary = a.getParcelableExtra(SummaryModel.Data.Product_summary.class.getName());
        eventDetail = a.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());

        sendMixpanel(productSummary, eventDetail);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        swipeRefresh.setOnRefreshListener(this);
        this.isLoading = false;

        paymentMethodGlobal = new HashMap<>();
    }

    private void sendMixpanel(SummaryModel.Data.Product_summary productSummary, EventDetailModel.Data.EventDetail eventDetail) {
        CommEventMixpanelModel commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription(), productSummary.getProduct_list().get(0).getName(), productSummary.getProduct_list().get(0).getTicket_type(),
                productSummary.getTotal_price(), productSummary.getProduct_list().get(0).getMax_buy());
        App.getInstance().trackMixPanelCommerce(Utils.COMM_PAYMENT_SELECTION, commEventMixpanelModel);
    }

    @Override
    public void onGlobalLayout() {
        this.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        refreshData();
    }

    @Override
    public void onRefresh() {
        if (this.isLoading) {
            // refresh is ongoing
            return;
        }
        this.isLoading = true;
        loadData(AccountManager.loadLogin().getFb_id());
    }

    private void loadPaymentMethod() {
        CommerceManager.loaderPaymentMethod(new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                PaymentMethod paymentMethod = (PaymentMethod) object;
                if(swipeRefresh.isRefreshing())
                    swipeRefresh.setRefreshing(false);
                isLoading = false;
                /*ArrayList<HashMap<String, PaymentMethod.Data.Paymentmethod>> data
                        = new ArrayList<HashMap<String, PaymentMethod.Data.Paymentmethod>>();
                Map<String, HashMap<String, PaymentMethod.Data.Paymentmethod>> tampung
                        = new HashMap<String, HashMap<String, PaymentMethod.Data.Paymentmethod>>();*/
                for(PaymentMethod.Data.Paymentmethod paymentMethoddd : paymentMethod.data.paymentmethod)
                {
                    paymentMethodGlobal.put(paymentMethoddd.type, paymentMethoddd);
                }

                setAdapters(section2Start, CommerceManager.arrCCScreen, paymentMethodGlobal);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                if(swipeRefresh.isRefreshing())
                    swipeRefresh.setRefreshing(false);
                isLoading = false;
            }
        });
    }

    HashMap<String, PaymentMethod.Data.Paymentmethod> paymentMethodGlobal;

    private void loadData(String fb_id) {
        CommerceManager.arrCCScreen.clear();
        CommerceManager.loaderCCList(fb_id, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                isLoading = false;
                swipeRefresh.setRefreshing(false);
                CCModel ccModel = (CCModel) object;
                if (ccModel != null) {
                    ArrayList<CCModel.Data.Creditcard_information> ccInformation = ccModel.getData().getCreditcard_informations();
                    for (int i = 0; i < ccInformation.size(); i++) {
                        CommerceManager.arrCCScreen.add(new CCScreenModel(ccInformation.get(i), null, Utils.BLANK));
                    }
                    section2Start = CommerceManager.arrCCScreen.size() + 1;

                } else {
                    section2Start = 0 + 1;
                    Toast.makeText(PaymentMethodActivity.this, getString(R.string.msg_wrong), Toast.LENGTH_LONG).show();
                }

                for (int i = 0; i < CommerceManager.arrCCLocal.size(); i++) {
                    CommerceManager.arrCCScreen.add(CommerceManager.arrCCLocal.get(i));
                }

                section2Start = section2Start + CommerceManager.arrCCLocal.size();
                // setAdapters(section2Start, CommerceManager.arrCCScreen);
                loadPaymentMethod();
            }

            @Override
            public void onFailure(int responseCode, String message) {
                isLoading = false;
                swipeRefresh.setRefreshing(false);
                if (responseCode == Utils.CODE_EMPTY_DATA) {
                    section2Start = 0 + 1;
                    loadPaymentMethod();
                }
            }
        });
    }

    private void setAdapters(int section2Start, ArrayList<CCScreenModel> dataCredit) {
        /*adapter = new PaymentMethodAdapter(PaymentMethodActivity.this, this, this, section2Start, dataCredit, order_id, payment_type, productSummary, eventDetail);
        recyclerView.setAdapter(adapter);*/

        setAdapters(section2Start, dataCredit, paymentMethodGlobal);
    }

    //wandy 15-04-2016
    private void setAdapters(int section2Start, ArrayList<CCScreenModel> dataCredit
            , HashMap<String, PaymentMethod.Data.Paymentmethod> paymentMethod) {
        adapter = new PaymentMethodAdapter(PaymentMethodActivity.this, this, this, section2Start, dataCredit, order_id, payment_type, productSummary, eventDetail, paymentMethod);
        recyclerView.setAdapter(adapter);
    }
    //end of wandy 15-04-2016

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(284); //do nothing
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onViewSelected(int position, CCScreenModel dataCredit) {
        if (position == (section2Start - 1)) {
            //add new CC
            Intent i = new Intent(PaymentMethodActivity.this, AddCreditCardActivity.class);
            i.putExtra(Common.FIELD_PRICE, totalPrice);
            i.putExtra(productSummary.getClass().getName(), productSummary);
            i.putExtra(eventDetail.getClass().getName(), eventDetail);
            startActivityForResult(i, 0);
        } else if (position == (section2Start)) {
            //bca
            setResult(RESULT_OK, new Intent().putExtra(Common.FIELD_PAYMENT_TYPE, Utils.TYPE_BCA));
            finish();
        } else if (position == (section2Start + 2)) {
            //other bank
            setResult(RESULT_OK, new Intent().putExtra(Common.FIELD_PAYMENT_TYPE, Utils.TYPE_VA));
            finish();
        } else if (position == section2Start + 1) {
            //mandiri
            setResult(RESULT_OK, new Intent().putExtra(Common.FIELD_PAYMENT_TYPE, Utils.TYPE_BP));
            finish();
        } else {
            //exist CC
            setResult(RESULT_OK, new Intent().putExtra(Common.FIELD_PAYMENT_TYPE, Utils.TYPE_CC).putExtra(dataCredit.getClass().getName(), dataCredit));
            finish();
        }
    }

    @Override
    public void onLongClick(int position, CCScreenModel dataCredit) {
        if (position == (section2Start - 1) || position == (section2Start + 1) || position == section2Start) {
            //do nothing
        } else {
            //exist CC
            showLongClickDialog(position, dataCredit.getCreditcardInformation());
        }
    }

    private void showLongClickDialog(final int position, final CCModel.Data.Creditcard_information dataCredit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Credit Card").setMessage("Are you sure to delete this Credit card?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogLongClick.dismiss();
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!dataCredit.getSaved_token_id().equals(Utils.BLANK)) {
                    PostDeleteCCModel postDeleteCCModel = new PostDeleteCCModel(AccountManager.loadLogin().getFb_id(), dataCredit.getMasked_card());
                    CommerceManager.loaderDeleteCC(postDeleteCCModel, new CommerceManager.OnResponseListener() {
                        @Override
                        public void onSuccess(Object object) {
                            CommerceManager.arrCCScreen.remove(position);
                            section2Start = CommerceManager.arrCCScreen.size() + 1;
                            setAdapters(section2Start, CommerceManager.arrCCScreen);
                        }

                        @Override
                        public void onFailure(int responseCode, String message) {
                            Log.d("delete", "failed");
                        }
                    });
                } else {
                    String m1 = CommerceManager.arrCCScreen.get(position).getCreditcardInformation().getMasked_card();
                    CommerceManager.arrCCScreen.remove(position);

                    for (int i = 0; i < CommerceManager.arrCCLocal.size(); i++) {
                        if (m1.equals(CommerceManager.arrCCLocal.get(i).getCreditcardInformation().getMasked_card()))
                            CommerceManager.arrCCLocal.remove(i);
                    }

                    section2Start = CommerceManager.arrCCScreen.size() + 1;
                    setAdapters(section2Start, CommerceManager.arrCCScreen);
                }
            }
        });
        dialogLongClick = builder.create();
        dialogLongClick.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //refreshData();
            section2Start = CommerceManager.arrCCScreen.size() + 1;
            setAdapters(section2Start, CommerceManager.arrCCScreen, paymentMethodGlobal);
        }
    }

    private void refreshData() {
        if(CommerceManager.arrCCScreen.size()==0){
            isLoading = false;
            swipeRefresh.setRefreshing(true);
            this.onRefresh();
        }else{
            section2Start = CommerceManager.arrCCScreen.size() + 1;
            //setAdapters(section2Start, CommerceManager.arrCCScreen);
            isLoading = true;
            swipeRefresh.setRefreshing(true);
            loadPaymentMethod();
        }

        //this.onRefresh();

        //loadPaymentMethod();
    }
}
