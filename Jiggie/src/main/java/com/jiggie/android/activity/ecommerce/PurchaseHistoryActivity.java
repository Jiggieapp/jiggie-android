package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.OrderHistoryAdapter;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.BaseManager;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.PurchaseHistoryModel;
import com.jiggie.android.model.SuccessTokenModel;

import butterknife.Bind;

/**
 * Created by LTE on 3/6/2016.
 */
public class PurchaseHistoryActivity extends ToolbarActivity
        implements OrderHistoryAdapter.ViewSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private OrderHistoryAdapter adapter;
    public final static String TAG = PurchaseHistoryActivity.class.getSimpleName();
    boolean fromHowToPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_purchase_history);
        super.bindView();
        super.setToolbarTitle(getResources().getString(R.string.his_title), true);

        Intent a = getIntent();
        fromHowToPay = a.getBooleanExtra(Common.FIELD_FROM_HOWTOPAY, false);
        //this.recyclerView.setAdapter(new OrderHistoryAdapter(this));
        adapter = new OrderHistoryAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.swipeRefresh.setOnRefreshListener(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefresh.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        //this.recyclerView.setAdapter(adapter);
        //loadOrderList();
        onRefresh();

        App.getInstance().trackMixPanelCommerce(Utils.COMM_ORDER_LIST, null);
    }

    @Override
    public void onViewSelected(PurchaseHistoryModel.Data.Order_list.Order order, PurchaseHistoryModel.Data.Order_list.Event event, boolean isPaid) {
        Intent i = null;
        if(isPaid){
            i = new Intent(PurchaseHistoryActivity.this, CongratsActivity.class);
        }else {
            i = new Intent(PurchaseHistoryActivity.this, HowToPayActivity.class);
            i.putExtra(order.getClass().getName(), order);
            i.putExtra(event.getClass().getName(), event);
        }
        i.putExtra(Common.FIELD_ORDER_ID, Long.parseLong(order.getOrder_id()));
        i.putExtra(Common.FIELD_FROM_ORDER_LIST, true);
        startActivity(i);
    }

    private void loadOrderList() {
        CommerceManager.getOrderList(AccountManager.loadLogin().getFb_id(), new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                PurchaseHistoryModel purchaseHistoryModel = (PurchaseHistoryModel) object;
                if(purchaseHistoryModel!=null){
                    adapter.clear();
                    for (PurchaseHistoryModel.Data.Order_list order_list : purchaseHistoryModel.getData().getOrder_lists()) {
                        adapter.add(order_list);
                    }
                    recyclerView.setAdapter(adapter);
                }else{
                    Toast.makeText(PurchaseHistoryActivity.this, getString(R.string.msg_wrong), Toast.LENGTH_LONG).show();
                }

                if (swipeRefresh.isRefreshing())
                    swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                if (swipeRefresh.isRefreshing())
                    swipeRefresh.setRefreshing(false);
                Toast.makeText(PurchaseHistoryActivity.this, message
                        , Toast.LENGTH_SHORT).show();
            }

            /*@Override
            public void onFailure(ExceptionModel exceptionModel) {

            }*/
        });
    }

    @Override
    public void onRefresh() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
                //loadOrderList();
                checkTokenHeader();
            }
        });
    }

    public void checkTokenHeader()
    {
        if(AccountManager.getAccessTokenFromPreferences().isEmpty())
        {
            Utils.d(TAG, "pref " +  AccountManager.getAccessTokenFromPreferences());
            AccountManager.getAccessToken(new CommerceManager.OnResponseListener()
            {
                @Override
                public void onSuccess(Object object) {
                    //do restart here
                    SuccessTokenModel successTokenModel = (SuccessTokenModel) object;
                    final String token = successTokenModel.data.token;
                    AccountManager.setAccessTokenToPreferences(token);
                    BaseManager.reinstantianteRetrofit();
                    CommerceManager.initCommerceService();;
                    loadOrderList();
                    //onNeedToRestart();
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    //onCustomCallbackFailure(message);
                }
            });
        }
        else loadOrderList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        redirectToMain();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                redirectToMain();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void redirectToMain()
    {
        if(fromHowToPay){
            Intent i = new Intent(PurchaseHistoryActivity.this, MainActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }else{
            finish();
        }
    }
}
