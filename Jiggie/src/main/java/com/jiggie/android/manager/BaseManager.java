package com.jiggie.android.manager;

import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by Wandy on 2/10/2016.
 */
public abstract class BaseManager implements Interceptor{
    public static final String TAG = BaseManager.class.getSimpleName();
    private Callback callback;
    public CustomCallback customCallback;
    private static Retrofit retrofit;

    public static OkHttpClient getHttpClient() {
        final String accessToken = AccountManager.getAccessTokenFromPreferences();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("authorization", accessToken)
                                .build();
                        return chain.proceed(request);
                    }
        }).build();
        /*httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("authorization", accessToken)
                        .build();
                return chain.proceed(request);
            }
        });*/
        return httpClient;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        return response;
    }

    /*public static Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = getHttpClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                            //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(okHttpClient)
                    .build();
        }
        return retrofit;
    }*/

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = getHttpClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                    //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static void reinstantianteRetrofit()
    {
        retrofit = null;
        //getRetrofit();
    }
}
