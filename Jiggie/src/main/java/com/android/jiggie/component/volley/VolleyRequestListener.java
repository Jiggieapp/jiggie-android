package com.android.jiggie.component.volley;

import com.android.volley.Response;

/**
 * Created by rangg on 05/11/2015.
 */
public interface VolleyRequestListener<TResult, TResponse> extends Response.ErrorListener {
    TResult onResponseAsync(TResponse response);
    void onResponseCompleted(TResult value);
}
