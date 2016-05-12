package com.jiggie.android.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiggie.android.R;

import butterknife.Bind;

/**
 * Created by LTE on 5/12/2016.
 */
public class InviteFriendsAdapter extends RecyclerView.Adapter<InviteFriendsAdapter.ViewHolder> {


    private InviteSelectedListener listener;

    public InviteFriendsAdapter(InviteSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {

        }catch (Exception e){

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_friend, parent, false), this.listener);
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

        public ViewHolder(View itemView, InviteSelectedListener listener) {
            super(itemView);
        }
    }

    public interface InviteSelectedListener {
        void onInviteSelected();
    }

}
