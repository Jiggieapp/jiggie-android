package com.jiggie.android.listener;

/**
 * Created by Wandy on 4/27/2016.
 */
public interface OnResponseListener {
    void onSuccess(Object object);
    void onFailure(int responseCode, String message);
}
