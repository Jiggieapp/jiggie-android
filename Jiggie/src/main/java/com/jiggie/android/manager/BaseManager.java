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
        final String accessToken = App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .getString(Utils.ACCESS_TOKEN, "");
        //OkHttpClient httpClient = new OkHttpClient();
        OkHttpClient httpClient = getOkHttpClientWithSSL();
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

    private static OkHttpClient getOkHttpClientWithSSL() {
        OkHttpClient client = new OkHttpClient();
        try {
            KeyStore keyStore = readKeyStore(); //your method to obtain KeyStore
            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            client.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return client;
    }


    private static KeyStore readKeyStore() throws IOException {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            // get user password and file input stream
            final String rsa =
                    "MIIEpAIBAAKCAQEAwq4gSNfWRKksORzUvkG3hCQ3W44SDMvNOpT6e59JbqOra1Av\n" +
                    "MgCOJD3U1XHZhtxz/xx3dD09G71tA3Za8LXjR74LaM6DRgLzfABXxn4nWwxWlmpy\n" +
                    "146J+4YhNvPq5HXFX3df3a2FCIkwLk9xpk9pJdsuEy9mnv8/HkyKjLU8qRdVr0YE\n" +
                    "rbKUN7ZHnt8KU041SV5+aJOZcDjeCBp7FrQNR3PzASKlYeM7SO+dPJJst7VAqw7K\n" +
                    "UjYqFalOa8VHu23nFPfp9NDqr9/P0tlIS3KW3d+apDQc/2FIypyOCkBuBydcwWVv\n" +
                    "knBbLhDMTS8mqyuKBVakadgKBMzRiLxw3IYCZQIDAQABAoIBAQCVlmA+OIJujaLq\n" +
                    "W4noiuxs/7r9gYPDC1Itok15ceJlviM4OQuamyydElmYjHt9kaUbsA7lZSywRPQf\n" +
                    "xfCIpun1tURlCtF5mUjnzgekAElTP37E4xRNJzxE5UJeTDv/wbKf8UTbO7pN3cQE\n" +
                    "wYxtIvWDXg/gM/rhTvV73YpUJepvbaJpKq6/yRrlolSROVjtlh5esFBr+LzBqS36\n" +
                    "SywFFNgoNe+1xsa3l+Rz+Y1cZ4Jv1zmRcG30B+b7Fg06Uf8szIYHlJieUXomf1Bt\n" +
                    "hgmL5FPHphzy3LLLMC5b7PtdsxOFRnBzQsjx+te+EYkrSWuSXiGCPKXn5PyKKGaa\n" +
                    "BKuXnpeBAoGBAPnnZ9CImRVfpU2RFeZeZLyisgh9B2LvRJDJAwKmNb62SZCBYbpz\n" +
                    "D3SgZr7YOBILsNB/PNmm0u3kMbQqyvsbUfUPhXZ70l69FBbigLuRt14LAxP+T97d\n" +
                    "mX+mqbvoeafeoMNSQBpUS/g5oJuHiiffuQwJqC2iq+ij+m4cYscZhp5hAoGBAMdt\n" +
                    "3E2FN8fc0DbnJ7OeC+nhoDqKf7/Br3cVOgSI6T3KYdVGdn6wWG9bSaqaw7Rd9Ccq\n" +
                    "c9vvkN8Yb1RsFNA6b8gXIpr9GqBs+u9lc6KkR1xo4xnq/GhEKV/wEAE/0vAwRgcO\n" +
                    "OU5IyydanTnP0GKqRcLhdDGgcf5V5GVkwkcMy/qFAoGAIEhGXufmbp5maji9zX6Q\n" +
                    "INYbjYEpPRyPjOyKmQMDmllN837jMwS+EGDQJQMlax9bffOR1sP4J4b87LtCcWG8\n" +
                    "Gp4grYedS2O9fp9P5esS9vEROrn1c2LI+3Z6Iq4LWSYnOlQbt8r/7Vb0amL989yw\n" +
                    "uN9efFaxQmI8bZD5KaMggsECgYEAh/kz1/cYzUCVBpocy67a2KBpqwB7kbbxd/QV\n" +
                    "FrpyrRqsEcp1SHPcGDDZzWSwQu48ZefgDvkMgdAbOChpGIB/bqG3io0/9lnXzhoR\n" +
                    "+bagoTUygp1hc4Xl3+/COdfWbsW7OVxNXj7rpO13fc9feaY24mTt4FhQ4OSXUTqd\n" +
                    "6+uhuDECgYA4G7iR7bCKj9B2V3uu9rDKEAfAYiujL5N83x6WYI7PdAT8kFiBabsm\n" +
                    "27as8hq9sziV00VTim7dkqjJy/SsTjjKYP90tFyWWUzfNXqErMWWvyg7TrYM38NS\n" +
                    "jDjpc3mCKYwKg0RpFLgA/VvlcJeWwq9c1tlkaE6eH3ur3Sdvg0PHtQ==";
            //char[] password = getPassword();
            char[] password = rsa.toCharArray();

            java.io.InputStream fis = null;
            try {
                //fis = new java.io.FileInputStream("keyStoreName");
                fis = App.getInstance().getApplicationContext()
                        .getResources().openRawResource(R.raw.server);
                ks.load(fis, password);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return ks;
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
