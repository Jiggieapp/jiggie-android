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

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jiggie.android.App;
import com.android.jiggie.R;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.model.Event;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by rangg on 03/11/2015.
 */
public class EventTabListAdapter extends RecyclerView.Adapter<EventTabListAdapter.ViewHolder> {
    private final EventSelectedListener listener;
    private final ArrayList<Event> items;
    private final Fragment fragment;

    public EventTabListAdapter(Fragment fragment, EventSelectedListener listener) {
        this.items = new ArrayList<>();
        this.listener = listener;
        this.fragment = fragment;
    }

    public void addAll(Collection<Event> items) { this.items.addAll(items); }
    public void add(Event item) { this.items.add(item); }
    public void clear() { this.items.clear(); }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final Event item = this.items.get(position);
            String imageUrl = item.getImageUrl();

            if (imageUrl == null) {
                imageUrl = String.format("%simages/event/%s", VolleyHandler.getInstance().getServerHost(), item.getId());
                item.setImageUrl(imageUrl);
            }

            holder.event = item;
            holder.txtTitle.setText(item.getTitle());
            holder.eventTagAdapter.setTags(item.getTags());
            holder.eventTagAdapter.notifyDataSetChanged();
            holder.txtVenueName.setText(item.getVenueName());
            Glide.with(this.fragment).load(imageUrl).into(holder.image);
            holder.txtDate.setText(item.getSimpleDate());
        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }
    }

    @Override
    public int getItemCount() { return this.items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.hListView) HListView tagListView;
        @Bind(R.id.txtEventName) TextView txtTitle;
        @Bind(R.id.txtVenue) TextView txtVenueName;
        @Bind(R.id.txtDate) TextView txtDate;
        @Bind(R.id.image) ImageView image;

        private EventSelectedListener listener;
        private EventTagAdapter eventTagAdapter;
        private Event event;

        public ViewHolder(View itemView, EventSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.tagListView.setAdapter(this.eventTagAdapter = new EventTagAdapter(R.layout.item_event_tag));

            if ((this.listener = listener) != null)
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onEventSelected(this.event);
        }
    }

    public interface EventSelectedListener {
        void onEventSelected(Event event);
    }
}
