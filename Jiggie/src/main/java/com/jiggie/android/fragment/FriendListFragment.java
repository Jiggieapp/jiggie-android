package com.jiggie.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiggie.android.activity.chat.FriendListPresenterImplementation;
import com.jiggie.android.activity.chat.FriendsFragmentView;
import com.jiggie.android.component.adapter.FriendListAdapter;
import com.jiggie.android.model.FriendListModel;

/**
 * Created by Wandy on 5/10/2016.
 */
public class FriendListFragment extends ChatTabFragment implements FriendsFragmentView {

    private FriendListPresenterImplementation friendListPresenterImplementation;
    private FriendListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        friendListPresenterImplementation = new FriendListPresenterImplementation(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void fetchChat() {
        friendListPresenterImplementation.loadFriendFromFb();
    }

    @Override
    public String getTitle() {
        return "lah";
    }

    @Override
    public void clearAdapter() {
        //adapter.clear();
    }

    @Override
    public void setAdapter(FriendListModel friendListModel) {
        adapter.clear();
        for(FriendListModel.Data.List_social_friends list_social_friends : friendListModel.getData().getList_social_friends())
        {
            adapter.add(list_social_friends);
        }
        adapter.notifyDataSetChanged();
        if(refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    @Override
    protected void setAdapter() {
        adapter = new FriendListAdapter(super.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        fetchChat();
    }
}
