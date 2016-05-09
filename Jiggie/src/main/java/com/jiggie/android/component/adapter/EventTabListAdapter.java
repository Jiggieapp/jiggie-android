package com.jiggie.android.component.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.manager.TooltipsManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rangg on 03/11/2015.Re
 */
public class EventTabListAdapter
        extends RecyclerView.Adapter<EventTabListAdapter.ViewHolder> {
    private final Fragment fragment;
    private final ViewSelectedListener listener;
    private final ArrayList<EventModel.Data.Events> items;
    private static final String TAG = EventTabListAdapter.class.getSimpleName();

    public EventTabListAdapter(Fragment fragment, ViewSelectedListener listener) {
        this.items = new ArrayList<>();
        this.listener = listener;
        this.fragment = fragment;
    }

    public void clear() {
        this.items.clear();
    }

    public void addAll(ArrayList<EventModel.Data.Events> items) {
        this.items.addAll(items);
    }

    public void add(EventModel.Data.Events item) {
        this.items.add(item);
    }

    public void setItems(ArrayList<EventModel.Data.Events> items) {
        this.items.addAll(items);
    }

    private Context context;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_event, parent, false), this.listener);
    }

    private EventTagAdapter eventTagAdapter;

    //Added by Aga
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final EventModel.Data.Events item = this.items.get(position);

            int sizePhoto = item.getPhotos().size();
            String imageUrl = null;
            if (sizePhoto > 0) {
                imageUrl = item.getPhotos().get(0);
            } else {
                if (imageUrl == null) {
                    imageUrl = String.format("%simages/event/%s", VolleyHandler.getInstance().getServerHost(), item.get_id());
                    //item.setImageUrl(imageUrl);
                }
            }

            //String imageUrl = String.format("%simages/event/%s", VolleyHandler.getInstance().getServerHost(), item.get_id());

            holder.event = item;
            holder.txtTitle.setText(item.getTitle());

            String[] tags = new String[item.getTags().size()];
            item.getTags().toArray(tags);

            this.eventTagAdapter = new EventTagAdapter(this.context, R.layout.item_event_tag);

            eventTagAdapter.setTags(tags);
            LinearLayoutManager layoutManager = new LinearLayoutManager(fragment.getContext()
                    , LinearLayoutManager.HORIZONTAL, false);
            holder.tagListView.setLayoutManager(layoutManager);
            holder.tagListView.setAdapter(eventTagAdapter);
            //holder.eventTagAdapter.notifyDataSetChanged();
            holder.txtVenueName.setText(item.getVenue_name());
            Utils.d(TAG, "imageUrl " + imageUrl);
            Glide
                    .with(this.fragment)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);

            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(item.getStart_datetime());
            final Date endDate = Common.ISO8601_DATE_FORMAT_UTC.parse(item.getEnd_datetime());
            String simpleDate = App.getInstance().getResources().getString(R.string.event_date_format, Common.SERVER_DATE_FORMAT_ALT.format(startDate), Common.SIMPLE_12_HOUR_FORMAT.format(endDate));

            holder.txtDate.setText(simpleDate);

            if (item.getLikes() > 0) {
                holder.relLike.setVisibility(View.VISIBLE);
                holder.txtCountLike.setText(String.valueOf(item.getLikes()));
            } else {
                holder.relLike.setVisibility(View.GONE);
            }

            if(item.getLowest_price() == 0)
            {
                holder.txtPriceFill.setVisibility(View.GONE);
                holder.txtPriceTitle.setVisibility(View.GONE);
            }
            else
            {
                holder.txtPriceTitle.setShadowLayer(1.6f, 1.5f, 1.3f, context.getResources().getColor(android.R.color.black));
                holder.txtPriceFill.setShadowLayer(1.6f, 1.5f, 1.3f, context.getResources().getColor(android.R.color.black));
                Utils.d(TAG, "item broh " + item.getLowest_price());
                String str = String.format(Locale.US, "Rp %,d", item.getLowest_price());
                holder.txtPriceFill.setText(str);
                //holder.txtPriceFill.setText("Lorem ipsum");
            }

            /*if(position==0){
                Utils.initTooltipWithPoint(fragment.getActivity(), new Point(Utils.getCenterPoint(fragment.getActivity())[0], Utils.getCenterPoint(fragment.getActivity())[1]), fragment.getActivity().getString(R.string.tooltip_event_list), Utils.myPixel(fragment.getActivity(), 320));
            }*/

        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }
    }
    //------------------------------

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.hListView)
        RecyclerView tagListView;
        @Bind(R.id.txtEventName)
        TextView txtTitle;
        @Bind(R.id.txtVenue)
        TextView txtVenueName;
        @Bind(R.id.txtDate)
        TextView txtDate;
        @Bind(R.id.image)
        ImageView image;

        @Bind(R.id.txt_count_like)
        TextView txtCountLike;
        @Bind(R.id.img_love)
        ImageView imgLove;
        @Bind(R.id.rel_like)
        RelativeLayout relLike;
        @Bind(R.id.txtPriceTitle)
        TextView txtPriceTitle;
        @Bind(R.id.txtPriceFill)
        TextView txtPriceFill;

        //private EventTagArrayAdapter eventTagAdapter;
        private ViewSelectedListener listener;
        private EventModel.Data.Events event;

        public ViewHolder(View itemView, ViewSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if ((this.listener = listener) != null)
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onViewSelected(this.event);
                TooltipsManager.setCanShowTooltips(TooltipsManager.TOOLTIP_EVENT_LIST, false);
            }
        }
    }

    public interface ViewSelectedListener {
        void onViewSelected(EventModel.Data.Events event);
    }
}
