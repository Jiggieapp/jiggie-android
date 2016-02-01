package com.jiggie.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.android.jiggie.R;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.EventTabListAdapter;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.model.Event;
import com.jiggie.android.model.Setting;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rangg on 21/10/2015.
 */
public class EventTabFragment extends Fragment implements TabFragment, SwipeRefreshLayout.OnRefreshListener, EventTabListAdapter.EventSelectedListener {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.img_wk)
    ImageView imgWk;
    @Bind(R.id.txt_wk_action)
    TextView txtWkAction;
    @Bind(R.id.txt_wk_title)
    TextView txtWkTitle;
    @Bind(R.id.txt_wk_desc)
    TextView txtWkDesc;
    @Bind(R.id.layout_walkthrough)
    RelativeLayout layoutWalkthrough;

    private EventTabListAdapter adapter;
    private ArrayList<Event> events;
    private HomeMain homeMain;
    private String searchText;
    private boolean isLoading;
    private View rootView;
    private String title;

    @Override
    public void onTabSelected() {
        App.getInstance().trackMixPanelEvent("View Events");
        if ((this.adapter != null) && (this.adapter.getItemCount() == 0))
            this.onRefresh();
    }

    @Override
    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public String getTitle() {
        return this.title == null ? (this.title = this.homeMain.getContext().getString(R.string.events)) : this.title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = this.rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, this.rootView);

        this.recyclerView.setAdapter(this.adapter = new EventTabListAdapter(this, this));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
        this.refreshLayout.setOnRefreshListener(this);
        this.events = new ArrayList<>();
        super.setHasOptionsMenu(true);

        if (Utils.SHOW_WALKTHROUGH_EVENT) {
            layoutWalkthrough.setVisibility(View.VISIBLE);
            imgWk.setImageResource(R.drawable.wk_img_event);
            txtWkAction.setVisibility(View.GONE);
            txtWkTitle.setText(R.string.wk_event_title);
            txtWkDesc.setText(R.string.wk_event_desc);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event, menu);
        final MenuItem searchMenu = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        final Handler handler = new Handler();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchText = ((TextUtils.isEmpty(query)) || (query.trim().length() == 0)) ? null : query.trim();
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        filter(true);
                    }
                }, getResources().getInteger(R.integer.event_search_delay));
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchMenu, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchText = null;
                filter(true);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
        this.searchText = null;
    }

    @Override
    public void onRefresh() {
        if (super.getContext() == null) {
            // fragment has been destroyed.
            return;
        } else if (this.isLoading) {
            // refresh is ongoing
            return;
        }
        this.isLoading = true;
        this.refreshLayout.setRefreshing(true);
        final AccessToken token = AccessToken.getCurrentAccessToken();
        final String url = String.format("events/list/%s/%s", token.getUserId(), Setting.getCurrentSetting().getGenderInterestString());

        VolleyHandler.getInstance().createVolleyArrayRequest(url, new VolleyRequestListener<Void, JSONArray>() {
            @Override
            public Void onResponseAsync(JSONArray response) {
                final int count = response == null ? 0 : response.length();

                adapter.clear();
                events.clear();

                for (int i = 0; i < count; i++) {
                    final JSONObject obj = response.optJSONObject(i);
                    final JSONArray subArray = obj == null ? null : obj.optJSONArray("events");
                    final int eventCount = subArray == null ? 0 : subArray.length();

                    for (int u = 0; u < eventCount; u++)
                        events.add(new Event(subArray.optJSONObject(u)));
                }

                if (searchText == null)
                    adapter.addAll(events);
                return null;
            }

            @Override
            public void onResponseCompleted(Void value) {
                isLoading = false;
                if (getContext() != null) {
                    refreshLayout.setRefreshing(false);
                    filter(true);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                if (getContext() != null) {
                    Toast.makeText(getContext(), App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onEventSelected(Event event) {
        super.startActivity(new Intent(super.getActivity(), EventDetailActivity.class).putExtra(event.getClass().getName(), event));
    }

    private void filter(boolean notify) {
        if ((!this.refreshLayout.isRefreshing()) && (this.searchText != null)) {
            this.adapter.clear();
            final int size = this.events.size();
            final String searchText = this.searchText.toLowerCase();

            for (int i = 0; i < size; i++) {
                final Event event = this.events.get(i);
                if (event.getTitle().toLowerCase().contains(searchText))
                    this.adapter.add(event);
                else if (event.getVenueName().toLowerCase().contains(searchText))
                    this.adapter.add(event);
                else {
                    final String[] tags = event.getTags();
                    final int tagCount = tags.length;

                    for (int t = 0; t < tagCount; t++) {
                        final String tag = tags[t];
                        if (tag.toLowerCase().contains(searchText)) {
                            this.adapter.add(event);
                            break;
                        }
                    }
                }
            }

            if (notify)
                adapter.notifyDataSetChanged();
        } else if (!this.refreshLayout.isRefreshing()) {
            this.adapter.clear();
            this.adapter.addAll(this.events);
            if (notify)
                this.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.layout_walkthrough)
    void walkthroughOnClick() {
        Utils.SHOW_WALKTHROUGH_EVENT = false;
        layoutWalkthrough.setVisibility(View.GONE);
        App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, false).commit();
    }

}
