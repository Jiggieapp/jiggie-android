package com.jiggie.android.manager;

import android.content.Context;

import com.jiggie.android.App;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
//import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Wandy on 2/10/2016.
 */
public abstract class BaseManager {
    public static final String TAG = BaseManager.class.getSimpleName();
    private Callback callback;
    public CustomCallback customCallback;
    private static Retrofit retrofit;

    public static OkHttpClient getHttpClient() {
        final String accessToken = App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .getString(Utils.ACCESS_TOKEN, "");
        // Utils.d(TAG, "accesstoken " + accessToken);
        /*if(accessToken.equals("")
                && !AccessToken.getCurrentAccessToken().getToken().equals("")
                && AccessToken.getCurrentAccessToken().getToken() != null)
        {

        }
        else
        {*/
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("authorization", accessToken)
                        .build();
                return chain.proceed(request);
            }
        });
        return httpClient;
        //}
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            //OkHttpClient okHttpClient = getHttpClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                    //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
