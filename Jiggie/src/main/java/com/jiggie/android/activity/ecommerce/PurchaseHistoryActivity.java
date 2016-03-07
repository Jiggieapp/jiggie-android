package com.jiggie.android.activity.ecommerce;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.OrderHistoryAdapter;

import butterknife.Bind;

/**
 * Created by LTE on 3/6/2016.
 */
public class PurchaseHistoryActivity extends ToolbarActivity implements OrderHistoryAdapter.ViewSelectedListener {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_purchase_history);
        super.bindView();
        super.setToolbarTitle(getResources().getString(R.string.his_title), true);

        this.recyclerView.setAdapter(new OrderHistoryAdapter(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onViewSelected() {

    }
}
