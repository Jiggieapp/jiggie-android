package com.jiggie.android.component.callback;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.BaseManager;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.SuccessTokenModel;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Wandy on 2/12/2016.
 */
public abstract class CustomCallback implements Callback {
    //public Response response;
    private final String TAG = CustomCallback.class.getSimpleName();

    @Override
    public void onResponse(Call call, Response response) {
        //Utils.d(TAG, "responsecode " + response.code());
        //AccountManager.setAccessTokenToPreferences("");
        //response.code() = 401;
        final int responseCode = response.code();
        if(responseCode == 401) //error
        {
            /*if(AccountManager.getAccessTokenFromPreferences().isEmpty())
            {
                getToken();
                //dorestarthere
                //onNeedToRestart();
            }
            else */onCustomCallbackFailure("");
        }
        else if(response.code() == 410) //expired
        {
            getToken();
            //do restart here
            //Utils.d(TAG, "do restart");
            //onNeedToRestart();
            //onNeedToRestart();
        }
        else onCustomCallbackResponse(response);
        //this.response = response;
    }

    private void getToken()
    {
        AccountManager.getAccessToken(new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                //do restart here
                SuccessTokenModel successTokenModel = (SuccessTokenModel) object;
                final String token = successTokenModel.data.token;
                //Utils.d(TAG, "success token model " +  successTokenModel.data.token);
                AccountManager.setAccessTokenToPreferences(token);
                BaseManager.reinstantianteRetrofit();
                CommerceManager.initCommerceService();
                onNeedToRestart();
            }

            @Override
            public void onFailure(int responseCode, String message) {
                onCustomCallbackFailure(message);
            }
        });
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        String d = t.toString();
        Utils.d(TAG, "CustomCallback " + t.toString());
        if (t instanceof java.net.UnknownHostException) {
            onCustomCallbackFailure(App.getInstance().getResources().getString(R.string.no_internet_connection));
        } else if (t instanceof SocketTimeoutException) {
            onCustomCallbackFailure(App.getInstance().getResources().getString(R.string.socket_timeout_exception));
        } else {
            onCustomCallbackFailure(App.getInstance().getResources().getString(R.string.standard_error_message));
        }
    }

    public abstract void onCustomCallbackResponse(Response response);
    public abstract void onCustomCallbackFailure(String t);
    public abstract void onNeedToRestart();
}
