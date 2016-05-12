package com.jiggie.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiggie.android.R;
import com.jiggie.android.activity.chat.ChatActivity;
import com.jiggie.android.activity.chat.FriendListPresenterImplementation;
import com.jiggie.android.activity.chat.FriendsFragmentView;
import com.jiggie.android.component.adapter.ChatTabListAdapter;
import com.jiggie.android.component.adapter.FriendListAdapter;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.Conversation;
import com.jiggie.android.model.FriendListModel;

/**
 * Created by Wandy on 5/10/2016.
 */
public class FriendListFragment extends ChatTabFragment implements FriendsFragmentView,
        FriendListAdapter.ConversationSelectedListener
{

    private FriendListPresenterImplementation friendListPresenterImplementation;
    private FriendListAdapter adapterrr;
    private static FriendListFragment instance;

    public static FriendListFragment getInstance()
    {
        if(instance == null)
            instance = new FriendListFragment();
        return instance;
    }

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
        return homeMain.getContext().getString(R.string.friends_title);
    }

    @Override
    public void clearAdapter() {
        //adapter.clear();
    }

    @Override
    public void setAdapter(FriendListModel friendListModel) {
        adapterrr.clear();
        for(FriendListModel.Data.List_social_friends list_social_friends : friendListModel.getData().getList_social_friends())
        {
            adapterrr.add(list_social_friends);
        }
        adapterrr.notifyDataSetChanged();
        if(refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    @Override
    protected void setAdapter() {
        adapterrr = new FriendListAdapter(super.getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
        recyclerView.setAdapter(adapterrr);
    }

    @Override
    public void onRefresh() {
        fetchChat();
    }


    @Override
    public void onConversationSelected(FriendListModel.Data.List_social_friends conversation) {
        final Intent intent = new Intent(super.getActivity(), ChatActivity.class);
        intent.putExtra(Conversation.FIELD_PROFILE_IMAGE, conversation.getImg_url());
        intent.putExtra(Conversation.FIELD_FACEBOOK_ID, conversation.getFb_id());
        intent.putExtra(Conversation.FIELD_FROM_NAME, conversation.getFirst_name());
        super.startActivityForResult(intent, 0);
    }
}
