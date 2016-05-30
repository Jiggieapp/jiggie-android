package com.jiggie.android.component.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rangg on 05/11/2015.
 */
public class MoreTabListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ItemSelectedListener listener;
    private final SharedPreferences pref;
    private final Fragment fragment;
    private int[] resources;
    private String[] items;
    public static final String TAG = MoreTabListAdapter.class.getSimpleName();
    //private String strCredit;

    public MoreTabListAdapter(Fragment fragment, ItemSelectedListener listener) {
        this.pref = App.getSharedPreferences();
        this.listener = listener;
        this.fragment = fragment;
        //this.strCredit = strCredit;
    }

    public void initItems() {
        final Context context = this.fragment.getContext();
        this.items = new String[]{

                //strCredit,
                context.getString(R.string.his_title),
                context.getString(R.string.get_free_credit),
                context.getString(R.string.promo_menu),
                context.getString(R.string.settings),
                context.getString(R.string.support),
                context.getString(R.string.logout)
        };
        this.resources = new int[]{
                //R.drawable.iccredit,
                R.drawable.icbookings,
                R.drawable.icinvitefriends,
                R.drawable.icpromotions,
                R.drawable.icsettings,
                R.drawable.icsupport,
                0
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*if (viewType == 0)
            return new AccountHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_header, parent, false), this.listener);*/
        return new AccountViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more, parent, false), this.listener);
    }

    @Override
    public int getItemViewType(int position) {
        //return position == 0 ? 0 : 1;
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //if (position > 0) {
        final AccountViewHolder viewHolder = (AccountViewHolder) holder;

        if (this.resources[position] == 0) {
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.text.setGravity(Gravity.CENTER_HORIZONTAL);
            viewHolder.imgArrow.setVisibility(View.GONE);
            viewHolder.divBottom.setVisibility(View.GONE);
        } else {
            viewHolder.imageView.setImageResource(this.resources[position]);
        }

        viewHolder.text.setText(this.items[position]);
        viewHolder.setPosition(position);

            /*if (position - 1 == 0) {
                viewHolder.divTop.setVisibility(View.VISIBLE);
            } else {
                viewHolder.divTop.setVisibility(View.GONE);
            }*/
        /*} else {
            final AccountHeaderViewHolder viewHolder = (AccountHeaderViewHolder) holder;
            final String userImage = this.pref.getString(Common.PREF_IMAGE, null);
            final String dataPath = App.getInstance().getDataPath(Common.PREF_IMAGES);
            final String imagePath;

            viewHolder.txtUser.setText(pref.getString(Common.PREF_FACEBOOK_NAME, null));

            //Added by Aga 12-2-2016
            if (userImage != null) {
                imagePath = String.format("file:///%s%s", dataPath, userImage);
            } else {
                final int width = viewHolder.image.getWidth() * 2;
                imagePath = App.getFacebookImage(AccessToken.getCurrentAccessToken().getUserId(), width);
            }
            //--------

            Glide.with(this.fragment).load(imagePath).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.image) {
                @Override
                protected void setResource(Bitmap resource) {
                    final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(fragment.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    super.getView().setImageDrawable(circularBitmapDrawable);
                }
            });

            *//*viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open new Profile detail
                    Intent intent = new Intent(fragment.getActivity(), ProfileDetailActivity.class);
                    intent.putExtra(Common.FIELD_FACEBOOK_ID, AccessToken.getCurrentAccessToken().getUserId());
                    fragment.getActivity().startActivity(intent);
                }
            });

            viewHolder.imgEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.getActivity().startActivity(new Intent(fragment.getActivity(), NewProfileDetailActivity.class));
                }
            });*//*
        }*/
    }

    @Override
    public int getItemCount() {
        return this.items == null ? 0 : this.items.length;
    }

    static class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.text)
        TextView text;
        /*@Bind(R.id.div_top)
        View divTop;*/
        @Bind(R.id.img_arrow)
        ImageView imgArrow;
        @Bind(R.id.divBottom)
        View divBottom;

        private ItemSelectedListener listener;
        private int position;

        public AccountViewHolder(View itemView, ItemSelectedListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) this.listener.onItemSelected(this.position);
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    /*public static class AccountHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.imageView)
        ImageView image;
        @Bind(R.id.txtUser)
        TextView txtUser;
        @Bind(R.id.txtVerifyPhoneNumber)
        TextView txtVerifyPhoneNumber;
        @Bind(R.id.lblPhoneNumber)
        TextView lblPhoneNumber;
        @Bind(R.id.imgEditProfile)
        ImageView imgEditProfile;

        @OnClick(R.id.txtVerifyPhoneNumber)
        @SuppressWarnings("unused")
        public void onVerifyPhoneNumberClick() {
            if (listener != null)
                this.listener.onVerifyPhoneNumberSelected();
        }

        @OnClick(R.id.lblPhoneNumber)
        @SuppressWarnings("unused")
        public void onLblPhoneNumberClick() {
            onVerifyPhoneNumberClick();
        }

        private ItemSelectedListener listener;

        public AccountHeaderViewHolder(View itemView, ItemSelectedListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);

            SettingModel settingModel = AccountManager.loadSetting();
            final String phoneNo = settingModel.getData().getPhone();
            *//*if (phoneNo == null || phoneNo.isEmpty())
            {
                txtVerifyPhoneNumber.setVisibility(View.VISIBLE);
                lblPhoneNumber.setVisibility(View.GONE);
            }
            else
            {
                txtVerifyPhoneNumber.setVisibility(View.GONE);
                lblPhoneNumber.setVisibility(View.VISIBLE);
                lblPhoneNumber.setText(phoneNo);
            }*//*
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //if (this.listener != null) this.listener.onItemSelected(0);
        }
    }*/

    public interface ItemSelectedListener {
        void onItemSelected(int position);

        void onVerifyPhoneNumberSelected();
    }
}
