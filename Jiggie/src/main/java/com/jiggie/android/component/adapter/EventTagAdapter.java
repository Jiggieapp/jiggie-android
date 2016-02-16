package com.jiggie.android.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;

/**
 * Created by rangg on 11/11/2015.
 */
public class EventTagAdapter extends BaseAdapter {
    private final int tagResourceId;
    private String[] tags;
    private final static String TAG = EventTagAdapter.class.getSimpleName();
    private LayoutInflater inflater;

    public EventTagAdapter(int tagResourceId)
    {
        this.tagResourceId = tagResourceId;
    }

    public void setTags(String[] tags)
    {
        this.tags = tags;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() { return this.tags == null ? 0 : this.tags.length; }
    @Override
    public Object getItem(int position) { return this.tags[position]; }
    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate
                    (this.tagResourceId, parent, false);
            //convertView = inflater.inflate(this.tagResourceId, null);
            convertView.setTag(holder = new ViewHolder(convertView));
            /*holder = new ViewHolder();
            holder.txtTag = (TextView) convertView.findViewById(R.id.txtTag);*/

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
    }

    private static class ViewHolder {
        private TextView txtTag;
        public ViewHolder(View itemView) {
            this.txtTag = (TextView) itemView.findViewById(R.id.txtTag); }

        public ViewHolder()
        {

        }
    }
}
