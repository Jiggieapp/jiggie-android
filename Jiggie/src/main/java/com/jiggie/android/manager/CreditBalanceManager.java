package com.jiggie.android.manager;

import com.jiggie.android.api.CreditInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;

import java.io.IOException;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LTE on 5/19/2016.
 */
public class CreditBalanceManager extends BaseManager {

    private static CreditInterface creditInterface;

    public static void initCreditBallanceService(){
        creditInterface = getRetrofit().create(CreditInterface.class);
    }

    private static CreditInterface getInstance(){
        if(creditInterface == null)
            initCreditBallanceService();

        return creditInterface;
    }

    private static void getCreditBalance(String fb_id, Callback callback) throws IOException {
        getInstance().getCreditBalance(fb_id).enqueue(callback);
    }

    public static void loaderCreditBalance(final String fb_id, final OnResponseListener onResponseListener) {
        try {
            getCreditBalance(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    if(response!=null){
                        int responseCode = response.code();
                        if (responseCode == Utils.CODE_SUCCESS) {
                            onResponseListener.onSuccess(response.body());
                        } else {
                            onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                        }
                    }else{
                        onResponseListener.onFailure(Utils.CODE_FAILED, Utils.RESPONSE_FAILED);
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {
                    loaderCreditBalance(fb_id, onResponseListener);
                }
            });
        } catch (IOException e) {
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public interface OnResponseListener {
        public void onSuccess(Object object);
        public void onFailure(int responseCode, String message);
    }

}
