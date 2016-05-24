package com.jiggie.android.component.adapter;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.Common;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rangg on 18/12/2015.
 */
public class ChatTabListAdapter extends RecyclerView.Adapter<ChatTabListAdapter.ViewHolder> {
    private ConversationSelectedListener listener;
    private ConversationLongClickListener longClickListener;
    private Fragment fragment;
    private static final String TAG = ChatTabListAdapter.class.getSimpleName();

    private ArrayList<ChatListModel.Data.ChatLists> items;

    public ChatTabListAdapter(Fragment fragment, ConversationSelectedListener listener, ConversationLongClickListener longClickListener) {
        this.items = new ArrayList<>();
        this.fragment = fragment;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    public void clear() { this.items.clear(); }
    /*public void add(Conversation item) { this.items.add(item); }
    public void remove(Conversation item) { this.items.remove(item); }
    public void move(Conversation item, int position) {
        this.items.remove(item);
        this.items.add(position, item);
    }*/

    public void add(ChatListModel.Data.ChatLists item) { this.items.add(item); }
    public void remove(
            ChatListModel.Data.ChatLists item) { this.items.remove(item);
    }
    public void move(ChatListModel.Data.ChatLists item, int position) {
        this.items.remove(item);
        this.items.add(position, item);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false), this.listener, this.longClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final ChatListModel.Data.ChatLists item = this.items.get(position);
            holder.conversation = item;
            holder.txtUser.setText(item.getFromName());
            holder.txtMessage.setText(item.getLast_message());

            final Date date = Common.ISO8601_DATE_FORMAT_UTC.parse(item.getLast_updated());
            String simpleDate = Common.SIMPLE_12_HOUR_FORMAT.format(date).replace("AM", "").replace("PM", "").trim();

            holder.txtTime.setText(simpleDate);
            holder.txtUnread.setText(String.valueOf(item.getUnread()));
            holder.txtUnread.setVisibility(item.getUnread() == 0 ? View.INVISIBLE : View.VISIBLE);
            holder.txtTime.setTextColor(ContextCompat.getColor(this.fragment.getContext(), item.getUnread() == 0 ? android.R.color.darker_gray : R.color.colorAccent));

            //Added by Aga 12-2-2016---
            String urlImage;
            if(item.getProfile_image()!=null){
                urlImage = item.getProfile_image();
            }else{
                final int width = holder.imageView.getWidth() * 2;
                urlImage = App.getFacebookImage(item.getFb_id(), width);
            }
            //---------
            Glide.with(this.fragment).load(urlImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(fragment.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    super.getView().setImageDrawable(circularBitmapDrawable);
                }
            });
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() { return this.items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @Bind(R.id.imageView) ImageView imageView;
        @Bind(R.id.txtUser) TextView txtUser;
        @Bind(R.id.txtMessage) TextView txtMessage;
        @Bind(R.id.txtTime) TextView txtTime;
        @Bind(R.id.txtUnread) TextView txtUnread;

        //Conversation conversation;
        ChatListModel.Data.ChatLists conversation;
        private ConversationSelectedListener listener;
        private ConversationLongClickListener longClickListener;

        public ViewHolder(View itemView, ConversationSelectedListener listener, ConversationLongClickListener longClickListener) {
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
                this.listener.onConversationSelected(this.conversation);
        }

        @Override
        public boolean onLongClick(View v) {
            if (this.longClickListener != null)
                this.longClickListener.onConversationLongClick(this.conversation);
            return true;
        }
    }

    public interface ConversationSelectedListener {
        void onConversationSelected(ChatListModel.Data.ChatLists conversation);
    }

    public interface ConversationLongClickListener {
        void onConversationLongClick(ChatListModel.Data.ChatLists conversation);
    }

    public ChatListModel.Data.ChatLists find(String facebookId) {
        final int length = this.items.size();
        for (int i = 0; i < length; i++) {
            final ChatListModel.Data.ChatLists item = this.items.get(i);
            //if (facebookId.equals(item.getFacebookId()))
            if (facebookId.equals(item.getFb_id()))
                return item;
        }
        return null;
    }

    public int countUnread() {
        final int length = this.items.size();
        int unreadCount = 0;

        for (int i = 0; i < length; i++)
            unreadCount += this.items.get(i).getUnread() > 0 ? 1 : 0;

        return unreadCount;
    }

    public List<ChatListModel.Data.ChatLists> getItems() { return this.items; }
}
