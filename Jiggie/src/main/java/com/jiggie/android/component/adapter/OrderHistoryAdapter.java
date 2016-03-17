package com.jiggie.android.component.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.PurchaseHistoryModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/6/2016.
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private final ViewSelectedListener listener;
    private Context context;
    ArrayList<PurchaseHistoryModel.Data.Order_list> items;

    public OrderHistoryAdapter(ArrayList<PurchaseHistoryModel.Data.Order_list> items, ViewSelectedListener listener)
    {
        this.items = items;
        this.listener = listener;
    }

    public OrderHistoryAdapter(ViewSelectedListener listener) {
        this.listener = listener;
        items = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_order_history, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        /*if(position==0){
            holder.txtEvent.setText(context.getString(R.string.his_ev1));
            holder.txtVenue.setText(context.getString(R.string.his_ven1));
            holder.txtDate.setText(context.getString(R.string.his_dt1));
            holder.imgBadge.setImageResource(R.drawable.bdg_expired);
        }
        else if(position==1){
            holder.txtEvent.setText(context.getString(R.string.his_ev2));
            holder.txtVenue.setText(context.getString(R.string.his_ven2));
            holder.txtDate.setText(context.getString(R.string.his_dt2));
            holder.imgBadge.setImageResource(R.drawable.bdg_paid);
        }if(position==2){
            holder.txtEvent.setText(context.getString(R.string.his_ev3));
            holder.txtVenue.setText(context.getString(R.string.his_ven3));
            holder.txtDate.setText(context.getString(R.string.his_dt3));
            holder.imgBadge.setImageResource(R.drawable.bdg_unpaid);
        }*/

        PurchaseHistoryModel.Data.Order_list item = items.get(position);
        holder.txtEvent.setText(item.getEvent().getTitle());
        holder.txtVenue.setText(item.getEvent().getVenue_name());
        holder.txtDate.setText(item.getEvent().getStart_datetime_str());

        if(item.getOrder().getPayment_status()
                .equalsIgnoreCase(Utils.PAYMENT_STATUS_AWAITING_PAYMENT))
        {
            holder.container.setBackground(
                    App.getInstance().getResources().getDrawable(R.drawable.btn_tag_yellow));
            /*holder.container.setBackground(
                    App.getInstance().getResources().getDrawable(R.drawable.btn_tag_blue));*/
            holder.lblStatus.setText(
                    App.getInstance().getResources().getString(R.string.awaiting_payment)
            );
        }
        else if(item.getOrder().getPayment_status()
                .equalsIgnoreCase(Utils.PAYMENT_STATUS_VOID))
        {
            holder.container.setBackground(
                    App.getInstance().getResources().getDrawable(R.drawable.btn_tag_red));
            holder.lblStatus.setText(
                    App.getInstance().getResources().getString(R.string.is_void)
            );
        }
        else if(item.getOrder().getPayment_status()
                .equalsIgnoreCase(Utils.PAYMENT_STATUS_EXPIRE))
        {
            holder.container.setBackground(
                    App.getInstance().getResources().getDrawable(R.drawable.btn_tag_red));
            holder.lblStatus.setText(
                    App.getInstance().getResources().getString(R.string.expired)
            );
        }
        else if(item.getOrder().getPayment_status()
                .equalsIgnoreCase(Utils.PAYMENT_STATUS_PAID))
        {
            holder.container.setBackground(
                    App.getInstance().getResources().getDrawable(R.drawable.btn_tag_blue));
            holder.lblStatus.setText(
                    App.getInstance().getResources().getString(R.string.paid)
            );
        }
        else if(item.getOrder().getPayment_status()
                .equalsIgnoreCase(Utils.PAYMENT_STATUS_REFUND))
        {
            holder.container.setBackground(
                    App.getInstance().getResources().getDrawable(R.drawable.btn_tag_red));
            holder.lblStatus.setText(
                    App.getInstance().getResources().getString(R.string.refund)
            );
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewSelectedListener listener;
        @Bind(R.id.txt_event)
        TextView txtEvent;
        @Bind(R.id.txt_venue)
        TextView txtVenue;
        @Bind(R.id.txt_date)
        TextView txtDate;
        /*@Bind(R.id.img_badge)
        ImageView imgBadge;*/
        @Bind(R.id.textView)
        TextView lblStatus;
        @Bind(R.id.container)
        LinearLayout container;

        public ViewHolder(View itemView, ViewSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if ((this.listener = listener) != null)
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                //listener.onViewSelected(this.event);
            }
        }
    }

    public interface ViewSelectedListener {
        void onViewSelected();
    }

    public void add(PurchaseHistoryModel.Data.Order_list order_list)
    {
        items.add(order_list);
        notifyDataSetChanged();
    }
}
