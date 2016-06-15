package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.Query;

import butterknife.ButterKnife;

/**
 * Created by LTE on 6/15/2016.
 */
public class FirebaseChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity a;
    Query query;

    public FirebaseChatListAdapter(Activity a, Query query){
        this.a = a;
        this.query = query;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    static class ViewHolderRoom extends RecyclerView.ViewHolder{
        public ViewHolderRoom(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
