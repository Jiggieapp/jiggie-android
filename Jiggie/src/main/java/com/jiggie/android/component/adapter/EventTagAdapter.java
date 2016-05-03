package com.jiggie.android.component.adapter;

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

    public EventTagAdapter(int tagResourceId)
    {
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
