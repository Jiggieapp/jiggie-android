package com.jiggie.android.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jiggie.android.R;

import java.util.ArrayList;

/**
 * Created by Wandy on 2/16/2016.
 */
public class EventTagArrayAdapter extends ArrayAdapter<String>{
    private ArrayList<String> tags;
    private Context context;

    public EventTagArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public EventTagArrayAdapter(Context context, ArrayList<String> tags)
    {
        super(context, 0, tags);
        this.context = context;
        this.tags = tags;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_event_tag, parent, false);
            convertView.setTag(holder = new ViewHolder(convertView));
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTag.setText(this.tags.get(position));
        return convertView;
    }

    private static class ViewHolder {
        private TextView txtTag;
        public ViewHolder(View itemView) {
            this.txtTag = (TextView) itemView.findViewById(R.id.txtTag); }
    }


    public void setData(ArrayList<String> tags)
    {
        this.tags = tags;
        notifyDataSetChanged();
    }
}
