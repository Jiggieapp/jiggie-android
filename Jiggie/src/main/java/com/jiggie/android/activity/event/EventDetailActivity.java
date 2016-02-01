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
import com.android.jiggie.R;
import com.jiggie.android.component.FlowLayout;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ImagePagerIndicatorAdapter;
import com.jiggie.android.component.volley.SimpleVolleyRequestListener;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.model.Event;
import com.jiggie.android.model.EventDetail;
import com.jiggie.android.model.Guest;
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

import butterknife.Bind;
import butterknife.OnClick;
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
    private EventDetail eventDetail;
    private ShareLink shareLink;
    private Event currentEvent;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_event_detail);
        super.bindView();

        this.currentEvent = super.getIntent().getParcelableExtra(Event.class.getName());
        this.txtVenue.setText(this.currentEvent.getVenueName());
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
        final String url = String.format("guest/events/viewed/%s/%s", AccessToken.getCurrentAccessToken().getUserId(), this.currentEvent.getId());
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
        final String url = String.format("event/details/%s/%s/%s", this.currentEvent.getId(), AccessToken.getCurrentAccessToken().getUserId(), Setting.getCurrentSetting().getGenderInterestString());

        VolleyHandler.getInstance().createVolleyRequest(url, new VolleyRequestListener<EventDetail, JSONObject>() {
            @Override
            public EventDetail onResponseAsync(JSONObject jsonObject) { return new EventDetail(jsonObject); }

            @Override
            public void onResponseCompleted(EventDetail value) {
                try {
                    if (!isActive())
                        return;

                    final Guest[] guests = value.getGuests();
                    int guestCount = guests == null ? 0 : guests.length;
                    final LatLng lat = new LatLng(value.getVenue().getLatitude(), value.getVenue().getLongitude());
                    imagePagerIndicatorAdapter.setImages(value.getPhotos());
                    txtDescription.setText(value.getDescrption());
                    txtAddress.setText(value.getVenue().getAddress());
                    txtGuestCounter.setText(String.format("+%s", guestCount - imageGuests.length));
                    txtGuestCounter.setVisibility(guestCount > imageGuests.length ? View.VISIBLE : View.GONE);
                    txtGuestCount.setText(getResources().getQuantityString(R.plurals.guest_count, guestCount, guestCount));

                    if (guestCount > 0) {
                        final int width = imageGuest1.getWidth() * 2;
                        guestCount = guestCount > imageGuests.length ? imageGuests.length : guestCount;

                        for (int i = 0; i < guestCount; i++) {
                            final String url = App.getFacebookImage(guests[i].getFacebookId(), width);
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

                    btnBook.setVisibility(StringUtility.isEquals(EventDetail.FullfillmentTypes.NONE, value.getFullfillmentType(), true) ? View.GONE : View.VISIBLE);
                    map.addMarker(new MarkerOptions().position(lat).title(value.getVenueName()));
                    layoutGuests.setVisibility(guestCount > 0 ? View.VISIBLE : View.GONE);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));
                    scrollView.setVisibility(View.VISIBLE);
                    txtDate.setText(value.getSimpleDate());
                    swipeRefresh.setRefreshing(false);
                    eventDetail = value;
                    invalidateOptionsMenu();
                    populateTags();

                    if (StringUtility.isEquals(EventDetail.FullfillmentTypes.PHONE_NUMBER, value.getFullfillmentType(), true)) {
                        txtExternalSite.setVisibility(View.GONE);
                        txtBookNow.setText(R.string.call);
                    } else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.RESERVATION, value.getFullfillmentType(), true)) {
                        txtExternalSite.setVisibility(View.GONE);
                        txtBookNow.setText(R.string.reserve);
                    } else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.PURCHASE, value.getFullfillmentType(), true)) {
                        txtExternalSite.setVisibility(View.GONE);
                        txtBookNow.setText(R.string.purchase);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(App.getErrorMessage(e), e);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isActive()) {
                    Toast.makeText(App.getInstance(), App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void populateTags() {
        final LayoutInflater inflater = super.getLayoutInflater();
        for (String tag : this.eventDetail.getTags()) {
            final View view = inflater.inflate(R.layout.item_event_tag_detail, this.flowLayout, false);
            final TextView textView = (TextView) view.findViewById(R.id.txtTag);
            this.flowLayout.addView(view);
            textView.setText(tag);
        }

        if (this.eventDetail.getTags().length > 2) {
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
            super.startActivityForResult(new Intent(this, EventGuestActivity.class).putExtra(Guest.class.getName(), this.eventDetail.getGuests()), 0);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.layoutSeeGuests)
    void layoutSeeGuestsOnClick() { this.guestViewOnClick(); }

    @SuppressWarnings("unused")
    @OnClick(R.id.layoutAddress)
    void layoutAddressOnClick() {
        if (this.eventDetail != null) {
            final Uri uri = Uri.parse(String.format("http://maps.google.com/maps?daddr=%f,%f", this.eventDetail.getVenue().getLatitude(), this.eventDetail.getVenue().getLongitude()));
            super.startActivity(Intent.createChooser(new Intent(android.content.Intent.ACTION_VIEW, uri), this.eventDetail.getVenueName()));
        }
    }

    @OnClick(R.id.btnBook)
    @SuppressWarnings("unused")
    void btnBookOnClick() {
        if (this.eventDetail != null) {
            final Intent intent = new Intent();
            App.getInstance().trackMixPanelEvent("Fulfillment Request");

            if (StringUtility.isEquals(EventDetail.FullfillmentTypes.PHONE_NUMBER, this.eventDetail.getFullfillmentType(), true)) {
                intent.setData(Uri.fromParts("tel", this.eventDetail.getFieldFullfillmentValue(), null));
                intent.setAction(Intent.ACTION_DIAL);
            } else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.LINK, this.eventDetail.getFullfillmentType(), true)) {
                intent.setData(Uri.parse(this.eventDetail.getFieldFullfillmentValue()));
                intent.setAction(Intent.ACTION_VIEW);
            } else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.RESERVATION, this.eventDetail.getFullfillmentType(), true)) {
                intent.setData(Uri.parse(this.eventDetail.getFieldFullfillmentValue()));
                intent.setAction(Intent.ACTION_VIEW);
            } else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.PURCHASE, this.eventDetail.getFullfillmentType(), true)) {
                intent.setData(Uri.parse(this.eventDetail.getFieldFullfillmentValue()));
                intent.setAction(Intent.ACTION_VIEW);
            }
            if (!TextUtils.isEmpty(intent.getAction()))
                super.startActivity(Intent.createChooser(intent, super.getString(R.string.book_now)));
            else if (StringUtility.isEquals(EventDetail.FullfillmentTypes.NONE, this.eventDetail.getFullfillmentType(), true))
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
                    URLEncoder.encode(this.eventDetail.getVenueName(), "UTF-8"),
                    URLEncoder.encode(this.eventDetail.getId(), "UTF-8"));

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
            final Guest guest = intent.getParcelableExtra(Guest.class.getName());
            final Guest[] guests = eventDetail.getGuests();
            final int length = guests.length;

            for (int i = 0; i < length; i++) {
                final Guest changed = guests[i];
                if (changed.getFacebookId().equals(guest.getFacebookId())) {
                    changed.setConnected(guest.isConnected());
                    changed.setInvited(guest.isInvited());
                    break;
                }
            }
        }
    };
}
