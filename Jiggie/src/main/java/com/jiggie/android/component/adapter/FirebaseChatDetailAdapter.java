package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.model.MessagesModel;
import com.jiggie.android.model.RoomModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 6/17/2016.
 */
public class FirebaseChatDetailAdapter extends RecyclerView.Adapter<FirebaseChatDetailAdapter.ViewHolder> {

    Activity a;
    ArrayList<MessagesModel> data = new ArrayList<>();
    public static final int TYPE_GROUP = 2;
    public static final int TYPE_PRIVATE = 1;

    public FirebaseChatDetailAdapter(Activity a, ArrayList<MessagesModel> data) {
        this.a = a;
        this.data = data;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessagesModel messageModel = data.get(position);
        holder.txtAuthor.setText(messageModel.getName());
        holder.txtBody.setText(messageModel.getMessage());
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
