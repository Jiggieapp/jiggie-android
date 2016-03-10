package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.ticket.TicketDetailActivity;
import com.jiggie.android.component.activity.ToolbarWithDotActivity;
import com.jiggie.android.component.adapter.ProductListAdapter;
import com.jiggie.android.model.Common;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductListActivity extends ToolbarWithDotActivity
        implements ViewTreeObserver.OnGlobalLayoutListener, SwipeRefreshLayout.OnRefreshListener, ProductListAdapter.ViewSelectedListener {

    ProductListAdapter adapter;

    public static final String TAG = ProductListActivity.class.getSimpleName();
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;


    private boolean isLoading;
    private String eventId;

    @Override
    protected int getCurrentStep() {
        return 1;
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        super.bindView();
        final Intent intent = getIntent();
        eventId = intent.getStringExtra(Common.FIELD_EVENT_ID);

        adapter = new ProductListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        //swipeRefresh.setOnRefreshListener(this);
        recyclerView.setAdapter(adapter);
        this.isLoading = false;
    }

    @Override
    protected String getToolbarTitle() {
        return "CHOOSE ADMISSION";
    }

    @Override
    public void onViewSelected() {
        startActivity(new Intent(ProductListActivity.this, TicketDetailActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onGlobalLayout() {
        //this.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onRefresh() {
        if (this.isLoading) {
            // refresh is ongoing
            return;
        }
        this.isLoading = true;
        //loadData(eventId);
    }
}
