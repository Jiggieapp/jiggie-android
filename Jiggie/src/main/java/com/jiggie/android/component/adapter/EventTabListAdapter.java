package com.jiggie.android.component.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.manager.EventManager;
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
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //private Fragment fragment;
    private ViewSelectedListener listener;
    private ArrayList<EventModel.Data.Events> items;
    private Context context;
    private static final String TAG = EventTabListAdapter.class.getSimpleName();

    /*public EventTabListAdapter(Fragment fragment, ViewSelectedListener listener) {
        this.items = new ArrayList<>();
        this.listener = listener;
        this.fragment = fragment;
    }*/

    public EventTabListAdapter(Context context, ViewSelectedListener listener)
    {
        this.items = new ArrayList<>();
        this.context = context;
        this.listener = listener;
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        if(viewType == 1)
            return new EventsViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_event, parent, false), this.listener);
        else
            return new ThemesViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_themes, parent, false), this.listener);

    }

    private EventTagAdapter eventTagAdapter;

    //Added by Aga
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final EventModel.Data.Events item = this.items.get(position);
        if (viewHolder instanceof EventsViewHolder) {
            EventsViewHolder holder = (EventsViewHolder) viewHolder;
            try {
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

                //---------
                StringBuilder builder = new StringBuilder();
                for (String s : tags) {
                    builder.append(s);
                }
                String d = builder.toString();
                //-----------

                this.eventTagAdapter = new EventTagAdapter(context, R.layout.item_event_tag);

                eventTagAdapter.setTags(tags);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context
                        , LinearLayoutManager.HORIZONTAL, false);
                holder.tagListView.setLayoutManager(layoutManager);
                holder.tagListView.setAdapter(eventTagAdapter);
                //holder.eventTagAdapter.notifyDataSetChanged();
                holder.txtVenueName.setText(item.getVenue_name());
                Glide
                        .with(context)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.image);

                final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(item.getStart_datetime());
                final Date endDate = Common.ISO8601_DATE_FORMAT_UTC.parse(item.getEnd_datetime());
            /*String simpleDate = App.getInstance().getResources().getString(R.string.event_date_format
                    , Common.SERVER_DATE_FORMAT_ALT.format(startDate), Common.SIMPLE_12_HOUR_FORMAT.format(endDate));*/
                String simpleDate = Utils.getTimeForEvent(startDate, endDate, item.getTz());

                holder.txtDate.setText(simpleDate);

                if (item.getLikes() > 0) {
                    holder.relLike.setVisibility(View.VISIBLE);
                    holder.txtCountLike.setText(String.valueOf(item.getLikes()));
                } else {
                    holder.relLike.setVisibility(View.GONE);
                }

            /*if (StringUtility.isEquals(EventManager.FullfillmentTypes.RESERVATION, fullfillmentType, true)) {
                txtExternalSite.setVisibility(View.GONE);
                txtBookNow.setText(R.string.book_now);
            } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.PURCHASE, fullfillmentType, true)) {
                txtExternalSite.setVisibility(View.GONE);
                txtBookNow.setText(R.string.book_now);
            } else if (StringUtility.isEquals(EventManager.FullfillmentTypes.TICKET, fullfillmentType, true)) {
                txtExternalSite.setVisibility(View.GONE);
                txtBookNow.setText(getResources().getString(R.string.book_now));
            }*/
                final String fullfillmentType = item.getFullfillment_type();
                final boolean isBookable = (StringUtility.isEquals(EventManager.FullfillmentTypes.RESERVATION, fullfillmentType, true)
                        || StringUtility.isEquals(EventManager.FullfillmentTypes.PURCHASE, fullfillmentType, true)
                        || (StringUtility.isEquals(EventManager.FullfillmentTypes.TICKET, fullfillmentType, true))); //free (tickets, tables, purchase)
                if (item.getLowest_price() == 0 && isBookable) {
                    holder.txtPriceTitle.setVisibility(View.VISIBLE);
                    holder.txtPriceFill.setVisibility(View.VISIBLE);
                    holder.txtPriceFill.setText(context.getResources().getString(R.string.free));
                } else if (item.getLowest_price() > 0 && isBookable) {
                    holder.txtPriceTitle.setShadowLayer(1.6f, 1.5f, 1.3f, context.getResources().getColor(android.R.color.black));
                    holder.txtPriceFill.setShadowLayer(1.6f, 1.5f, 1.3f, context.getResources().getColor(android.R.color.black));
                    holder.txtPriceTitle.setVisibility(View.VISIBLE);
                    holder.txtPriceFill.setVisibility(View.VISIBLE);
                    try {
                        //String str = String.format(Locale.US, "Rp %,d", item.getLowest_price());
                        String str = StringUtility.getRupiahFormat(item.getLowest_price().toString());
                        holder.txtPriceFill.setText(str);
                    } catch (Exception e) {
                        Utils.d(TAG, "exception " + e.toString());
                    }
                } else {
                    holder.txtPriceTitle.setVisibility(View.GONE);
                    holder.txtPriceFill.setVisibility(View.GONE);
                }

            /*if(position==0){
                Utils.initTooltipWithPoint(fragment.getActivity(), new Point(Utils.getCenterPoint(fragment.getActivity())[0], Utils.getCenterPoint(fragment.getActivity())[1]), fragment.getActivity().getString(R.string.tooltip_event_list), Utils.myPixel(fragment.getActivity(), 320));
            }*/

            } catch (ParseException e) {
                throw new RuntimeException(App.getErrorMessage(e), e);
            }
        }
        else if(viewHolder instanceof ThemesViewHolder)
        {
            ThemesViewHolder holder = (ThemesViewHolder) viewHolder;
            try
            {
                holder.event = item;
                holder.txtTitle.setText(item.getTitle() + " bah");

                Glide
                        .with(context)
                        .load(items.get(position).getPhotos().get(0))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.image);
                holder.txtDescription.setText(item.getDescription());

            }
            catch (Exception e)
            {

            }
        }

    }
    //------------------------------

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public EventsViewHolder(View itemView, ViewSelectedListener listener) {
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

    static class ThemesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.txtEventName)
        TextView txtTitle;
        @Bind(R.id.txtDescription)
        TextView txtDescription;
        @Bind(R.id.image)
        ImageView image;

        private int themeId;

        //private EventTagArrayAdapter eventTagAdapter;
        private ViewSelectedListener listener;
        private EventModel.Data.Events event;

        public ThemesViewHolder(View itemView, ViewSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if ((this.listener = listener) != null)
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                //Utils.d(TAG, "event " + event.getTitle() + "/" + event.get_id() + "/" + event.getThemes_id());
                listener.onViewSelected(this.event);
                TooltipsManager.setCanShowTooltips(TooltipsManager.TOOLTIP_EVENT_LIST, false);
            }
        }
    }

    public interface ViewSelectedListener {
        void onViewSelected(EventModel.Data.Events event);
    }

    /*public interface ThemeSelectedListener
    {
        void onThemeSelected(int position);
    }*/

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).isEvent)
            return 1;
        else return 0;
    }

}
