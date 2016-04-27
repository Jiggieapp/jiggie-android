package com.jiggie.android.component.service;

import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.facebook.AccessToken;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.SocialManager;

/**
 * Created by Wandy on 4/27/2016.
 */
public class SocialFeedJob extends Job {
    private static final int PRIORITY = 1;
    private String userId, fromFbId;
    private boolean confirm;
    private static final String TAG = SocialFeedJob.class.getSimpleName();

    public SocialFeedJob(final String userId
            , final String fromFbId, final boolean confirm) {
        super(new Params(PRIORITY).requireNetwork().persist());
        this.userId = userId;
        this.fromFbId = fromFbId;
        this.confirm = confirm;
    }

    @Override
    public void onAdded() {
        Log.d(TAG, "on added");
    }

    @Override
    public void onRun() throws Throwable {
        /*SocialManager.loaderSocialMatch(userId
                , this.fromFbId
                , confirm ? "approved" : "denied");*/
        Log.d(TAG,
                userId
                + " " + this.fromFbId
                + " " + confirm + " oke");
    }

    @Override
    protected void onCancel(int cancelReason) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
