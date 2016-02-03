package com.jiggie.android.fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnRecyclerViewOnScrollListener;
import com.etiennelawlor.quickreturn.library.utils.QuickReturnUtils;
import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.EventTabListAdapter;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.model.EventModel;

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
    @Bind(R.id.txtFilter)
    TextView txtFilter;
    @Bind(R.id.footerContainer)
    LinearLayout footerContainer;
    /*@Nullable @Bind(R.id.main_content)
    CoordinatorLayout coordinatorLayout;*/

    private EventTabListAdapter adapter;
    //private ArrayList<Event> events;
    private HomeMain homeMain;
    private String searchText;
    private boolean isLoading;
    private View rootView;
    private String title;
    private boolean mIsHiding;
    private boolean mIsShowing;

    private ArrayList<EventModel.Data.Events> events = new ArrayList<EventModel.Data.Events>();
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final String TAG = EventTabFragment.class.getSimpleName();

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

        EventBus.getDefault().register(this);
        EventManager.initEventService();

        this.recyclerView.setAdapter(this.adapter = new EventTabListAdapter(this, this));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
        this.recyclerView.setHasFixedSize(true);
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


        RecyclerView.OnScrollListener listenerrr = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Utils.d(TAG, "dy " + dy);
                if (dy > 0) {
                    // Scrolling up
                    hide(txtFilter);
                } else {
                    // Scrolling down
                    show(txtFilter);
                }
            }
        };
        recyclerView.addOnScrollListener(listenerrr);

        /*QuickReturnRecyclerViewOnScrollListener scrollListener =
                new QuickReturnRecyclerViewOnScrollListener.Builder(QuickReturnViewType.FOOTER)
                .footer(txtFilter)
                .minFooterTranslation(getResources().getDimensionPixelOffset(R.dimen.footer_height))
                //.minFooterTranslation(txtFilter.getMeasuredHeight())
                .isSnappable(true)
                .build();*/

        /*QuickReturnRecyclerViewOnScrollListener scrollListener =
                new QuickReturnRecyclerViewOnScrollListener.Builder(QuickReturnViewType.TWITTER)
                        //.header(getActivity().getActionBar().getCustomView())
                        //.minHeaderTranslation(android.R.attr.actionBarSize)
                        .footer(txtFilter)
                        .minFooterTranslation(-getResources().getDimensionPixelOffset(R.dimen.footer_height))
                        .isSnappable(true)
                        .build();*/

        /*int footerHeight = getResources().getDimensionPixelOffset(R.dimen.footer_height);
        int indicatorHeight = QuickReturnUtils.dp2px(getActivity(), 4);
        int footerTranslation = -footerHeight + indicatorHeight;*/

        /*QuickReturnRecyclerViewOnScrollListener scrollListener =
                new QuickReturnRecyclerViewOnScrollListener.Builder(QuickReturnViewType.FOOTER)
                        .footer(footerContainer)
                        .minFooterTranslation(-footerTranslation)
                        .isSnappable(true)
                        .build();*/

        /*QuickReturnRecyclerViewOnScrollListener scrollListener =
                new QuickReturnRecyclerViewOnScrollListener.Builder(QuickReturnViewType.FOOTER)
                //.header((()))
                //.minHeaderTranslation(headerTranslation)
                .footer(footerContainer)
                .minFooterTranslation(-footerTranslation)
                .isSnappable(true)
                .build();
        recyclerView.addOnScrollListener(scrollListener);*/
    }

    public static int dp2px(Context context, int dp) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (dp * displaymetrics.density + 0.5f);
    }

    /**
     * Hide the quick return view.
     *
     * Animates hiding the view, with the view sliding down and out of the screen.
     * After the view has disappeared, its visibility will change to GONE.
     *
     * @param view The quick return view
     */
    private void hide(final View view) {
        mIsHiding = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(view.getHeight())
                .setInterpolator(INTERPOLATOR)
                .setDuration(200);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                // Prevent drawing the View after it is gone
                mIsHiding = false;
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a hide should show the view
                mIsHiding = false;
                if (!mIsShowing) {
                    show(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        animator.start();
    }

    /**
     * Show the quick return view.
     *
     * Animates showing the view, with the view sliding up from the bottom of the screen.
     * After the view has reappeared, its visibility will change to VISIBLE.
     *
     * @param view The quick return view
     */
    private void show(final View view) {
        mIsShowing = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(0)
                .setInterpolator(INTERPOLATOR)
                .setDuration(200);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mIsShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a show should hide the view
                mIsShowing = false;
                if (!mIsHiding) {
                    hide(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animator.start();
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
        //final String url = String.format("events/list/%s/%s", token.getUserId(), Setting.getCurrentSetting().getGenderInterestString());

        /*VolleyHandler.getAccountInterface().createVolleyArrayRequest(url, new VolleyRequestListener<Void, JSONArray>() {
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
        });*/

        //Added by Aga
        EventManager.loaderEvent(token.getUserId());
        //--------------
    }

    /*@Override
    public void onEventSelected(Event event) {
        super.startActivity(new Intent(super.getActivity(), EventDetailActivity.class).putExtra(event.getClass().getName(), event));
    }*/

    /*private void filter(boolean notify) {
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
    }*/

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

    //Added by Aga
    public void onEvent(ArrayList<EventModel.Data.Events> message){
        int size = message.size();
        events = message;

        adapter.clear();
        //events.clear();

        if (searchText == null)
            adapter.addAll(events);

        refreshLayout.setRefreshing(false);
        filter(true);
    }

    @Override
    public void onViewSelected(EventModel.Data.Events event) {
        super.startActivity(new Intent(super.getActivity(), EventDetailActivity.class).putExtra(event.getClass().getName(), event));
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
        EventBus.getDefault().unregister(this);
    }


}
