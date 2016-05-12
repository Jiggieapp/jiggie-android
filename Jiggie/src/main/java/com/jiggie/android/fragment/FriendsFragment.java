package com.jiggie.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiggie.android.R;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wandy on 5/10/2016.
 */
public class FriendsFragment extends Fragment implements TabFragment, HomeMain{
    private HomeMain homeMain;
    private String title;
    @Bind(R.id.view_pager_friends)
    ViewPager viewPager;
    private PageAdapter pageAdapter;
    @Bind(R.id.tab_friends)
    TabLayout tab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //pageAdapter = new PageAdapter(this, getActivity().getSupportFragmentManager());
        pageAdapter = new PageAdapter(super.getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        tab.setupWithViewPager(viewPager);
    }

    @Override
    public String getTitle() {
        return this.title == null ? (this.title = this.homeMain.getContext().getString(R.string.chat)) : this.title;
    }

    @Override
    public void onTabSelected() {
    }

    @Override
    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_chat_white_24dp;
    }

    @Override
    public void onTabTitleChanged(TabFragment fragment) {
        final int position = this.pageAdapter.getFragmentPosition(fragment);
        final TabLayout.Tab tabSmall = position >= 0 ? this.tab.getTabAt(position) : null;

        if (tabSmall != null)
            tabSmall.setText(fragment.getTitle());
    }

    private class PageAdapter extends FragmentPagerAdapter
    {
        private final Fragment[] fragments;

        public PageAdapter(HomeMain homeMain, FragmentManager fm) {
            super(fm);
            this.fragments = new  Fragment[] {
                    new ChatTabFragment(),
                    new FriendListFragment()
            };
        }

        public PageAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new  Fragment[] {
                    new ChatTabFragment(),
                    new FriendListFragment()
            };
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
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
}
