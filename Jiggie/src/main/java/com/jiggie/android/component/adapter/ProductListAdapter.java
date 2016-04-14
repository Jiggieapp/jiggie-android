package com.jiggie.android.component.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ProductListModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 2/24/2016.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private final ViewSelectedListener listener;
    private Context context;

    private String eventName;
    private String venueName;
    private String startTime;
    private boolean isTwoType;
    private int section2Start;
    private ArrayList<ProductListModel.Data.ProductList.Purchase> dataPurchase = new ArrayList<>();
    private ArrayList<ProductListModel.Data.ProductList.Reservation> dataReservation = new ArrayList<>();

    public ProductListAdapter(String eventName, String venueName, String startTime, boolean isTwoType, int section2Start, ArrayList<ProductListModel.Data.ProductList.Purchase> dataPurchase, ArrayList<ProductListModel.Data.ProductList.Reservation> dataReservation, ViewSelectedListener listener) {
        this.listener = listener;
        this.eventName = eventName;
        this.venueName = venueName;
        this.startTime = startTime;
        this.isTwoType = isTwoType;
        this.section2Start = section2Start;
        this.dataPurchase = dataPurchase;
        this.dataReservation = dataReservation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_product_list, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductListModel.Data.ProductList.Purchase itemPurchases = null;
        ProductListModel.Data.ProductList.Reservation itemReservations = null;

        holder.position = position;
        holder.isTwoType = isTwoType;
        holder.section2Start = section2Start;

        if (isTwoType) {
            if (position < section2Start) {
                itemPurchases = dataPurchase.get(position);
                holder.itemPurchase = itemPurchases;
            } else {
                int index = position - section2Start;
                itemReservations = dataReservation.get(index);
                holder.itemReservation = itemReservations;
            }
        } else {
            itemPurchases = dataPurchase.get(position);
            holder.itemPurchase = itemPurchases;
        }

        holder.lblEventName.setText(eventName);

        try {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(startTime);
            holder.lblEventLocation.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate) + " - " + venueName);
        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }


        if (isTwoType) {
            if (position < section2Start) {
                if (itemPurchases.getStatus().equals(Common.FIELD_STATUS_SOLD_OUT) || itemPurchases.getQuantity() == 0) {
                    holder.txtTicketName.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                    holder.txtTicketName.setText(itemPurchases.getName() + " " + "(SOLD OUT)");
                } else {
                    holder.txtTicketName.setTextColor(context.getResources().getColor(android.R.color.black));
                    holder.txtTicketName.setText(itemPurchases.getName());
                }

                //holder.txtTicketInfo.setText(itemPurchases.getSummary());
                holder.txtPrice.setText(StringUtility.getRupiahFormat(itemPurchases.getPrice()));
                //holder.txtPriceInfo.setText(context.getString(R.string.pr_max_purchase) + " " + itemPurchases.getMax_purchase());
                if (position == 0) {
                    holder.txtSectionTicket.setText(context.getString(R.string.section_ticket));
                    holder.linSection.setVisibility(View.VISIBLE);
                    //holder.headerContainer.setVisibility(View.VISIBLE);
                    holder.headerContainer.setVisibility(View.GONE);
                } else {
                    holder.linSection.setVisibility(View.GONE);
                }
                //holder.headerContainer.setVisibility(View.GONE);
            } else {
                if (itemReservations.getStatus().equals(Common.FIELD_STATUS_SOLD_OUT) || itemReservations.getQuantity() == 0) {
                    holder.txtTicketName.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                    holder.txtTicketName.setText(itemReservations.getName() + " " + "(SOLD OUT)");
                } else {
                    holder.txtTicketName.setTextColor(context.getResources().getColor(android.R.color.black));
                    holder.txtTicketName.setText(itemReservations.getName());
                }
                //holder.txtTicketInfo.setText(itemReservations.getSummary());
                holder.txtPrice.setText(StringUtility.getRupiahFormat(itemReservations.getPrice()));
                //holder.txtPriceInfo.setText(context.getString(R.string.pr_max_guest) + " " + itemReservations.getMax_guests());
                if ((position - section2Start) == 0) {
                    holder.txtSectionTicket.setText(context.getString(R.string.section_table));
                    holder.linSection.setVisibility(View.VISIBLE);
                } else {
                    holder.linSection.setVisibility(View.GONE);
                }
            }
        } else {
            if (itemPurchases.getStatus().equals(Common.FIELD_STATUS_SOLD_OUT) || itemPurchases.getQuantity() == 0) {
                holder.txtTicketName.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                holder.txtTicketName.setText(itemPurchases.getName() + " " + "(SOLD OUT)");
            } else {
                holder.txtTicketName.setTextColor(context.getResources().getColor(android.R.color.black));
                holder.txtTicketName.setText(itemPurchases.getName());
            }
            //holder.txtTicketInfo.setText(itemPurchases.getSummary());
            holder.txtPrice.setText(StringUtility.getRupiahFormat(itemPurchases.getPrice()));
            //holder.txtPriceInfo.setText(context.getString(R.string.pr_max_purchase) + " " + itemPurchases.getMax_purchase());
            if (position == 0) {
                holder.txtSectionTicket.setText(context.getString(R.string.section_ticket));
                holder.linSection.setVisibility(View.VISIBLE);
                //holder.headerContainer.setVisibility(View.VISIBLE);
                holder.headerContainer.setVisibility(View.GONE);
            } else {
                holder.linSection.setVisibility(View.GONE);
            }
            //holder.headerContainer.setVisibility(View.GONE);
        }

        //holder.txtTicketInfo.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (isTwoType) {
            size = dataPurchase.size() + dataReservation.size();
        } else {
            size = dataPurchase.size();
        }
        return size;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.lblEventName)
        TextView lblEventName;
        @Bind(R.id.lblEventLocation)
        TextView lblEventLocation;
        @Bind(R.id.header_container)
        RelativeLayout headerContainer;
        @Bind(R.id.txt_section_ticket)
        TextView txtSectionTicket;
        @Bind(R.id.lin_section)
        LinearLayout linSection;
        @Bind(R.id.txt_ticket_name)
        TextView txtTicketName;
        /*@Bind(R.id.txt_ticket_info)
        TextView txtTicketInfo;*/
        @Bind(R.id.txt_price)
        TextView txtPrice;
        /*@Bind(R.id.txt_price_info)
        TextView txtPriceInfo;*/
        @Bind(R.id.lin_item)
        LinearLayout linItem;
        @Bind(R.id.card_view)
        CardView cardView;

        private ViewSelectedListener listener;
        private int position;
        private boolean isTwoType;
        private int section2Start;
        private ProductListModel.Data.ProductList.Purchase itemPurchase;
        private ProductListModel.Data.ProductList.Reservation itemReservation;


        public ViewHolder(View itemView, ViewSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.listener = listener;
            /*this.position = position;
            this.isTwoType = isTwoType;
            this.section2Start = section2Start;
            this.itemPurchase = itemPurchase;
            this.itemReservation = itemReservation;*/


            //linItem.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                //should pass data
                if (isTwoType) {
                    if (position < section2Start) {
                        listener.onViewSelected(this.position, this.itemPurchase);
                    } else {
                        listener.onViewSelected(this.position, this.itemReservation);
                    }
                } else {
                    listener.onViewSelected(this.position, this.itemPurchase);
                }
            }
        }
    }

    public interface ViewSelectedListener {
        void onViewSelected(int position, Object object);
    }

}
