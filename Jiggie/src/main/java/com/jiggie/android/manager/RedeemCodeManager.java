package com.jiggie.android.manager;

import com.jiggie.android.api.RedeemCodeInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.PostRedeemCodeModel;

import java.io.IOException;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LTE on 5/16/2016.
 */
public class RedeemCodeManager extends BaseManager{

    private static RedeemCodeInterface redeemCodeInterface;

    public static void initRedeemCodeService(){
        redeemCodeInterface = getRetrofit().create(RedeemCodeInterface.class);
    }

    private static RedeemCodeInterface getInstance(){
        if(redeemCodeInterface == null)
            initRedeemCodeService();

        return redeemCodeInterface;
    }

    private static void postRedeemCode(PostRedeemCodeModel postRedeemCodeModel, Callback callback) throws IOException {
        getInstance().postRedeemCode(Utils.URL_REDEEM_CODE, postRedeemCodeModel).enqueue(callback);
    }

    public static void loaderRedeemCode(final PostRedeemCodeModel postRedeemCodeModel, final OnResponseListener onResponseListener){
        try {
            postRedeemCode(postRedeemCodeModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        onResponseListener.onSuccess(response.body());
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {
                    loaderRedeemCode(postRedeemCodeModel, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public interface OnResponseListener {
        public void onSuccess(Object object);
        public void onFailure(int responseCode, String message);
    }

}
