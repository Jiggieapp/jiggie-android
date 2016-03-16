package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.ticket.TicketDetailActivity;
import com.jiggie.android.component.activity.ToolbarWithDotActivity;
import com.jiggie.android.component.adapter.ProductListAdapter;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ProductListModel;

import java.util.ArrayList;

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

    String eventId = "56b1a0bf89bfed03005c50f0";
    boolean isTwoType = false;
    int section2Start = 0;
    String eventName, venueName, startTime;

    private boolean isLoading;

    @Override
    protected int getCurrentStep() {
        return 0;
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        super.bindView();
        final Intent intent = getIntent();
        //eventId = intent.getStringExtra(Common.FIELD_EVENT_ID);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        swipeRefresh.setOnRefreshListener(this);
        this.isLoading = false;
    }

    @Override
    protected String getToolbarTitle() {
        return "CHOOSE ADMISSION";
    }

    @Override
    public void onViewSelected(int position, Object object) {
        Intent i = new Intent(ProductListActivity.this, TicketDetailActivity.class);
        i.putExtra(Common.FIELD_EVENT_ID, eventId);
        i.putExtra(Common.FIELD_EVENT_NAME, eventName);
        i.putExtra(Common.FIELD_VENUE_NAME, venueName);
        i.putExtra(Common.FIELD_STARTTIME, startTime);


        if(isTwoType){
            if(position<section2Start){
                ProductListModel.Data.ProductList.Purchase itemData = (ProductListModel.Data.ProductList.Purchase)object;
                i.putExtra(Common.FIELD_TRANS_TYPE, itemData.getTicket_type());
                i.putExtra(itemData.getClass().getName(), itemData);
            }else{
                ProductListModel.Data.ProductList.Reservation itemData = (ProductListModel.Data.ProductList.Reservation)object;
                i.putExtra(Common.FIELD_TRANS_TYPE, itemData.getTicket_type());
                i.putExtra(itemData.getClass().getName(), itemData);
            }
        }else{
            ProductListModel.Data.ProductList.Purchase itemData = (ProductListModel.Data.ProductList.Purchase)object;
            Log.d("desc",itemData.getDescription());
        }

        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onGlobalLayout() {
        this.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        //this.loadData(eventId);
        this.onRefresh();
    }

    @Override
    public void onRefresh() {
        if (this.isLoading) {
            // refresh is ongoing
            return;
        }
        this.isLoading = true;
        loadData(eventId);
    }

    private void loadData(String eventId){
        CommerceManager.loaderProductList(eventId, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                ProductListModel data = (ProductListModel) object;
                eventName = data.getData().getProduct_lists().getEvent_name();
                venueName = data.getData().getProduct_lists().getVenue_name();
                startTime = data.getData().getProduct_lists().getStart_datetime();
                ArrayList<ProductListModel.Data.ProductList.Purchase> dataPurchase = data.getData().getProduct_lists().getPurchase();
                ArrayList<ProductListModel.Data.ProductList.Reservation> dataReservation = data.getData().getProduct_lists().getReservation();

                if (dataReservation.size() > 0) {
                    isTwoType = true;
                    section2Start = dataPurchase.size();
                }

                setsAdapter(eventName, venueName, startTime, isTwoType, section2Start, dataPurchase, dataReservation);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Log.d(String.valueOf(responseCode), message);
            }
        });
    }

    private void setsAdapter(String eventName, String venueName, String startTime, boolean isTwoType, int section2Start, ArrayList<ProductListModel.Data.ProductList.Purchase> dataPurchase, ArrayList<ProductListModel.Data.ProductList.Reservation> dataReservation){
        adapter = new ProductListAdapter(eventName, venueName, startTime, isTwoType, section2Start, dataPurchase, dataReservation, this);
        recyclerView.setAdapter(adapter);
    }
}