package com.jiggie.android.component.adapter;

import android.accounts.Account;
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
import com.google.gson.Gson;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.InviteManager;
import com.jiggie.android.model.ContactPhoneModel;
import com.jiggie.android.model.InviteCodeResultModel;
import com.jiggie.android.model.ResponseContactModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 5/12/2016.
 */
public class InviteFriendsNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ContactPhoneModel> data = new ArrayList<>();
    private InviteSelectedListener listener;
    private Activity a;
    private static final String TAG = InviteFriendsNewAdapter.class.getSimpleName();

    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

    private final int HEADER = 0;
    private final int BODY = 1;

    InviteCodeResultModel inviteCodeResultModel;


    public InviteFriendsNewAdapter(Activity a, ArrayList<ContactPhoneModel> data, InviteSelectedListener listener) {
        this.a = a;
        this.data = data;
        this.listener = listener;

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);

        inviteCodeResultModel = new Gson().fromJson(AccountManager.getInviteCodeFromPreference(), InviteCodeResultModel.class);
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderr, int position) {
        if(holderr instanceof InviteFriendsNewAdapter.ViewHolderBody)
        {
            position = position - 1;
            InviteFriendsNewAdapter.ViewHolderBody holder = (InviteFriendsNewAdapter.ViewHolderBody) holderr;
            try {
                holder.position = position;
                holder.contact = data.get(position);
                holder.txtName.setText(data.get(position).getName());


                String phoneNumber = Utils.BLANK;
                String displayedPhoneNumber = Utils.BLANK;
                for (int i = 0; i < data.get(position).getPhone().size(); i++) {
                    //phoneNumber = phoneNumber.concat(", "+data.get(position).getPhone().get(i));
                    if (i == 0) {
                        phoneNumber = data.get(position).getPhone().get(i);
                    } else {
                        phoneNumber = phoneNumber.concat(", " + data.get(position).getPhone().get(i));
                    }
                }

                if (data.get(position).getPhone().size() > 0) {
                    displayedPhoneNumber = data.get(position).getPhone().get(0);
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

                if (data.get(position).getEmail().size() > 0) {
                    email = data.get(position).getEmail().get(0);
                }

                if (phoneNumber.equals(Utils.BLANK)) {
                    holder.txtPhone.setVisibility(View.GONE);
                } else {
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
                            if (data.get(position).getName().length() > 0) {
                                String initial = data.get(position).getName().substring(0, 1);
                                holder.txtInitial.setText(initial);
                            }
                            holder.txtInitial.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }

                if (!isFound) {
                    photo = Utils.BLANK;
                    Glide.clear(holder.imgPhoto);
                    holder.imgPhoto.setImageResource(R.drawable.shape_cigrey);
                    if (data.get(position).getName().length() > 0) {
                        String initial = data.get(position).getName().substring(0, 1);
                        holder.txtInitial.setText(initial);
                    }
                    holder.txtInitial.setVisibility(View.VISIBLE);
                }

                /*for (int i = 0; i < data.size(); i++) {

                }*/

                if (InviteManager.arrBtnInvite2.get(position)) {
                    setInviteEnable(holder.btnInvite, true);
                } else {
                    setInviteEnable(holder.btnInvite, false);
                }
                holder.btnInvite.setVisibility(View.VISIBLE);
                //InviteManager.arrBtnInvite.get(position).setEnabled(holder.btnInvite.isEnabled());

                //= new Gson().fromJson(inv, InviteCodeResultModel.class);

                holder.txtCredit.setText("+Rp. " + formatter.format(Double.parseDouble
                        (inviteCodeResultModel.getData().getInvite_code().getRewards_inviter())));
                holder.txtCredit.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                Utils.d(TAG, e.toString());
            }
        }
        else
        {
            ViewHolderHeader holderHeader = (ViewHolderHeader) holderr;
            final String textHeader = String.format("You have %d contacts not yet on Jiggie.%nEarn up to Rp. %s in free credit!"
                    , getItemCount(), formatter.format( Double.parseDouble(inviteCodeResultModel.getData().getInvite_code().getRewards_inviter()) * getItemCount()));
            /*final String textHeader = "You have  "+ getItemCount() + " contacts not yet on Jiggie \n"
                    + "Earn up to Rp. " + formatter.format(Double.parseDouble(inviteCodeResultModel.getData().getInvite_code().getRewards_inviter()) * getItemCount())
                    + " in free credits";*/
            holderHeader.lblInviteHeader.setText(textHeader);
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == HEADER) {
            return new ViewHolderHeader(inflater.inflate(R.layout.item_invite_header, parent, false));
        } else {
            return new ViewHolderBody(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_friend, parent, false), this.listener, a);
        }
    }


    static class ViewHolderHeader extends RecyclerView.ViewHolder {
        @Bind(R.id.lbl_invite_header)
        TextView lblInviteHeader;

        public ViewHolderHeader(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderBody extends RecyclerView.ViewHolder {
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

        int position;

        private InviteSelectedListener listener;
        private Activity a;
        private ContactPhoneModel contact;

        public ViewHolderBody(View itemView, final InviteSelectedListener listener, final Activity a) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        btnInvite.setEnabled(false);
                        //btnInvite.setText(a.getString(R.string.in_sent));
                        listener.onInviteSelected(contact, position);
                    }
                }
            });
        }
    }

    public interface InviteSelectedListener {
        void onInviteSelected(ContactPhoneModel contact,int position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        else return BODY;
    }

}