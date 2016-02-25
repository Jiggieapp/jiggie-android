package com.jiggie.android.component.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 2/24/2016.
 */
public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    private final ViewSelectedListener listener;
    int section2Start = 3;


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
            holder.txtHow.setVisibility(View.GONE);
        } else if (position == section2Start) {
            String sectionClick = "HOW IT WORKS?";
            holder.txtSection.setText(context.getString(R.string.section_va));
            holder.linSection.setVisibility(View.VISIBLE);

            SpannableStringBuilder builder = new SpannableStringBuilder(sectionClick);
            int start = 0;
            int end = sectionClick.length();

            ClickableURLSpan url = new ClickableURLSpan(sectionClick);
            builder.setSpan(url, start, end, 0);

            holder.txtHow.setText(builder);
            holder.txtHow.setMovementMethod(LinkMovementMethod.getInstance());

            holder.txtPaymentName.setText(context.getString(R.string.va_mandiri));

        } else {
            holder.linSection.setVisibility(View.GONE);

            if(position==(section2Start+1)){
                holder.txtPaymentName.setText(context.getString(R.string.va_bca));
            }else if(position==(section2Start+2)){
                holder.img.setVisibility(View.GONE);
                holder.txtPaymentName.setText(context.getString(R.string.other_bank));
            }
        }
        //holder.txtTicketInfo.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.txt_section)
        TextView txtSection;
        @Bind(R.id.txt_how)
        TextView txtHow;
        @Bind(R.id.lin_section)
        RelativeLayout linSection;
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

    public class ClickableURLSpan extends URLSpan {
        public ClickableURLSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {

            Log.d("How it works", "click");

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true);
        }

    }

}
