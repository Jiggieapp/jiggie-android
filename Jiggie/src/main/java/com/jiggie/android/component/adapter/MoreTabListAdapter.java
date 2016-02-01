package com.jiggie.android.component.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.model.Common;

/**
 * Created by rangg on 05/11/2015.
 */
public class MoreTabListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ItemSelectedListener listener;
    private final SharedPreferences pref;
    private final Fragment fragment;
    private int[] resources;
    private String[] items;

    public MoreTabListAdapter(Fragment fragment, ItemSelectedListener listener) {
        this.pref = App.getSharedPreferences();
        this.listener = listener;
        this.fragment = fragment;
    }

    public void initItems() {
        final Context context = this.fragment.getContext();
        this.items = new String[]{
                context.getString(R.string.profile),
                context.getString(R.string.invite),
                context.getString(R.string.settings),
                context.getString(R.string.support),
                context.getString(R.string.logout)
        };
        this.resources = new int[] {
                R.mipmap.ic_profile,
                R.mipmap.ic_invite,
                R.mipmap.ic_settings,
                R.mipmap.ic_support,
                R.mipmap.ic_logout,
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new AccountHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_header, parent, false), this.listener);
        return new AccountViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more, parent, false), this.listener);
    }

    @Override
    public int getItemViewType(int position) { return position == 0 ? 0 : 1; }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position > 0) {
            final AccountViewHolder viewHolder = (AccountViewHolder) holder;
            viewHolder.imageView.setImageResource(this.resources[position - 1]);
            viewHolder.text.setText(this.items[position - 1]);
            viewHolder.setPosition(position - 1);
        } else {
            final AccountHeaderViewHolder viewHolder = (AccountHeaderViewHolder) holder;
            final String userImage = this.pref.getString(Common.PREF_IMAGE, null);
            final String dataPath = App.getInstance().getDataPath(Common.PREF_IMAGES);
            final String imagePath = String.format("file:///%s%s", dataPath, userImage);

            viewHolder.txtUser.setText(pref.getString(Common.PREF_FACEBOOK_NAME, null));
            Glide.with(this.fragment).load(imagePath).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.image){
                @Override
                protected void setResource(Bitmap resource) {
                    final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(fragment.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    super.getView().setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }

    @Override
    public int getItemCount() { return this.items == null ? 0 : this.items.length + 1; }

    static class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.imageView) ImageView imageView;
        @Bind(R.id.text) TextView text;

        private ItemSelectedListener listener;
        private int position;

        public AccountViewHolder(View itemView, ItemSelectedListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) { if (this.listener != null) this.listener.onItemSelected(this.position); }
        public void setPosition(int position) { this.position = position; }
    }

    static class AccountHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.imageView) ImageView image;
        @Bind(R.id.txtUser) TextView txtUser;

        private ItemSelectedListener listener;

        public AccountHeaderViewHolder(View itemView, ItemSelectedListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) { if (this.listener != null) this.listener.onItemSelected(0); }
    }

    public interface ItemSelectedListener {
        void onItemSelected(int position);
    }
}
