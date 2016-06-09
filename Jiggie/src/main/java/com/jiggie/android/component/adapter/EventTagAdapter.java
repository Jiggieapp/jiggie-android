package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.model.TagNewModel;

/**
 * Created by rangg on 11/11/2015.
 */
public class EventTagAdapter
        extends /*BaseAdapter*/ RecyclerView.Adapter<EventTagAdapter.ViewHolder>{
    private final int tagResourceId;
    private String[] tags;
    private final static String TAG = EventTagAdapter.class.getSimpleName();
    private Activity activity;

    public EventTagAdapter(Activity activity, int tagResourceId)
    {
        this.activity = activity;
        this.tagResourceId = tagResourceId;
    }

    public void setTags(String[] tags)
    {
        this.tags = tags;
        //notifyDataSetChanged();
    }

    /*@Override
    public int getCount()
    {
        return this.tags == null ? 0 : this.tags.length;
    }
    @Override
    public Object getItem(int position)
    {
        return this.tags[position];
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(this.tagResourceId, parent, false);
        return new ViewHolder(view);
        //return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtTag.setText(this.tags[position]);

        String text = this.tags[position];

        /*if (text.equalsIgnoreCase("Art & Culture")) {
            holder.txtTag.setBackground(context.getResources().getDrawable(R.drawable.btn_tag_red_f));
            holder.txtTag.setTextColor(context.getResources().getColor(R.color.pink));
        } else if (text.equalsIgnoreCase("Fashion")) {
            holder.txtTag.setBackground(context.getResources().getDrawable(R.drawable.btn_tag_green));
            holder.txtTag.setTextColor(context.getResources().getColor(R.color.green_tag));
        } else if (text.equalsIgnoreCase("Nightlife")) {
            holder.txtTag.setBackground(context.getResources().getDrawable(R.drawable.btn_tag_greydark));
            holder.txtTag.setTextColor(context.getResources().getColor(R.color.greydark_tag));
        } else if (text.equalsIgnoreCase("Music")) {
            holder.txtTag.setBackground(context.getResources().getDrawable(R.drawable.btn_tag_blues));
            holder.txtTag.setTextColor(context.getResources().getColor(R.color.bluedark_tag));
        } else if (text.equalsIgnoreCase("Food & Drink")) {
            holder.txtTag.setBackground(context.getResources().getDrawable(R.drawable.btn_yellow_tag_f));
            holder.txtTag.setTextColor(context.getResources().getColor(R.color.yellow_warning));
        } else if (text.equalsIgnoreCase("Featured")) {
                *//*holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_blue));
                holder.textView.setTextColor(getResources().getColor(R.color.bluedark_tag));*//*
        }*/


        TagNewModel tagNewModel = EventManager.loadTagsListNew();
        int sizeTag = tagNewModel.getData().getTagslist().size();

        for(int i=0;i<sizeTag;i++){
            String nameTag = tagNewModel.getData().getTagslist().get(i).getName();
            if(text.equalsIgnoreCase(nameTag)){
                holder.txtTag.setBackground(setDrawableTag(activity, activity.getResources().getColor(R.color.background), Color.parseColor(tagNewModel.getData().getTagslist().get(position).getColor())));
                holder.txtTag.setTextColor(Color.parseColor(tagNewModel.getData().getTagslist().get(position).getColor()));
                break;
            }
        }

    }

    public static GradientDrawable setDrawableTag(Activity a, int backgroundColor, int borderColor)
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        //shape.setCornerRadii(new float[]{Utils.myPixel(a, 100), Utils.myPixel(a, 100), Utils.myPixel(a, 100), Utils.myPixel(a, 100), 0, 0, 0, 0});
        shape.setCornerRadius(Utils.myPixel(a, 100));
        //shape.setColor(backgroundColor);
        shape.setStroke(Utils.myPixel(a, 1), borderColor);
        return  shape;
    }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public int getItemCount() {
        return this.tags == null ? 0 : this.tags.length;
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate
                    (this.tagResourceId, parent, false);
            //convertView = inflater.inflate(this.tagResourceId, null);
            convertView.setTag(holder = new ViewHolder(convertView));
            *//*holder = new ViewHolder();
            holder.txtTag = (TextView) convertView.findViewById(R.id.txtTag);*//*

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        String temp = "";
        for(int i=0;i<tags.length;i++)
        {
            temp += tags[i] + " ";
        }
        Utils.d(TAG, "temp maneh " + temp);
        Utils.d(TAG, "temp maneh 2 " + tags[position]);
        holder.txtTag.setText(this.tags[position]);
        return convertView;
    }*/

     public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTag;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtTag = (TextView) itemView.findViewById(R.id.txtTag); }

    }
}
