package com.jiggie.android.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.Common;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rangg on 21/10/2015.
 */
public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener, ViewTreeObserver.OnGlobalLayoutListener, HomeMain {
    @Bind(R.id.appBar)
    AppBarLayout appBarLayout;
    @Bind(R.id.viewpagerw)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab)
    TabLayout tab;
    //@Bind(R.id.fab) FloatingActionButton fab;

    private TabFragment lastSelectedFragment;
    private PageAdapter adapter;
    private View rootView;
    public final String TAG = HomeFragment.class.getSimpleName();
    Animation makeInAnimation, makeOutAnimation;

    final int EVENT_TAB = 0;
    final int SOCIAL_TAB = 1;
    final int CHAT_TAB = 2;

    private int currentPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.rootView = inflater.inflate(R.layout.fragment_home, container, false);
    }

    private boolean isNeedToBeRedirected() {
        final SharedPreferences pref = App.getSharedPreferences();
        boolean isNeedToBeRedirected = pref.getBoolean(Utils.IS_NEED_TO_BE_REDIRECTED_TO_EVENT_DETAIL, true);

        if (isNeedToBeRedirected) {
            String afSub1 = Utils.AFsub2;
            pref.edit().putBoolean(Utils.IS_NEED_TO_BE_REDIRECTED_TO_EVENT_DETAIL, false).commit();
            return (isNeedToBeRedirected && !afSub1.isEmpty() && !afSub1.equalsIgnoreCase("null"));
        }
        else return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, this.rootView);

        final AppCompatActivity activity = (AppCompatActivity) super.getActivity();
        //this.toolbar.setNavigationIcon(R.drawable.logo);
        //this.toolbar.getLogo().set;
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        final ImageView imgView = (ImageView) toolbar.findViewById(R.id.logo_image);
        //imgView.setImageDrawable(getResources().getDrawable(R.drawable.logo2));
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.logo));

        this.toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);

        this.adapter = new PageAdapter(this, activity.getSupportFragmentManager());
        this.viewPager.setOffscreenPageLimit(this.adapter.getCount());
        this.viewPager.setAdapter(this.adapter);

        this.tab.setupWithViewPager(this.viewPager);
        /*this.tab.getTabAt(1).setCustomView(R.layout.tab_custom_with_badge);
        TextView txtView = (TextView) tab.findViewById(R.id.tab_badge);
        txtView.setText("99");*/
        this.viewPager.addOnPageChangeListener(this);

        final int tabCount = this.adapter.getCount();
        for (int i = 0; i < tabCount; i++) {
            final Fragment fragment = this.adapter.getItem(i);
            if (fragment instanceof AppBarLayout.OnOffsetChangedListener)
                this.appBarLayout.addOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener) fragment);
        }

        super.setHasOptionsMenu(true);
        this.viewPager.getViewTreeObserver().addOnGlobalLayoutListener(this);

        //Load animation
        makeOutAnimation = AnimationUtils.loadAnimation(this.getActivity(),
                R.anim.slide_down);

        makeInAnimation = AnimationUtils.loadAnimation(this.getActivity(),
                R.anim.slide_up);


        /*makeInAnimation = AnimationUtils.makeInAnimation(this.getActivity(), false);
        makeOutAnimation = AnimationUtils.makeOutAnimation(this.getActivity(), true);

        makeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                fab.setVisibility(View.VISIBLE);
            }
        });
        makeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });*/

        if (isNeedToBeRedirected()) {
            Intent i = new Intent(super.getActivity(), EventDetailActivity.class);
            i.putExtra(Common.FIELD_EVENT_ID
                    , Utils.AFsub2 /*"56cbf750acbe12030016860d"*/);
            //i.putExtra(Common.FIELD_EVENT_NAME, event.getTitle());
            super.startActivity(i);
        }
        else //app invite
        {

        }
    }

    @Override
    public void onGlobalLayout() {
        this.viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        this.onPageSelected(0);
        setupTabIcons();
        if ((super.getArguments() != null) && (super.getArguments().getBoolean("chat", false)))
            this.viewPager.setCurrentItem(2);
        else if ((super.getArguments() != null)
                && (super.getArguments().getBoolean(Common.TO_TAB_SOCIAL, false)))
            this.viewPager.setCurrentItem(1);
        else if ((super.getArguments() != null)
                && (super.getArguments().getBoolean(Common.TO_TAB_CHAT, false)))
            this.viewPager.setCurrentItem(2);
        /*else if((super.getArguments() != null)
                && (!super.getArguments().getString(Common.FIELD_EVENT_ID, "").equals("")))
        {
            Intent i = new Intent(super.getActivity(), EventDetailActivity.class);
            final String eventId = super.getArguments().getString(Common.FIELD_EVENT_ID);
            i.putExtra(Common.FIELD_EVENT_ID, eventId);
            super.startActivity(i);
        }*/
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        if (position == CHAT_TAB) {
            startFetchChat();
        } else {
            stopFetchChat();
        }
        this.lastSelectedFragment = (TabFragment) this.adapter.fragments[position];
        this.lastSelectedFragment.onTabSelected();
    }

    private void startFetchChat() {
        if(AccountManager.loadMemberSetting().getChat() == 0)
            ((ChatTabFragment) this.adapter.getItem(CHAT_TAB)).startRepeatingTask();
    }

    private void stopFetchChat() {
        ((ChatTabFragment) this.adapter.getItem(CHAT_TAB)).stopRepeatingTask();
    }

    @Override
    public void onTabTitleChanged(TabFragment fragment) {
        final int position = this.adapter.getFragmentPosition(fragment);
        final TabLayout.Tab tab = position >= 0
                ? this.tab.getTabAt(position) : null;

        if (tab != null) {
            if (position == EVENT_TAB) {
                tab.setText(fragment.getTitle());
            }
            /*else if(position == 1)
            {

            }*/
            else {
                TextView lblBadge = (TextView) tab.getCustomView().findViewById(R.id.tab_badge);
                final String badgeCount = fragment.getTitle() /*"13"*/;

                if (badgeCount.equals("0")) {
                    lblBadge.setVisibility(View.GONE);
                } else {
                    lblBadge.setVisibility(View.VISIBLE);
                    lblBadge.setText(fragment.getTitle());
                }
            }
        }
    }

    private static class PageAdapter extends FragmentPagerAdapter {
        private final Fragment[] fragments;

        public PageAdapter(HomeMain homeMain, FragmentManager fm) {
            super(fm);
            this.fragments = new Fragment[]{
                    //new EventTabFragment()
                    new EventsFragment()
                    , new SocialTabFragment()
                    , new ChatTabFragment()

                    //,new MoreTabFragment()
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

        public int getIcon(int position) {
            return ((TabFragment) this.fragments[position]).getIcon();
        }

        public int getFragmentPosition(Object fragment) {
            final int length = this.fragments.length;
            for (int i = 0; i < length; i++) {
                if (fragment == this.fragments[i])
                    return i;
            }
            return -1;
        }
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom, null);
        tabOne.setText(adapter.getPageTitle(0));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getIcon(0), 0, 0);
        tab.getTabAt(EVENT_TAB).setCustomView(tabOne);

        View tabTwo = LayoutInflater.from(getActivity())
                .inflate(R.layout.tab_custom_with_badge, null);
        TextView tabTwoTitle = (TextView) tabTwo.findViewById(R.id.tab);
        tabTwoTitle.setText(adapter.getPageTitle(1));
        //tabTwoTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_chat_white_24dp, 0, 0);
        tabTwoTitle.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getIcon(1), 0, 0);
        TextView tabTwoBadge = (TextView) tabTwo.findViewById(R.id.tab_badge);
        tabTwoBadge.setText("99");
        tab.getTabAt(SOCIAL_TAB).setCustomView(tabTwo);

        View tabThree = LayoutInflater.from(getActivity())
                .inflate(R.layout.tab_custom_with_badge, null);
        TextView tabThreeTitle = (TextView) tabThree.findViewById(R.id.tab);
        tabThreeTitle.setText(adapter.getPageTitle(2));
        tabThreeTitle.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getIcon(2), 0, 0);
        TextView tabThreeBadge = (TextView) tabThree.findViewById(R.id.tab_badge);
        tabThreeBadge.setText("99");

        tab.getTabAt(CHAT_TAB).setCustomView(tabThree);
    }

    /*@OnClick(R.id.fab)
    void onFabClick()
    {
        Intent i = new Intent(this.getActivity(), FilterActivity.class);
        startActivity(i);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(fetchChatReceiver
                , new IntentFilter(Utils.FETCH_CHAT_RECEIVER));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(fetchChatReceiver);
    }

    private BroadcastReceiver fetchChatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(Utils.IS_ON, true)) {
                stopFetchChat();
            } else startFetchChat();
        }
    };


}
