package com.android.jiggie.component.adapter;

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

import com.android.jiggie.R;
import com.android.jiggie.model.Conversation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rangg on 18/12/2015.
 */
public class ChatTabListAdapter extends RecyclerView.Adapter<ChatTabListAdapter.ViewHolder> {
    private ConversationSelectedListener listener;
    private ArrayList<Conversation> items;
    private Fragment fragment;

    public ChatTabListAdapter(Fragment fragment, ConversationSelectedListener listener) {
        this.items = new ArrayList<>();
        this.fragment = fragment;
        this.listener = listener;
    }

    public void clear() { this.items.clear(); }
    public void add(Conversation item) { this.items.add(item); }
    public void remove(Conversation item) { this.items.remove(item); }
    public void move(Conversation item, int position) {
        this.items.remove(item);
        this.items.add(position, item);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final Conversation item = this.items.get(position);
            holder.conversation = item;
            holder.txtUser.setText(item.getFromName());
            holder.txtMessage.setText(item.getLastMessage());
            holder.txtTime.setText(item.getSimpleDate());
            holder.txtUnread.setText(String.valueOf(item.getUnread()));
            holder.txtUnread.setVisibility(item.getUnread() == 0 ? View.INVISIBLE : View.VISIBLE);
            holder.txtTime.setTextColor(ContextCompat.getColor(this.fragment.getContext(), item.getUnread() == 0 ? android.R.color.darker_gray : R.color.colorAccent));

            Glide.with(this.fragment).load(item.getProfileImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imageView) {
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

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.imageView) ImageView imageView;
        @Bind(R.id.txtUser) TextView txtUser;
        @Bind(R.id.txtMessage) TextView txtMessage;
        @Bind(R.id.txtTime) TextView txtTime;
        @Bind(R.id.txtUnread) TextView txtUnread;

        Conversation conversation;
        private ConversationSelectedListener listener;

        public ViewHolder(View itemView, ConversationSelectedListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null)
                this.listener.onConversationSelected(this.conversation);
        }
    }

    public interface ConversationSelectedListener {
        void onConversationSelected(Conversation conversation);
    }

    public Conversation find(String facebookId) {
        final int length = this.items.size();
        for (int i = 0; i < length; i++) {
            final Conversation item = this.items.get(i);
            if (facebookId.equals(item.getFacebookId()))
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

    public List<Conversation> getItems() { return this.items; }
}
