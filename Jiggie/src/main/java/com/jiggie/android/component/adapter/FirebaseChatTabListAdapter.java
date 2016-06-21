package com.jiggie.android.component.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.component.Utils;
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

            final RoomModel roomModel = data.get(position);
            long type = roomModel.getType();
            holder.roomModel = roomModel;
            if(type== FirebaseChatManager.TYPE_GROUP){
                holder.txtUser.setText(roomModel.getInfo().getName());
                holder.txtMessage.setText(roomModel.getInfo().getLast_message());
            }else{
                holder.txtUser.setText(roomModel.getInfo().getName());
                holder.txtMessage.setText(roomModel.getInfo().getLast_message());
            }

            try {
                String dates = getSimpleDate(Common.ISO8601_DATE_FORMAT.format(new Date(roomModel.getInfo().getUpdated_at())));
                holder.txtTime.setText(dates);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            //int unread = roomModel.getInfo().getUnread();
            Log.d(TAG+" countBadge", String.valueOf(countBadge()));
            long unread = getUnreadCounter(roomModel);
            holder.txtUnread.setText(String.valueOf(unread));
            holder.txtUnread.setVisibility(unread == 0 ? View.INVISIBLE : View.VISIBLE);
            holder.txtTime.setTextColor(ContextCompat.getColor(this.fragment.getContext(), unread == 0 ? android.R.color.darker_gray : R.color.colorAccent));

            //Added by Aga 12-2-2016---
            String urlImage = data.get(position).getInfo().getAvatar();
            //---------
            Glide.with(this.fragment).load(urlImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(fragment.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    super.getView().setImageDrawable(circularBitmapDrawable);
                }
            });

            final long types = type;
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = roomModel.getKey();
                    if(types== FirebaseChatManager.TYPE_GROUP){
                        //go to event detail
                        fragment.getActivity().startActivity(new Intent(fragment.getActivity(), EventDetailActivity.class).putExtra(Common.FIELD_EVENT_ID, key));
                    }else{
                        fragment.getActivity().startActivity(new Intent(fragment.getActivity(), ProfileDetailActivity.class).putExtra(Common.FIELD_FACEBOOK_ID, getFbIdFriend(key)));
                    }
                }
            });

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
        /*final int length = this.data.size();
        int unreadCount = 0;

        for (int i = 0; i < length; i++)
            unreadCount += this.data.get(i).getInfo().getUnread() > 0 ? 1 : 0;

        return unreadCount;*/

        final int length = this.data.size();
        int unreadCount = 0;

        for (int i = 0; i < length; i++){
            String fb_idMatch = Utils.BLANK;
            int counter = 0;
            for(int j=0;j<this.data.size();j++){
                fb_idMatch = data.get(i).getUnreads().get(j).getFb_id();
                if(FirebaseChatManager.fb_id.equals(fb_idMatch)){
                    counter = Integer.parseInt(String.valueOf(data.get(i).getUnreads().get(j).getCounter()));
                    break;
                }
            }
            unreadCount += counter > 0 ? 1 : 0;
        }

        return unreadCount;
    }

    public int countBadge(){
        int countBadge = 0;

        for(int i=0;i<FirebaseChatManager.arrAllRoomMembers.size();i++){
            String roomId = FirebaseChatManager.arrAllRoomMembers.get(i);
            for(int j=0;j<FirebaseChatManager.arrAllRoom.size();j++){
                RoomModel roomModel = FirebaseChatManager.arrAllRoom.get(j);
                String roomIdMatch = roomModel.getKey();
                if(roomId==roomIdMatch){
                    for(int k=0;k<roomModel.getUnreads().size();k++){
                        RoomModel.Unread unread = roomModel.getUnreads().get(k);
                        if(FirebaseChatManager.fb_id.equals(unread.getFb_id())){
                            countBadge = countBadge + Integer.parseInt(String.valueOf(unread.getCounter()));
                            break;
                        }
                    }
                    break;
                }
            }
        }

        return countBadge;
    }

    private long getUnreadCounter(RoomModel roomModel){

        String fb_idMatch = Utils.BLANK;
        long counter = 0;

        for(int j=0;j<roomModel.getUnreads().size();j++){
            fb_idMatch = roomModel.getUnreads().get(j).getFb_id();
            if(FirebaseChatManager.fb_id.equals(fb_idMatch)){
                counter = roomModel.getUnreads().get(j).getCounter();
                break;
            }
        }

        return counter;
    }

    public String getSimpleDate(String createdAt) throws ParseException {
        String simpleDate = Utils.BLANK;
        final Date date = Common.ISO8601_DATE_FORMAT_UTC.parse(createdAt);
        simpleDate = Common.SIMPLE_12_HOUR_FORMAT.format(date);
        return simpleDate;
    }

    private String getFbIdFriend(String identifier){
        String id1 = identifier.substring(0, identifier.indexOf("_"));
        String id2 = identifier.substring(identifier.indexOf("_")+1, identifier.length());

        String idFriend = Utils.BLANK;
        if(FirebaseChatManager.fb_id.equals(id1)){
            idFriend = id2;
        }else {
            idFriend = id1;
        }

        return idFriend;
    }

}
