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
import com.jiggie.android.R;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.EventTabListAdapter;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Event;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.Setting;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by rangg on 21/10/2015.
 */
public class EventTabFragment extends Fragment implements TabFragment, SwipeRefreshLayout.OnRefreshListener, EventTabListAdapter.ViewSelectedListener {
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
    private HomeMain homeMain;
    private String searchText;
    private boolean isLoading;
    private View rootView;
    private String title;

    private ArrayList<EventModel.Data.Events> events = new ArrayList<EventModel.Data.Events>();

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

        EventBus.getDefault().register(this);

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

        if (App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_EVENT, false)) {
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

        EventManager.loaderEvent(token.getUserId());

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

    public void onEvent(EventModel message){
        events.clear();
        events = message.getData().getEvents();

        int size = message.getData().getEvents().size();

        adapter.clear();


        if (searchText == null)
            adapter.addAll(events);

        refreshLayout.setRefreshing(false);
        filter(true);
    }

    public void onEvent(ExceptionModel message){
        if(message.getFrom().equals(Utils.FROM_EVENT)){
            isLoading = false;
            if (getContext() != null) {
                Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onViewSelected(EventModel.Data.Events event) {
        Intent i = new Intent(super.getActivity(), EventDetailActivity.class);
        i.putExtra(Common.FIELD_EVENT_ID, event.get_id());
        i.putExtra(Common.FIELD_EVENT_NAME, event.getTitle());
        super.startActivity(i);
    }

    private void filter(boolean notify) {
        if ((!this.refreshLayout.isRefreshing()) && (this.searchText != null)) {
            this.adapter.clear();
            final int size = this.events.size();
            final String searchText = this.searchText.toLowerCase();

            for (int i = 0; i < size; i++) {
                final EventModel.Data.Events event = this.events.get(i);
                if (event.getTitle().toLowerCase().contains(searchText))
                    this.adapter.add(event);
                else if (event.getVenue_name().toLowerCase().contains(searchText))
                    this.adapter.add(event);
                else {

                    final String[] tags =  new String[event.getTags().size()];
                    event.getTags().toArray(tags);
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
            if (notify) {
                int sizes = events.size();
                this.adapter.notifyDataSetChanged();
            }
        }
    }
    //--------------------------------


    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
