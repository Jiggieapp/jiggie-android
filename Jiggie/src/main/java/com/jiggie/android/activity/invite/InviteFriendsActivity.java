package com.jiggie.android.activity.invite;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.InviteFriendsAdapter;

import butterknife.Bind;

/**
 * Created by LTE on 5/12/2016.
 */
public class InviteFriendsActivity extends ToolbarActivity implements SwipeRefreshLayout.OnRefreshListener, InviteFriendsAdapter.InviteSelectedListener {

    @Bind(R.id.appBar)
    AppBarLayout appBar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    InviteFriendsAdapter adapter;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_invite_friends);
        super.bindView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Invite Friend by Phone");

        this.recyclerView.setAdapter(this.adapter = new InviteFriendsAdapter(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onInviteSelected() {

    }
}
