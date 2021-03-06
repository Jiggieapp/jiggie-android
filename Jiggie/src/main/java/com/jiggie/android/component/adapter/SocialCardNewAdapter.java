package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiggie.android.R;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.SocialManager;
import com.jiggie.android.manager.TooltipsManager;
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
    Activity a;

    public SocialCardNewAdapter(ArrayList<SocialModel.Data.SocialFeeds> data
            , Context mContext, OnSocialCardClickListener onSocialCardClickListener, Activity a) {
        this.data = data;
        context = mContext;
        this.onSocialCardClickListener = onSocialCardClickListener;
        this.a = a;
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
        final ViewHolder holder;
        final SocialModel.Data.SocialFeeds model = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_social_card_general, parent, false);
            /*Utils.d(TAG, "typefeed " + getItem(position).getType_feed());
            if(getItem(position).getType_feed() == Utils.TYPE_FEED_EVENT)
            {
                holder = new ViewHolder(convertView, model.getFrom_first_name(), onSocialCardClickListener);
            }
            else
            {
                holder = new ViewHolder(convertView, model.getFrom_first_name(), null);
            }*/
            holder = new ViewHolder(convertView, model.getFrom_first_name(), onSocialCardClickListener);
            convertView.setTag(holder);
            assert convertView != null;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide
                .with(context)
                .load(model.getImage()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(holder.generalImage);

        if(getItem(position).getType_feed() == Utils.TYPE_FEED_EVENT)
        {
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
        }
        else if(getItem(position).getType_feed() == Utils.TYPE_FEED_NEARBY)
        {
            if (!getItem(position).getType().equalsIgnoreCase("approved")) {
                holder.generalBtnYes.setText(context.getResources().getString(R.string.connect));
                holder.generalBtnNo.setText(context.getResources().getString(R.string.skip));
                holder.chat_icon.setVisibility(View.GONE);
                holder.generalTxtUser.setText(model.getFrom_first_name());
            }else{
                holder.chat_icon.setVisibility(View.VISIBLE);
                holder.generalTxtUser.setText(context.getString(R.string.approved_nearby, model.getFrom_first_name()));
            }

            holder.generalTxtEvent.setText("Is Nearby");
            holder.generalTxtConnect.setText(context.getString(R.string.connect_with
                    , model.getFrom_first_name()));
        }

        //holder.chat_icon.setVisibility(View.VISIBLE);
        setBtnYesGeneral(holder.generalBtnYes);

        if (getItem(position).getBadge_booking())
        {
            holder.imgHasTable.setVisibility(View.VISIBLE);
            holder.imgHasTable.bringToFront();
        }
        else holder.imgHasTable.setVisibility(View.GONE);

        if (getItem(position).getBadge_ticket())
        {
            holder.imgHasTicket.setVisibility(View.VISIBLE);
            holder.imgHasTicket.bringToFront();
        }

        else holder.imgHasTicket.setVisibility(View.GONE);

        holder.chat_icon.bringToFront();
        holder.imgConnect.bringToFront();
        holder.imgSkip.bringToFront();

        holder.generalBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgConnect.setAlpha((float) 1.0);
                //onSocialCardClickListener.onYesClick();
            }
        });

        holder.generalBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgSkip.setAlpha((float) 1.0);
                //onSocialCardClickListener.onYesClick();
            }
        });


        return convertView;
    }

    public void removeFirstObject() {

    }

    Button lastYes;

    public void setBtnYesGeneral(Button btn) {
        lastYes = btn;
    }

    public Button getBtnYesGeneral() {
        return lastYes;
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

        @Bind(R.id.image_connect)
        ImageView imgConnect;

        @Bind(R.id.image_skip)
        ImageView imgSkip;

        @Bind(R.id.img_has_ticket)
        ImageView imgHasTicket;

        @Bind(R.id.img_has_table)
        ImageView imgHasTable;

        @Bind(R.id.lbl_container)
        RelativeLayout lblContainer;

        OnSocialCardClickListener onSocialCardClickListener;

        ViewHolder(View view, final String name, OnSocialCardClickListener onSocialCardClickListener) {
            ButterKnife.bind(this, view);
            //generalTxtUser = (TextView) ;
            if(onSocialCardClickListener != null)
                this.onSocialCardClickListener = onSocialCardClickListener;
        }

        /*@OnClick(R.id.btnNoGeneral)
        public void btnNoOnClick() {
            imgSkip.setAlpha((float) 1.0);
            //onSocialCardClickListener.onNoClick();
        }

        @OnClick(R.id.btnYesGeneral)
        public void btnYesOnClick() {
            imgConnect.setAlpha((float) 1.0);
            //onSocialCardClickListener.onYesClick();
        }

        @OnClick(R.id.imageUserGeneral)
        public void cardGeneralOnClick() {
            // onSocialCardClickListener.onGeneralClick();
        }*/
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