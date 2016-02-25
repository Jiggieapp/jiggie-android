package com.jiggie.android.component.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 2/24/2016.
 */
public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    private final ViewSelectedListener listener;


    private Context context;

    public PaymentMethodAdapter(ViewSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_payment_method, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*try {

        }catch (ParseException e){
            throw new RuntimeException(App.getErrorMessage(e), e);
        }*/
        if (position == 0) {
            holder.txtSection.setText(context.getString(R.string.section_credit_card));
            holder.linSection.setVisibility(View.VISIBLE);
        } else if (position == 3) {
            holder.txtSection.setText(context.getString(R.string.section_va));
            holder.linSection.setVisibility(View.VISIBLE);
        } else {
            holder.linSection.setVisibility(View.GONE);
        }
        //holder.txtTicketInfo.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.txt_section)
        TextView txtSection;
        @Bind(R.id.lin_section)
        LinearLayout linSection;
        @Bind(R.id.img)
        ImageView img;
        @Bind(R.id.txt_payment_name)
        TextView txtPaymentName;

        private ViewSelectedListener listener;
        //private EventModel.Data.Events event;

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

}
