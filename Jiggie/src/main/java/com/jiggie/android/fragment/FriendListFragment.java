package com.jiggie.android.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.jiggie.android.R;
import com.jiggie.android.activity.chat.ChatActivity;
import com.jiggie.android.activity.chat.FirebaseChatActivity;
import com.jiggie.android.activity.chat.FriendListPresenterImplementation;
import com.jiggie.android.activity.chat.FriendsFragmentView;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.ChatTabListAdapter;
import com.jiggie.android.component.adapter.FriendListAdapter;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.FirebaseChatManager;
import com.jiggie.android.manager.SocialManager;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.Conversation;
import com.jiggie.android.model.FriendListModel;
import com.jiggie.android.model.PostFriendModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Wandy on 5/10/2016.
 */
public class FriendListFragment extends ChatTabFragment implements FriendsFragmentView,
        FriendListAdapter.ConversationSelectedListener
{

    private FriendListPresenterImplementation friendListPresenterImplementation;
    private FriendListAdapter adapterrr;
    private static FriendListFragment instance;
    OnFriendClickListener onFriendClickListener;

    public static FriendListFragment getInstance()
    {
        if(instance == null)
        {
            instance = new FriendListFragment();
        }
        return instance;
    }

    public void setOnFriendClickListener(OnFriendClickListener onFriendClickListener)
    {
        getInstance().onFriendClickListener = onFriendClickListener;
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
    void registerEventBus() {
        //EventBus.getDefault().register(FriendListFragment.this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_friend_list;
    }

    @Override
    public void setAdapter(FriendListModel friendListModel) {
        if(adapterrr != null)
        {
            adapterrr.clear();
        }

        if(friendListModel != null)
        {
            for(FriendListModel.Data.List_social_friends list_social_friends : friendListModel.getData().getList_social_friends())
            {
                //list_social_friends.setIs_connect("false");
                adapterrr.add(list_social_friends);
            }
            adapterrr.notifyDataSetChanged();
        }
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
    public void onConversationSelected(final FriendListModel.Data.List_social_friends conversation) {
        final boolean isConnect = conversation.getIs_connect().equalsIgnoreCase("true");
        //final boolean isConnect = false;
        if(isConnect)
        {
            /*final Intent intent = new Intent(super.getActivity(), ChatActivity.class);
            intent.putExtra(Conversation.FIELD_PROFILE_IMAGE, conversation.getImg_url());
            intent.putExtra(Conversation.FIELD_FACEBOOK_ID, conversation.getFb_id());
            intent.putExtra(Conversation.FIELD_FROM_NAME, conversation.getFirst_name());
            super.startActivityForResult(intent, 0);*/

            getInstance().onFriendClickListener.doRedirect(conversation);
        }
        else
        {
            ArrayList<String> friend_fb_id = new ArrayList<>();
            friend_fb_id.add(conversation.getFb_id());
            PostFriendModel postFriendModel = new PostFriendModel(
                    AccessToken.getCurrentAccessToken().getUserId(), friend_fb_id);
            showProgressDialog();
            SocialManager.postFriendList(postFriendModel, new OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    dismissProgressDialog();
                    /*final Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra(Conversation.FIELD_PROFILE_IMAGE, conversation.getImg_url());
                    intent.putExtra(Conversation.FIELD_FACEBOOK_ID, conversation.getFb_id());
                    intent.putExtra(Conversation.FIELD_FROM_NAME, conversation.getFirst_name());
                    FriendListFragment.this.startActivityForResult(intent, 0);
                    conversation.setIs_connect("true");*/
                    //adapterrr.notifyDataSetChanged();

                    //Firebase part-----------------------------------
                    String roomId = Utils.BLANK;

                    String a = FirebaseChatManager.fb_id;
                    String b = conversation.getFb_id();

                    if(a.length()<b.length()){
                        roomId = a+"_"+b;
                    }else if(a.length()>b.length()){
                        roomId = b+"_"+a;
                    }else{
                        ArrayList<String> d = new ArrayList<>();
                        d.add(a);
                        d.add(b);
                        Collections.sort(d, new Comparator<String>() {
                            @Override
                            public int compare(String lhs, String rhs) {
                                return lhs.compareToIgnoreCase(rhs);
                            }
                        });

                        StringBuilder sb = new StringBuilder();
                        sb.append(d.get(0)).append("_").append(d.get(1));

                        //String roomId = d.get(0)+"_"+d.get(1);
                        roomId = sb.toString();
                    }

                    final Intent intent = new Intent(getActivity(), FirebaseChatActivity.class);
                    intent.putExtra(Utils.ROOM_ID, roomId);
                    intent.putExtra(Utils.LOAD_ROOM_DETAIL, true);
                    FriendListFragment.this.startActivity(intent);
                    //End of Firebase part-----------------------------------

                }

                @Override
                public void onFailure(int responseCode, String message) {
                    dismissProgressDialog();
                    Toast.makeText(FriendListFragment.this.getActivity()
                            , getActivity().getResources().getString(R.string.socket_timeout_exception)
                                    , Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private ProgressDialog progressDialog;
    private void showProgressDialog()
    {
        progressDialog = ProgressDialog.show(
                FriendListFragment.this.getActivity()
                , ""
                , getActivity().getResources().getString(R.string.wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog()
    {
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public interface OnFriendClickListener
    {
        void doRedirect(FriendListModel.Data.List_social_friends listSocialFriends);
    }
}
