package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ProductListAdapter;
import com.jiggie.android.component.adapter.SimpleSectionedRecycleViewAdapter;
import com.jiggie.android.model.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class ProductListActivity extends ToolbarActivity
    implements ViewTreeObserver.OnGlobalLayoutListener, SwipeRefreshLayout.OnRefreshListener
{
    //@Bind(R.id.recycler_view_product) RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    ProductListAdapter adapter;

    public static final String TAG = ProductListActivity.class.getSimpleName();
    private boolean isLoading;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        super.bindView();

        EventBus.getDefault().register(this);

        final Intent intent = getIntent();
        eventId = intent.getStringExtra(Common.FIELD_EVENT_ID);

        //adapter = new ProductListAdapter();
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        //recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        this.isLoading = false;
        //onRefresh();
        Utils.d(TAG, "eventId " + eventId);
        //loadData(eventId);
    }

    /*private void loadData(final String eventId)
    {
        ProductManager.getProductList(eventId);
    }*/

    /*public void onEvent(ProductModel productModel)
    {
        this.isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
        adapter.clear();
        ArrayList<ProductModel.Data.Product_lists.Purchase> purchase
                = productModel.getData().getProduct_lists().getPurchase();
        //This is the code to provide a sectioned list
        List<SimpleSectionedRecycleViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecycleViewAdapter.Section>();
        adapter.addPurchases(purchase);
        //Sections
        sections.add(new SimpleSectionedRecycleViewAdapter.Section(0,
                purchase.get(0).getTicket_type()));

        ArrayList<ProductModel.Data.Product_lists.Reservation> reservations
                = productModel.getData().getProduct_lists().getReservation();
        sections.add(new SimpleSectionedRecycleViewAdapter.Section(adapter.getItemCount(),
                reservations.get(0).getTicket_type()));
        for(int i=0;i<reservations.size();i++)
        {
           //adapter.addReservations(reservations);
            ProductModel.Data.Product_lists.Purchase temp
                    = new ProductModel.Data.Product_lists.Purchase(reservations.get(i));
            adapter.add(temp);
        }
        adapter.notifyDataSetChanged();

        *//*sections.add(new SimpleSectionedRecycleViewAdapter.Section(12,"Section 3"));
        sections.add(new SimpleSectionedRecycleViewAdapter.Section(14,"Section 4"));
        sections.add(new SimpleSectionedRecycleViewAdapter.Section(20,"Section 5"));
*//*
        //Add your adapter to the sectionAdapter
        SimpleSectionedRecycleViewAdapter.Section[] dummy = new SimpleSectionedRecycleViewAdapter.Section[sections.size()];
        SimpleSectionedRecycleViewAdapter mSectionedAdapter = new
                SimpleSectionedRecycleViewAdapter(this,R.layout.section,R.id.section_text, adapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        recyclerView.setAdapter(mSectionedAdapter);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
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
