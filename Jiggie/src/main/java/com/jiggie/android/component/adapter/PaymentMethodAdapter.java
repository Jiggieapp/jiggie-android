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
import com.jiggie.android.model.CCModel;
import com.jiggie.android.model.CCScreenModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 2/24/2016.
 */
public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    private final ViewSelectedListener clickListener;
    private final LongClickListener longClickListener;
    int section2Start;

    private Activity a;

    private Context context;
    private ArrayList<CCScreenModel> arrDataCredit = new ArrayList<>();

    public PaymentMethodAdapter(Activity a, ViewSelectedListener clickListener, LongClickListener longClickListener, int section2Start, ArrayList<CCScreenModel> arrDataCredit) {
        this.a = a;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.section2Start = section2Start;
        this.arrDataCredit = arrDataCredit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_payment_method, parent, false), this.clickListener, this.longClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;
        CCScreenModel dataCredit = null;
        if(position<=(arrDataCredit.size()-1)){
            dataCredit = arrDataCredit.get(position);
            holder.dataCredit = dataCredit;
        }

        if (position == 0) {
            holder.txtSection.setText(context.getString(R.string.section_credit_card));
            holder.linSection.setVisibility(View.VISIBLE);
            holder.txtHow.setVisibility(View.GONE);

            if((section2Start - 1)!=0){
                String maskedCard = dataCredit.getCreditcardInformation().getMasked_card();
                holder.txtPaymentName.setText("• • • • "+maskedCard.substring(maskedCard.indexOf("-")+1, maskedCard.length()));

                String headCC = maskedCard.substring(0, 1);
                if(headCC.equals("4")){
                    holder.img.setImageResource(R.drawable.logo_visa);
                }else{
                    holder.img.setImageResource(R.drawable.logo_mastercard);
                }
            }else{
                //execute kalau data kosong
                holder.img.setImageResource(R.drawable.ic_plus);
                holder.txtPaymentName.setText(context.getString(R.string.vor_payment_cc_new));
                holder.txtPaymentName.setTextColor(context.getResources().getColor(R.color.purple));
                holder.txtPaymentName.setTypeface(holder.txtPaymentName.getTypeface(), Typeface.BOLD);
            }



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

            /*if (position == (section2Start + 1)) {
                holder.img.setImageResource(R.drawable.logo_bca2);
                holder.txtPaymentName.setText(context.getString(R.string.va_bca));
            } */
            if (position == (section2Start + 1)) {
                holder.img.setVisibility(View.GONE);
                holder.txtPaymentName.setText(context.getString(R.string.other_bank));
            }
            else if (position == (section2Start - 1)) {
                if((section2Start - 1)!=0){
                    holder.img.setImageResource(R.drawable.ic_plus);
                    holder.txtPaymentName.setText(context.getString(R.string.vor_payment_cc_new));
                    holder.txtPaymentName.setTextColor(context.getResources().getColor(R.color.purple));
                    holder.txtPaymentName.setTypeface(holder.txtPaymentName.getTypeface(), Typeface.BOLD);
                }

            }else{
                String maskedCard = dataCredit.getCreditcardInformation().getMasked_card();
                holder.txtPaymentName.setText("• • • • "+maskedCard.substring(maskedCard.indexOf("-")+1, maskedCard.length()));

                String headCC = maskedCard.substring(0, 1);
                if(headCC.equals("4")){
                    holder.img.setImageResource(R.drawable.logo_visa);
                }else{
                    holder.img.setImageResource(R.drawable.logo_mastercard);
                }
            }
            /*else if (position == 1) {
                holder.img.setImageResource(R.drawable.logo_mastercard);
                holder.txtPaymentName.setText("\u2022 \u2022 \u2022 \u2022 \u2022 35341");
            }*/
        }
        //holder.txtTicketInfo.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return (arrDataCredit.size() + 3);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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

        private ViewSelectedListener clickListener;
        private  LongClickListener longClickListener;
        private int position;
        private CCScreenModel dataCredit = null;

        public ViewHolder(View itemView, ViewSelectedListener clickListener, LongClickListener longClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.clickListener = clickListener;
            this.longClickListener = longClickListener;
            linItem.setOnClickListener(this);
            linItem.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onViewSelected(this.position, dataCredit);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener != null) {
                longClickListener.onLongClick(this.position, dataCredit);
            }
            return true;
        }
    }

    public interface ViewSelectedListener {
        void onViewSelected(int position, CCScreenModel dataCredit);
    }

    public interface LongClickListener {
        void onLongClick(int position, CCScreenModel dataCredit);
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
