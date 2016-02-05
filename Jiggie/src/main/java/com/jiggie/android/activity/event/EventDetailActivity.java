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
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.FlowLayout;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ImagePagerIndicatorAdapter;
import com.jiggie.android.component.volley.SimpleVolleyRequestListener;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.manager.GuestManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Event;
import com.jiggie.android.model.EventDetail;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.Guest;
import com.jiggie.android.model.GuestModel;
import com.jiggie.android.model.Setting;
import com.jiggie.android.model.ShareLink;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import it.sephiroth.android.library.widget.HListView;

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

    @Bind(R.id.imageGuest1) ImageView imageGuest1;
    @Bind(R.id.imageGuest2) ImageView imageGuest2;
    @Bind(R.id.imageGuest3) ImageView imageGuest3;
    @Bind(R.id.imageGuest4) ImageView imageGuest4;

    private ImagePagerIndicatorAdapter imagePagerIndicatorAdapter;
    private ImageView[] imageGuests;
    private ShareLink shareLink;
    private GoogleMap map;

    private EventModel.Data.Events currentEvent;
    private EventDetailModel.Data.EventDetail eventDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_event_detail);
        super.bindView();

        EventBus.getDefault().register(this);

        this.currentEvent = super.getIntent().getParcelableExtra(EventModel.Data.Events.class.getName());
        this.txtVenue.setText(this.currentEvent.getVenue_name());
        this.txtGuestCounter.setVisibility(View.GONE);
        this.appBarLayout.addOnOffsetChangedListener(this);
        this.swipeRefresh.setOnRefreshListener(this);
        this.scrollView.setVisibility(View.INVISIBLE);
        this.collapsingToolbarLayout.setTitleEnabled(false);
        this.imageGuests = new ImageView[]{imageGuest1, imageGuest2, imageGuest3, imageGuest4};

        super.setToolbarTitle(this.currentEvent.getTitle().toUpperCase(), true);
        final FragmentManager fragmentManager = super.getSupportFragmentManager();
        ((SupportMapFragment)fragmentManager.findFragmentById(R.id.map)).getMapAsync(this);

        this.imagePagerIndicatorAdapter = new ImagePagerIndicatorAdapter(super.getSupportFragmentManager(), this.imageViewPager);
        this.imagePagerIndicator.setAdapter(this.imagePagerIndicatorAdapter.getIndicatorAdapter());

        super.registerReceiver(this.guestInvitedReceiver, new IntentFilter(super.getString(R.string.broadcastGuestInvited)));
        App.getInstance().trackMixPanelEvent("View Event Details");


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final String url = String.format("guest/events/viewed/%s/%s", AccessToken.getCurrentAccessToken().getUserId(), this.currentEvent.get_id());
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
        this.swipeRefresh.setRefreshing(true);

        EventManager.loaderEventDetail(this.currentEvent.get_id(), AccessToken.getCurrentAccessToken().getUserId(), AccountManager.loadSetting().getData().getGender_interest());
    }

    public void onEvent(EventDetailModel message){
        try {
            if (!isActive())
                return;

            ArrayList<EventDetailModel.Data.EventDetail.GuestViewed> guestArr = message.getData().getEvents_detail().getGuests_viewed();

            int size = guestArr.size();

            int guestCount = size;
            final double latt = Double.parseDouble(message.getData().getEvents_detail().getVenue().getLat());
            final double lon = Double.parseDouble(message.getData().getEvents_detail().getVenue().getLon());
            final LatLng lat = new LatLng(latt, lon);

            ArrayList<String> photoArr = message.getData().getEvents_detail().getPhotos();
            String[] photo = new String[photoArr.size()];
            photo = photoArr.toArray(photo);

            imagePagerIndicatorAdapter.setImages(photo);
            txtDescription.setText(message.getData().getEvents_detail().getDescription());
            txtAddress.setText(message.getData().getEvents_detail().getVenue().getAddress());
            txtGuestCounter.setText(String.format("+%s", guestCount - imageGuests.length));
            txtGuestCounter.setVisibility(guestCount > imageGuests.length ? View.VISIBLE : View.GONE);
            txtGuestCount.setText(getResources().getQuantityString(R.plurals.guest_count, guestCount, guestCount));

            if (guestCount > 0) {
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
            }

            btnBook.setVisibility(StringUtility.isEquals(EventDetail.FullfillmentTypes.NONE, message.getData().getEvents_detail().getFullfillment_type(), true) ? View.GONE : View.VISIBLE);
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

            if (StringUtility.isEquals(EventDetail.FullfillmentTypes.PHONE_NUMBER, message.getData().getEvents_detail().getFullfillment_type(), true)) {
                txtExternalSite.setVisibility(View.GONE);
                txtBookNow.setText(R.string.call);
            } else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.RESERVATION, message.getData().getEvents_detail().getFullfillment_type(), true)) {
                txtExternalSite.setVisibility(View.GONE);
                txtBookNow.setText(R.string.reserve);
            } else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.PURCHASE, message.getData().getEvents_detail().getFullfillment_type(), true)) {
                txtExternalSite.setVisibility(View.GONE);
                txtBookNow.setText(R.string.purchase);
            }
        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }
    }

    public void onEvent(ExceptionModel message){
        if (isActive()) {
            Toast.makeText(App.getInstance(), message.getMessage(), Toast.LENGTH_SHORT).show();
            swipeRefresh.setRefreshing(false);
        }
    }

    private void populateTags() {
        final LayoutInflater inflater = super.getLayoutInflater();
        for (String tag : this.eventDetail.getTags()) {
            final View view = inflater.inflate(R.layout.item_event_tag_detail, this.flowLayout, false);
            final TextView textView = (TextView) view.findViewById(R.id.txtTag);
            this.flowLayout.addView(view);
            textView.setText(tag);
        }

        if (this.eventDetail.getTags().size() > 2) {
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
            final Uri uri = Uri.parse(String.format("http://maps.google.com/maps?daddr=%f,%f", this.eventDetail.getVenue().getLat(), this.eventDetail.getVenue().getLon()));
            super.startActivity(Intent.createChooser(new Intent(android.content.Intent.ACTION_VIEW, uri), this.eventDetail.getVenue_name()));
        }
    }

    @OnClick(R.id.btnBook)
    @SuppressWarnings("unused")
    void btnBookOnClick() {
        if (this.eventDetail != null) {
            final Intent intent = new Intent();
            App.getInstance().trackMixPanelEvent("Fulfillment Request");

            if (StringUtility.isEquals(EventDetail.FullfillmentTypes.PHONE_NUMBER, this.eventDetail.getFullfillment_type(), true)) {
                intent.setData(Uri.fromParts("tel", this.eventDetail.getFullfillment_value(), null));
                intent.setAction(Intent.ACTION_DIAL);
            } else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.LINK, this.eventDetail.getFullfillment_type(), true)) {
                intent.setData(Uri.parse(this.eventDetail.getFullfillment_value()));
                intent.setAction(Intent.ACTION_VIEW);
            } else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.RESERVATION, this.eventDetail.getFullfillment_type(), true)) {
                intent.setData(Uri.parse(this.eventDetail.getFullfillment_value()));
                intent.setAction(Intent.ACTION_VIEW);
            } else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.PURCHASE, this.eventDetail.getFullfillment_type(), true)) {
                intent.setData(Uri.parse(this.eventDetail.getFullfillment_value()));
                intent.setAction(Intent.ACTION_VIEW);
            }
            if (!TextUtils.isEmpty(intent.getAction()))
                super.startActivity(Intent.createChooser(intent, super.getString(R.string.book_now)));
            else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.NONE, this.eventDetail.getFullfillment_type(), true))
                Toast.makeText(this, R.string.no_fullfillment, Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            try {
                this.shareEvent();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareEvent() throws UnsupportedEncodingException {
        if (this.shareLink != null) {
            App.getInstance().trackMixPanelEvent("Share Event");
            super.startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, this.shareLink.toString()).setType("text/plain"), super.getString(R.string.share)));
        } else {
            final ProgressDialog progressDialog = App.showProgressDialog(this);
            final String url = String.format("invitelink?from_fb_id=%s&type=event&os=android&venue_name=%s&event_id=%s",
                    URLEncoder.encode(AccessToken.getCurrentAccessToken().getUserId(), "UTF-8"),
                    URLEncoder.encode(this.eventDetail.getVenue_name(), "UTF-8"),
                    URLEncoder.encode(this.eventDetail.getVenue_id(), "UTF-8"));

            VolleyHandler.getInstance().createVolleyRequest(url, new VolleyRequestListener<ShareLink, JSONObject>() {
                @Override
                public ShareLink onResponseAsync(JSONObject jsonObject) { return new ShareLink(jsonObject); }

                @Override
                public void onResponseCompleted(ShareLink value) {
                    if (isActive()) {
                        startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, value.toString()).setType("text/plain"), getString(R.string.share)));
                        App.getInstance().trackMixPanelEvent("Share Event");
                        progressDialog.dismiss();
                        shareLink = value;
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (isActive()) {
                        progressDialog.dismiss();
                        Toast.makeText(EventDetailActivity.this, App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    }
                }
            });
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


}
