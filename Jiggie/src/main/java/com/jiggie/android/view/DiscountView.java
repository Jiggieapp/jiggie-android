package com.jiggie.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 5/18/2016.
 */
public class DiscountView extends RelativeLayout {

    @Bind(R.id.txt_discount_title)
    TextView txtDiscountTitle;
    @Bind(R.id.txt_discount_fill)
    TextView txtDiscountFill;
    private Context context;
    String title, value;
    boolean isUpperCase;
    int titleColor, valueColor;
    float textSize;

    public DiscountView(Context context, String title, String value, boolean isUpperCase, int titleColor, int valueColor, float textSize) {
        super(context);
        this.context = context;
        this.title = title;
        this.value = value;
        this.isUpperCase = isUpperCase;
        this.titleColor = titleColor;
        this.valueColor = valueColor;
        this.textSize = textSize;
        initView();
    }

    public DiscountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DiscountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_discount, this, true);
        ButterKnife.bind(this);

        if(isUpperCase){
            txtDiscountTitle.setText(title.toUpperCase());
        }else{
            txtDiscountTitle.setText(title);
        }

        txtDiscountFill.setText("- " + StringUtility.getRupiahFormat(value));

        if(titleColor!=0){
            txtDiscountTitle.setTextColor(titleColor);
        }

        txtDiscountFill.setTextColor(valueColor);

        txtDiscountTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        txtDiscountFill.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }



}
