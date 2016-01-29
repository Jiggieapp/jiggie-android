package com.android.jiggie.fragment;

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

import com.android.jiggie.R;
import com.android.jiggie.component.HomeMain;
import com.android.jiggie.component.TabFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rangg on 21/10/2015.
 */
public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener, ViewTreeObserver.OnGlobalLayoutListener, HomeMain {
    @Bind(R.id.appBar) AppBarLayout appBarLayout;
    @Bind(R.id.viewpager) ViewPager viewPager;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tab) TabLayout tab;

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
        this.toolbar.setTitle(R.string.app_name);
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
                    new EventTabFragment(),
                    new SocialTabFragment(),
                    new ChatTabFragment(),
                    new MoreTabFragment()
            };
            ((TabFragment)this.fragments[0]).setHomeMain(homeMain);
            ((TabFragment)this.fragments[1]).setHomeMain(homeMain);
            ((TabFragment)this.fragments[2]).setHomeMain(homeMain);
            ((TabFragment)this.fragments[3]).setHomeMain(homeMain);
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
}
