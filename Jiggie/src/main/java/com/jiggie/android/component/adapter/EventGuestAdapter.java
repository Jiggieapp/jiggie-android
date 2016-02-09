package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jiggie.android.model.GuestModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rangg on 13/11/2015.
 */
public class EventGuestAdapter extends RecyclerView.Adapter<EventGuestAdapter.ViewHolder> {
    private final GuestConnectListener listener;
    private final Activity activity;

    private ArrayList<GuestModel.Data.GuestInterests> guests;

    public EventGuestAdapter(Activity activity, GuestConnectListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public void setGuests(ArrayList<GuestModel.Data.GuestInterests> guests){ this.guests = guests; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_guest, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GuestModel.Data.GuestInterests item = this.guests.get(position);
        final String url = App.getFacebookImage(item.getFb_id(), holder.image.getWidth() * 2);

        holder.guest = item;
        holder.text.setText(String.format("%s %s", item.getFirst_name(), ""));
        Glide.with(this.activity).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.image) {
            @Override
            protected void setResource(Bitmap resource) {
                final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                super.getView().setImageDrawable(circularBitmapDrawable);
            }
        });
        if (item.is_connected())
            this.connect(holder);
        else if (item.is_invited())
            this.invite(holder);
        else this.deselect(holder);
    }

    public void connect(ViewHolder viewHolder) {
        viewHolder.txtConnect.setTextColor(ContextCompat.getColor(this.activity, android.R.color.white));
        viewHolder.layoutConnect.setBackgroundResource(R.drawable.btn_event_guest_connect);
        viewHolder.checkView.setVisibility(View.GONE);
        viewHolder.txtConnect.setText(R.string.connected);
    }

    public void invite(ViewHolder viewHolder) {
        viewHolder.txtConnect.setTextColor(ContextCompat.getColor(this.activity, android.R.color.white));
        viewHolder.layoutConnect.setBackgroundResource(R.drawable.btn_event_guest_connect);
        viewHolder.checkView.setVisibility(View.VISIBLE);
        viewHolder.txtConnect.setText(R.string.sent);
    }

    public void deselect(ViewHolder viewHolder) {
        viewHolder.txtConnect.setTextColor(ContextCompat.getColor(this.activity, R.color.colorPrimary));
        viewHolder.layoutConnect.setBackgroundResource(R.drawable.btn_event_guest_connect_highlight);
        viewHolder.checkView.setVisibility(View.GONE);
        viewHolder.txtConnect.setText(R.string.connect);
    }

    @Override
    public int getItemCount() { return this.guests == null ? 0 : this.guests.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.layoutConnect) View layoutConnect;
        @Bind(R.id.txtConnect) TextView txtConnect;
        @Bind(R.id.checkView) View checkView;
        @Bind(R.id.imageView) ImageView image;
        @Bind(R.id.txtUser) TextView text;

        private GuestConnectListener listener;
        private GuestModel.Data.GuestInterests guest;

        public ViewHolder(final View itemView, GuestConnectListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @SuppressWarnings("unused")
        @OnClick(R.id.layoutConnect)
        void btnConnectOnClick() {
            if (this.listener != null)
                this.listener.onGuestConnect(this);
        }

        public GuestModel.Data.GuestInterests getGuest() { return this.guest; }

        @Override
        public void onClick(View v) {
            if (this.listener != null)
                this.listener.onGuestClick(this);
        }
    }

    public interface GuestConnectListener {
        void onGuestConnect(ViewHolder viewHolder);
        void onGuestClick(ViewHolder viewHolder);
    }
}
