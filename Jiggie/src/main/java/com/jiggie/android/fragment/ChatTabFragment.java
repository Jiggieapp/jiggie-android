package com.jiggie.android.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.chat.ChatActivity;
import com.jiggie.android.api.OnResponseListener;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.ChatTabListAdapter;
import com.jiggie.android.manager.ChatManager;
import com.jiggie.android.manager.WalkthroughManager;
import com.jiggie.android.model.ChatActionModel;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.Conversation;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.PostWalkthroughModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rangg on 21/10/2015.
 */
public class ChatTabFragment extends Fragment implements TabFragment, SwipeRefreshLayout.OnRefreshListener
        , ChatTabListAdapter.ConversationSelectedListener, ChatTabListAdapter.ConversationLongClickListener {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    /*@Bind(R.id.contentView)
    ViewGroup contentView;*/
    @Bind(R.id.contentView2)
    FrameLayout contentView2;

    public ChatTabListAdapter adapter;
    private boolean isLoading;
    protected HomeMain homeMain;
    private View failedView;
    private View emptyView;
    private View rootView;
    private String title;

    private Dialog dialogWalkthrough;
    private Dialog dialogLongClick;
    private ChatListModel.Data.ChatLists conversation;
    public static final String TAG = ChatTabFragment.class.getSimpleName();
    private final static int INTERVAL = 1000 *5; //5 detik
    private Handler handler;
    private static ChatTabFragment instance;

    public static ChatTabFragment getInstance()
    {
        if(instance == null)
            instance = new ChatTabFragment();
        return instance;
    }


    @Override
    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public String getTitle() {
        //return this.title == null ? (this.title = this.homeMain.getContext().getString(R.string.chat)) : this.title;
        return this.homeMain.getContext().getString(R.string.chat);
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_chat_white_24dp;
    }

    @Override
    public void onTabSelected() {
        App.getInstance().trackMixPanelEvent("Conversations List");

        if (App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_CHAT, false)) {
            //showWalkthroughDialog();
        }

        //if ((this.adapter != null) && (this.adapter.getItemCount() == 0)||ChatManager.NEED_REFRESH_CHATLIST)
        if (getInstance().adapter != null && (getInstance().adapter.getItemCount() == 0)){
            this.onRefresh();
        }

        //startRepeatingTask();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = this.rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    protected void setAdapter()
    {
        getInstance().adapter = new ChatTabListAdapter(this, this, this);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
        this.recyclerView.setAdapter(getInstance().adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, this.rootView);

        setAdapter();
        this.refreshLayout.setOnRefreshListener(this);
        this.handler = new Handler();

        final App app = App.getInstance();
        app.registerReceiver(this.notificationReceived, new IntentFilter(super.getString(R.string.broadcast_notification)));
        app.registerReceiver(this.socialChatReceiver, new IntentFilter(super.getString(R.string.broadcast_social_chat)));
        app.registerReceiver(chatCounterBroadCastReceiver
                , new IntentFilter(TAG));

        this.refreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onRefresh();
            }
        });

        //wandy 08-05-2016
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                refreshLayout.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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

        fetchChat();
    }

    protected void fetchChat()
    {
        //ChatManager.loaderChatList(AccessToken.getCurrentAccessToken().getUserId());
        ChatManager.loaderChatList2(AccessToken.getCurrentAccessToken().getUserId()
                , new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                onEvent((ChatListModel) object);
                if(getContext() != null)
                {
                    Intent i = new Intent(Utils.CHECK_NEW_MESSAGE_RECEIVER);
                    getActivity().sendBroadcast(i);
                }
            }

            @Override
            public void onFailure(ExceptionModel exceptionModel) {
                onEvent(exceptionModel);
            }
        });
    }

    public void onEvent(ChatListModel message){
        adapter.clear();
        for (int i = 0; i < message.getData().getChat_lists().size(); i++)
            getInstance().adapter.add(message.getData().getChat_lists().get(i));
        isLoading = false;
        if (getContext() != null) {
            getEmptyView().setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(adapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
            if(refreshLayout.isRefreshing())
                refreshLayout.setRefreshing(false);
            getInstance().adapter.notifyDataSetChanged();
            setHomeTitle();
        }
    }

    public void onEvent(ChatActionModel message){
        String from = message.getFrom();
        boolean changed = false;
        if(from.equals(Utils.FROM_BLOCK_CHAT)){
            App.getInstance().trackMixPanelEvent("Block User");
            getInstance().adapter.remove(conversation);
            changed = true;
        }else if(from.equals(Utils.FROM_DELETE_CHAT)){
            App.getInstance().trackMixPanelEvent("Delete Messages");
            conversation.setLast_message(null);
            conversation.setUnread(0);

            getInstance().adapter.remove(conversation);
            changed = true;
        }
        if(changed){
            getInstance().adapter.notifyDataSetChanged();
            this.setHomeTitle();
        }
    }

    public void onEvent(ExceptionModel message) {
        String from = message.getFrom();
        if(from.equals(Utils.FROM_CHAT)){
            isLoading = false;
            if (getContext() != null) {
                String msg = message.getMessage();
                if(msg.equals(Utils.MSG_EMPTY_DATA)){
                    getEmptyView().setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    getFailedView().setVisibility(View.VISIBLE);
                }
                recyclerView.setVisibility(View.GONE);
                if(refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
            }
        }else if(from.equals(Utils.FROM_BLOCK_CHAT)||from.equals(Utils.FROM_DELETE_CHAT)){
            Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onConversationLongClick(ChatListModel.Data.ChatLists conversation) {
        this.conversation = conversation;
        showLongClickDialog(conversation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (super.getActivity() != null) {
            final String facebookId = data == null ? null : data.getStringExtra(Conversation.FIELD_FACEBOOK_ID);
            final ChatListModel.Data.ChatLists conversation
                    = facebookId == null ? null : getInstance().adapter.find(facebookId);
            boolean changed = false;
            boolean fromReplied = false;

            if ((resultCode == Activity.RESULT_OK) && (conversation != null)) {
                conversation.setUnread(0);
                changed = true;
            } else if ((resultCode == ChatActivity.RESULT_BLOCKED) && (conversation != null)) {
                getInstance().adapter.remove(conversation);
                changed = true;
            } else if ((resultCode == ChatActivity.RESULT_CLEARED) && (conversation != null)) {
                conversation.setLast_message(null);
                conversation.setUnread(0);
                changed = true;
            } else if ((resultCode == ChatActivity.RESULT_REPLIED) && (conversation != null)) {
                getInstance().adapter.move(conversation, 0);
                final String lastUpdated = data.getStringExtra(Conversation.FIELD_LAST_UPDATED);
                conversation.setLast_updated(lastUpdated);
                conversation.setUnread(0);
                changed = true;
                fromReplied = true;
            }

            if (changed) {
                if(fromReplied){
                    onRefresh();
                }else {
                    getInstance().adapter.notifyDataSetChanged();
                    this.setHomeTitle();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int unreadCount = 0;
    private void setHomeTitle() {
        if (this.homeMain != null) {
            unreadCount = getInstance().adapter.countUnread();
            /*if (unreadCount > 0)
                this.title = String.format("%s (%d)", getString(R.string.chat), unreadCount);*/
            if(unreadCount > 0)
            {
                if(unreadCount > 99)
                {
                    this.title = "99";
                }
                else
                {
                    this.title = unreadCount + "";
                }
            }
            else if(unreadCount <= 0)
            {
                unreadCount = 0;
                this.title = "0";
            }

            //else this.title = super.getString(R.string.chat);
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
            if ((getContext() != null) && (!refreshLayout.isRefreshing())) {
                //ChatManager.NEED_REFRESH_CHATLIST = true;
                onRefresh();
            }
        }
    };

    @Override
    public void onDestroy() {
        final App app = App.getInstance();
        app.unregisterReceiver(this.notificationReceived);
        app.unregisterReceiver(this.socialChatReceiver);
        app.unregisterReceiver(this.chatCounterBroadCastReceiver);
        /*if(handler != null)
            handler.removeCallbacks(mHandlerTask);*/
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void showWalkthroughDialog() {
        dialogWalkthrough = new Dialog(getActivity());
        dialogWalkthrough.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWalkthrough.setContentView(R.layout.walkthrough_screen);
        dialogWalkthrough.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogWalkthrough.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT
                , WindowManager.LayoutParams.MATCH_PARENT);

        RelativeLayout layout = (RelativeLayout)dialogWalkthrough.findViewById(R.id.layout_walkthrough);
        ImageView imgWk = (ImageView)dialogWalkthrough.findViewById(R.id.img_wk);
        TextView txtWkAction = (TextView)dialogWalkthrough.findViewById(R.id.txt_wk_action);
        TextView txtWkTitle = (TextView)dialogWalkthrough.findViewById(R.id.txt_wk_title);
        TextView txtWkDesc = (TextView)dialogWalkthrough.findViewById(R.id.txt_wk_desc);
        txtWkAction.setPadding(0, 0, Utils.myPixel(getActivity(), 135), Utils.myPixel(getActivity(), 22));
        txtWkAction.setText(getString(R.string.wk_chat_action));
        imgWk.setImageResource(R.drawable.wk_img_chat);
        txtWkTitle.setText(R.string.wk_chat_title);
        txtWkDesc.setText(getResources().getText(R.string.wk_chat_desc));

        dialogWalkthrough.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Utils.SHOW_WALKTHROUGH_CHAT = false;
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_CHAT, false).commit();

                PostWalkthroughModel postWalkthroughModel = new PostWalkthroughModel(AccessToken.getCurrentAccessToken().getUserId(), Utils.TAB_CHAT, Utils.DEVICE_ID);
                WalkthroughManager.loaderPostWalkthrough(postWalkthroughModel);
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.SHOW_WALKTHROUGH_CHAT = false;
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_CHAT, false).commit();
                dialogWalkthrough.dismiss();

                PostWalkthroughModel postWalkthroughModel = new PostWalkthroughModel(AccessToken.getCurrentAccessToken().getUserId(), Utils.TAB_CHAT, Utils.DEVICE_ID);
                WalkthroughManager.loaderPostWalkthrough(postWalkthroughModel);
            }
        });

        dialogWalkthrough.setCanceledOnTouchOutside(true);
        dialogWalkthrough.setCancelable(true);
        dialogWalkthrough.show();
    }

    public void showLongClickDialog(final ChatListModel.Data.ChatLists conversation) {
        String block = "Block "+conversation.getFromName();
        String[] menu = {block, "Delete Chat"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()/*, R.style.fullHeightDialog*/)
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //Log.d("tes", AccessToken.getCurrentAccessToken().getUserId()+"  "+conversation.getFb_id()+"  "+conversation.getFromId() );
                                ChatManager.loaderBlockChat(AccessToken.getCurrentAccessToken().getUserId(), conversation.getFb_id());
                                dialogLongClick.dismiss();
                                break;
                            case 1:
                                ChatManager.loaderDeleteChat(AccessToken.getCurrentAccessToken().getUserId(), conversation.getFb_id());
                                dialogLongClick.dismiss();
                                break;
                            default:
                                dialogLongClick.dismiss();
                                break;
                        }
                    }
                });
        dialogLongClick = builder.create();
        dialogLongClick.show();
    }


    BroadcastReceiver chatCounterBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String facebookId  = intent.getStringExtra(Conversation.FIELD_FACEBOOK_ID);
            if(!facebookId.equals(""))
            {
                final ChatListModel.Data.ChatLists conversation
                        = facebookId == null ? null
                        : getInstance().adapter.find(facebookId);
                if(conversation != null)
                {
                    conversation.setUnread(0);
                    getInstance().adapter.notifyDataSetChanged();
                    setHomeTitle();
                }
            }
        }
    };

    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            fetchChat();
            if(handler == null)
                handler = new Handler();
            handler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        if(handler != null)
            handler.removeCallbacks(mHandlerTask);
    }
}
