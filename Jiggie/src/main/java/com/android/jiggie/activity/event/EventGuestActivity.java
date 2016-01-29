package com.android.jiggie.activity.event;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

import com.android.jiggie.App;
import com.android.jiggie.R;
import com.android.jiggie.activity.profile.ProfileDetailActivity;
import com.android.jiggie.component.SimpleJSONObject;
import com.android.jiggie.component.activity.ToolbarActivity;
import com.android.jiggie.component.adapter.EventGuestAdapter;
import com.android.jiggie.component.volley.VolleyHandler;
import com.android.jiggie.component.volley.VolleyRequestListener;
import com.android.jiggie.model.Common;
import com.android.jiggie.model.Guest;
import com.android.jiggie.model.Setting;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;

import org.json.JSONObject;

import java.util.Date;

import butterknife.Bind;

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
        new AsyncTask<Void, Void, Guest[]>() {
            @Override
            protected Guest[] doInBackground(Void... params) {
                final Intent intent = getIntent();

                if (intent != null) {
                    final Parcelable[] items = intent.getParcelableArrayExtra(Guest.class.getName());
                    final Guest[] guests = new Guest[items == null ? 0 : items.length];
                    final int length = guests.length;

                    for (int i = 0; i < length; i++)
                        guests[i] = (Guest) (items != null ? items[i] : null);

                    return guests;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Guest[] guests) {
                adapter.setGuests(guests);
                adapter.notifyDataSetChanged();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onGuestConnect(final EventGuestAdapter.ViewHolder viewHolder) {
        this.adapter.invite(viewHolder);

        final Guest guest = viewHolder.getGuest();
        final ProgressDialog dialog = App.showProgressDialog(this);
        final String id = AccessToken.getCurrentAccessToken().getUserId();
        final String url = String.format("partyfeed/match/%s/%s/approved", id, guest.getFacebookId());

        VolleyHandler.getInstance().createVolleyRequest(url, new VolleyRequestListener<Void, JSONObject>() {
            @Override
            public Void onResponseAsync(JSONObject jsonObject) {
                guest.setInvited(true);
                sendBroadcast(new Intent(getString(R.string.broadcastGuestInvited)).putExtra(Guest.class.getName(), guest));
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
        super.startActivityForResult(new Intent(this, ProfileDetailActivity.class).putExtra(Guest.class.getName(), viewHolder.getGuest()), 0);
    }
}
