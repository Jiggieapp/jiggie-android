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
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.jiggie.android.activity.ecommerce.ProductListActivity;
import com.jiggie.android.component.FlowLayout;
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
import com.jiggie.android.manager.ShareManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.GuestModel;
import com.jiggie.android.model.ShareLinkModel;

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
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
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
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.imagePagerIndicator) HListView imagePagerIndicator;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.txtExternalSite) TextView txtExternalSite;
    @Bind(R.id.txtGuestCounter) TextView txtGuestCounter;
    @Bind(R.id.imageViewPager) ViewPager imageViewPager;
    @Bind(R.id.txtDescription) TextView txtDescription;
    @Bind(R.id.scrollView) NestedScrollView scrollView;
    @Bind(R.id.txtGuestCount) TextView txtGuestCount;
    @Bind(R.id.appBar) AppBarLayout appBarLayout;
    @Bind(R.id.flowLayout) FlowLayout flowLayout;
    @Bind(R.id.layoutGuests) View layoutGuests;
    @Bind(R.id.txtBookNow) TextView txtBookNow;
    @Bind(R.id.txtAddress) TextView txtAddress;
    @Bind(R.id.txtVenue) TextView txtVenue;
    @Bind(R.id.txtDate) TextView txtDate;
    @Bind(R.id.btnBook) View btnBook;
    @Bind(R.id.element_containers)
    LinearLayout elementContainers;
    @Bind(R.id.element_containers2)
    LinearLayout elementContainers2;

    @Bind(R.id.imageGuest1) ImageView imageGuest1;
    @Bind(R.id.imageGuest2) ImageView imageGuest2;
    @Bind(R.id.imageGuest3) ImageView imageGuest3;
    @Bind(R.id.imageGuest4) ImageView imageGuest4;

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

    ProgressDialog progressDialog;
    public static final String TAG = EventDetailActivity.class.getSimpleName();
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_event_detail);
        super.bindView();

        EventBus.getDefault().register(this);

        Intent a = super.getIntent();
        event_id = a.getStringExtra(Common.FIELD_EVENT_ID);
        event_name = a.getStringExtra(Common.FIELD_EVENT_NAME);
        event_venue_name = a.getStringExtra(Common.FIELD_EVENT_VENUE_NAME);
        event_tags = a.getStringArrayListExtra(Common.FIELD_EVENT_TAGS);
        event_day = a.getStringExtra(Common.FIELD_EVENT_DAY);
        event_end = a.getStringExtra(Common.FIELD_EVENT_DAY_END);
        event_pics = a.getStringArrayListExtra(Common.FIELD_EVENT_PICS);
        event_description = a.getStringExtra(Common.FIELD_EVENT_DESCRIPTION);

        this.imagePagerIndicatorAdapter = new ImagePagerIndicatorAdapter(super.getSupportFragmentManager(), this.imageViewPager);
        this.imagePagerIndicator.setAdapter(this.imagePagerIndicatorAdapter.getIndicatorAdapter());

        if(a != null)
        {
            this.txtVenue.setText("");

            if(event_venue_name != null)
                this.txtVenue.setText(event_venue_name);

            if(event_tags != null)
            {
                populateTags(event_tags);
            }


            if(event_name != null)
                super.setToolbarTitle(event_name, true);
            else super.setToolbarTitle("", true);

            if(event_day != null && event_name != null)
            {
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

            if(event_pics != null)
                fillPhotos(event_pics);

            if(event_description != null)
                txtDescription.setText(event_description);

            scrollView.setVisibility(View.VISIBLE);
            elementContainers.setVisibility(View.INVISIBLE);
            elementContainers2.setVisibility(View.INVISIBLE);

            if(event_id == null || event_id.equalsIgnoreCase("null")){
                //wandy 17-03-2016
                //contoh working scheme
                //jiggie://event_detail/<event_id>&af_chrome_lp=true&af_deeplink=true&app-id=1630402100&campaign=None&media_source=App_Invite
                //end of contoh working scheme

                String dataString = a.getDataString();
                final boolean isJiggieUrl = isJiggieUrl(dataString);
                if(isJiggieUrl)
                {
                    Uri inputUri = Uri.parse(dataString);
                    /*event_id = inputUri.getQueryParameter("af_sub2");
                    for(String segment : inputUri.getPathSegments())
                    {
                        Utils.d(TAG, "segment " + segment);
                    }*/
                    event_id = inputUri.getPathSegments().get(0);
                }
                else {
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
        }

        if(event_name!=null){
            super.setToolbarTitle(event_name.toUpperCase(), true);
        }

        this.appBarLayout.addOnOffsetChangedListener(this);
        this.swipeRefresh.setOnRefreshListener(this);

        this.txtGuestCounter.setVisibility(View.GONE);
        //this.scrollView.setVisibility(View.INVISIBLE);
        this.collapsingToolbarLayout.setTitleEnabled(false);
        this.imageGuests = new ImageView[]{imageGuest1, imageGuest2, imageGuest3, imageGuest4};

        final FragmentManager fragmentManager = super.getSupportFragmentManager();
        ((SupportMapFragment)fragmentManager.findFragmentById(R.id.map)).getMapAsync(this);

        super.registerReceiver(this.guestInvitedReceiver, new IntentFilter(super.getString(R.string.broadcastGuestInvited)));

        if(file!= null && file.exists())
            file.delete();
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
        VolleyHandler.getInstance().createVolleyRequest(url, new SimpleVolleyRequestListener<Object, JSONObject>(){
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.eventDetail != null)
            super.getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    private void fillPhotos(ArrayList<String> photoArr)
    {
        String[] photo = new String[photoArr.size()];
        photo = photoArr.toArray(photo);

        imagePagerIndicatorAdapter.setImages(photo);
    }

    public void onEvent(EventDetailModel message) {
        if (message.getFrom().equalsIgnoreCase(TAG)) {
            try {
                eventDetail = message.getData().getEvents_detail();
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
                final double latt = Double.parseDouble(message.getData().getEvents_detail().getVenue().getLat());
                final double lon = Double.parseDouble(message.getData().getEvents_detail().getVenue().getLon());
                final LatLng lat = new LatLng(latt, lon);

                fillPhotos(message.getData().getEvents_detail().getPhotos());

                txtDescription.setText(message.getData().getEvents_detail().getDescription());
                txtAddress.setText(message.getData().getEvents_detail().getVenue().getAddress());
                txtGuestCounter.setText(String.format("+%s", guestCount - imageGuests.length));
                txtGuestCounter.setVisibility(guestCount > imageGuests.length ? View.VISIBLE : View.GONE);
                txtGuestCount.setText(getResources().getQuantityString(R.plurals.guest_count, guestCount, guestCount));

                if (guestCount > 0) {
                    Intent intent = new Intent(SocialTabFragment.TAG);
                    sendBroadcast(intent);

                    final int width = imageGuest1.getWidth() * 2;
                    guestCount = guestCount > imageGuests.length ? imageGuests.length : guestCount;

                    for (int i = 0; i < guestCount; i++) {
                        final String url = App.getFacebookImage(guestArr.get(i).getFb_id(), width);
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

                map.addMarker(new MarkerOptions().position(lat).title(message.getData().getEvents_detail().getVenue_name()));
                layoutGuests.setVisibility(guestCount > 0 ? View.VISIBLE : View.GONE);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));
                scrollView.setVisibility(View.VISIBLE);

                final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(message.getData().getEvents_detail().getStart_datetime());
                final Date endDate = Common.ISO8601_DATE_FORMAT_UTC.parse(message.getData().getEvents_detail().getEnd_datetime());
                String simpleDate = App.getInstance().getResources().getString(R.string.event_date_format, Common.SERVER_DATE_FORMAT_ALT.format(startDate), Common.SIMPLE_12_HOUR_FORMAT.format(endDate));
                txtDate.setText(simpleDate);

                swipeRefresh.setRefreshing(false);
                eventDetail = message.getData().getEvents_detail();
                invalidateOptionsMenu();
                populateTags();

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
                    txtBookNow.setText(R.string.reserve);
                } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.PURCHASE, fullfillmentType, true)) {
                    txtExternalSite.setVisibility(View.GONE);
                    txtBookNow.setText(R.string.purchase);
                } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.TICKET, fullfillmentType, true)) {
                    txtExternalSite.setVisibility(View.GONE);
                    txtBookNow.setText(getResources().getString(R.string.purchase_ticket));
                }
                //Changed by Aga 16-2-2016
                else if (StringUtility.isEquals(EventManager.FullfillmentTypes.LINK, message.getData().getEvents_detail().getFullfillment_type(), true)) {
                    txtExternalSite.setVisibility(View.VISIBLE);
                    txtBookNow.setText(R.string.book_now);
                }
            } catch (ParseException e) {
                throw new RuntimeException(App.getErrorMessage(e), e);
            }
        }
    }

    public void onEvent(ExceptionModel message){
        if(message.getFrom().equals(Utils.FROM_EVENT_DETAIL)){
            if (isActive()) {
                Toast.makeText(App.getInstance(), message.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }
        }else{
            if (isActive()) {
                //if(progressDialog!=null&&progressDialog.isShowing()){
                progressDialog.dismiss();
                //}

                if(!message.getMessage().equals(Utils.RESPONSE_FAILED + " " + "empty data")){
                    Toast.makeText(EventDetailActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void populateTags() {
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
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.guestView)
    void guestViewOnClick() {
        if ((!this.swipeRefresh.isRefreshing()) && (this.eventDetail != null))
            super.startActivityForResult(new Intent(this, EventGuestActivity.class).putExtra(EventDetailModel.Data.EventDetail.class.getName(), this.eventDetail), 0);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.layoutSeeGuests)
    void layoutSeeGuestsOnClick() { this.guestViewOnClick(); }

    @SuppressWarnings("unused")
    @OnClick(R.id.layoutAddress)
    void layoutAddressOnClick() {
        if (this.eventDetail != null) {
            final Uri uri = Uri.parse(String.format("http://maps.google.com/maps?daddr=%f,%f", Float.parseFloat(this.eventDetail.getVenue().getLat()), Float.parseFloat(this.eventDetail.getVenue().getLon())));
            super.startActivity(Intent.createChooser(new Intent(android.content.Intent.ACTION_VIEW, uri), this.eventDetail.getVenue_name()));
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


            if (!TextUtils.isEmpty(intent.getAction())){
                if (StringUtility.isEquals(EventManager.FullfillmentTypes.LINK, this.eventDetail.getFullfillment_type(), true)) {
                    String value = this.eventDetail.getFullfillment_value();
                    /*if(Patterns.WEB_URL.matcher(this.eventDetail.getFullfillment_value()).matches()){
                        super.startActivity(Intent.createChooser(intent, super.getString(R.string.book_now)));
                    }*/
                    if(Patterns.WEB_URL.matcher(value).matches()&&(value.startsWith("http://")||value.startsWith("https://"))){
                        super.startActivity(Intent.createChooser(intent, super.getString(R.string.book_now)));
                    }
                }else{
                    super.startActivity(Intent.createChooser(intent, super.getString(R.string.book_now)));
                }

            }else if (StringUtility.isEquals(EventManager.FullfillmentTypes.NONE, this.eventDetail.getFullfillment_type(), true))
                Toast.makeText(this, R.string.no_fullfillment, Toast.LENGTH_SHORT).show();
            else if(StringUtility.isEquals(EventManager.FullfillmentTypes.TICKET, this.eventDetail.getFullfillment_type(), true))
            {
                Intent i = new Intent(this, ProductListActivity.class);
                i.putExtra(Common.FIELD_EVENT_ID, this.eventDetail.get_id());
                i.putExtra(eventDetail.getClass().getName(), eventDetail);
                startActivity(i);
            }
            else
                Toast.makeText(this, R.string.book_error, Toast.LENGTH_SHORT).show();
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
        if(file != null)
            file.delete();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            try {
                this.shareEvent();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        else if(item.getItemId() == R.id.home)
        {
            redirectToHome();
        }
        return super.onOptionsItemSelected(item);
    }

    private File createFile(final Bitmap bmp)
    {
        //create a file to write bitmap data
        try {
            //File f = new File(this.getCacheDir(), "temp.png");
            File f = new File(Environment.getExternalStorageDirectory()
                    , /*venueName +*/ "event.png");
            if(!f.exists())
            {
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
            }
            else
            {
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
                                //Utils.d(TAG, "file getabsolutepath " + file.getAbsolutePath());
                                i.putExtra(android.content.Intent.EXTRA_STREAM,
                                        Uri.parse("file:" + file.getAbsolutePath()));
                            } else {
                                //Utils.d(TAG, "file not existss");
                            }

                            i.setType("text/plain");
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();

                            EventDetailActivity.this.startActivity(Intent.createChooser
                                    (i, EventDetailActivity.this.getString(R.string.share)));
                            //file.delete();
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
            progressDialog = App.showProgressDialog(this);
            ShareManager.loaderShareEvent(eventDetail.get_id(), AccessToken.getCurrentAccessToken().getUserId(), URLEncoder.encode(eventDetail.getVenue_name(), "UTF-8"));
        }
    }

    public void onEvent(ShareLinkModel message)throws UnsupportedEncodingException{
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
        redirectToHome();
    }
}