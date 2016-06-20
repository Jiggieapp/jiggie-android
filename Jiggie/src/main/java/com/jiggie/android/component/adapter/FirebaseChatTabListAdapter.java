package com.jiggie.android.component.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.manager.FirebaseChatManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.RoomModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 6/19/2016.
 */
public class FirebaseChatTabListAdapter extends RecyclerView.Adapter<FirebaseChatTabListAdapter.ViewHolder> {

    private RoomSelectedListener listener;
    private RoomLongClickListener longClickListener;
    private Fragment fragment;
    private static final String TAG = ChatTabListAdapter.class.getSimpleName();

    ArrayList<RoomModel> data = new ArrayList<>();

    public FirebaseChatTabListAdapter(Fragment fragment, ArrayList<RoomModel> data, RoomSelectedListener listener, RoomLongClickListener longClickListener) {
        this.data = data;
        this.fragment = fragment;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false), this.listener, this.longClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //try {

            RoomModel roomModel = data.get(position);
            long type = roomModel.getType();
            holder.roomModel = roomModel;
            if(type== FirebaseChatManager.TYPE_GROUP){
                holder.txtUser.setText(roomModel.getInfo().getName());
                holder.txtMessage.setText(roomModel.getInfo().getLast_message());
            }else{
                holder.txtUser.setText(roomModel.getInfo().getEvent());
                holder.txtMessage.setText(roomModel.getInfo().getLast_message());
            }

            /*final Date date = Common.ISO8601_DATE_FORMAT_UTC.parse(DateFormat.format("MM/dd/yyyy", new Date(roomModel.getInfo().getUpdated_at())).toString());
            String simpleDate = Common.SIMPLE_12_HOUR_FORMAT.format(date).replace("AM", "").replace("PM", "").trim();*/

            //holder.txtTime.setText(simpleDate);
            int unread = data.get(position).getInfo().getUnread();
            holder.txtUnread.setText(String.valueOf(unread));
            holder.txtUnread.setVisibility(unread == 0 ? View.INVISIBLE : View.VISIBLE);
            holder.txtTime.setTextColor(ContextCompat.getColor(this.fragment.getContext(), unread == 0 ? android.R.color.darker_gray : R.color.colorAccent));

            //Added by Aga 12-2-2016---
            String urlImage = urlImage = data.get(position).getInfo().getAvatar();
            /*if(item.getProfile_image()!=null){
                urlImage = item.getProfile_image();
            }else{
                final int width = holder.imageView.getWidth() * 2;
                urlImage = App.getFacebookImage(item.getFb_id(), width);
            }*/
            //---------
            Glide.with(this.fragment).load(urlImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(fragment.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    super.getView().setImageDrawable(circularBitmapDrawable);
                }
            });

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //fragment.getActivity().startActivity(new Intent(fragment.getActivity(), ProfileDetailActivity.class).putExtra(Common.FIELD_FACEBOOK_ID, item.getFb_id()));
                }
            });

        /*} catch (ParseException e) {
            throw new RuntimeException(e);
        }*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.txtUser)
        TextView txtUser;
        @Bind(R.id.txtMessage) TextView txtMessage;
        @Bind(R.id.txtTime) TextView txtTime;
        @Bind(R.id.txtUnread) TextView txtUnread;

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

    public int countUnread() {
        final int length = this.data.size();
        int unreadCount = 0;

        for (int i = 0; i < length; i++)
            unreadCount += this.data.get(i).getInfo().getUnread() > 0 ? 1 : 0;

        return unreadCount;
    }

}
