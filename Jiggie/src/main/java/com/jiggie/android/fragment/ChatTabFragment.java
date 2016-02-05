package com.jiggie.android.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.chat.ChatActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.ChatTabListAdapter;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.manager.ChatManager;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.Conversation;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.jiggie.android.model.ExceptionModel;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by rangg on 21/10/2015.
 */
public class ChatTabFragment extends Fragment implements TabFragment, SwipeRefreshLayout.OnRefreshListener, ChatTabListAdapter.ConversationSelectedListener {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    /*@Bind(R.id.contentView)
    ViewGroup contentView;*/
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
    @Bind(R.id.contentView2)
    FrameLayout contentView2;

    private ChatTabListAdapter adapter;
    private boolean isLoading;
    private HomeMain homeMain;
    private View failedView;
    private Handler handler;
    private View emptyView;
    private View rootView;
    private String title;

    @Override
    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public String getTitle() {
        return this.title == null ? (this.title = this.homeMain.getContext().getString(R.string.chat)) : this.title;
    }

    @Override
    public void onTabSelected() {
        App.getInstance().trackMixPanelEvent("Conversations List");
        if ((this.adapter != null) && (this.adapter.getItemCount() == 0))
            this.onRefresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = this.rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, this.rootView);

        this.recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
        this.recyclerView.setAdapter(this.adapter = new ChatTabListAdapter(this, this));
        this.refreshLayout.setOnRefreshListener(this);
        this.handler = new Handler();

        final App app = App.getInstance();
        app.registerReceiver(this.notificationReceived, new IntentFilter(super.getString(R.string.broadcast_notification)));
        app.registerReceiver(this.socialChatReceiver, new IntentFilter(super.getString(R.string.broadcast_social_chat)));

        this.refreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onRefresh();
            }
        });

        if (App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_CHAT, false)) {
            layoutWalkthrough.setVisibility(View.VISIBLE);
            txtWkAction.setPadding(0, 0, Utils.myPixel(getActivity(), 135), Utils.myPixel(getActivity(), 22));
            txtWkAction.setText(getString(R.string.wk_chat_action));
            imgWk.setImageResource(R.drawable.wk_img_chat);
            txtWkTitle.setText(R.string.wk_chat_title);
            txtWkDesc.setText(getResources().getText(R.string.wk_chat_desc));
        }

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
        this.getFailedView().setVisibility(View.GONE);

        ChatManager.loaderChatList(AccessToken.getCurrentAccessToken().getUserId());
    }

    public void onEvent(ChatListModel message){

        adapter.clear();

        for (int i = 0; i < message.getData().getChat_lists().size(); i++)
            adapter.add(message.getData().getChat_lists().get(i));

        isLoading = false;
        if (getContext() != null) {
            getEmptyView().setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(adapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
            refreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
            setHomeTitle();
        }

    }

    public void onEvent(ExceptionModel message){
        isLoading = false;
        if (getContext() != null) {
            Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
            getFailedView().setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onConversationSelected(ChatListModel.Data.ChatLists conversation) {
        final Intent intent = new Intent(super.getActivity(), ChatActivity.class);
        intent.putExtra(Conversation.FIELD_PROFILE_IMAGE, conversation.getProfile_image());
        intent.putExtra(Conversation.FIELD_FACEBOOK_ID, conversation.getFb_id());
        intent.putExtra(Conversation.FIELD_FROM_NAME, conversation.getFromName());
        super.startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (super.getActivity() != null) {
            final String facebookId = data == null ? null : data.getStringExtra(Conversation.FIELD_FACEBOOK_ID);
            final ChatListModel.Data.ChatLists conversation = facebookId == null ? null : this.adapter.find(facebookId);
            boolean changed = false;

            if ((resultCode == Activity.RESULT_OK) && (conversation != null)) {
                conversation.setUnread(0);
                changed = true;
            } else if ((resultCode == ChatActivity.RESULT_BLOCKED) && (conversation != null)) {
                this.adapter.remove(conversation);
                changed = true;
            } else if ((resultCode == ChatActivity.RESULT_CLEARED) && (conversation != null)) {
                conversation.setLast_message(null);
                conversation.setUnread(0);
                changed = true;
            } else if ((resultCode == ChatActivity.RESULT_REPLIED) && (conversation != null)) {
                this.adapter.move(conversation, 0);

                final String lastUpdated = data.getStringExtra(Conversation.FIELD_LAST_UPDATED);
                conversation.setLast_updated(lastUpdated);
                conversation.setUnread(0);
                changed = true;
            }

            if (changed) {
                this.adapter.notifyDataSetChanged();
                this.setHomeTitle();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setHomeTitle() {
        if (this.homeMain != null) {
            final int unreadCount = this.adapter.countUnread();
            if (unreadCount > 0)
                this.title = String.format("%s (%d)", getString(R.string.chat), unreadCount);
            else this.title = super.getString(R.string.chat);
            this.homeMain.onTabTitleChanged(this);
        }
    }

    private View getEmptyView() {
        if (this.emptyView == null) {
            //Added by Aga
            contentView2.setVisibility(View.VISIBLE);
            //-------------

            this.emptyView = LayoutInflater.from(super.getContext()).inflate(R.layout.fragment_chat_empty, this.contentView2, false);
            this.contentView2.addView(this.emptyView);
        }
        return this.emptyView;
    }

    private View getFailedView() {
        if (this.failedView == null) {

            //Added by Aga
            contentView2.setVisibility(View.VISIBLE);
            //-------------

            this.failedView = LayoutInflater.from(super.getContext()).inflate(R.layout.view_failed, this.contentView2, false);
            this.failedView.findViewById(R.id.btnRetry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefresh();
                }
            });
            this.contentView2.addView(this.failedView);
        }
        return this.failedView;
    }

    private BroadcastReceiver notificationReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // check getContext() to make sure the fragment is not destroyed.
            if (getContext() != null) {
                handler.removeCallbacks(checkNewMessageRunnable);
                handler.postDelayed(checkNewMessageRunnable, getResources().getInteger(R.integer.chat_notif_delay));
            }
        }
    };

    private BroadcastReceiver socialChatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            notificationReceived.onReceive(context, intent);
        }
    };

    private Runnable checkNewMessageRunnable = new Runnable() {
        @Override
        public void run() {
            if ((getContext() != null) && (!refreshLayout.isRefreshing()))
                onRefresh();
        }
    };

    @Override
    public void onDestroy() {
        final App app = App.getInstance();
        app.unregisterReceiver(this.notificationReceived);
        app.unregisterReceiver(this.socialChatReceiver);

        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.layout_walkthrough)
    void walkthroughOnClick() {
        Utils.SHOW_WALKTHROUGH_CHAT = false;
        App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_CHAT, false).commit();
        layoutWalkthrough.setVisibility(View.GONE);
    }

}
