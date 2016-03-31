package com.jiggie.android.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wandy on 2/24/2016.
 */
public class CustomToolbar extends Toolbar {
    @Bind(R.id.first_circle)
    View firstCircle;

    @Bind(R.id.second_circle)
    View secondCircle;

    @Bind(R.id.third_circle)
    View thirdCircle;

    @Bind(R.id.first_connector)
    View firstConnector;

    @Bind(R.id.second_connector)
    View secondConnector;

    @Bind(R.id.lblTitle)
    TextView lblTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;

    public CustomToolbar(Context context) {
        super(context);
    }

    private Context context;

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.toolbar_with_dots, this, true);
        ButterKnife.bind(this);

        //Added by Aga 8-3-2016
        final Context con = this.context;
        imgHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to support email
                /*final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", con.getString(R.string.support_email), null));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {con.getString(R.string.support_email)}); // hack for android 4.3
                intent.putExtra(Intent.EXTRA_SUBJECT, "Support");
                con.startActivity(Intent.createChooser(intent, con.getString(R.string.support)));*/

                Uri uri = Uri.parse("smsto:081218288317");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                //it.putExtra("sms_body", "The SMS text");
                con.startActivity(it);
            }
        });
        //--------
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitle(final String title) {
        lblTitle.setText(title);
    }

    public void setSelected(int index) {
        switch (index) {
            case 1:
                firstConnector.setBackground(
                        context.getResources().getDrawable(R.drawable.white_connector));
                secondConnector.setBackground(
                        context.getResources().getDrawable(R.drawable.gray_connector));

                secondCircle.setBackground(
                        context.getResources().getDrawable(R.drawable.white_dot));
                thirdCircle.setBackground(
                        context.getResources().getDrawable(R.drawable.grey_dot));
                break;
            case 2:
                final float[] from = new float[3],
                        to = new float[3];

                Color.colorToHSV(Color.parseColor("#FFFFFF"), from);   // from white
                Color.colorToHSV(Color.parseColor("#616161"), to);     // to red

                ValueAnimator anim = ValueAnimator.ofFloat(0, 1);   // animate from 0 to 1
                anim.setDuration(300);                              // for 300 ms

                final float[] hsv = new float[3];                  // transition color
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // Transition along each axis of HSV (hue, saturation, value)
                        hsv[0] = from[0] + (to[0] - from[0]) * animation.getAnimatedFraction();
                        hsv[1] = from[1] + (to[1] - from[1]) * animation.getAnimatedFraction();
                        hsv[2] = from[2] + (to[2] - from[2]) * animation.getAnimatedFraction();

                        //view.setBackgroundColor(Color.HSVToColor(hsv));
                        /*secondCircle.setBackgroundColor(Color.HSVToColor(hsv));
                        firstConnector.setBackgroundColor(Color.HSVToColor(hsv));*/
                        secondCircle.setBackground(
                                context.getResources().getDrawable(R.drawable.white_dot));
                        firstConnector.setBackground(
                                context.getResources().getDrawable(R.drawable.white_connector));
                        thirdCircle.setBackground(
                                context.getResources().getDrawable(R.drawable.white_dot));
                        secondConnector.setBackground(
                                context.getResources().getDrawable(R.drawable.white_connector));
                    }
                });

                anim.start();
                /*thirdCircle.setBackground(
                        context.getResources().getDrawable(R.drawable.grey_dot));
                secondConnector.setBackground(
                        context.getResources().getDrawable(R.drawable.gray_connector));*/
                break;
        }
    }

}
