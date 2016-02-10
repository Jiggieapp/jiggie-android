package com.jiggie.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.activity.profile.FilterActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.ProductManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rangg on 21/10/2015.
 */
public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener, ViewTreeObserver.OnGlobalLayoutListener, HomeMain {
    @Bind(R.id.appBar) AppBarLayout appBarLayout;
    @Bind(R.id.viewpager) ViewPager viewPager;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tab) TabLayout tab;
    @Bind(R.id.fab) FloatingActionButton fab;

    private TabFragment lastSelectedFragment;
    private PageAdapter adapter;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.rootView = inflater.inflate(R.layout.fragment_home, container, false);
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
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.logo2));

        this.toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);

        this.adapter = new PageAdapter(this, activity.getSupportFragmentManager());
        this.viewPager.setOffscreenPageLimit(this.adapter.getCount());
        this.viewPager.setAdapter(this.adapter);
        this.tab.setupWithViewPager(this.viewPager);
        this.viewPager.addOnPageChangeListener(this);

        final int tabCount = this.adapter.getCount();
        for (int i = 0; i < tabCount; i++) {
            final Fragment fragment = this.adapter.getItem(i);
            if (fragment instanceof AppBarLayout.OnOffsetChangedListener)
                this.appBarLayout.addOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener) fragment);
        }

        super.setHasOptionsMenu(true);
        this.viewPager.getViewTreeObserver().addOnGlobalLayoutListener(this);

        if ((super.getArguments() != null) && (super.getArguments().getBoolean("chat", false)))
            this.viewPager.setCurrentItem(2);

        setupTabIcons();
    }

    @Override
    public void onGlobalLayout() {
        this.viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        this.onPageSelected(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
    @Override
    public void onPageScrollStateChanged(int state) { }
    @Override
    public void onPageSelected(int position) {
        this.lastSelectedFragment = (TabFragment) this.adapter.fragments[position];
        this.lastSelectedFragment.onTabSelected();
    }

    @Override
    public void onTabTitleChanged(TabFragment fragment) {
        final int position = this.adapter.getFragmentPosition(fragment);
        final TabLayout.Tab tab = position >= 0 ? this.tab.getTabAt(position) : null;

        if (tab != null)
            tab.setText(fragment.getTitle());
    }

    private static class PageAdapter extends FragmentPagerAdapter {
        private final Fragment[] fragments;

        public PageAdapter(HomeMain homeMain, FragmentManager fm) {
            super(fm);
            this.fragments = new Fragment[] {
                    //new EventTabFragment()
                    new EventsFragment()
                    ,new ChatTabFragment()
                    ,new SocialTabFragment()
                    //,new MoreTabFragment()
            };
            ((TabFragment)this.fragments[0]).setHomeMain(homeMain);
            ((TabFragment)this.fragments[1]).setHomeMain(homeMain);
            ((TabFragment)this.fragments[2]).setHomeMain(homeMain);
            //((TabFragment)this.fragments[3]).setHomeMain(homeMain);
        }

        @Override
        public Fragment getItem(int position) { return this.fragments[position]; }
        @Override
        public int getCount() { return this.fragments.length; }
        @Override
        public CharSequence getPageTitle(int position) { return ((TabFragment)this.fragments[position]).getTitle(); }

        public int getFragmentPosition(Object fragment) {
            final int length = this.fragments.length;

            for (int i = 0; i < length; i++) {
                if (fragment == this.fragments[i])
                    return i;
            }

            return -1;
        }
    }

    private void setupTabIcons()
    {
        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom, null);
        tabOne.setText(adapter.getPageTitle(0));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_event_white_24dp, 0, 0);
        tab.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom, null);
        tabTwo.setText(adapter.getPageTitle(1));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_chat_white_24dp, 0, 0);
        tab.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom, null);
        tabThree.setText(adapter.getPageTitle(2));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_visibility_white_24dp, 0, 0);
        tab.getTabAt(2).setCustomView(tabThree);
    }

    @OnClick(R.id.fab)
    void onFabClick()
    {
        Intent i = new Intent(this.getActivity(), FilterActivity.class);
        startActivity(i);

        //AccountManager.getAccessToken();
        //ProductManager.getProductList("56b1a0bf89bfed03005c50f0");
    }
}
