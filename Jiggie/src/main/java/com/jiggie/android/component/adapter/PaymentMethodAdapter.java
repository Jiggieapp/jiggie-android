package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.HowToPayActivity;
import com.jiggie.android.activity.ecommerce.ProductListActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 2/24/2016.
 */
public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    private final ViewSelectedListener listener;
    int section2Start = 3;

    private Activity a;

    private Context context;

    public PaymentMethodAdapter(Activity a, ViewSelectedListener listener, int section2Start) {
        this.a = a;
        this.listener = listener;
        this.section2Start = section2Start;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_payment_method, parent, false), this.listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;
        if (position == 0) {
            holder.txtSection.setText(context.getString(R.string.section_credit_card));
            holder.linSection.setVisibility(View.VISIBLE);
            holder.txtHow.setVisibility(View.GONE);

            holder.img.setImageResource(R.drawable.logo_visa);
            holder.txtPaymentName.setText("• • • • • 12345");

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
            holder.img.setImageResource(R.drawable.logo_mandiri);
            holder.txtPaymentName.setText(context.getString(R.string.va_mandiri));

        } else {
            holder.linSection.setVisibility(View.GONE);

            if (position == (section2Start + 1)) {
                holder.img.setImageResource(R.drawable.logo_bca2);
                holder.txtPaymentName.setText(context.getString(R.string.va_bca));
            } else if (position == (section2Start + 2)) {
                holder.img.setVisibility(View.GONE);
                holder.txtPaymentName.setText(context.getString(R.string.other_bank));
            } else if (position == (section2Start - 1)) {
                holder.img.setImageResource(R.drawable.ic_plus);
                holder.txtPaymentName.setText(context.getString(R.string.vor_payment_cc_new));
                holder.txtPaymentName.setTextColor(context.getResources().getColor(R.color.purple));
                holder.txtPaymentName.setTypeface(holder.txtPaymentName.getTypeface(), Typeface.BOLD);
            } else if (position == 1) {
                holder.img.setImageResource(R.drawable.logo_mastercard);
                holder.txtPaymentName.setText("\u2022 \u2022 \u2022 \u2022 \u2022 35341");
            }
        }
        //holder.txtTicketInfo.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        @Bind(R.id.lin_item)
        LinearLayout linItem;

        private ViewSelectedListener listener;
        private int position;

        public ViewHolder(View itemView, ViewSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            linItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onViewSelected(this.position);
            }
        }
    }

    public interface ViewSelectedListener {
        void onViewSelected(int position);
    }

    public class ClickableURLSpan extends URLSpan {
        public ClickableURLSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {

            Log.d("How it works", "click");
            a.startActivity(new Intent(a, HowToPayActivity.class));

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true);
        }

    }

}
