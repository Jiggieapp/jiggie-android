package com.jiggie.android.manager;

import android.accounts.Account;
import android.os.AsyncTask;

import com.facebook.AccessToken;
import com.jiggie.android.api.AccountInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.AccessTokenModel;
import com.jiggie.android.model.SuccessTokenModel;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by Wandy on 2/10/2016.
 */
public abstract class BaseManager implements Interceptor {
    public static final String TAG = BaseManager.class.getSimpleName();
    private Callback callback;
    public CustomCallback customCallback;
    private static Retrofit retrofit;

    public static OkHttpClient getHttpClient() {
        if (AccountManager.getAccessTokenFromPreferences().isEmpty()) {
            final String fb_token = AccessToken.getCurrentAccessToken().getToken();
            final AccessTokenModel accessTokenModel = new AccessTokenModel();
            accessTokenModel.setToken(fb_token);
            String token = "kos";
            try {
                myHandler hand = new myHandler();
                token = hand.execute(accessTokenModel).get();
            } catch (InterruptedException e) {
                Utils.d(TAG, "interrupted " + e.toString());
                //e.printStackTrace();
            } catch (ExecutionException e) {
                Utils.d(TAG, "execution " + e.toString());
                //e.printStackTrace();
            }
        }

        //final String accessToken = AccountManager.getAccessTokenFromPreferences();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("authorization", AccountManager.getAccessTokenFromPreferences())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();
        return httpClient;
    }

    static class myHandler extends AsyncTask<AccessTokenModel, String, String> {
        @Override
        protected String doInBackground(AccessTokenModel... params) {
            try {
                retrofit2.Response<SuccessTokenModel> responsee = getBasicRetrofit()
                        .create(AccountInterface.class)
                        .getAccessToken(Utils.URL_GET_ACCESS_TOKEN, params[0]).execute();
                AccountManager.setAccessTokenToPreferences(responsee.body().data.token);
                //Utils.d(TAG, "token di invitecode " + responsee.body().data.token);
                return responsee.body().data.token;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    };

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
        if(AccountManager.getAccessTokenFromPreferences().isEmpty())
            retrofit = null;
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

    public static Retrofit getBasicRetrofit() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient)
                .build();
        return retrofit;
    }

    public static void reinstantianteRetrofit() {
        retrofit = null;
        //getRetrofit();
    }

    /*public static void isTokenAlready(final OnExistListener onExistListener) {
        if (AccountManager.getAccessTokenFromPreferences().isEmpty()) {
            AccountManager.getAccessToken(new CommerceManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    //do restart here
                    SuccessTokenModel successTokenModel = (SuccessTokenModel) object;
                    final String token = successTokenModel.data.token;
                    AccountManager.setAccessTokenToPreferences(token);
                    BaseManager.reinstantianteRetrofit();
                    CommerceManager.initCommerceService();
                    //loadData(eventId);
                    //onNeedToRestart();
                    onExistListener.onExist(true);
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    //onCustomCallbackFailure(message);
                    onExistListener.onExist(false);
                }
            });
        } else {
            onExistListener.onExist(true);
        }
    }

    public interface OnExistListener {
        public void onExist(boolean isExist);
    }*/
}
