package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.CommerceInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.PostPaymentModel;
import com.jiggie.android.model.PostSummaryModel;
import com.jiggie.android.model.ProductListModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SummaryModel;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by LTE on 2/22/2016.
 */
public class CommerceManager {

    private static CommerceInterface commerceInterface;

    public static void initCommerceService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        commerceInterface = retrofit.create(CommerceInterface.class);
    }

    private static CommerceInterface getInstance(){
        if(commerceInterface == null)
            initCommerceService();

        return commerceInterface;
    }

    private static void getProductList(String event_id, Callback callback) throws IOException {
        getInstance().getProductList(event_id).enqueue(callback);
    }

    private static void postSummary(PostSummaryModel postSummaryModel, Callback callback) throws IOException {
        getInstance().postSummary(Utils.URL_SUMMARY, postSummaryModel).enqueue(callback);
    }

    private static void postPayment(PostPaymentModel postPaymentModel, Callback callback) throws IOException {
        getInstance().postPayment(Utils.URL_PAYMENT, postPaymentModel).enqueue(callback);
    }

    public static void loaderProductList(String event_id, final OnResponseListener onResponseListener){
        try {
            getProductList(event_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        ProductListModel dataTemp = (ProductListModel) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderSummary(PostSummaryModel postSummaryModel, final OnResponseListener onResponseListener){
        try {
            postSummary(postSummaryModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        SummaryModel dataTemp = (SummaryModel) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderPayment(PostPaymentModel postPaymentModel, final OnResponseListener onResponseListener){
        try {
            postPayment(postPaymentModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", response.toString());

                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    /**
     * Created by Wandy on 3/10/2016.
     */
    public interface OnResponseListener {
        public void onSuccess(Object object);
        public void onFailure(int responseCode, String message);
    }
}