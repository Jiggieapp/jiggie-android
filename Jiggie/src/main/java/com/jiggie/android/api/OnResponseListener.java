package com.jiggie.android.api;

import com.jiggie.android.model.ExceptionModel;

/**
 * Created by Wandy on 4/27/2016.
 */
public interface OnResponseListener {
    public void onSuccess(Object object);
    public void onFailure(ExceptionModel exceptionModel);
}
