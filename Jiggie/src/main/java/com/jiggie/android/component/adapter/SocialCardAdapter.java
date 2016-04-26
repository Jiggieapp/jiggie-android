package com.jiggie.android.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardStackAdapter;
import com.jiggie.android.R;

/**
 * Created by Wandy on 4/25/2016.
 */
public class SocialCardAdapter extends CardStackAdapter {

    public SocialCardAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View getCardView(int position, CardModel model, View convertView, ViewGroup parent)  {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            convertView = inflater.inflate(R.layout.item_social_card, parent, false);

            assert convertView != null;
        }

        ((ImageView)convertView.findViewById(com.andtinder.R.id.image)).setImageDrawable(model.getCardImageDrawable());
        ((TextView)convertView.findViewById(com.andtinder.R.id.title)).setText(model.getTitle());
        ((TextView)convertView.findViewById(com.andtinder.R.id.description)).setText(model.getDescription());
        return convertView;
    }
}
