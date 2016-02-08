package com.jiggie.android.activity.event;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.EventGuestAdapter;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.GuestManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.Guest;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.jiggie.android.model.GuestModel;
import com.jiggie.android.model.SettingModel;

import org.json.JSONObject;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by rangg on 13/11/2015.
 */
public class EventGuestActivity extends ToolbarActivity implements ViewTreeObserver.OnGlobalLayoutListener, EventGuestAdapter.GuestConnectListener {
    @Bind(R.id.recycler) RecyclerView recyclerView;

    private EventGuestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_event_guest);
        super.bindView();
        super.setBackEnabled(true);

        EventBus.getDefault().register(this);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.recyclerView.setAdapter(this.adapter = new EventGuestAdapter(this, this));

        App.getInstance().trackMixPanelEvent("View Guest Listing");
    }

    @Override
    public void onGlobalLayout() {
        this.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        this.loadData();
    }

    private void loadData() {

        final Intent intent = getIntent();
        EventDetailModel.Data.EventDetail dataEventDetail = intent.getParcelableExtra(EventDetailModel.Data.EventDetail.class.getName());
        SettingModel settingModel = AccountManager.loadSetting();

        String event_id = dataEventDetail.get_id();
        String fb_id = settingModel.getData().getFb_id();
        String gender_interest = settingModel.getData().getGender_interest();

        GuestManager.loaderGuestInterest(event_id, fb_id, gender_interest);
    }

    public void onEvent(GuestModel message){
        adapter.setGuests(message.getData().getGuest_interests());
        adapter.notifyDataSetChanged();
    }

    public void onEvent(ExceptionModel message){
        Toast.makeText(App.getInstance(), message.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGuestConnect(final EventGuestAdapter.ViewHolder viewHolder) {
        this.adapter.invite(viewHolder);

        final GuestModel.Data.GuestInterests guest = viewHolder.getGuest();
        final ProgressDialog dialog = App.showProgressDialog(this);
        final String id = AccessToken.getCurrentAccessToken().getUserId();
        final String url = String.format("partyfeed/match/%s/%s/approved", id, guest.getFb_id());

        VolleyHandler.getInstance().createVolleyRequest(url, new VolleyRequestListener<Void, JSONObject>() {
            @Override
            public Void onResponseAsync(JSONObject jsonObject) {
                guest.setIs_invited(true);
                sendBroadcast(new Intent(getString(R.string.broadcastGuestInvited)).putExtra(GuestModel.Data.GuestInterests.class.getName(), guest));
                return null;
            }

            @Override
            public void onResponseCompleted(Void value) {
                adapter.invite(viewHolder);
                dialog.dismiss();
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                adapter.deselect(viewHolder);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onGuestClick(EventGuestAdapter.ViewHolder viewHolder) {
        super.startActivityForResult(new Intent(this, ProfileDetailActivity.class).putExtra(Common.FIELD_FACEBOOK_ID, viewHolder.getGuest().getFb_id()), 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
