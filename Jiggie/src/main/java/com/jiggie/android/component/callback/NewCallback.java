package com.jiggie.android.component.callback;

import com.jiggie.android.component.Utils;

import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Wandy on 3/8/2016.
 */
public class NewCallback extends CustomCallback{

    public static final String TAG = NewCallback.class.getSimpleName();

    @Override
    public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
        Utils.d(TAG, "oncustomcallbackresponse" + Utils.print(response));
    }

    @Override
    public void onCustomCallbackFailure(String t) {
        Utils.d(TAG, "onCustomCallbackFailure " + t);
    }
}
