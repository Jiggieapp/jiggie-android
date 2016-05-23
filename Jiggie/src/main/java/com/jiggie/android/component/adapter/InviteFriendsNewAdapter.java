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
import com.jiggie.android.manager.InviteManager;
import com.jiggie.android.model.ContactPhoneModel;
import com.jiggie.android.model.ResponseContactModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 5/12/2016.
 */
public class InviteFriendsNewAdapter extends RecyclerView.Adapter<InviteFriendsNewAdapter.ViewHolder> {

    private ArrayList<ContactPhoneModel> data = new ArrayList<>();
    private InviteSelectedListener listener;
    private Activity a;
    private static final String TAG = InviteFriendsNewAdapter.class.getSimpleName();

    
    public InviteFriendsNewAdapter(Activity a, ArrayList<ContactPhoneModel> data, InviteSelectedListener listener)
    {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.contact = data.get(position);
            holder.txtName.setText(data.get(position).getName());

            String phoneNumber = Utils.BLANK;
            String displayedPhoneNumber = Utils.BLANK;
            for(int i=0;i<data.get(position).getPhoneNumber().size();i++){
                //phoneNumber = phoneNumber.concat(", "+data.get(position).getPhoneNumber().get(i));
                if (i == 0) {
                    phoneNumber = data.get(position).getPhoneNumber().get(i);
                } else {
                    phoneNumber = phoneNumber.concat(", " + data.get(position).getPhoneNumber().get(i));
                }
            }

            if(data.get(position).getPhoneNumber().size() > 0)
            {
                displayedPhoneNumber = data.get(position).getPhoneNumber().get(0);
            }

            String email = Utils.BLANK;
            for (int i = 0; i < data.get(position).getEmail().size(); i++) {
                //email = email.concat(", "+data.get(position).getEmail().get(i));
                if (i == 0) {
                    email = data.get(position).getEmail().get(i);
                } else {
                    email = email.concat(", " + data.get(position).getEmail().get(i));
                }
            }

            if(data.get(position).getEmail().size() > 0)
            {
                email = data.get(position).getEmail().get(0);
            }

            if (phoneNumber.equals(Utils.BLANK)) {
                holder.txtPhone.setVisibility(View.GONE);
            }else{
                //holder.txtPhone.setText(phoneNumber);
                holder.txtPhone.setText(displayedPhoneNumber);
                holder.txtPhone.setVisibility(View.VISIBLE);
            }

            if (email == null || email.equals(Utils.BLANK)) {
                holder.txtEmail.setVisibility(View.GONE);
            } else {
                holder.txtEmail.setText(email);
                holder.txtEmail.setVisibility(View.VISIBLE);
            }

            String photo = Utils.BLANK;
            boolean isFound = false;
            for (int i = 0; i < data.size(); i++) {
                String as = data.get(position).getId();
                String bs = data.get(i).getId();
                if (as.equals(bs)) {
                    isFound = true;
                    photo = data.get(i).getPhotoThumbnail();
                    if (!photo.equals(Utils.BLANK)) {
                        Glide.with(a).load(photo).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imgPhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(a.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                super.getView().setImageDrawable(circularBitmapDrawable);
                            }
                        });
                        holder.txtInitial.setVisibility(View.GONE);
                    } else {
                        photo = Utils.BLANK;
                        Glide.clear(holder.imgPhoto);
                        holder.imgPhoto.setImageResource(R.drawable.shape_cigrey);
                        if(data.get(position).getName().length()>0){
                            String initial = data.get(position).getName().substring(0, 1);
                            holder.txtInitial.setText(initial);
                        }
                        holder.txtInitial.setVisibility(View.VISIBLE);
                    }
                    break;
                }
            }

            if(!isFound){
                photo = Utils.BLANK;
                Glide.clear(holder.imgPhoto);
                holder.imgPhoto.setImageResource(R.drawable.shape_cigrey);
                if(data.get(position).getName().length()>0){
                    String initial = data.get(position).getName().substring(0, 1);
                    holder.txtInitial.setText(initial);
                }
                holder.txtInitial.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < data.size(); i++) {

            }

            /*if (data.get(position).is_active()) {
                setInviteEnable(holder.btnInvite, true);
            } else {
                setInviteEnable(holder.btnInvite, false);
            }*/
            holder.btnInvite.setVisibility(View.VISIBLE);

            InviteManager.arrBtnInvite.add(holder.btnInvite);

            //holder.txtCredit.setText("+"+String.valueOf(data.get(position).getCredit())+" credits");
            holder.txtCredit.setVisibility(View.GONE);

        } catch (Exception e) {
            Utils.d(TAG, e.toString());
        }
    }

    public void setInviteEnable(Button btnInvite, boolean enable) {
        btnInvite.setEnabled(enable);
        if (enable) {
            btnInvite.setText(a.getString(R.string.in_invite));
        } else {
            btnInvite.setText(a.getString(R.string.in_sent));
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
        @Bind(R.id.txt_initial)
        TextView txtInitial;

        private InviteSelectedListener listener;
        private Activity a;
        private ContactPhoneModel contact;

        public ViewHolder(View itemView, final InviteSelectedListener listener, final Activity a) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        //btnInvite.setEnabled(false);
                        //btnInvite.setText(a.getString(R.string.in_sent));
                        listener.onInviteSelected(contact);
                    }
                }
            });
        }
    }

    public interface InviteSelectedListener {
        void onInviteSelected(ContactPhoneModel contact);
    }

}