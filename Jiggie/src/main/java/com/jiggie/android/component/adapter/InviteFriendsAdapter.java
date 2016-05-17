package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class InviteFriendsAdapter extends RecyclerView.Adapter<InviteFriendsAdapter.ViewHolder> {

    private ArrayList<ContactPhoneModel> data = new ArrayList<>();
    private ArrayList<ResponseContactModel.Data.Contact> dataRest = new ArrayList<>();
    private InviteSelectedListener listener;
    private Activity a;
    private static final String TAG = InviteFriendsAdapter.class.getSimpleName();

    public InviteFriendsAdapter(Activity a, ArrayList<ContactPhoneModel> data, ArrayList<ResponseContactModel.Data.Contact> dataRest, InviteSelectedListener listener) {
        this.listener = listener;
        this.data = data;
        this.a = a;
        this.dataRest = dataRest;
    }

    @Override
    public int getItemCount() {
        return dataRest.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.contact = dataRest.get(position);
            holder.txtName.setText(dataRest.get(position).getName());

            String phoneNumber = Utils.BLANK;
            for(int i=0;i<dataRest.get(position).getPhone().size();i++){
                //phoneNumber = phoneNumber.concat(", "+dataRest.get(position).getPhone().get(i));
                phoneNumber = dataRest.get(position).getPhone().get(i);
            }

            String email = Utils.BLANK;
            for(int i=0;i<dataRest.get(position).getEmail().size();i++){
                //email = email.concat(", "+dataRest.get(position).getEmail().get(i));
                email = dataRest.get(position).getEmail().get(i);
            }

            holder.txtPhone.setText(phoneNumber);
            holder.txtEmail.setText(email);

            String photo = Utils.BLANK;
            for(int i=0;i<data.size();i++){
                String as = dataRest.get(position).getRecord_id();
                String bs = data.get(i).getId();
                if(as.equals(bs)){
                    photo = data.get(i).getPhotoThumbnail();
                    if(!photo.equals(Utils.BLANK)){
                        Glide.with(a).load(photo).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imgPhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(a.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                super.getView().setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    }else{
                        photo = Utils.BLANK;
                    }
                    break;
                }
            }

            if(dataRest.get(position).is_active()){
                setInviteEnable(holder.btnInvite, true);
            }else{
                setInviteEnable(holder.btnInvite, false);
            }

            InviteManager.arrBtnInvite.add(holder.btnInvite);

            holder.txtCredit.setText("+"+String.valueOf(dataRest.get(position).getCredit())+" credits");

        }catch (Exception e){
            Log.d(TAG, e.toString());
        }
    }

    public void setInviteEnable(Button btnInvite, boolean enable){
        btnInvite.setEnabled(enable);
        if(enable){
            btnInvite.setText(a.getString(R.string.in_invite));
        }else {
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

        private InviteSelectedListener listener;
        private Activity a;
        private ResponseContactModel.Data.Contact contact;

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
                        listener.onInviteSelected(contact);
                    }
                }
            });
        }
    }

    public interface InviteSelectedListener {
        void onInviteSelected(ResponseContactModel.Data.Contact contact);
    }

}