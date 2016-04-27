package com.jiggie.android.listener;

/**
 * Created by Wandy on 4/27/2016.
 */
public interface OnResponseListener {
    public void onSuccess(Object object);
    public void onFailure(int responseCode, String message);
}
