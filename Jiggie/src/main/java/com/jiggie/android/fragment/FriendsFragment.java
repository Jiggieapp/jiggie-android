package com.jiggie.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiggie.android.R;
import com.jiggie.android.activity.chat.ChatActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.Conversation;
import com.jiggie.android.model.FriendListModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wandy on 5/10/2016.
 */
public class FriendsFragment extends Fragment implements TabFragment, HomeMain, FriendListFragment.OnFriendClickListener {
    private HomeMain homeMain;
    private String title;
    @Bind(R.id.view_pager_friends)
    ViewPager viewPager;
    private PageAdapter pageAdapter;
    @Bind(R.id.tab_friends)
    TabLayout tab;

    private final static String TAG = FriendsFragment.class.getSimpleName();

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
        pageAdapter = new PageAdapter(this, super.getActivity().getSupportFragmentManager());
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

        if (tabSmall != null) {
            tabSmall.setText(fragment.getTitle());
        }
    }


    private class PageAdapter extends FragmentPagerAdapter {
        private final Fragment[] fragments;

        public PageAdapter(HomeMain homeMain, FragmentManager fm) {
            super(fm);
            this.fragments = new Fragment[]{
                    ChatTabFragment.getInstance()
                    //,atTabFragment.getInstance()
                    //FriendListFragment.getInstance(FriendsFragment.this)
                    , FriendListFragment.getInstance(FriendsFragment.this)
            };

            ((TabFragment) this.fragments[0]).setHomeMain(homeMain);
            ((TabFragment) this.fragments[1]).setHomeMain(homeMain);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ((TabFragment) this.fragments[position]).getTitle();
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

    @Override
    public void doRedirect(FriendListModel.Data.List_social_friends conversation) {
        final Intent intent = new Intent(super.getActivity(), ChatActivity.class);
        intent.putExtra(Conversation.FIELD_PROFILE_IMAGE, conversation.getImg_url());
        intent.putExtra(Conversation.FIELD_FACEBOOK_ID, conversation.getFb_id());
        intent.putExtra(Conversation.FIELD_FROM_NAME, conversation.getFirst_name());
        super.startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ChatTabFragment.getInstance().onActivityResult(requestCode, resultCode, data);
        //super.onActivityResult(requestCode, resultCode, data);
    }


    void startRepeatingTask()
    {
        ChatTabFragment.getInstance().startRepeatingTask();
    }

    void stopRepeatingTask()
    {
        ChatTabFragment.getInstance().stopRepeatingTask();
    }
}