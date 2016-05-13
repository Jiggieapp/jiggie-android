package com.jiggie.android.component.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.jiggie.android.R;
import com.jiggie.android.model.ChatListModel;
import com.jiggie.android.model.FriendListModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wandy on 5/12/2016.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private ArrayList<FriendListModel.Data.List_social_friends> data;
    private Context context;
    private ConversationSelectedListener listener;

    public FriendListAdapter(Context context, FriendListAdapter.ConversationSelectedListener listener)
    {
        data = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false), this.listener);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FriendListModel.Data.List_social_friends friendLists = data.get(position);
        holder.friends = friendLists;
        holder.txtUser.setText(friendLists.getFirst_name());
        holder.txtMessage.setText(friendLists.getAbout());

        Glide.with(context).load(friendLists.getImg_url()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                super.getView().setImageDrawable(circularBitmapDrawable);
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
        @Bind(R.id.txtMessage)
        TextView txtMessage;

        //Conversation conversation;
        FriendListModel.Data.List_social_friends friends;
        private ConversationSelectedListener listener;

        public ViewHolder(View itemView, ConversationSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if(listener != null)
            {
                listener.onConversationSelected(this.friends);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }

    public void add(FriendListModel.Data.List_social_friends listSocialFriends)
    {
        data.add(listSocialFriends);
    }

    public void clear()
    {
        data.clear();
    }

    public interface ConversationSelectedListener {
        void onConversationSelected(FriendListModel.Data.List_social_friends conversation);
    }
}
