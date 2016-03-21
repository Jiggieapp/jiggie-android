package com.jiggie.android.component.callback;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.ChatManager;
import com.jiggie.android.model.ExceptionModel;

import java.net.SocketTimeoutException;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Wandy on 2/12/2016.
 */
public abstract class CustomCallback implements Callback{
    //public Response response;
    private final String TAG = CustomCallback.class.getSimpleName();

    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        onCustomCallbackResponse(response, retrofit);
        //this.response = response;
    }

    @Override
    public void onFailure(Throwable t) {
        ExceptionModel exceptionModel;
        String d = t.toString();
        if(t instanceof java.net.UnknownHostException)
        {
            onCustomCallbackFailure(App.getInstance().getResources().getString(R.string.no_internet_connection));
        }
        else if(t instanceof SocketTimeoutException)
        {
            onCustomCallbackFailure(App.getInstance().getResources().getString(R.string.socket_timeout_exception));
        }
        else
        {
            onCustomCallbackFailure(App.getInstance().getResources().getString(R.string.standard_error_message));
        }
    }

    public abstract void onCustomCallbackResponse(Response response, Retrofit retrofit);
    public abstract void onCustomCallbackFailure(String t);
}
