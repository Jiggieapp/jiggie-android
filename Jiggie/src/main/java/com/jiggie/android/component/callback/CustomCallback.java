package com.jiggie.android.component.callback;

import com.jiggie.android.App;
import com.jiggie.android.R;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Wandy on 2/12/2016.
 */
public abstract class CustomCallback implements Callback{

    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        onCustomCallbackResponse(response, retrofit);
    }

    @Override
    public void onFailure(Throwable t) {
        if(t instanceof java.net.UnknownHostException)
        {
            onCustomCallbackFailure(App.getInstance().getResources().getString(R.string.no_internet_connection));
        }
        else
        {
            onCustomCallbackFailure(App.getInstance().getResources().getString(R.string.standard_error_message));
        }
    }

    public abstract void onCustomCallbackResponse(Response response, Retrofit retrofit);
    public abstract void onCustomCallbackFailure(String t);

}
