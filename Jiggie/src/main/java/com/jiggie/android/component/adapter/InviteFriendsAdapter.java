package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ContactPhoneModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 5/12/2016.
 */
public class InviteFriendsAdapter extends RecyclerView.Adapter<InviteFriendsAdapter.ViewHolder> {

    private ArrayList<ContactPhoneModel> data = new ArrayList<>();
    private InviteSelectedListener listener;
    private Activity a;

    public InviteFriendsAdapter(Activity a, ArrayList<ContactPhoneModel> data, InviteSelectedListener listener) {
        this.listener = listener;
        this.data = data;
        this.a = a;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.txtName.setText(data.get(position).getName());
            holder.txtPhone.setText(data.get(position).getPhoneNumber());
            holder.txtEmail.setText(data.get(position).getEmail());
            String photo = data.get(position).getPhotoThumbnail();
            if(!photo.equals(Utils.BLANK)){
                Glide.with(a).load(photo).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imgPhoto) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(a.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        super.getView().setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        }catch (Exception e){

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_friend, parent, false), this.listener, a);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img_photo)
        ImageView imgPhoto;
        @Bind(R.id.txt_name)
        TextView txtName;
        @Bind(R.id.txt_phone)
        TextView txtPhone;
        @Bind(R.id.txt_email)
        TextView txtEmail;
        @Bind(R.id.btn_invite)
        Button btnInvite;
        @Bind(R.id.txt_credit)
        TextView txtCredit;

        private InviteSelectedListener listener;
        private Activity a;

        public ViewHolder(View itemView, final InviteSelectedListener listener, final Activity a) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        btnInvite.setEnabled(false);
                        btnInvite.setText(a.getString(R.string.in_sent));
                        listener.onInviteSelected();
                    }
                }
            });
        }
    }

    public interface InviteSelectedListener {
        void onInviteSelected();
    }

}