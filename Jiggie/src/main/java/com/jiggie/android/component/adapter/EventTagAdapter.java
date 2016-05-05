package com.jiggie.android.component.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiggie.android.R;

/**
 * Created by rangg on 11/11/2015.
 */
public class EventTagAdapter
        extends /*BaseAdapter*/ RecyclerView.Adapter<EventTagAdapter.ViewHolder>{
    private final int tagResourceId;
    private String[] tags;
    private final static String TAG = EventTagAdapter.class.getSimpleName();
    private Context context;

    public EventTagAdapter(Context context, int tagResourceId)
    {
        this.context = context;
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

        if (text.equalsIgnoreCase("Art & Culture")) {
            holder.txtTag.setBackground(context.getResources().getDrawable(R.drawable.btn_tag_red));
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
            holder.txtTag.setBackground(context.getResources().getDrawable(R.drawable.btn_tag_yellow));
            holder.txtTag.setTextColor(context.getResources().getColor(R.color.yellow_warning));
        } else if (text.equalsIgnoreCase("Featured")) {
                /*holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_blue));
                holder.textView.setTextColor(getResources().getColor(R.color.bluedark_tag));*/
        }
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
