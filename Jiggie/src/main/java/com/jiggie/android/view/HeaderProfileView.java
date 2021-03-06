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

public class HeaderProfileView extends HeaderView {

    @Bind(R.id.user_name)
    TextView name;

    public HeaderProfileView(Context context) {
        super(context);
    }

    public HeaderProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderProfileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void bindView() {
        ButterKnife.bind(HeaderProfileView.this);
    }

    public void bindTo(String userName) {
        this.name.setText(userName);
    }

    public void setTextSize(float size) {
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }
}
