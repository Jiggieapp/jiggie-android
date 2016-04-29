package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
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

import com.jiggie.android.R;
import com.jiggie.android.activity.ecommerce.HowToPayActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.CCScreenModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.PaymentMethod;
import com.jiggie.android.model.SummaryModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 2/24/2016.
 */
public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    private final ViewSelectedListener clickListener;
    private final LongClickListener longClickListener;
    int section2Start;
    long order_id;

    private Activity a;

    private Context context;
    private ArrayList<CCScreenModel> arrDataCredit = new ArrayList<>();
    private String paymentType;
    EventDetailModel.Data.EventDetail eventDetail;
    SummaryModel.Data.Product_summary productSummary;
    HashMap<String, PaymentMethod.Data.Paymentmethod> paymentMethod;
    public final static String TAG = PaymentMethodAdapter.class.getSimpleName();

    public PaymentMethodAdapter(Activity a, ViewSelectedListener clickListener, LongClickListener longClickListener, int section2Start, ArrayList<CCScreenModel> arrDataCredit,
                                long order_id, String paymentType, SummaryModel.Data.Product_summary productSummary, EventDetailModel.Data.EventDetail eventDetail) {
        this.a = a;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.section2Start = section2Start;
        this.arrDataCredit = arrDataCredit;
        this.order_id = order_id;
        this.paymentType = paymentType;
        this.productSummary = productSummary;
        this.eventDetail = eventDetail;
    }

    public PaymentMethodAdapter(Activity a, ViewSelectedListener clickListener, LongClickListener longClickListener, int section2Start, ArrayList<CCScreenModel> arrDataCredit,
                                long order_id, String paymentType, SummaryModel.Data.Product_summary productSummary, EventDetailModel.Data.EventDetail eventDetail, HashMap<String, PaymentMethod.Data.Paymentmethod> paymentMethod) {
        this.a = a;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.section2Start = section2Start;
        this.arrDataCredit = arrDataCredit;
        this.order_id = order_id;
        this.paymentType = paymentType;
        this.productSummary = productSummary;
        this.eventDetail = eventDetail;
        this.paymentMethod = paymentMethod;
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
        if (position <= (arrDataCredit.size() - 1)) {
            dataCredit = arrDataCredit.get(position);
            holder.dataCredit = dataCredit;
        }

        if (position == 0) {
            if(paymentMethod.get("cc") != null)
            {
                holder.txtSection.setText(context.getString(R.string.section_credit_card));
                holder.linSection.setVisibility(View.VISIBLE);
                holder.txtHow.setVisibility(View.GONE);

                if(paymentMethod.get("cc").status == true){
                    if ((section2Start - 1) != 0) {
                        String maskedCard = dataCredit.getCreditcardInformation().getMasked_card();
                        holder.txtPaymentName.setText("• • • • " + maskedCard.substring(maskedCard.indexOf("-") + 1, maskedCard.length()));

                        String headCC = maskedCard.substring(0, 1);
                        if (headCC.equals("4")) {
                            holder.img.setVisibility(View.VISIBLE);
                            holder.img.setImageResource(R.drawable.logo_visa2);
                        } else if (headCC.equals("5")) {
                            holder.img.setVisibility(View.VISIBLE);
                            holder.img.setImageResource(R.drawable.logo_mastercard2);
                        } else {
                            holder.img.setVisibility(View.GONE);
                        }
                    } else {
                        //execute kalau data kosong
                        //holder.img.setImageResource(R.drawable.ic_plus);
                        holder.img.setVisibility(View.GONE);
                        holder.imgPlus.setVisibility(View.VISIBLE);
                        holder.txtPaymentName.setText(context.getString(R.string.vor_payment_cc_new));
                        holder.txtPaymentName.setTextColor(context.getResources().getColor(R.color.blue_selector));
                        holder.txtPaymentName.setTypeface(holder.txtPaymentName.getTypeface(), Typeface.BOLD);
                    }
                }else{
                    if ((section2Start - 1) != 0) {
                        holder.linItem.setVisibility(View.GONE);
                    }else{
                        holder.img.setVisibility(View.GONE);
                        holder.imgPlus.setVisibility(View.VISIBLE);
                        holder.txtPaymentName.setText(context.getString(R.string.vor_payment_cc_new));
                        holder.txtPaymentName.setTextColor(context.getResources().getColor(R.color.blue_selector));
                        holder.txtPaymentName.setTypeface(holder.txtPaymentName.getTypeface(), Typeface.BOLD);
                    }

                }


            }else{
                holder.linSection.setVisibility(View.GONE);
                holder.txtHow.setVisibility(View.GONE);
                holder.linItem.setVisibility(View.GONE);
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
            if(paymentMethod.get("bca") != null
                    && paymentMethod.get("bca").status == true)
            {
                holder.img.setImageResource(R.drawable.logo_bca2);
                holder.txtPaymentName.setText(context.getString(R.string.va_bca));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, Utils.myPixel(a, 28), 0, 0);
                holder.linSection.setLayoutParams(layoutParams);
            }
            else
            {
                holder.linItem.setVisibility(View.GONE);
            }
        } else {
            holder.linSection.setVisibility(View.GONE);
            if (position == (section2Start + 1)) {
                if(paymentMethod .get("bp") != null &&
                        paymentMethod.get("bp").status == true)
                {
                    holder.img.setImageResource(R.drawable.logo_mandiri);
                    holder.txtPaymentName.setText(context.getString(R.string.va_mandiri));
                }
                else
                {
                    //holder.txtPaymentName.setText("geje skl");
                    holder.linItem.setVisibility(View.GONE);
                }
            } else if (position == (section2Start + 2)) {
                if(paymentMethod.get("va") != null &&
                        paymentMethod.get("va").status == true)
                {
                    holder.img.setVisibility(View.GONE);
                    holder.txtPaymentName.setText(context.getString(R.string.other_bank));
                }
                else
                {
                    holder.linItem.setVisibility(View.GONE);
                }

            } else if (position == (section2Start - 1)) {
                if ((section2Start - 1) != 0) {
                    //holder.img.setImageResource(R.drawable.ic_plus);
                    if(paymentMethod.get("cc") != null){
                        holder.img.setVisibility(View.GONE);
                        holder.imgPlus.setVisibility(View.VISIBLE);
                        holder.txtPaymentName.setText(context.getString(R.string.vor_payment_cc_new));
                        holder.txtPaymentName.setTextColor(context.getResources().getColor(R.color.blue_selector));
                        holder.txtPaymentName.setTypeface(holder.txtPaymentName.getTypeface(), Typeface.BOLD);
                    }else{
                        holder.linItem.setVisibility(View.GONE);
                    }

                }
            } else{
                if(paymentMethod.get("cc") != null){
                    String maskedCard = dataCredit.getCreditcardInformation().getMasked_card();
                    holder.txtPaymentName.setText("• • • • " + maskedCard.substring(maskedCard.indexOf("-") + 1, maskedCard.length()));

                    String headCC = maskedCard.substring(0, 1);
                    if (headCC.equals("4")) {
                        holder.img.setVisibility(View.VISIBLE);
                        holder.img.setImageResource(R.drawable.logo_visa2);
                    } else if (headCC.equals("5")) {
                        holder.img.setVisibility(View.VISIBLE);
                        holder.img.setImageResource(R.drawable.logo_mastercard2);
                    } else {
                        holder.img.setVisibility(View.GONE);
                    }
                }else{
                    holder.linItem.setVisibility(View.GONE);
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
        /*if(paymentMethod.size() == 0)
            return 0;
        else
            return (arrDataCredit.size() + 4);*/
        return (arrDataCredit.size() + 4);
        /*int count = 0;
        if(paymentMethod.get("cc").status == true)
            count+=1;
        if(paymentMethod.get("bca").status == true)
            count+=1;
        if(paymentMethod.get("bp").status == true)
            count+=1;
        if(paymentMethod.get("va").status == true)
            count+=1;
        return (arrDataCredit.size() + count);*/
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
        CardView linItem;
        @Bind(R.id.img_plus)
        ImageView imgPlus;

        private ViewSelectedListener clickListener;
        private LongClickListener longClickListener;
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

            //Utils.d("How it works", "click");
            Intent i = new Intent(a, HowToPayActivity.class);
            i.putExtra(Common.FIELD_WALKTHROUGH_PAYMENT, true);
            i.putExtra(Common.FIELD_ORDER_ID, order_id);
            i.putExtra(Common.FIELD_PAYMENT_TYPE, paymentType);
            i.putExtra(productSummary.getClass().getName(), productSummary);
            i.putExtra(eventDetail.getClass().getName(), eventDetail);
            i.putExtra(Common.FIELD_FROM_ORDER_LIST, false);
            a.startActivity(i);

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true);
        }
    }
}
