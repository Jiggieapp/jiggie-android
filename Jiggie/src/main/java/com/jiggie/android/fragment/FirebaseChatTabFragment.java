package com.jiggie.android.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.chat.FirebaseChatActivity;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.api.OnResponseListener;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.FirebaseChatTabListAdapter;
import com.jiggie.android.manager.ChatManager;
import com.jiggie.android.manager.FirebaseChatManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.FLRefreshModel;
import com.jiggie.android.model.RoomModel;
import com.jiggie.android.model.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by LTE on 6/19/2016.
 */
public class FirebaseChatTabFragment extends Fragment implements TabFragment, SwipeRefreshLayout.OnRefreshListener
        , FirebaseChatTabListAdapter.RoomSelectedListener, FirebaseChatTabListAdapter.RoomLongClickListener {

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    /*@Bind(R.id.contentView)
    ViewGroup contentView;*/
    @Bind(R.id.contentView2)
    FrameLayout contentView2;

    public FirebaseChatTabListAdapter adapter;
    private boolean isLoading = false;
    protected HomeMain homeMain;
    private View failedView;
    private View emptyView;
    private View rootView;
    private String title;

    private Dialog dialogLongClick;
    public static final String TAG = FirebaseChatTabFragment.class.getSimpleName();
    private final static int INTERVAL = 1000 *5; //5 detik
    private Handler handler;
    private static FirebaseChatTabFragment instance;

    ValueEventListener userEvent, roomEvent;
    //ArrayList<UserModel> arrUser = new ArrayList<>();

    public static FirebaseChatTabFragment getInstance()
    {
        if(instance == null)
            instance = new FirebaseChatTabFragment();
        return instance;
    }

    @Override
    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public String getTitle() {
        return this.homeMain.getContext().getString(R.string.chat);
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_chat_white_24dp;
    }

    @Override
    public void onTabSelected() {
        App.getInstance().trackMixPanelEvent("Conversations List");

        if (getInstance().adapter != null && (getInstance().adapter.getItemCount() == 0)){
            this.onRefresh();
        }
    }

    protected int getLayout()
    {
        return R.layout.fragment_recycler;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = this.rootView = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);

        this.refreshLayout.setOnRefreshListener(this);
        this.handler = new Handler();

        final App app = App.getInstance();
        /*app.registerReceiver(this.notificationReceived, new IntentFilter(super.getString(R.string.broadcast_notification)));
        app.registerReceiver(this.socialChatReceiver, new IntentFilter(super.getString(R.string.broadcast_social_chat)));
        app.registerReceiver(chatCounterBroadCastReceiver
                , new IntentFilter(TAG));*/

        this.recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
        this.refreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onRefresh();
            }
        });
        this.refreshLayout.setDistanceToTriggerSync(999999);

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

        return view;
    }

    protected void setAdapters()
    {
        if(FirebaseChatManager.arrAllRoom.size()==FirebaseChatManager.arrAllRoomMembers.size()){

            Collections.sort(FirebaseChatManager.arrAllRoom, new Comparator<RoomModel>() {
                @Override
                public int compare(RoomModel lhs, RoomModel rhs) {
                    return rhs.getInfo().getUpdate().compareTo(lhs.getInfo().getUpdate());
                }
            });

            if(adapter==null){
                getInstance().adapter = new FirebaseChatTabListAdapter(FirebaseChatTabFragment.this, FirebaseChatManager.arrAllRoom, this, this);
                recyclerView.setAdapter(adapter);

                /*getEmptyView().setVisibility(getInstance().adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(getInstance().adapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);*/
            }else{
                adapter.notifyDataSetChanged();

            }
            this.setHomeTitle();
        }

        this.recyclerView.setAdapter(adapter);

        refreshLayout.setRefreshing(false);
        this.isLoading = false;
        try{
            getEmptyView().setVisibility(View.GONE);
        }catch (Exception e){
            Log.d(TAG, e.toString());
        }
        recyclerView.setVisibility(View.VISIBLE);
        contentView2.setVisibility(View.GONE);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*ButterKnife.bind(this, this.rootView);

        if(!EventBus.getDefault().isRegistered(this))
            registerEventBus();*/
    }

    @Override
    public void onRefresh() {
        ChatManager.loaderMigrateChatFirebase(FirebaseChatManager.fb_id, new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                refreshData();
            }

            @Override
            public void onFailure(ExceptionModel exceptionModel) {
                Log.e(TAG, exceptionModel.toString());
                refreshLayout.setRefreshing(false);
                isLoading = false;
;            }
        });
        //refreshData();
    }

    private void refreshData(){
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

        if(FirebaseChatManager.arrUser.size()==0){
            getUsers();
        }
        //arrUser = FirebaseChatManager.getArrUser();
        getRoomMembers();
    }

    private int unreadCount = 0;
    private void setHomeTitle() {
        if (this.homeMain != null) {
            unreadCount = getInstance().adapter.countBadge();
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

            FirebaseChatManager.badgeChat = String.valueOf(unreadCount);
            this.homeMain.onTabTitleChanged(this);

        }
    }

    private View getEmptyView() {
        this.emptyView = null;
        if(contentView2.getChildCount()>0){
            contentView2.removeAllViews();
        }
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

    private void getUsers(){
        Query queryUsers = FirebaseChatManager.getQueryUsers();
        userEvent = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean isDataExist = dataSnapshot.exists();

                if(isDataExist){
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                        boolean isData2Exist = messageSnapshot.exists();
                        boolean isNameExist = messageSnapshot.child("name").exists();
                        boolean isAvatarExist = messageSnapshot.child("avatar").exists();

                        if(isData2Exist&&isNameExist&&isAvatarExist){
                            String key = (String) messageSnapshot.getKey();
                            //String fb_id = String.valueOf(messageSnapshot.child("fb_id").getValue());
                            String name = (String) messageSnapshot.child("name").getValue();
                            String avatar = (String) messageSnapshot.child("avatar").getValue();

                            UserModel userModel = new UserModel(key, key, name, avatar);
                            FirebaseChatManager.arrUser.add(userModel);
                        }else{
                            //do nothing
                        }

                    }

                    /*String sd = String.valueOf(new Gson().toJson(FirebaseChatManager.arrUser));
                    Log.d("sd","sd");*/
                }else{
                    //do nothing
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                refreshLayout.setRefreshing(false);
                isLoading = false;
            }
        };
        queryUsers.addValueEventListener(userEvent);
    }

    private void getRoomMembers(){
        Query queryRoomMembers = FirebaseChatManager.getQueryRoomMembers(FirebaseChatManager.fb_id);
        if(queryRoomMembers!=null){
            roomEvent = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseChatManager.arrAllRoomMembers.clear();
                    FirebaseChatManager.arrAllRoom.clear();

                    boolean isDataExist = dataSnapshot.exists();

                    if(isDataExist){
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                            boolean isDatasExist = messageSnapshot.exists();

                            if(isDatasExist){
                                String key = (String)messageSnapshot.getKey();
                                FirebaseChatManager.arrAllRoomMembers.add(key);

                                getRoomDetail(key);
                            }else{
                                //do nothing
                            }

                        }



                        /*Log.d("sd",String.valueOf(sizeRoom));
                        String sd = String.valueOf(new Gson().toJson(FirebaseChatManager.arrAllRoom));*/
                    }else {
                        //do nothing
                    }

                    sizeRoom = FirebaseChatManager.arrAllRoomMembers.size();

                    if(sizeRoom==0){
                        refreshLayout.setRefreshing(false);
                        isLoading = false;
                        getEmptyView().setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        contentView2.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    refreshLayout.setRefreshing(false);
                    isLoading = false;
                }
            };
            queryRoomMembers.addValueEventListener(roomEvent);
        }

    }


    int sizeRoom = 0;
    private void getRoomDetail(String key){
        Query queryRoom = FirebaseChatManager.getQueryRoom(key);
        queryRoom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean isDataExist = dataSnapshot.exists();
                boolean isInfoExist = dataSnapshot.child("info").exists();
                boolean isTypeExist = dataSnapshot.child("type").exists();

                if(isDataExist&&isInfoExist&&isTypeExist){
                    String key = (String) dataSnapshot.getKey();
                    long type = (long) dataSnapshot.child("type").getValue();

                    RoomModel.Info info = null;
                    if(type==FirebaseChatManager.TYPE_GROUP){
                        String event = (String) dataSnapshot.child("info").child("event").getValue();
                        String avatar = (String) dataSnapshot.child("info").child("avatar").getValue();
                        String last_message = (String) dataSnapshot.child("info").child("last_message").getValue();
                        long created_at = (long) dataSnapshot.child("info").child("created_at").getValue();
                        long updated_at = (long) dataSnapshot.child("info").child("updated_at").getValue();

                        info = new RoomModel.Info(event, avatar, event, last_message, created_at, updated_at);
                    }else{
                        String event = (String) dataSnapshot.child("info").child("event").getValue();
                        String identifier = (String) dataSnapshot.child("info").child("identifier").getValue();
                        String last_message = (String) dataSnapshot.child("info").child("last_message").getValue();
                        long created_at = (long) dataSnapshot.child("info").child("created_at").getValue();
                        long updated_at = (long) dataSnapshot.child("info").child("updated_at").getValue();

                        String id1 = identifier.substring(0, identifier.indexOf("_"));
                        String id2 = identifier.substring(identifier.indexOf("_")+1, identifier.length());

                        String name = Utils.BLANK;
                        String avatar = Utils.BLANK;
                        String idFriend = Utils.BLANK;
                        if(FirebaseChatManager.fb_id.equals(id1)){
                            idFriend = id2;
                        }else {
                            idFriend = id1;
                        }

                        for(int i=0;i<FirebaseChatManager.arrUser.size();i++){
                            String idUser = FirebaseChatManager.arrUser.get(i).getFb_id();
                            if(idFriend.equals(idUser)){
                                name = FirebaseChatManager.arrUser.get(i).getName();
                                avatar = FirebaseChatManager.arrUser.get(i).getAvatar();
                                break;
                            }else{
                                //do nothing
                            }
                        }

                        info = new RoomModel.Info(name, avatar, event, last_message, created_at, updated_at);
                    }

                    ArrayList<RoomModel.Unread> arrUnread = new ArrayList<RoomModel.Unread>();
                    for (DataSnapshot unreadSnapshot: dataSnapshot.child("info").child("unread").getChildren()) {
                        String fb_id = (String)unreadSnapshot.getKey();
                        long counter = (long)unreadSnapshot.getValue();

                        RoomModel.Unread unread = new RoomModel.Unread(fb_id, counter);
                        arrUnread.add(unread);
                    }

                    RoomModel roomModel = new RoomModel(key, info, type, arrUnread);

                    //checker if already just update, if not then added---------------------
                    if(FirebaseChatManager.arrAllRoom.size()==0){
                        FirebaseChatManager.arrAllRoom.add(roomModel);
                    }else{
                        boolean isExist = false;
                        int existPosition = 0;
                        for(int i=0; i<FirebaseChatManager.arrAllRoom.size();i++){
                            String keyMatch = FirebaseChatManager.arrAllRoom.get(i).getKey();
                            if(key.equals(keyMatch)){
                                existPosition = i;
                                isExist = true;
                                break;
                            }
                        }

                        if(isExist){
                            FirebaseChatManager.arrAllRoom.set(existPosition, roomModel);
                        }else{
                            FirebaseChatManager.arrAllRoom.add(roomModel);
                        }
                    }
                    //--------------------------------------------------------------------------

                    String sds = String.valueOf(new Gson().toJson(FirebaseChatManager.arrAllRoom));
                    Log.d("sd","sd");

                    setAdapters();
                }else{
                    //do nothing
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                refreshLayout.setRefreshing(false);
                isLoading = false;
            }
        });
    }

    @Override
    public void onRoomSelected(RoomModel roomModel) {
        Intent i = new Intent(getActivity(), FirebaseChatActivity.class);
        i.putExtra(Utils.ROOM_ID, roomModel.getKey());
        i.putExtra(Utils.ROOM_TYPE, roomModel.getType());
        i.putExtra(Utils.ROOM_EVENT, roomModel.getInfo().getEvent());
        super.startActivityForResult(i, 0);
    }

    @Override
    public void onRoomLongClick(RoomModel roomModel) {
        showLongClickDialog(roomModel);
    }

    public void showLongClickDialog(final RoomModel roomModel) {

        if(roomModel.getType()==FirebaseChatManager.TYPE_PRIVATE){
            final String block = "Block "+roomModel.getInfo().getName();
            final String delete = "Delete "+roomModel.getInfo().getName();
            String[] menu = {block, delete};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()/*, R.style.fullHeightDialog*/)
                    .setItems(menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    //FirebaseChatManager.blockChatList(roomModel.getKey(), FirebaseChatManager.fb_id);
                                    new AlertDialog.Builder(getActivity())
                                            .setMessage(R.string.confirmation)
                                            .setTitle(block)
                                            .setNegativeButton(android.R.string.no, null)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    blockChatList(roomModel.getKey(), roomModel);
                                                }
                                            }).show();

                                    dialogLongClick.dismiss();
                                    break;
                                case 1:
                                    //FirebaseChatManager.deleteChatList(roomModel.getKey(), FirebaseChatManager.fb_id);
                                    new AlertDialog.Builder(getActivity())
                                            .setMessage(R.string.confirmation)
                                            .setTitle(delete)
                                            .setNegativeButton(android.R.string.no, null)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    deleteChatList(roomModel.getKey());
                                                }
                                            }).show();
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
        }else{
            String[] menu = {"Exit Group"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()/*, R.style.fullHeightDialog*/)
                    .setItems(menu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    //FirebaseChatManager.blockChatList(roomModel.getKey(), FirebaseChatManager.fb_id);
                                    new AlertDialog.Builder(getActivity())
                                            .setMessage(R.string.confirmation)
                                            .setTitle("Exit Group")
                                            .setNegativeButton(android.R.string.no, null)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    blockChatList(roomModel.getKey(), roomModel);
                                                }
                                            }).show();

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


    }

    private void deleteChatList(String roomId){

        String id1 = roomId.substring(0, roomId.indexOf("_"));
        String id2 = roomId.substring(roomId.indexOf("_")+1, roomId.length());

        String idFriend = Utils.BLANK;
        if(FirebaseChatManager.fb_id.equals(id1)){
            idFriend = id2;
        }else {
            idFriend = id1;
        }

        HashMap<String, Object> deleteModel = new HashMap<>();
        deleteModel.put("fb_id", FirebaseChatManager.fb_id);
        deleteModel.put("member_fb_id", idFriend);

        ChatManager.loaderDeleteChatFirebase(deleteModel, new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                //do nothing
            }

            @Override
            public void onFailure(ExceptionModel exceptionModel) {
                //do nothing
            }
        });
    }

    private void blockChatList(String roomId, RoomModel roomModel){
        FirebaseChatManager.isNeedRefreshFriend = true;
        HashMap<String, Object> blockModel = new HashMap<>();
        blockModel.put("fb_id", FirebaseChatManager.fb_id);

        if(roomModel.getType()==FirebaseChatManager.TYPE_PRIVATE){
            String id1 = roomId.substring(0, roomId.indexOf("_"));
            String id2 = roomId.substring(roomId.indexOf("_")+1, roomId.length());

            String idFriend = Utils.BLANK;
            if(FirebaseChatManager.fb_id.equals(id1)){
                idFriend = id2;
            }else {
                idFriend = id1;
            }
            blockModel.put("member_fb_id", idFriend);
        }
        else{
            blockModel.put("member_fb_id", Utils.BLANK);
        }

        blockModel.put("type", String.valueOf(roomModel.getType()));
        blockModel.put("room_id", roomId);

        ChatManager.loaderBlockChatFirebase(blockModel, new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                //do nothing
                EventBus.getDefault().post(new FLRefreshModel(true));
            }

            @Override
            public void onFailure(ExceptionModel exceptionModel) {
                //do nothing
            }
        });

    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }*/
}
