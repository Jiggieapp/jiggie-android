package com.jiggie.android.activity.event;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.activity.ecommerce.ProductListActivity;
import com.jiggie.android.activity.invite.InviteFriendsActivity;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ImagePagerIndicatorAdapter;
import com.jiggie.android.component.volley.SimpleVolleyRequestListener;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.fragment.SocialTabFragment;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.manager.GuestManager;
import com.jiggie.android.manager.InviteManager;
import com.jiggie.android.manager.ShareManager;
import com.jiggie.android.manager.TooltipsManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.GuestModel;
import com.jiggie.android.model.ShareLinkModel;
import com.jiggie.android.model.likeModel;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import it.sephiroth.android.library.tooltip.Tooltip;
import it.sephiroth.android.library.widget.HListView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by rangg on 11/11/2015.
 */
public class EventDetailActivity extends ToolbarActivity implements SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener, OnMapReadyCallback {
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    /*@Bind(R.id.imagePagerIndicator)
    HListView imagePagerIndicator;*/
    @Bind(R.id.titles)
    CirclePageIndicator titlePageIndicator;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.txtExternalSite)
    TextView txtExternalSite;
    @Bind(R.id.txtGuestCounter)
    TextView txtGuestCounter;
    @Bind(R.id.imageViewPager)
    ViewPager imageViewPager;
    @Bind(R.id.txtDescription)
    TextView txtDescription;
    @Bind(R.id.scrollView)
    NestedScrollView scrollView;
    @Bind(R.id.appBar)
    AppBarLayout appBarLayout;
    @Bind(R.id.layoutGuests)
    View layoutGuests;
    @Bind(R.id.txtBookNow)
    TextView txtBookNow;
    @Bind(R.id.txtAddress)
    TextView txtAddress;
    @Bind(R.id.txtVenue)
    TextView txtVenue;
    @Bind(R.id.txtDate)
    TextView txtDate;
    @Bind(R.id.btnBook)
    View btnBook;
    @Bind(R.id.element_containers)
    LinearLayout elementContainers;
    @Bind(R.id.element_containers2)
    LinearLayout elementContainers2;

    @Bind(R.id.imageGuest1)
    ImageView imageGuest1;
    @Bind(R.id.imageGuest2)
    ImageView imageGuest2;
    @Bind(R.id.imageGuest3)
    ImageView imageGuest3;
    @Bind(R.id.imageGuest4)
    ImageView imageGuest4;
    @Bind(R.id.txtPriceTitle)
    TextView txtPriceTitle;
    @Bind(R.id.txtPriceFill)
    TextView txtPriceFill;
    @Bind(R.id.img_love)
    ImageView imgLove;
    @Bind(R.id.txt_count_like)
    TextView txtCountLike;
    @Bind(R.id.img_share)
    ImageView imgShare;
    @Bind(R.id.txtEventName)
    TextView txtEventName;
    @Bind(R.id.rel_desc_more)
    RelativeLayout relDescMore;
    @Bind(R.id.lin_see_all_guest)
    LinearLayout linSeeAllGuest;

    private ImagePagerIndicatorAdapter imagePagerIndicatorAdapter;
    private ImageView[] imageGuests;
    private ShareLinkModel shareLinkModel;
    private GoogleMap map;
    private EventDetailModel.Data.EventDetail eventDetail;
    String event_id = "";
    String event_name = "";
    String event_venue_name = "";
    ArrayList<String> event_tags, event_pics;
    String event_day = "";
    String event_end = "";
    String event_description = "";
    int lowest_price;
    String fullfilmentType = "";

    ProgressDialog progressDialog;
    public static final String TAG = EventDetailActivity.class.getSimpleName();
    private File file;
    private int count_like, count_like_new;
    boolean canClickLike = false;
    Timer timerLike, timerInvite;
    TimerTask timerTask, timerTaskInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_event_detail);
        super.bindView();

        EventBus.getDefault().register(this);
        eventDetail = new EventDetailModel.Data.EventDetail();

        Intent a = super.getIntent();
        event_id = a.getStringExtra(Common.FIELD_EVENT_ID);
        eventDetail.set_id(event_id);
        event_name = a.getStringExtra(Common.FIELD_EVENT_NAME);
        event_venue_name = a.getStringExtra(Common.FIELD_EVENT_VENUE_NAME);
        event_tags = a.getStringArrayListExtra(Common.FIELD_EVENT_TAGS);
        event_day = a.getStringExtra(Common.FIELD_EVENT_DAY);
        event_end = a.getStringExtra(Common.FIELD_EVENT_DAY_END);
        event_pics = a.getStringArrayListExtra(Common.FIELD_EVENT_PICS);
        event_description = a.getStringExtra(Common.FIELD_EVENT_DESCRIPTION);
        count_like = a.getIntExtra(Common.FIELD_EVENT_LIKE, 0);
        count_like_new = count_like;
        lowest_price = a.getIntExtra(Common.FIELD_EVENT_LOWEST_PRICE, 0);
        fullfilmentType = a.getStringExtra(Common.FIELD_FULLFILMENT_TYPE);

        this.imagePagerIndicatorAdapter = new ImagePagerIndicatorAdapter(super.getSupportFragmentManager(), this.imageViewPager);
        //this.imagePagerIndicator.setAdapter(this.imagePagerIndicatorAdapter.getIndicatorAdapter());
        titlePageIndicator.setViewPager(this.imageViewPager);

        if (a != null) {
            this.txtVenue.setText("");
            if (event_venue_name != null) {
                eventDetail.setVenue_name(event_venue_name);
                this.txtVenue.setText(event_venue_name);
            }


            /*if(event_tags != null)
            {
                populateTags(event_tags);
            }*/

            if (event_name != null) {
                //super.setToolbarTitle(event_name, true);
                super.setToolbarTitle(Utils.BLANK, true);
                txtEventName.setText(event_name);
            } else super.setToolbarTitle("", true);

            if (event_day != null && event_name != null) {
                try {
                    final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse
                            (event_day);
                    final Date endDate = Common.ISO8601_DATE_FORMAT_UTC.parse
                            (event_end);
                    String simpleDate = App.getInstance().getResources().getString(R.string.event_date_format, Common.SERVER_DATE_FORMAT_ALT.format(startDate), Common.SIMPLE_12_HOUR_FORMAT.format(endDate));
                    txtDate.setText(simpleDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (event_pics != null) {
                eventDetail.setPhotos(event_pics);
                fillPhotos(event_pics);
            }

            if (event_description != null) {
                event_description = event_description.replace("\n", "<br />");
                txtDescription.setText(Html.fromHtml(event_description));
                txtDescription.post(new Runnable() {
                    @Override
                    public void run() {
                        int lineCnt = txtDescription.getLineCount();
                        if (lineCnt < 4) {
                            relDescMore.setVisibility(View.GONE);
                        } else {
                            relDescMore.setVisibility(View.VISIBLE);

                            String subDes = "";
                            if (event_description.length() > 270) {
                                subDes = event_description.substring(0, 270);
                            } else {
                                subDes = event_description;
                            }
                            subDes = subDes.replace("\n", "<br />");
                            txtDescription.setText(Html.fromHtml(subDes));
                        }
                    }
                });
            }
            scrollView.setVisibility(View.VISIBLE);
            elementContainers.setVisibility(View.INVISIBLE);
            elementContainers2.setVisibility(View.INVISIBLE);

            if (event_id == null || event_id.equalsIgnoreCase("null")) {
                //wandy 17-03-2016
                //contoh working scheme
                //jiggie://event_detail/<event_id>&af_chrome_lp=true&af_deeplink=true&app-id=1630402100&campaign=None&media_source=App_Invite
                //end of contoh working scheme

                String dataString = a.getDataString();
                final boolean isJiggieUrl = isJiggieUrl(dataString);
                if (isJiggieUrl) {
                    Uri inputUri = Uri.parse(dataString);
                    /*event_id = inputUri.getQueryParameter("af_sub2");
                    for(String segment : inputUri.getPathSegments())
                    {
                        Utils.d(TAG, "segment " + segment);
                    }*/
                    event_id = inputUri.getPathSegments().get(0);
                } else {
                    Uri data = a.getData();
                    try {
                        Map<String, String> tamp = StringUtility.splitQuery(new URL(data.toString()));
                        event_id = tamp.get("af_sub2");
                        //Utils.d(TAG, "event_id oiii " + event_id + " " + data.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                //end of wandy 17-03-2016
            }

            if(fullfilmentType != null && !fullfilmentType.isEmpty())
            {
                final boolean isBookable = (StringUtility.isEquals(EventManager.FullfillmentTypes.RESERVATION, fullfilmentType, true)
                        || StringUtility.isEquals(EventManager.FullfillmentTypes.PURCHASE, fullfilmentType, true)
                        || (StringUtility.isEquals(EventManager.FullfillmentTypes.TICKET, fullfilmentType, true))); //free (tickets, tables, purchase)

                if (lowest_price == 0 && isBookable) {
                    txtPriceTitle.setVisibility(View.VISIBLE);
                    txtPriceFill.setVisibility(View.VISIBLE);
                    txtPriceFill.setText(getResources().getString(R.string.free));

                } else if(lowest_price > 0 && isBookable){
                    txtPriceTitle.setVisibility(View.VISIBLE);
                    txtPriceFill.setVisibility(View.VISIBLE);
                    txtPriceTitle.setShadowLayer(1.6f, 1.5f, 1.3f, getResources().getColor(android.R.color.black));
                    txtPriceFill.setShadowLayer(1.6f, 1.5f, 1.3f, getResources().getColor(android.R.color.black));
                    try {
                        //String str = String.format(Locale.US, "Rp %,d", lowest_price);
                        String str = StringUtility.getRupiahFormat(lowest_price + "");
                        txtPriceFill.setText(str);
                    } catch (Exception e) {
                        Utils.d(TAG, "exception " + e.toString());
                    }
                }
                else
                {
                    txtPriceTitle.setVisibility(View.GONE);
                    txtPriceFill.setVisibility(View.GONE);
                }
            }
            else
            {
                txtPriceTitle.setVisibility(View.GONE);
                txtPriceFill.setVisibility(View.GONE);
            }

        }

        if (event_name != null) {
            super.setToolbarTitle(event_name.toUpperCase(), true);
        }

        this.appBarLayout.addOnOffsetChangedListener(this);
        this.swipeRefresh.setOnRefreshListener(this);

        this.txtGuestCounter.setVisibility(View.GONE);
        //this.scrollView.setVisibility(View.INVISIBLE);
        this.collapsingToolbarLayout.setTitleEnabled(false);
        this.imageGuests = new ImageView[]
                {
                        imageGuest1, imageGuest2, imageGuest3, imageGuest4
                }

        ;

        final FragmentManager fragmentManager = super.getSupportFragmentManager();
        ((SupportMapFragment) fragmentManager.findFragmentById(R.id.map)).
                getMapAsync(this);
        super.registerReceiver(this.guestInvitedReceiver, new IntentFilter(super.getString(R.string.broadcastGuestInvited)

        ));

        if (file != null && file.exists())
            file.delete();

        txtCountLike.setText(String.valueOf(count_like));

        if (TooltipsManager.canShowTooltipAt(TooltipsManager.TOOLTIP_SHARE))

        {
            TooltipsManager.initTooltipWithAnchor(this, imgShare, getString(R.string.tooltip_share), Utils.myPixel(this, 380), Tooltip.Gravity.BOTTOM);
            TooltipsManager.setAlreadyShowTooltips(TooltipsManager.ALREADY_TOOLTIP_SHARE, true);
        }

        cekLike();
        runInvite();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Execute some code after 2 seconds have passed
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cekCounter();
                *//*if(InviteManager.validateTimeInvite(Calendar.getInstance().getTimeInMillis())){
                    startActivity(new Intent(EventDetailActivity.this, InviteFriendsActivity.class));
                }*//*
            }
        }, 1000);*/

        cekCounter();
        

    }

    private void cekCounter() {
        final int counter = AccountManager.getCounterEvent();
        if (counter + 1 < 4) {
            AccountManager.setCounterEvent(counter + 1);
        } else if (counter + 1 == 4) {
            AccountManager.setCounterEvent(5);
            if (InviteManager.validateTimeInvite(Calendar.getInstance().getTimeInMillis())) {
                startActivity(new Intent(EventDetailActivity.this, InviteFriendsActivity.class));
            }

            startActivity(new Intent(this, InviteFriendsActivity.class));
        } else if (counter > 4) {
            /*Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //cekCounter();
                    if (InviteManager.validateTimeInvite(Calendar.getInstance().getTimeInMillis())) {
                        startActivity(new Intent(EventDetailActivity.this, InviteFriendsActivity.class));
                    }
                }
            }, 1000);*/

            if (InviteManager.validateTimeInvite(Calendar.getInstance().getTimeInMillis())) {
                startActivity(new Intent(EventDetailActivity.this, InviteFriendsActivity.class));
            }
        }
    }

    private void cekLike() {
        try {
            if (EventManager.dataLike.size() > 0) {
                boolean exist = false;
                for (int i = 0; i < EventManager.dataLike.size(); i++) {
                    String eventId = EventManager.dataLike.get(i).getEvent_id();
                    if (eventId.equals(event_id)) {
                        imgLove.setSelected(EventManager.dataLike.get(i).isLiked());
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    EventManager.dataLike.add(new likeModel(event_id, false));
                }
            } else {
                EventManager.dataLike.add(new likeModel(event_id, false));
            }
        } catch (Exception e) {
            Log.d("cekLike", "exception");
        }

    }

    private void setLike() {
        try {
            for (int i = 0; i < EventManager.dataLike.size(); i++) {
                String eventId = EventManager.dataLike.get(i).getEvent_id();
                if (eventId.equals(event_id)) {
                    EventManager.dataLike.get(i).setIsLiked(imgLove.isSelected());
                    break;
                }
            }
        } catch (Exception e) {
            Log.d("setLike", "exception");
        }

    }

    private void setToolbarTitles(String title, boolean backEnable) {
        super.setToolbarTitle(title, backEnable);
    }

    private static boolean isJiggieUrl(String dataString) {

        if (dataString == null) {
            return false;
        }

        for (String url : Utils.JIGGIE_URLS) {
            if (dataString.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final String url = String.format("guest/events/viewed/%s/%s", AccessToken.getCurrentAccessToken().getUserId(), event_id);
        VolleyHandler.getInstance().createVolleyRequest(url, new SimpleVolleyRequestListener<Object, JSONObject>() {
            @Override
            public void onResponseCompleted(Object value) {
                if (isActive()) {
                    sendBroadcast(new Intent(getString(R.string.broadcast_social)));
                    super.onResponseCompleted(value);
                }
            }
        });
        this.map = googleMap;
        this.onRefresh();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        this.swipeRefresh.setEnabled(verticalOffset == 0);

        if (collapsingToolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
            setToolbarTitles(event_name, true);
        } else {
            setToolbarTitles(Utils.BLANK, true);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.eventDetail != null)
            super.getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public void onRefresh() {
        this.btnBook.setVisibility(View.GONE);
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });

        EventManager.loaderEventDetail(event_id, AccessToken.getCurrentAccessToken().getUserId()
                , AccountManager.loadSetting().getData().getGender_interest(), TAG);
    }

    private void fillPhotos(ArrayList<String> photoArr) {
        String[] photo = new String[photoArr.size()];
        photo = photoArr.toArray(photo);
        if (event_pics == null) {
            event_pics = new ArrayList<String>(Arrays.asList(photo));
        }
        imagePagerIndicatorAdapter.setImages(photo);
    }

    public void onEvent(EventDetailModel message) {
        if (message.getFrom().equalsIgnoreCase(TAG)) {
            try {
                eventDetail = message.getData().getEvents_detail();
                event_description = eventDetail.getDescription();
                App.getInstance().trackMixPanelViewEventDetail("View Event Details", eventDetail);
                elementContainers.setVisibility(View.VISIBLE);
                elementContainers2.setVisibility(View.VISIBLE);

                if (event_name == null) {
                    super.setToolbarTitle(eventDetail.getTitle().toUpperCase(), true);
                }

                this.txtVenue.setText(eventDetail.getVenue_name());

                if (!isActive())
                    return;

                ArrayList<EventDetailModel.Data.EventDetail.GuestViewed> guestArr = message.getData().getEvents_detail().getGuests_viewed();

                int size = guestArr.size();

                int guestCount = size;

                fillPhotos(message.getData().getEvents_detail().getPhotos());

                //txtDescription.setText(message.getData().getEvents_detail().getDescription());
                txtAddress.setText(message.getData().getEvents_detail().getVenue().getAddress());
                txtGuestCounter.setText(String.format("+%s", guestCount - imageGuests.length));
                txtGuestCounter.setVisibility(guestCount > imageGuests.length ? View.VISIBLE : View.GONE);
                //txtGuestCount.setText(getResources().getQuantityString(R.plurals.guest_count, guestCount, guestCount));

                if (guestCount > 0) {
                    Intent intent = new Intent(SocialTabFragment.TAG);
                    intent.putExtra(Utils.IS_ON
                            , AccountManager.loadSetting().getData().getNotifications().isFeed());
                    sendBroadcast(intent);

                    final int width = imageGuest1.getWidth() * 2;
                    guestCount = guestCount > imageGuests.length ? imageGuests.length : guestCount;

                    for (int i = 0; i < guestCount; i++) {
                        final String url = App.getFacebookImage(guestArr.get(i).getFb_id(), width);
                        Glide.with(EventDetailActivity.this)
                                .load(url)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()
                                .into(new BitmapImageViewTarget(imageGuests[i]) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        final Resources resources = getResources();
                                        if (resources != null) {
                                            final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource);
                                            circularBitmapDrawable.setCircular(true);
                                            super.getView().setImageDrawable(circularBitmapDrawable);
                                        }
                                    }
                                });
                    }

                    //not finished yet----
                    /*for (int i = guestCount; i > 0; i--) {
                        final String url = App.getFacebookImage(guestArr.get(guestCount - i).getFb_id(), width);
                        Glide.with(EventDetailActivity.this).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageGuests[i]) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                final Resources resources = getResources();
                                if (resources != null) {
                                    final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource);
                                    circularBitmapDrawable.setCircular(true);
                                    super.getView().setImageDrawable(circularBitmapDrawable);
                                }
                            }
                        });
                    }*/
                    //-----------------------
                }

                //Added by Aga 10-2-2016
                if (StringUtility.isEquals(EventManager.FullfillmentTypes.NONE, message.getData().getEvents_detail().getFullfillment_type(), true) ||
                        TextUtils.isEmpty(message.getData().getEvents_detail().getFullfillment_value())) {
                    btnBook.setVisibility(View.GONE);
                } else {
                    btnBook.setVisibility(View.VISIBLE);
                }
                //btnBook.setVisibility(StringUtility.isEquals(EventManager.FullfillmentTypes.NONE, message.getData().getEvents_detail().getFullfillment_type(), true) ? View.GONE : View.VISIBLE);

                layoutGuests.setVisibility(guestCount > 0 ? View.VISIBLE : View.GONE);
                scrollView.setVisibility(View.VISIBLE);

                final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(message.getData().getEvents_detail().getStart_datetime());
                final Date endDate = Common.ISO8601_DATE_FORMAT_UTC.parse(message.getData().getEvents_detail().getEnd_datetime());
                String simpleDate = App.getInstance().getResources().getString(R.string.event_date_format, Common.SERVER_DATE_FORMAT_ALT.format(startDate), Common.SIMPLE_12_HOUR_FORMAT.format(endDate));
                txtDate.setText(simpleDate);

                swipeRefresh.setRefreshing(false);
                eventDetail = message.getData().getEvents_detail();
                invalidateOptionsMenu();
                //populateTags();

                final String fullfillmentType = message.getData().getEvents_detail().getFullfillment_type();
                btnBook.setVisibility(
                        StringUtility.isEquals(EventManager.FullfillmentTypes.NONE, message.getData().getEvents_detail().getFullfillment_type(), true)
                                ? View.GONE : View.VISIBLE);
                /*if(EventManager.FullfillmentTypes.NONE.equals(fullfillmentType))
                {
                    btnBook.setVisibility(View.GONE);
                }
                else if(EventManager.FullfillmentTypes.TICKET.equals(fullfillmentType))
                {
                    btnBook.setVisibility(View.VISIBLE);
                }
                else btnBook.setVisibility(View.VISIBLE);*/


                if (StringUtility.isEquals(EventManager.FullfillmentTypes.PHONE_NUMBER, fullfillmentType, true)) {
                    txtExternalSite.setVisibility(View.GONE);
                    txtBookNow.setText(R.string.call);
                } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.RESERVATION, fullfillmentType, true)) {
                    txtExternalSite.setVisibility(View.GONE);
                    txtBookNow.setText(R.string.book_now);
                } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.PURCHASE, fullfillmentType, true)) {
                    txtExternalSite.setVisibility(View.GONE);
                    txtBookNow.setText(R.string.book_now);
                } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.TICKET, fullfillmentType, true)) {
                    txtExternalSite.setVisibility(View.GONE);
                    txtBookNow.setText(getResources().getString(R.string.book_now));
                }
                //Changed by Aga 16-2-2016
                else if (StringUtility.isEquals(EventManager.FullfillmentTypes.LINK, message.getData().getEvents_detail().getFullfillment_type(), true)) {
                    txtExternalSite.setVisibility(View.VISIBLE);
                    txtBookNow.setText(R.string.book_now);
                }

                //move it to the most bottom of the code, so the guest interested list will still be shown
                try {
                    final double latt = Double.parseDouble(message.getData().getEvents_detail().getVenue().getLat());
                    final double lon = Double.parseDouble(message.getData().getEvents_detail().getVenue().getLon());
                    final LatLng lat = new LatLng(latt, lon);
                    map.addMarker(new MarkerOptions().position(lat).title(message.getData().getEvents_detail().getVenue_name()));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));
                } catch (NumberFormatException e) {
                    swipeRefresh.setRefreshing(false);
                    throw new RuntimeException(App.getErrorMessage(e), e);
                }

                //Like PART====================
                if (eventDetail.is_liked()) {
                    imgLove.setSelected(true);
                    TooltipsManager.setCanShowTooltips(TooltipsManager.TOOLTIP_LIKE, false);

                } else {
                    imgLove.setSelected(false);
                    if (TooltipsManager.canShowTooltipAt(TooltipsManager.TOOLTIP_LIKE)) {
                        TooltipsManager.initTooltipWithAnchor(this, imgLove, getString(R.string.tooltip_like), Utils.myPixel(this, 380), Tooltip.Gravity.BOTTOM);
                        TooltipsManager.setAlreadyShowTooltips(TooltipsManager.ALREADY_TOOLTIP_LIKE, true);
                    }
                }

                count_like = eventDetail.getLikes();
                count_like_new = count_like;
                txtCountLike.setText(String.valueOf(count_like));

                setLike();
                canClickLike = true;
                //END of Like PART===================

            } catch (ParseException e) {
                swipeRefresh.setRefreshing(false);
                throw new RuntimeException(App.getErrorMessage(e), e);
            }

            if (event_description != null) {
                event_description = event_description.replace("\n", "<br />");
                txtDescription.setText(Html.fromHtml(event_description));
                txtDescription.post(new Runnable() {
                    @Override
                    public void run() {
                        int lineCnt = txtDescription.getLineCount();
                        if (lineCnt < 4) {
                            relDescMore.setVisibility(View.GONE);
                        } else {
                            relDescMore.setVisibility(View.VISIBLE);

                            String subDes = "";
                            if (event_description.length() > 270) {
                                subDes = event_description.substring(0, 270);
                            } else {
                                subDes = event_description;
                            }
                            subDes = subDes.replace("\n", "<br />");
                            txtDescription.setText(Html.fromHtml(subDes));
                        }
                    }
                });
            }
        }

        lowest_price = eventDetail.getLowest_price();
        final String fullfillmentType = eventDetail.getFullfillment_type();
        final boolean isBookable = (StringUtility.isEquals(EventManager.FullfillmentTypes.RESERVATION, fullfillmentType, true)
                || StringUtility.isEquals(EventManager.FullfillmentTypes.PURCHASE, fullfillmentType, true)
                || (StringUtility.isEquals(EventManager.FullfillmentTypes.TICKET, fullfillmentType, true))); //free (tickets, tables, purchase)
        if (lowest_price == 0 && isBookable) {
            txtPriceTitle.setVisibility(View.VISIBLE);
            txtPriceFill.setVisibility(View.VISIBLE);
            txtPriceFill.setText(getResources().getString(R.string.free));

        } else if (lowest_price > 0 && isBookable) {
            txtPriceTitle.setVisibility(View.VISIBLE);
            txtPriceFill.setVisibility(View.VISIBLE);
            txtPriceTitle.setShadowLayer(1.6f, 1.5f, 1.3f, getResources().getColor(android.R.color.black));
            txtPriceFill.setShadowLayer(1.6f, 1.5f, 1.3f, getResources().getColor(android.R.color.black));
            try {
                //String str = String.format(Locale.US, "Rp %,d", lowest_price);
                String str = StringUtility.getRupiahFormat(lowest_price + "");
                txtPriceFill.setText(str);
            } catch (Exception e) {
                Utils.d(TAG, "exception " + e.toString());
            }
        } else {
            txtPriceTitle.setVisibility(View.GONE);
            txtPriceFill.setVisibility(View.GONE);
        }
    }

    public void onEvent(ExceptionModel message) {
        if (message.getFrom().equals(Utils.FROM_EVENT_DETAIL)) {
            if (isActive()) {
                Toast.makeText(App.getInstance(), message.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }
        } else {
            if (isActive()) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (!message.getMessage().equals(Utils.RESPONSE_FAILED + " " + "empty data")) {
                    Toast.makeText(EventDetailActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    /*private void populateTags() {
        populateTags(eventDetail.getTags());
    }

    private void populateTags(ArrayList<String> tags)
    {
        if(flowLayout.getChildCount()>0){
            flowLayout.removeAllViews();
        }

        final LayoutInflater inflater = super.getLayoutInflater();
        for (String tag : tags) {
            final View view = inflater.inflate(R.layout.item_event_tag_detail, this.flowLayout, false);
            final TextView textView = (TextView) view.findViewById(R.id.txtTag);
            this.flowLayout.addView(view);
            textView.setText(tag);
        }

        if (tags.size() > 2) {
            // hack (buggy flow layout always missing 1 last item).
            final View view = inflater.inflate(R.layout.item_event_tag_detail, this.flowLayout, false);
            final TextView textView = (TextView) view.findViewById(R.id.txtTag);
            this.flowLayout.addView(view);
            textView.setText("");
            view.setVisibility(View.INVISIBLE);
        }
    }*/

    @SuppressWarnings("unused")
    @OnClick(R.id.guestView)
    void guestViewOnClick() {
        if ((!this.swipeRefresh.isRefreshing()) && (this.eventDetail != null))
            super.startActivityForResult(new Intent(this, EventGuestActivity.class).putExtra(EventDetailModel.Data.EventDetail.class.getName(), this.eventDetail), 0);
    }

    /*@SuppressWarnings("unused")
    @OnClick(R.id.layoutSeeGuests)
    void layoutSeeGuestsOnClick() { this.guestViewOnClick(); }*/

    @SuppressWarnings("unused")
    @OnClick(R.id.layoutAddress)
    void layoutAddressOnClick() {
        if (this.eventDetail != null) {
            final Uri uri = Uri.parse(String.format("http://maps.google.com/maps?daddr=%f,%f", Float.parseFloat(this.eventDetail.getVenue().getLat()), Float.parseFloat(this.eventDetail.getVenue().getLon())));
            super.startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, uri), this.eventDetail.getVenue_name()));
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.img_love)
    void loveOnClick() {
        //if (canClickLike) {
        if (imgLove.isSelected()) {
            imgLove.setSelected(false);
            if (count_like_new > 0) {
                count_like_new = count_like_new - 1;
            }
            App.getInstance().trackMixPanelViewEventDetail("Like Event Detail", eventDetail);
            //actionLike(Utils.ACTION_LIKE_NO);
        } else {
            imgLove.setSelected(true);
            count_like_new = count_like_new + 1;
            //actionLike(Utils.ACTION_LIKE_YES);

            App.getInstance().trackMixPanelViewEventDetail("Unlike Event Detail", eventDetail);
        }
        txtCountLike.setText(String.valueOf(count_like_new));
        canClickLike = false;
        TooltipsManager.setCanShowTooltips(TooltipsManager.TOOLTIP_LIKE, false);
        setLike();
        runBackgroundLike();
        //}

    }

    private void runBackgroundLike() {
        if (timerLike != null) {
            timerLike.cancel();
        }
        timerLike = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!eventDetail.is_liked() && imgLove.isSelected()) {
                    actionLike(Utils.ACTION_LIKE_YES);
                } else if (eventDetail.is_liked() && !imgLove.isSelected()) {
                    actionLike(Utils.ACTION_LIKE_NO);
                } else {
                    //do nothing
                }
            }
        };
        timerLike.schedule(timerTask, 3000);
    }

    private void runInvite() {
        if (timerInvite == null) {
            timerInvite = new Timer();
            timerTaskInvite = new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(EventDetailActivity.this, InviteFriendsActivity.class));
                }
            };
            timerInvite.schedule(timerTaskInvite, 1 * 60 * 60 * 1000);
        } else {
            Log.d("timer already", "yes");
        }
    }

    @OnClick(R.id.btnBook)
    @SuppressWarnings("unused")
    void btnBookOnClick() {
        if (this.eventDetail != null) {
            final Intent intent = new Intent();
            App.getInstance().trackMixPanelEvent("Fulfillment Request");

            if (StringUtility.isEquals(EventManager.FullfillmentTypes.PHONE_NUMBER, this.eventDetail.getFullfillment_type(), true)) {
                intent.setData(Uri.fromParts("tel", this.eventDetail.getFullfillment_value(), null));
                intent.setAction(Intent.ACTION_DIAL);
            } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.LINK, this.eventDetail.getFullfillment_type(), true)) {
                intent.setData(Uri.parse(this.eventDetail.getFullfillment_value()));
                intent.setAction(Intent.ACTION_VIEW);
            } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.RESERVATION, this.eventDetail.getFullfillment_type(), true)) {
                intent.setData(Uri.parse(this.eventDetail.getFullfillment_value()));
                intent.setAction(Intent.ACTION_VIEW);
            } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.PURCHASE, this.eventDetail.getFullfillment_type(), true)) {
                intent.setData(Uri.parse(this.eventDetail.getFullfillment_value()));
                intent.setAction(Intent.ACTION_VIEW);
            }


            if (!TextUtils.isEmpty(intent.getAction())) {
                if (StringUtility.isEquals(EventManager.FullfillmentTypes.LINK, this.eventDetail.getFullfillment_type(), true)) {
                    String value = this.eventDetail.getFullfillment_value();
                    /*if(Patterns.WEB_URL.matcher(this.eventDetail.getFullfillment_value()).matches()){
                        super.startActivity(Intent.createChooser(intent, super.getString(R.string.book_now)));
                    }*/
                    if (Patterns.WEB_URL.matcher(value).matches() && (value.startsWith("http://") || value.startsWith("https://"))) {
                        super.startActivity(Intent.createChooser(intent, super.getString(R.string.book_now)));
                    }
                } else {
                    super.startActivity(Intent.createChooser(intent, super.getString(R.string.book_now)));
                }

            } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.NONE, this.eventDetail.getFullfillment_type(), true))
                Toast.makeText(this, R.string.no_fullfillment, Toast.LENGTH_SHORT).show();
            else if (StringUtility.isEquals(EventManager.FullfillmentTypes.TICKET, this.eventDetail.getFullfillment_type(), true)) {
                Intent i = new Intent(this, ProductListActivity.class);
                i.putExtra(Common.FIELD_EVENT_ID, this.eventDetail.get_id());
                i.putExtra(eventDetail.getClass().getName(), eventDetail);
                if (event_pics.size() > 0)
                    i.putExtra("images", event_pics.get(0));
                startActivity(i);
            } else
                Toast.makeText(this, R.string.book_error, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.img_share)
    void shareOnClick() {
        try {
            this.shareEvent();
            TooltipsManager.setCanShowTooltips(TooltipsManager.TOOLTIP_SHARE, false);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.lin_see_all_guest)
    void seeGuestOnClick() {
        this.guestViewOnClick();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.rel_desc_more)
    void moreDescOnClick() {
        relDescMore.setVisibility(View.GONE);
        if (event_description != null) {
            event_description = event_description.replace("\n", "<br />");
            txtDescription.setText(Html.fromHtml(event_description));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK) && (super.isActive()))
            this.onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.unregisterReceiver(this.guestInvitedReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (file != null)
            file.delete();
    }

    private void actionLike(final String action) {
        EventManager.loaderLikeEvent(event_id, AccessToken.getCurrentAccessToken().getUserId(), action, new EventManager.OnResponseEventListener() {
            @Override
            public void onSuccess(Object object) {
                //do nothing
                canClickLike = true;
            }

            @Override
            public void onFailure(int responseCode, String message) {
                //rollback like value
                canClickLike = true;
                if (action.equals(Utils.ACTION_LIKE_YES)) {
                    imgLove.setSelected(false);
                    txtCountLike.setText(String.valueOf(count_like));
                } else {
                    imgLove.setSelected(true);
                    txtCountLike.setText(String.valueOf(count_like));
                }
                count_like_new = count_like;
            }
        });
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            try {
                this.shareEvent();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else if (item.getItemId() == R.id.home) {
            redirectToHome();
        }
        return super.onOptionsItemSelected(item);
    }*/

    private File createFile(final Bitmap bmp) {
        //create a file to write bitmap data
        try {
            //File f = new File(this.getCacheDir(), "temp.png");
            File f = new File(Environment.getExternalStorageDirectory()
                    , /*venueName +*/ "event.png");
            if (!f.exists()) {
                f.createNewFile();
                //Convert bitmap to byte array
                Bitmap bitmap = bmp;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                return f;
            } else {
                return f;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void shareEvent() throws UnsupportedEncodingException {
        if (this.shareLinkModel != null) {
            App.getInstance().trackMixPanelViewEventDetail("Share Event", eventDetail);
            /*Utils.d(TAG, Build.VERSION.SDK_INT + " " + Build.VERSION_CODES.LOLLIPOP_MR1);
            if (android.os.Build.VERSION.SDK_INT
                    < Build.VERSION_CODES.LOLLIPOP_MR1) {
                String share = String.format("%s\n\n%s", shareLinkModel.getMessage(), shareLinkModel.getUrl());
                Intent i = new Intent(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, share)
                        .putExtra(Intent.EXTRA_SUBJECT, "Lets Go Out With Jiggie");
                i.setType("text/plain");
                super.startActivity(Intent.createChooser
                        (i, super.getString(R.string.share)
                        //,)
                        ));
            }
            else {
                String share = String.format("%s\n\n%s", shareLinkModel.getMessage(), shareLinkModel.getUrl());
                super.startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, share).putExtra(Intent.EXTRA_SUBJECT, "Lets Go Out With Jiggie").setType("text/plain"), super.getString(R.string.share)));

            }*/

            final String share = String.format("%s\n\n%s", shareLinkModel.getMessage(), shareLinkModel.getUrl());

            Observable<File> myObservable
                    = Observable.create(new Observable.OnSubscribe<File>() {
                @Override
                public void call(Subscriber subscriber) {
                    try {
                        Bitmap bmp = Glide.with(EventDetailActivity.this)
                                .load(eventDetail.getPhotos()
                                        .get(0)).asBitmap().into(200, 200).get();
                        file = createFile(bmp);
                        subscriber.onNext(file);
                    } catch (InterruptedException e) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            });

            myObservable
                    .subscribeOn(Schedulers.newThread()) // Create a new Thread
                    .observeOn(AndroidSchedulers.mainThread()) // Use the UI thread
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            //view.setText(view.getText() + "\n" + s); // Change a View
                            Intent i = new Intent(Intent.ACTION_SEND)
                                    .putExtra(Intent.EXTRA_TEXT, share)
                                    .putExtra(Intent.EXTRA_SUBJECT, "Lets Go Out With Jiggie");

                            if (file != null && file.exists()) {
                                i.putExtra(Intent.EXTRA_STREAM,
                                        Uri.parse("file:" + file.getAbsolutePath()));
                            }

                            i.setType("text/plain");
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();

                            EventDetailActivity.this.startActivity(Intent.createChooser
                                    (i, EventDetailActivity.this.getString(R.string.share)));
                        }
                    });

            /*super.startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, share).
                    putExtra(Intent.EXTRA_STREAM, Uri.parse(eventDetail.getPhotos().get(0))).
                    putExtra(Intent.EXTRA_SUBJECT, "Lets Go Out With Jiggie").setType("**///*"), super.getString(R.string.share)));*/

            /*Glide.with(this).load(eventDetail.getPhotos().get(0)).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(fragment.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    super.getView().setImageDrawable(circularBitmapDrawable);
                }
            });*/
        } else {
            if (eventDetail.getVenue_name() != null) {
                progressDialog = App.showProgressDialog(this);
                ShareManager.loaderShareEvent(eventDetail.get_id()
                        , AccessToken.getCurrentAccessToken().getUserId()
                        , URLEncoder.encode(eventDetail.getVenue_name(), "UTF-8"));
            }
        }
    }

    public void onEvent(ShareLinkModel message) throws UnsupportedEncodingException {
        if (isActive()) {
            /*String share = String.format("%s\n\n%s", message.getMessage(), message.getUrl());
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, share).setType("text/plain"), getString(R.string.share)));
            App.getInstance().trackMixPanelEvent("Share Event");
            progressDialog.dismiss();
            shareLinkModel = message;*/

            //wandy 15-02-2016
            shareLinkModel = message;
            this.shareEvent();

        }
    }

    private BroadcastReceiver guestInvitedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isActive()) return;
            if (eventDetail == null) return;
            final GuestModel.Data.GuestInterests guest = intent.getParcelableExtra(GuestModel.Data.GuestInterests.class.getName());

            ArrayList<EventDetailModel.Data.EventDetail.GuestViewed> guestArr = eventDetail.getGuests_viewed();

            int size = guestArr.size();
            final int length = size;

            for (int i = 0; i < length; i++) {
                final GuestModel.Data.GuestInterests changed = GuestManager.dataGuestInterest.get(i);
                if (changed.getFb_id().equals(guest.getFb_id())) {
                    changed.setIs_connected(guest.is_connected());
                    changed.setIs_invited(guest.is_invited());
                    break;
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (count_like != count_like_new) {
            //i.putExtra(Utils.TAG_ISREFRESH, true);
            Utils.isRefreshDetail = true;
            Utils.event_id_refresh = event_id;
            Utils.count_like_new = count_like_new;
            for (int j = 0; j < EventManager.events.size(); j++) {
                if (EventManager.events.get(j).get_id().equals(event_id)) {
                    EventManager.events.get(j).setLikes(count_like_new);
                }
            }

            /*for(int j=0;j<EventManager.todayEvents.size();j++){
                if(EventManager.todayEvents.get(j).get_id().equals(event_id)){
                    EventManager.todayEvents.get(j).setLikes(count_like_new);
                }
            }

            for(int j=0;j<EventManager.tomorrowEvents.size();j++){
                if(EventManager.tomorrowEvents.get(j).get_id().equals(event_id)){
                    EventManager.tomorrowEvents.get(j).setLikes(count_like_new);
                }
            }

            for(int j=0;j<EventManager.upcomingEvents.size();j++){
                if(EventManager.upcomingEvents.get(j).get_id().equals(event_id)){
                    EventManager.upcomingEvents.get(j).setLikes(count_like_new);
                }
            }*/
        }

        startActivity(i);
        finish();
    }
}