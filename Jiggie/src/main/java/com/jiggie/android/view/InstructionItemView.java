package com.jiggie.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/21/2016.
 */
public class InstructionItemView extends RelativeLayout {

    @Bind(R.id.txt_number)
    TextView txtNumber;
    @Bind(R.id.txt_msg)
    TextView txtMsg;
    private Context context;
    String text, number;

    public InstructionItemView(Context context, String number, String text) {
        super(context);
        this.context = context;
        this.number = number;
        this.text = text;

        initView();
    }

    public InstructionItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InstructionItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_instruction, this, true);
        ButterKnife.bind(this);

        txtNumber.setText(number);
        txtMsg.setText(text);
    }

    public void setTextSizes(int size){
        txtNumber.setTextSize(size);
        txtMsg.setTextSize(size);
    }

}
