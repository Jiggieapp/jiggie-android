package com.jiggie.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/17/2016.
 */
public class StepView extends RelativeLayout {

    @Bind(R.id.txt_no)
    TextView txtNo;
    @Bind(R.id.txt_step)
    TextView txtStep;
    private Context context;
    String text, number;
    boolean showDivider;

    public StepView(Context context, String number, String text, boolean showDivider) {
        super(context);
        this.context = context;
        this.number = number;
        this.text = text;
        this.showDivider = showDivider;

        initView();
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_step, this, true);
        ButterKnife.bind(this);

        txtNo.setText(number);
        txtStep.setText(text);
        /*if(showDivider){
            divider.setVisibility(VISIBLE);
        }else{
            divider.setVisibility(GONE);
        }*/
    }

}
