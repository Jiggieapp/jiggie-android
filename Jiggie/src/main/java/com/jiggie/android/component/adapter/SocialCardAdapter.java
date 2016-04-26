package com.jiggie.android.component.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.andtinder.view.CardStackAdapter;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.SocialModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wandy on 4/25/2016.
 */
public class SocialCardAdapter extends CardStackAdapter {
    private final static String TAG = SocialCardAdapter.class.getSimpleName();
    ArrayList<SocialModel.Data.SocialFeeds> data;
    Context context;

    public SocialCardAdapter(Context mContext) {
        super(mContext);
        context = mContext;
    }

    public SocialCardAdapter(ArrayList<SocialModel.Data.SocialFeeds> data, ArrayList<com.andtinder.model.CardModel> item, Context mContext)
    {
        super(mContext, item);
        this.data = data;
        context = mContext;
    }

    @Override
    protected View getCardView(int i, com.andtinder.model.CardModel cardModel, View view, ViewGroup viewGroup) {
        return getCardView(i, data.get(i), view, viewGroup);
    }

    protected View getCardView(int position, SocialModel.Data.SocialFeeds model, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            convertView = inflater.inflate(R.layout.item_social_card_general, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            assert convertView != null;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*((ImageView)convertView.findViewById(com.andtinder.R.id.image)).setImageDrawable(model.getCardImageDrawable());
        ((TextView)convertView.findViewById(com.andtinder.R.id.title)).setText(model.getTitle());
        ((TextView)convertView.findViewById(com.andtinder.R.id.description)).setText(model.getDescription());*/
        holder.generalTxtUser.setText(model.getFrom_first_name());
        holder.generalTxtEvent.setText(model.getEvent_name());
        holder.generalTxtUser.setText(context.getString(R.string.user_viewing
                , model.getFrom_first_name()));
        holder.generalTxtConnect.setText(context.getString(R.string.connect_with
                , model.getFrom_first_name()));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.card)
        CardView cardView;

        @Bind(R.id.cardGeneral)
        FrameLayout cardGeneral;

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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            //generalTxtUser = (TextView) ;
        }
    }
}
