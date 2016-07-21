package com.jiggie.android.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by Wandy on 7/21/2016.
 */
public class CirclePlay extends ImageView implements Animatable {
    private final ValueAnimator mStartAnimator;
    private ValueAnimator mEndAnimator;
    private Callbacks mCallbacks;

    public CirclePlay(Context context) {
        this(context, null, 0);
    }

    public CirclePlay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePlay(Context context, AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mStartAnimator = ObjectAnimator.ofFloat(this, View.ROTATION, 0, 360);
        mStartAnimator.setInterpolator(new LinearInterpolator());
        mStartAnimator.setRepeatCount(Animation.INFINITE);
        mStartAnimator.setDuration(1000);
        mStartAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                float current = getRotation();
                //float target = current > HALF_ANGLE ? FULL_ANGLE : 0; // Choose the shortest distance to 0 rotation
                //float diff = target > 0 ? FULL_ANGLE - current : current;
                mEndAnimator = ObjectAnimator.ofFloat(CirclePlay.this, View.ROTATION, current, 360);
                mEndAnimator.setInterpolator(new LinearInterpolator());
                mEndAnimator.setDuration((int) (500));
                mEndAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setRotation(0);
                        /*if (mCallbacks != null) {
                            mCallbacks.onStopAnimation();
                        }*/
                    }
                });
                mEndAnimator.start();
            }
        });
    }

    @Override
    public void start() {
        mStartAnimator.start();
    }

    @Override
    public void stop() {
        if (isRunning()) {
            mStartAnimator.cancel();
        } else {
            if (mCallbacks != null) {
                mCallbacks.onStopAnimation();
            }
        }
    }

    @Override
    public boolean isRunning() {
        return mStartAnimator.isRunning()
                || (mEndAnimator != null && mEndAnimator.isRunning());
    }

    public interface Callbacks {
        void onStopAnimation();
    }

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }
}
