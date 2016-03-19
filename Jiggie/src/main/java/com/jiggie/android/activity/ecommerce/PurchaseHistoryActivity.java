package com.jiggie.android.activity.ecommerce;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.jiggie.android.R;
import com.jiggie.android.api.OnResponseListener;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.OrderHistoryAdapter;
import com.jiggie.android.manager.PurchaseHistoryManager;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.PurchaseHistoryModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_purchase_history);
        super.bindView();
        super.setToolbarTitle(getResources().getString(R.string.his_title), true);

        //this.recyclerView.setAdapter(new OrderHistoryAdapter(this));
        adapter = new OrderHistoryAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.swipeRefresh.setOnRefreshListener(this);
        this.recyclerView.setLayoutManager(layoutManager);
        //this.recyclerView.setAdapter(adapter);
        //loadOrderList();
        onRefresh();

    }

    @Override
    public void onViewSelected() {

    }

    private void loadOrderList() {
        PurchaseHistoryManager.getOrderList("321321", new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                PurchaseHistoryModel purchaseHistoryModel = (PurchaseHistoryModel) object;
                for (PurchaseHistoryModel.Data.Order_list order_list : purchaseHistoryModel.getData().getOrder_lists()) {
                    adapter.add(order_list);
                }
                recyclerView.setAdapter(adapter);
                if (swipeRefresh.isRefreshing())
                    swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(ExceptionModel exceptionModel) {
                if (swipeRefresh.isRefreshing())
                    swipeRefresh.setRefreshing(false);
                Toast.makeText(PurchaseHistoryActivity.this, exceptionModel.getMessage()
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        this.swipeRefresh.setRefreshing(true);
        loadOrderList();
    }
}
