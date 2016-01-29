package com.android.jiggie.component.volley;

import com.android.volley.VolleyError;

/**
 * Created by rangg on 19/01/2016.
 */
public class SimpleVolleyRequestListener<TResult, TResponse> implements VolleyRequestListener<TResult,TResponse> {
    @Override
    public TResult onResponseAsync(TResponse tResponse) { return null; }
    @Override
    public void onResponseCompleted(TResult value) { }
    @Override
    public void onErrorResponse(VolleyError error) { }
}
