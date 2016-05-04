package com.jiggie.android.component.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiggie.android.R;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.SocialManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.SocialModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Wandy on 4/27/2016.
 */
public class SocialCardNewAdapter extends BaseAdapter {
    private final static String TAG = SocialCardNewAdapter.class.getSimpleName();
    ArrayList<SocialModel.Data.SocialFeeds> data;
    Context context;

    public SocialCardNewAdapter(ArrayList<SocialModel.Data.SocialFeeds> data
            , Context mContext, OnSocialCardClickListener onSocialCardClickListener) {
        this.data = data;
        context = mContext;
        this.onSocialCardClickListener = onSocialCardClickListener;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SocialModel.Data.SocialFeeds getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SocialModel.Data.SocialFeeds model = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_social_card_general, parent, false);
            holder = new ViewHolder(convertView, model.getFrom_first_name(), onSocialCardClickListener);
            convertView.setTag(holder);
            assert convertView != null;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*((ImageView)convertView.findViewById(com.andtinder.R.id.image)).setImageDrawable(model.getCardImageDrawable());
        ((TextView)convertView.findViewById(com.andtinder.R.id.title)).setText(model.getTitle());
        ((TextView)convertView.findViewById(com.andtinder.R.id.description)).setText(model.getDescription());*/
        /*holder.generalTxtUser.setText(model.getFrom_first_name());*/
        Glide
                .with(context)
                .load(model.getImage()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(holder.generalImage);

        /*holder.generalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileDetailActivity.class);
                i.putExtra(Common.FIELD_FACEBOOK_ID, getItem(0).getFrom_fb_id());
                context.startActivity(i);
            }
        });*/

        if (getItem(position).getType().equalsIgnoreCase("approved")) {
            holder.generalTxtEvent.setText(model.getEvent_name());
            holder.generalTxtUser.setText(context.getString(R.string.wants_to_go_with
                    , model.getFrom_first_name()));
            holder.generalTxtConnect.setText(
                    context.getResources().getString(R.string.interested_ask));
            holder.chat_icon.setVisibility(View.VISIBLE);
        } else {
            holder.generalTxtEvent.setText(model.getEvent_name());
            holder.generalTxtUser.setText(context.getString(R.string.user_viewing
                    , model.getFrom_first_name()));
            holder.generalTxtConnect.setText(context.getString(R.string.connect_with
                    , model.getFrom_first_name()));
            holder.generalBtnYes.setText(context.getResources().getString(R.string.connect));
            holder.generalBtnNo.setText(context.getResources().getString(R.string.skip));
            holder.chat_icon.setVisibility(View.GONE);
        }


        return convertView;
    }

    public void removeFirstObject() {

    }

    static class ViewHolder {
        @Bind(R.id.card_general)
        CardView cardView;

        @Bind(R.id.txtConnectGeneral)
        TextView generalTxtConnect;

        @Bind(R.id.txtEventGeneral)
        TextView generalTxtEvent;

        @Bind(R.id.imageUserGeneral)
        ImageView generalImage;

        @Bind(R.id.txtUserGeneral)
        TextView generalTxtUser;

        @Bind(R.id.btnYesGeneral)
        Button generalBtnYes;
        @Bind(R.id.btnNoGeneral)
        Button generalBtnNo;

        @Bind(R.id.chat_small_icon)
        ImageView chat_icon;


        OnSocialCardClickListener onSocialCardClickListener;

        ViewHolder(View view, final String name, OnSocialCardClickListener onSocialCardClickListener) {
            ButterKnife.bind(this, view);
            //generalTxtUser = (TextView) ;
            this.onSocialCardClickListener = onSocialCardClickListener;
        }

        @OnClick(R.id.btnNoGeneral)
        public void btnNoOnClick() {
            onSocialCardClickListener.onNoClick();
        }

        @OnClick(R.id.btnYesGeneral)
        public void btnYesOnClick() {
            onSocialCardClickListener.onYesClick();
        }

        /*@OnClick(R.id.card_general)
        public void cardGeneralOnClick() { onSocialCardClickListener.onGeneralClick(); }*/
    }

    OnSocialCardClickListener onSocialCardClickListener;

    public interface OnSocialCardClickListener {
        void onYesClick();

        void onNoClick();

        void onGeneralClick();
    }

    public void deleteFirstItem() {
        data.remove(0);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}
