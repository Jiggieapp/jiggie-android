package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.Chat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rangg on 21/12/2015.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<Chat> items;
    private String profileImage;
    private String toId;
    private final static String TAG = ChatAdapter.class.getSimpleName();

    public ChatAdapter(Activity activity, String profileImage, String toId) {
        this.profileImage = profileImage;
        this.items = new ArrayList<>();
        this.activity = activity;
        this.toId = toId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == CHAT_HEADER)
        {
            return new ViewHolderHeader(inflater.inflate(R.layout.item_chat_header, parent, false));
        }
        else
        {
            return new ViewHolderBody(inflater.inflate(R.layout.item_chat, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderr, int position) {
        final Chat item = this.items.get(position);
        if(holderr instanceof ChatAdapter.ViewHolderBody)
        {
            ChatAdapter.ViewHolderBody holder = (ChatAdapter.ViewHolderBody) holderr;
            try {

                holder.txtMessage.setText(item.getMessage().trim());
                holder.txtLeftTime.setText(item.getSimpleDate());
                holder.txtRightTime.setText(item.getSimpleDate());
                holder.imageView.setVisibility(item.isFromYou() ? View.GONE : View.VISIBLE);
                holder.txtLeftTime.setVisibility(item.isFromYou() ? View.VISIBLE : View.GONE);
                holder.txtRightTime.setVisibility(item.isFromYou() ? View.GONE : View.VISIBLE);
                holder.txtMessage.setBackgroundResource(item.isFromYou() ? R.drawable.bg_chat_self : R.drawable.bg_chat_blue);
                holder.txtMessage.setTextColor(ContextCompat.getColor(holder.root.getContext(), item.isFromYou() ? R.color.textDarkGray : android.R.color.white));

                final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.root.getLayoutParams();
                layoutParams.gravity = item.isFromYou() ? Gravity.END : Gravity.START;

                if (!item.isFromYou()) {

                    //Added by Aga 12-2-2016---
                    //holder.txtMessage.setGravity(Gravity.RIGHT);
                    String urlImage;
                    if (this.profileImage != null) {
                        urlImage = this.profileImage;
                    } else {
                        final int width = holder.imageView.getWidth() * 2;
                        urlImage = App.getFacebookImage(toId, width);
                    }
                    //---------
                    Utils.d(TAG, "profileImage " + urlImage);

                    Glide.with(this.activity).load(urlImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            final Resources resources = activity.getResources();
                            if (resources != null) {
                                final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource);
                                circularBitmapDrawable.setCircular(true);
                                super.getView().setImageDrawable(circularBitmapDrawable);
                            }
                        }
                    });
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        else if(holderr instanceof ViewHolderHeader)
        {
            ViewHolderHeader holderHeader = (ViewHolderHeader) holderr;
            if(item.getTitle().equalsIgnoreCase("generic")){
                holderHeader.lblChatHeader.setVisibility(View.GONE);
            }else{
                holderHeader.lblChatHeader.setVisibility(View.VISIBLE);
                holderHeader.lblChatHeader.setText(item.getTitle());
            }


        }

    }

    public void clear(boolean notify) {
        this.items.clear();
        if (notify)
            super.notifyDataSetChanged();
    }

    public void clear() {
        this.items.clear();
    }

    public void add(Chat chat) {
        this.items.add(chat);
    }

    public Chat get(int position) {
        return this.items.get(position);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    /*@Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }*/

    static class ViewHolderBody extends RecyclerView.ViewHolder {
        @Bind(R.id.txtRightTime)
        TextView txtRightTime;
        @Bind(R.id.txtLeftTime)
        TextView txtLeftTime;
        @Bind(R.id.txtMessage)
        TextView txtMessage;
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.root)
        View root;

        public ViewHolderBody(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderHeader extends RecyclerView.ViewHolder {
        @Bind(R.id.lbl_chat_header)
        TextView lblChatHeader;

        public ViewHolderHeader(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return CHAT_HEADER;
        else return CHAT_BODY;
    }

    private final int CHAT_HEADER = 0;
    private final int CHAT_BODY = 1;
}
