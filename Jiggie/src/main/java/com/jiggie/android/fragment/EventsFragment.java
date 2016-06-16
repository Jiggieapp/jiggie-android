package com.jiggie.android.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.manager.WalkthroughManager;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.PostWalkthroughModel;
import com.jiggie.android.view.NonSwipeableViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by wandywijayanto on 2/4/16.
 */
public class EventsFragment extends Fragment
        implements ViewPager.OnPageChangeListener, HomeMain
        , ViewTreeObserver.OnGlobalLayoutListener, TabFragment, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.time_tab)
    TabLayout timeTab;
    @Bind(R.id.viewpagerevents)
    /*ViewPager viewPagerEvents;*/ NonSwipeableViewPager viewPagerEvents;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;

    private PageAdapter pageAdapter;
    private View rootView;
    private String title;
    private HomeMain homeMain;
    private TabFragment lastSelectedFragment;
    public final static String TAG = EventsFragment.class.getSimpleName();
    private boolean isLoading;
    private String searchText;
    private Dialog dialogWalkthrough;
    SearchView searchView;

    @Override
    public String getTitle() {
        return this.title == null ? (this.title = this.homeMain.getContext().getString(R.string.events)) : this.title;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_event_white_24dp;
    }

    @Override
    public void onTabSelected() {
        App.getInstance().trackMixPanelEvent("View Events");
        if (getEvents() != null) {
            /*boolean isExpanded = false;
            if(!searchView.isIconified())
            {
                isExpanded = true;
            }
            filter("", isExpanded);*/
            showTab();
        }

        if (App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_EVENT, false)) {
            //showWalkthroughDialog();
        }
    }

    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public void onTabTitleChanged(TabFragment fragment) {
        final int position = this.pageAdapter.getFragmentPosition(fragment);
        final TabLayout.Tab tab = position >= 0 ? this.timeTab.getTabAt(position) : null;

        if (tab != null)
            tab.setText(fragment.getTitle());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private int currentPosition = 0;

    @Override
    public void onPageSelected(int position) {
        this.lastSelectedFragment = (TabFragment) this.pageAdapter.fragments[position];
        this.lastSelectedFragment.onTabSelected();
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    //EventTabFragment todayFragment, tomorrowFragment, upcomingFragment;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class PageAdapter extends FragmentPagerAdapter {
        private final Fragment[] fragments;

        public PageAdapter(HomeMain homeMain, FragmentManager fm) {

            super(fm);
            Bundle bundle = new Bundle();
            bundle.putInt("position", 0);
            //todayFragment = new EventTabFragment();
            if(getTodayFragment().getArguments() == null)
                getTodayFragment().setArguments(bundle);
            else getTodayFragment().getArguments().putAll(bundle);

            bundle = new Bundle();
            bundle.putInt("position", 1);
            //tomorrowFragment = new EventTabFragment();
            if(getTomorrowFragment().getArguments() == null)
                getTomorrowFragment().setArguments(bundle);
            else getTomorrowFragment().getArguments().putAll(bundle);

            bundle = new Bundle();
            bundle.putInt("position", 2);
            //upcomingFragment = new EventTabFragment();
            if(getUpcomingFragment().getArguments() == null)
                getUpcomingFragment().setArguments(bundle);
            else getUpcomingFragment().getArguments().putAll(bundle);

            /*EventTabFragment todayFragment = EventTabFragment.getInstance(0);
            EventTabFragment tomorrowFragment = EventTabFragment.getInstance(1);
            EventTabFragment upcomingFragment = EventTabFragment.getInstance(2);*/

            this.fragments = new Fragment[]{
                    getTodayFragment(),
                    getTomorrowFragment(),
                    getUpcomingFragment()
            };

            ((TabFragment) this.fragments[0]).setHomeMain(homeMain);
            ((TabFragment) this.fragments[1]).setHomeMain(homeMain);
            ((TabFragment) this.fragments[2]).setHomeMain(homeMain);
            //((TabFragment)this.fragments[3]).setHomeMain(homeMain);
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments[position];
        }

        @Override
        public int getCount() {
            return this.fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ((TabFragment) this.fragments[position]).getTitle();
        }

        public int getFragmentPosition(Object fragment) {
            final int length = this.fragments.length;

            for (int i = 0; i < length; i++) {
                if (fragment == this.fragments[i])
                    return i;
            }
            return -1;
        }

        /*@Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Yet another bug in FragmentStatePagerAdapter that destroyItem is called on fragment that hasnt been added. Need to catch
            try {
                super.destroyItem(container, position, object);
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_events, container, false);
        return this.rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isRefreshDetail) {
            filter("", false);
            Utils.isRefreshDetail = false;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setRetainInstance(true);

        EventBus.getDefault().register(this);
        ButterKnife.bind(this, this.rootView);
        final AppCompatActivity activity = (AppCompatActivity) super.getActivity();
        this.pageAdapter = new PageAdapter(this,
                /*((AppCompatActivity)getActivity()).getSupportFragmentManager()*/
                /*activity.getSupportFragmentManager()*/
                /*getChildFragmentManager()*/
                activity.getSupportFragmentManager());
        this.viewPagerEvents.setOffscreenPageLimit(this.pageAdapter.getCount());
        this.viewPagerEvents.setAdapter(pageAdapter);
        this.timeTab.setupWithViewPager(this.viewPagerEvents);
        this.viewPagerEvents.addOnPageChangeListener(this);

        this.viewPagerEvents.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.viewPagerEvents.setPagingEnabled(true);
        this.refreshLayout.setOnRefreshListener(this);

        //this.refreshLayout.canChildScrollUp();
        //this.refreshLayout.setNestedScrollingEnabled(true);

        super.setHasOptionsMenu(true);

        getTodayFragment().handleSwipeIssue(refreshLayout);
        getTomorrowFragment().handleSwipeIssue(refreshLayout);
        getUpcomingFragment().handleSwipeIssue(refreshLayout);
    }

    @Override
    public void onGlobalLayout() {
        this.viewPagerEvents.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        //this.onPageSelected(0);
        onRefresh();
    }

    //Added by Aga
    public void onEvent(EventModel eventModel) {
        if(eventModel != null && eventModel.getData() != null && eventModel.getData().getEvents().size() > 0)
        {
            ArrayList<EventModel.Data.Events> message = eventModel.getData().getEvents();
            int size = message.size();
            setEvents(message);
            setThemes(eventModel.getData().getThemes());
        }

        boolean isExpanded = false;
        //if(searchText != null  /* && !searchText.isEmpty()*/)
        if (searchView == null) {
            isExpanded = false;
        } else if (!searchView.isIconified()) {
            isExpanded = true;
        }
        filter(searchText, isExpanded);
        this.isLoading = false;
        this.refreshLayout.setRefreshing(false);
    }

    public void onEvent(ExceptionModel exceptionModel) {
        /*Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
        snackbar.show();*/

        if (exceptionModel.getMessage().equalsIgnoreCase(Utils.MSG_EMPTY_DATA) &&
                exceptionModel.getFrom().equalsIgnoreCase(Utils.FROM_EVENT)) {
            setEvents(null);
            //onRefresh();
            filter("");
        }

        this.isLoading = false;
        this.refreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_event, menu);
        final MenuItem searchMenu = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        final Handler handler = new Handler();
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                        filter(searchText, true);
                    }
                }, getResources().getInteger(R.integer.event_search_delay));
                return true;
            }
        });*/

        MenuItemCompat.setOnActionExpandListener(searchMenu, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                hideTab();
                searchText = "";
                filter(searchText, true);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        searchText = ((TextUtils.isEmpty(query)) || (query.trim().length() == 0))
                                ? null : query.trim();

                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (searchText != null) {
                                    filter(searchText, true);
                                } else {
                                    boolean isExpanded = false;
                                    if (searchView != null && !searchView.isIconified()) {
                                        isExpanded = true;
                                    }
                                    filter("", isExpanded);
                                    //filter(searchText, false);
                                }
                            }
                        }, getResources().getInteger(R.integer.event_search_delay));

                        return true;
                    }
                });
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchText = null;
                //showTab();
                handler.removeCallbacksAndMessages(null);
                filter("", false);
                searchView.setOnQueryTextListener(null);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
        this.searchText = null;
    }

    private ArrayList<EventModel.Data.Events> events;

    public ArrayList<EventModel.Data.Events> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<EventModel.Data.Events> events) {
        this.events = events;
        EventManager.events = events;
    }

    private ArrayList<EventModel.Data.Theme> themes;

    public ArrayList<EventModel.Data.Theme> getThemes() {
        return themes;
    }

    public void setThemes(ArrayList<EventModel.Data.Theme> themes) {
        this.themes = themes;
    }


    private void filter(String searchText, boolean isSearch) {
        ArrayList<EventModel.Data.Events> todayEvents = new ArrayList<>();
        ArrayList<EventModel.Data.Events> tomorrowEvents = new ArrayList<>();
        ArrayList<EventModel.Data.Events> upcomingEvents = new ArrayList<>();

        if (getEvents() != null) {
            if (searchText == null)
                searchText = "";
            //timeTab.setVisibility(View.VISIBLE);
            searchText = searchText.toLowerCase();


            //wandy16-06-2016
            for(EventModel.Data.Theme themeEvent : getThemes())
            {
                if (themeEvent.name.toLowerCase().contains(searchText)
                        || searchText.equals("")) {
                    if (!isSearch) {
                        if(themeEvent.status.equalsIgnoreCase(Utils.DATE_TODAY))
                        {
                            todayEvents.add(new EventModel.Data.Events(themeEvent));
                        }
                        else if(themeEvent.status.equalsIgnoreCase(Utils.DATE_TOMORROW))
                        {
                            tomorrowEvents.add(new EventModel.Data.Events(themeEvent));
                        }
                        else if(themeEvent.status.equalsIgnoreCase(Utils.DATE_UPCOMING))
                        {
                            upcomingEvents.add(new EventModel.Data.Events(themeEvent));
                        }
                    }
                }
            }

            /*for(EventModel.Data.Events themeEvent : todayEvents)
            {
                Utils.d(TAG, themeEvent.getTitle() + " " + themeEvent.isEvent);
            }*/
            //end of wandy

            for (EventModel.Data.Events tempEvent : getEvents()) {
                //new Date(event.getDate_day());
                if (tempEvent.getTitle().toLowerCase().contains(searchText)
                        || tempEvent.getVenue_name().toLowerCase().contains(searchText)
                        || tempEvent.getTags().toString().toLowerCase().contains(searchText)
                        || searchText.equals("")) {
                    if (!isSearch) {
                        showTab();
                        final String diffDays = Utils.calculateTime(tempEvent.getStart_datetime());
                        if (diffDays.equals(Utils.DATE_TODAY)) {
                            todayEvents.add(tempEvent);
                        } else if (diffDays.equals(Utils.DATE_TOMORROW)) {
                            tomorrowEvents.add(tempEvent);
                        } else if (diffDays.equals(Utils.DATE_UPCOMING)) {
                            upcomingEvents.add(tempEvent);
                        }
                        /*todayFragment.onEvent(todayEvents);
                        tomorrowFragment.onEvent(tomorrowEvents);
                        upcomingFragment.onEvent(upcomingEvents);*/
                    } else {
                        hideTab();
                        switch (currentPosition) {
                            case 0:
                                todayEvents.add(tempEvent);
                                //todayFragment.onEvent(todayEvents);
                                break;
                            case 1:
                                tomorrowEvents.add(tempEvent);
                                //tomorrowFragment.onEvent(tomorrowEvents);
                                break;
                            case 2:
                                upcomingEvents.add(tempEvent);
                                //upcomingFragment.onEvent(upcomingEvents);
                                break;
                        }
                    }
                }
            }
        } else {
            //timeTab.setVisibility(View.GONE);
            viewPagerEvents.setPagingEnabled(false);
            hideTab();
        }

        for(EventModel.Data.Events themeEvent : todayEvents)
        {
            Utils.d(TAG, themeEvent.getTitle() + " " + themeEvent.isEvent);
        }

        todayFragment.onEvent(todayEvents);
        tomorrowFragment.onEvent(tomorrowEvents);
        upcomingFragment.onEvent(upcomingEvents);
    }

    private void filter(String searchText) {
        filter(searchText, false);
    }

    protected void showTab() {
        if (timeTab.getVisibility() == View.GONE) {
            timeTab.animate()
                    .translationY(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            timeTab.setVisibility(View.VISIBLE);
                            viewPagerEvents.setPagingEnabled(true);
                        }
                    });
        }
    }

    protected void hideTab() {
        if (timeTab.getVisibility() == View.VISIBLE) {
            timeTab.animate()
                    .translationY(-timeTab.getMeasuredHeight())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            timeTab.setVisibility(View.GONE);
                            viewPagerEvents.setPagingEnabled(false
                            );
                        }
                    });
            /*Animation makeInAnimation = AnimationUtils.loadAnimation(this.getActivity(),
                    R.anim.slide_up);
            timeTab.startAnimation(makeInAnimation);*/
        }

    }

    public void onEvent(final String tag) {
        if (TAG.equalsIgnoreCase(tag)) {
            onRefresh();
        }
    }

    private void showWalkthroughDialog() {
        dialogWalkthrough = new Dialog(getActivity());
        dialogWalkthrough.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWalkthrough.setContentView(R.layout.walkthrough_screen);
        dialogWalkthrough.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogWalkthrough.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        RelativeLayout layout = (RelativeLayout) dialogWalkthrough.findViewById(R.id.layout_walkthrough);
        ImageView imgWk = (ImageView) dialogWalkthrough.findViewById(R.id.img_wk);
        TextView txtWkAction = (TextView) dialogWalkthrough.findViewById(R.id.txt_wk_action);
        TextView txtWkTitle = (TextView) dialogWalkthrough.findViewById(R.id.txt_wk_title);
        TextView txtWkDesc = (TextView) dialogWalkthrough.findViewById(R.id.txt_wk_desc);
        imgWk.setImageResource(R.drawable.wk_event);
        txtWkAction.setVisibility(View.GONE);
        txtWkTitle.setText(R.string.wk_event_title);
        txtWkDesc.setText(R.string.wk_event_desc);

        dialogWalkthrough.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Utils.SHOW_WALKTHROUGH_EVENT = false;
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, false).commit();

                PostWalkthroughModel postWalkthroughModel = new PostWalkthroughModel(AccessToken.getCurrentAccessToken().getUserId(), Utils.TAB_EVENT, Utils.DEVICE_ID);

                String sd = String.valueOf(new Gson().toJson(postWalkthroughModel));

                WalkthroughManager.loaderPostWalkthrough(postWalkthroughModel);
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.SHOW_WALKTHROUGH_EVENT = false;
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, false).commit();
                dialogWalkthrough.dismiss();

                PostWalkthroughModel postWalkthroughModel = new PostWalkthroughModel(AccessToken.getCurrentAccessToken().getUserId(), Utils.TAB_EVENT, Utils.DEVICE_ID);

                String sd = String.valueOf(new Gson().toJson(postWalkthroughModel));

                WalkthroughManager.loaderPostWalkthrough(postWalkthroughModel);
            }
        });

        dialogWalkthrough.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState, "todayfragment", getTodayFragment());
        getFragmentManager().putFragment(outState, "tomorrowfragment", getTomorrowFragment());
        getFragmentManager().putFragment(outState, "upcomingfragment", getUpcomingFragment());
    }

    private static EventTabFragment todayFragment;
    private static EventTabFragment tomorrowFragment;
    private static EventTabFragment upcomingFragment;

    public static EventTabFragment getTodayFragment()
    {
        if(todayFragment == null)
            todayFragment = new EventTabFragment();
        return todayFragment;
    }

    public static EventTabFragment getTomorrowFragment()
    {
        if(tomorrowFragment == null)
            tomorrowFragment = new EventTabFragment();
        return tomorrowFragment;
    }

    public static EventTabFragment getUpcomingFragment()
    {
        if(upcomingFragment == null)
            upcomingFragment = new EventTabFragment();
        return upcomingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
        {
            todayFragment = (EventTabFragment) getFragmentManager().getFragment(savedInstanceState, "todayfragment");
            tomorrowFragment = (EventTabFragment) getFragmentManager().getFragment(savedInstanceState, "tomorrowfragment");
            upcomingFragment = (EventTabFragment) getFragmentManager().getFragment(savedInstanceState, "upcomingfragment");
        }
    }
}
