package com.jiggie.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/14/2016.
 */
public class TermsItemView extends RelativeLayout {

    @Bind(R.id.img_check)
    ImageView imgCheck;
    @Bind(R.id.txt_term)
    TextView txtTerm;
    private Context context;
    String text;

    public TermsItemView(Context context, String text) {
        super(context);
        this.context = context;
        this.text = text;
        initView();
    }

    public TermsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TermsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_terms, this, true);
        ButterKnife.bind(this);

        txtTerm.setText(text);
        imgCheck.setSelected(true);
        imgCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgCheck.isSelected()) {
                    imgCheck.setSelected(false);
                } else {
                    imgCheck.setSelected(true);
                }
            }
        });
    }

    public ImageView getImgCheck() {
        return imgCheck;
    }

}
