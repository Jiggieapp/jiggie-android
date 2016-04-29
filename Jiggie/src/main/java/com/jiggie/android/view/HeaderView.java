package com.jiggie.android.view;

/**
 * Created by Wandy on 4/8/2016.
 */
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by anton on 11/12/15.
 */

public class HeaderView extends LinearLayout {

    @Bind(R.id.event_name)
    TextView name;

    @Bind(R.id.lblEventLocation)
    TextView lblEventLocation;



    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(String eventName) {
        this.name.setText(eventName);
    }

    public void bindTo(String eventName, String location) {
        this.name.setText(eventName);
        this.lblEventLocation.setText(location);
        this.lblEventLocation.setSelected(true);
    }

    public void setTextSize(float size) {
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
}
