package com.jiggie.android.component.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiggie.android.R;

/**
 * Created by rangg on 11/11/2015.
 */
public class EventTagAdapter extends BaseAdapter {
    private final int tagResourceId;
    private String[] tags;

    public EventTagAdapter(int tagResourceId) { this.tagResourceId = tagResourceId; }
    public void setTags(String[] tags) { this.tags = tags; }

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
            convertView = LayoutInflater.from(parent.getContext()).inflate(this.tagResourceId, parent, false);
            convertView.setTag(holder = new ViewHolder(convertView));
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTag.setText(this.tags[position]);
        return convertView;
    }

    private static class ViewHolder {
        private TextView txtTag;
        public ViewHolder(View itemView) {
            this.txtTag = (TextView) itemView.findViewById(R.id.txtTag); }
    }
}
