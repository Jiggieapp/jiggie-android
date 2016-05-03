package com.jiggie.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 5/2/2016.
 */
public class CircleIndicatorView extends RelativeLayout {

    @Bind(R.id.img_indicator)
    ImageView imgIndicator;
    private Context context;
    private boolean isSelecteds;

    public CircleIndicatorView(Context context, boolean isSelecteds) {
        super(context);
        this.context = context;
        this.isSelecteds = isSelecteds;

        initView();
    }

    public CircleIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_circle_ind, this, true);
        ButterKnife.bind(this);

        if(isSelecteds){
            imgIndicator.setSelected(true);
        }else{
            imgIndicator.setSelected(false);
        }
    }

    public void setSelectedIndicator(boolean isSelecteds) {
        if(isSelecteds){
            imgIndicator.setSelected(true);
        }else{
            imgIndicator.setSelected(false);
        }
    }

    public ImageView getImgIndicator(){
        return imgIndicator;
    }

}
