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
    private RoomSelectedListener listener;
    private RoomLongClickListener longClickListener;

    public FirebaseChatListAdapter(Activity a, ArrayList<RoomModel> data, RoomSelectedListener listener, RoomLongClickListener longClickListener) {
        this.a = a;
        this.data = data;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoomModel roomModel = data.get(position);
        long type = roomModel.getType();
        holder.roomModel = roomModel;
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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_firebase_chatlist, parent, false), listener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        @Bind(R.id.txt_author)
        TextView txtAuthor;
        @Bind(R.id.txt_body)
        TextView txtBody;

        private RoomSelectedListener listener;
        private RoomLongClickListener longClickListener;
        RoomModel roomModel;

        public ViewHolder(View itemView, RoomSelectedListener listener, RoomLongClickListener longClickListener) {
            super(itemView);
            this.listener = listener;
            this.longClickListener = longClickListener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null)
                this.listener.onRoomSelected(this.roomModel);
        }

        @Override
        public boolean onLongClick(View v) {
            if (this.longClickListener != null)
                this.longClickListener.onRoomLongClick(this.roomModel);
            return true;
        }
    }

    public interface RoomSelectedListener {
        void onRoomSelected(RoomModel roomModel);
    }

    public interface RoomLongClickListener {
        void onRoomLongClick(RoomModel roomModel);
    }
}
