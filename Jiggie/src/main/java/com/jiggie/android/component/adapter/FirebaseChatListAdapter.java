package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.model.RoomModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 6/15/2016.
 */
public class FirebaseChatListAdapter extends RecyclerView.Adapter<FirebaseChatListAdapter.ViewHolder> {

    Activity a;
    ArrayList<RoomModel> data = new ArrayList<>();
    public static final int TYPE_GROUP = 2;
    public static final int TYPE_PRIVATE = 1;


    public FirebaseChatListAdapter(Activity a, ArrayList<RoomModel> data) {
        this.a = a;
        this.data = data;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoomModel roomModel = data.get(position);
        long type = roomModel.getType();
        if(type==TYPE_GROUP){
            holder.txtAuthor.setText(roomModel.getInfo().getName());
            holder.txtBody.setText(roomModel.getInfo().getLast_message());
        }else{
            holder.txtAuthor.setText(roomModel.getInfo().getEvent());
            holder.txtBody.setText(roomModel.getInfo().getLast_message());
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_firebase_chatlist, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_author)
        TextView txtAuthor;
        @Bind(R.id.txt_body)
        TextView txtBody;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
