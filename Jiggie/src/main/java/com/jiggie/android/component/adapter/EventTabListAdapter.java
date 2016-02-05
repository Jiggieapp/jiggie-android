package com.jiggie.android.component.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Event;
import com.jiggie.android.model.EventModel;

import it.sephiroth.android.library.widget.HListView;

/**
 * Created by rangg on 03/11/2015.
 */
public class EventTabListAdapter extends RecyclerView.Adapter<EventTabListAdapter.ViewHolder> {
    private final Fragment fragment;
    private final ViewSelectedListener listener;
    private final ArrayList<EventModel.Data.Events> items;

    public EventTabListAdapter(Fragment fragment, ViewSelectedListener listener) {
        this.items = new ArrayList<>();
        this.listener = listener;
        this.fragment = fragment;
    }

    public void clear() { this.items.clear(); }
    public void addAll(ArrayList<EventModel.Data.Events> items) { this.items.addAll(items); }
    public void add(EventModel.Data.Events item) { this.items.add(item); }
    public void setItems(ArrayList<EventModel.Data.Events> items){
        this.items.addAll(items);}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false), this.listener);
    }

    //Added by Aga
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final EventModel.Data.Events item = this.items.get(position);

            int sizePhoto = item.getPhotos().size();
            String imageUrl = null;
            if(sizePhoto>0){
                imageUrl = item.getPhotos().get(0);
            }else{
                if (imageUrl == null) {
                    imageUrl = String.format("%simages/event/%s", VolleyHandler.getInstance().getServerHost(), item.get_id());
                    //item.setImageUrl(imageUrl);
                }
            }


            //String imageUrl = String.format("%simages/event/%s", VolleyHandler.getAccountInterface().getServerHost(), item.get_id());

            holder.event = item;
            holder.txtTitle.setText(item.getTitle());

            String[] tags =  new String[item.getTags().size()];
            item.getTags().toArray(tags);

            holder.eventTagAdapter.setTags(tags);
            holder.eventTagAdapter.notifyDataSetChanged();
            holder.txtVenueName.setText(item.getVenue_name());
            Glide.with(this.fragment).load(imageUrl).into(holder.image);

            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(item.getStart_datetime());
            final Date endDate = Common.ISO8601_DATE_FORMAT_UTC.parse(item.getEnd_datetime());
            String simpleDate = App.getInstance().getResources().getString(R.string.event_date_format, Common.SERVER_DATE_FORMAT_ALT.format(startDate), Common.SIMPLE_12_HOUR_FORMAT.format(endDate));

            holder.txtDate.setText(simpleDate);

        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }
    }
    //------------------------------

    @Override
    public int getItemCount() { return this.items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.hListView) HListView tagListView;
        @Bind(R.id.txtEventName) TextView txtTitle;
        @Bind(R.id.txtVenue) TextView txtVenueName;
        @Bind(R.id.txtDate) TextView txtDate;
        @Bind(R.id.image) ImageView image;

        //private EventSelectedListener listener;
        private EventTagAdapter eventTagAdapter;
        //private Event event;

        private ViewSelectedListener listener;
        private EventModel.Data.Events event;

        public ViewHolder(View itemView, ViewSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.tagListView.setAdapter(this.eventTagAdapter = new EventTagAdapter(R.layout.item_event_tag));

            if ((this.listener = listener) != null)
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                //EventModel.Data.Events eventss = this.event;

                //listener.onEventSelected(this.event);
                listener.onViewSelected(this.event);
            }


        }
    }

    public interface ViewSelectedListener{
        void onViewSelected(EventModel.Data.Events event);
    }
}
