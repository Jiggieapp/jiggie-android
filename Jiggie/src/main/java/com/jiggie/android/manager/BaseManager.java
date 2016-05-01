package com.jiggie.android.manager;

import android.content.Context;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

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
        final String accessToken = AccountManager.getAccessTokenFromPreferences();
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
        //Utils.d(TAG, "reinstantiate");
        retrofit = null;
        //getRetrofit();
    }
}
