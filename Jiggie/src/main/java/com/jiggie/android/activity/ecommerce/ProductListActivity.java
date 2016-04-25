package com.jiggie.android.activity.ecommerce;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.ticket.ReservationActivity;
import com.jiggie.android.activity.ecommerce.ticket.TicketDetailActivity;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ProductListAdapter;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.BaseManager;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CommEventMixpanelModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.ProductListModel;
import com.jiggie.android.model.SuccessTokenModel;
import com.jiggie.android.view.HeaderView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductListActivity extends ToolbarActivity
        implements ViewTreeObserver.OnGlobalLayoutListener, SwipeRefreshLayout.OnRefreshListener, ProductListAdapter.ViewSelectedListener,
        AppBarLayout.OnOffsetChangedListener {

    ProductListAdapter adapter;

    public static final String TAG = ProductListActivity.class.getSimpleName();
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    //String eventId = "56b1a0bf89bfed03005c50f0";
    String eventId;
    boolean isTwoType = false;
    int section2Start = 0;
    String eventName, venueName, startTime;

    private boolean isLoading;
    EventDetailModel.Data.EventDetail eventDetail;

    @Bind(R.id.toolbar_header_view)
    protected HeaderView toolbarHeaderView;

    @Bind(R.id.float_header_view)
    protected HeaderView floatHeaderView;

    /*@Bind(R.id.lblEventLocation)
    protected TextView lblEventLocation;*/

    @Bind(R.id.appBar)
    protected AppBarLayout appBarLayout;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.event_image)
    ImageView eventImage;

    @Bind(R.id.back_button)
    ImageButton backButton;

    private boolean isHideToolbarView = false;

    /*@Override
    protected int getThemeResource() {
        return R.style.AppTheme_StatusBarTransparent;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        //super.bindView();
        final Intent intent = getIntent();
        eventId = intent.getStringExtra(Common.FIELD_EVENT_ID);
        eventDetail = intent.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        sendMixpanel(eventDetail);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        swipeRefresh.setOnRefreshListener(this);
        this.isLoading = false;

        appBarLayout.addOnOffsetChangedListener(this);
        final String eventPics = getIntent().getStringExtra("images");
        Utils.d(TAG, "event pics " + eventPics);
        if (eventPics != null) {
            Glide.with(this)
                    .load(eventPics)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(eventImage);
        }

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        try {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(eventDetail.getStart_datetime());
            final String startTime = Common.SERVER_DATE_FORMAT_COMM.format(startDate);
            toolbarHeaderView.bindTo(eventDetail.getTitle()
                    , eventDetail.getVenue_name() + ", " + startTime);
            floatHeaderView.bindTo(eventDetail.getTitle()
                    , eventDetail.getVenue_name() + "\n"
                            + startTime);
        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }


    }

    public void checkTokenHeader()
    {
        if(AccountManager.getAccessTokenFromPreferences().isEmpty())
        {
            AccountManager.getAccessToken(new CommerceManager.OnResponseListener()
            {
                @Override
                public void onSuccess(Object object) {
                    //do restart here
                    SuccessTokenModel successTokenModel = (SuccessTokenModel) object;
                    final String token = successTokenModel.data.token;
                    Utils.d(TAG, "success token model " +  successTokenModel.data.token);
                    AccountManager.setAccessTokenToPreferences(token);
                    CommerceManager.initCommerceService();
                    BaseManager.reinstantianteRetrofit();
                    loadData(eventId);
                    //onNeedToRestart();
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    //onCustomCallbackFailure(message);
                }
            });
        }
        else loadData(eventId);
    }


    private void sendMixpanel(EventDetailModel.Data.EventDetail eventDetail){
        CommEventMixpanelModel commEventMixpanelModel = new CommEventMixpanelModel(eventDetail.getTitle(), eventDetail.getVenue_name(), eventDetail.getVenue().getCity(), eventDetail.getStart_datetime_str(),
                eventDetail.getEnd_datetime_str(), eventDetail.getTags(), eventDetail.getDescription());
        App.getInstance().trackMixPanelCommerce(Utils.COMM_PRODUCT_LIST, commEventMixpanelModel);
    }

    @Override
    public void onViewSelected(int position, Object object) {
        Intent i = null;
        if(isTwoType){
            if(position<section2Start){
                i = new Intent(ProductListActivity.this, TicketDetailActivity.class);
                ProductListModel.Data.ProductList.Purchase itemData = (ProductListModel.Data.ProductList.Purchase)object;
                i.putExtra(Common.FIELD_TRANS_TYPE, itemData.getTicket_type());
                //Utils.d(TAG, "detailPurchase  brother " + itemData.getSummary());
                i.putExtra(itemData.getClass().getName(), itemData);
            }else{
                i = new Intent(ProductListActivity.this, ReservationActivity.class);
                ProductListModel.Data.ProductList.Reservation itemData = (ProductListModel.Data.ProductList.Reservation)object;
                i.putExtra(Common.FIELD_TRANS_TYPE, itemData.getTicket_type());
                i.putExtra(itemData.getClass().getName(), itemData);
            }
        }else{
            i = new Intent(ProductListActivity.this, TicketDetailActivity.class);
            ProductListModel.Data.ProductList.Purchase itemData = (ProductListModel.Data.ProductList.Purchase)object;
            i.putExtra(Common.FIELD_TRANS_TYPE, itemData.getTicket_type());
            i.putExtra(itemData.getClass().getName(), itemData);
        }
        i.putExtra(Common.FIELD_EVENT_ID, eventId);
        i.putExtra(Common.FIELD_EVENT_NAME, eventName);
        i.putExtra(Common.FIELD_VENUE_NAME, venueName);
        i.putExtra(Common.FIELD_STARTTIME, startTime);
        i.putExtra(eventDetail.getClass().getName(), eventDetail);
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
        checkTokenHeader();
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
        swipeRefresh.setRefreshing(true);
        CommerceManager.loaderProductList(eventId, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                ProductListModel data = (ProductListModel) object;

                if (data != null) {
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
                } else {
                    Toast.makeText(ProductListActivity.this, getString(R.string.msg_wrong), Toast.LENGTH_LONG).show();
                }

                swipeRefresh.setRefreshing(false);
                isLoading = false;
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Utils.d(String.valueOf(responseCode), message);
                //Toast.makeText(ProductListActivity.this, message, Toast.LENGTH_LONG);
                swipeRefresh.setRefreshing(false);
                isLoading = false;
            }
        });
    }

    private void setsAdapter(String eventName, String venueName, String startTime, boolean isTwoType, int section2Start, ArrayList<ProductListModel.Data.ProductList.Purchase> dataPurchase, ArrayList<ProductListModel.Data.ProductList.Reservation> dataReservation){
        adapter = new ProductListAdapter(eventName, venueName, startTime, isTwoType, section2Start, dataPurchase, dataReservation, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    @OnClick(R.id.back_button)
    public void onBackButtonClick()
    {
        super.onBackPressed();
    }
}
