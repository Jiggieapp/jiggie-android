package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.CommerceInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.CCScreenModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.PostCCModel;
import com.jiggie.android.model.PostDeleteCCModel;
import com.jiggie.android.model.PostFreePaymentModel;
import com.jiggie.android.model.PostPaymentModel;
import com.jiggie.android.model.PostSummaryModel;
import com.jiggie.android.model.ProductListModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SummaryModel;
import com.jiggie.android.model.SupportModel;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by LTE on 2/22/2016.
 */
public class CommerceManager extends BaseManager{

    private static CommerceInterface commerceInterface;
    public static ArrayList<CCScreenModel> arrCCScreen = new ArrayList<>();
    public static ArrayList<CCScreenModel> arrCCLocal = new ArrayList<>();
    public static SupportModel.Data.Support supportData = null;
    public static String lastPaymentType = Utils.BLANK;

    public static void initCommerceService(){
        commerceInterface = getRetrofit().create(CommerceInterface.class);
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

    private static void getCCList(String fb_id, Callback callback) throws IOException {
        getInstance().getCC(fb_id).enqueue(callback);
    }

    private static void getPaymentMethod(Callback callback)
    {
        getInstance().getPaymentMethod().enqueue(callback);
    }

    private static void getGuest(Callback callback)
    {
        getInstance().getGuest(AccountManager.loadLogin().getFb_id()).enqueue(callback);
    }

    private static void postCC(PostCCModel postCCModel, Callback callback) throws IOException {
        getInstance().postCC(Utils.URL_POST_CC, postCCModel).enqueue(callback);
    }

    private static void deleteCC(PostDeleteCCModel postDeleteCCModel, Callback callback) throws IOException {
        getInstance().deleteCC(Utils.URL_DELETE_CC, postDeleteCCModel).enqueue(callback);
    }

    private static void getSuccessScreenVABP(String order_id, Callback callback) throws IOException {
        getInstance().getSucScreenVABP(order_id).enqueue(callback);
    }

    private static void getSuccessScreenCC(String order_id, Callback callback) throws IOException {
        getInstance().getSucScreenCC(order_id).enqueue(callback);
    }

    private static void getSuccessScreenWalkthrough(Callback callback) throws IOException {
        getInstance().getSucScreenWalkthrough().enqueue(callback);
    }

    private static void getSupport(Callback callback) throws IOException {
        getInstance().getSupport().enqueue(callback);
    }

    private static void postFreePayment(PostFreePaymentModel postFreePaymentModel, Callback callback) throws IOException {
        getInstance().postFreePayment(Utils.URL_FREE_PAYMENT, postFreePaymentModel).enqueue(callback);
    }

    public static void loaderProductList(final String event_id, final OnResponseListener onResponseListener){
        try {
            getProductList(event_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

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
                    loaderProductList(event_id, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderSummary(final PostSummaryModel postSummaryModel, final OnResponseListener onResponseListener){
        try {
            postSummary(postSummaryModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);

                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {

                        try {
                            JSONObject jObj = new JSONObject(responses);
                            int resp = jObj.getInt("response");
                            if (resp != Utils.CODE_FAILED) {
                                onResponseListener.onSuccess(response.body());
                            } else {
                                String msg = jObj.getString("msg");
                                onResponseListener.onFailure(responseCode, msg);
                            }
                        } catch (Exception e) {
                            onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                        }

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
                    loaderSummary(postSummaryModel, onResponseListener);
                }

            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderPayment(final PostPaymentModel postPaymentModel, final OnResponseListener onResponseListener){
        try {
            postPayment(postPaymentModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", response.toString());

                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        if (dataTemp.getResponse() == 1) {
                            lastPaymentType = postPaymentModel.getType();
                            onResponseListener.onSuccess(dataTemp);

                        } else {
                            /*if(dataTemp.getType() != null && dataTemp.getType().equals("paid")){
                                onResponseListener.onFailure(dataTemp.getResponse(), dataTemp.getMsg());
                            }*/
                            onResponseListener.onFailure(dataTemp.getResponse(), dataTemp.getMsg());
                        }

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
                    loaderPayment(postPaymentModel, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderPaymentMethod(final OnResponseListener onResponseListener)
    {
        getPaymentMethod(new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                int responseCode = response.code();
                if (responseCode == Utils.CODE_SUCCESS) {
                    onResponseListener.onSuccess(response.body());
                } else {
                    onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                }
            }

            @Override
            public void onCustomCallbackFailure(String t) {
                onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
            }

            @Override
            public void onNeedToRestart() {
                loaderPaymentMethod(onResponseListener);
            }
        });
    }

    public static void loaderGuest(final OnResponseListener onResponseListener)
    {
        getGuest(new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                onResponseListener.onSuccess(response.body());
            }

            @Override
            public void onCustomCallbackFailure(String t) {
                onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION);
            }

            @Override
            public void onNeedToRestart() {
                loaderGuest(onResponseListener);
            }
        });
    }

    public static void loaderCCList(final String fb_id, final OnResponseListener onResponseListener){
        try {
            getCCList(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Utils.d("restoran", responses);

                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        onResponseListener.onSuccess(response.body());
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {
                    loaderCCList(fb_id, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderPostCC(final PostCCModel postCCModel, final OnResponseListener onResponseListener){

        try {
            postCC(postCCModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);

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
                    loaderPostCC(postCCModel, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderDeleteCC(final PostDeleteCCModel postDeleteCCModel, final OnResponseListener onResponseListener){
        try {
            deleteCC(postDeleteCCModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);

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
                    loaderDeleteCC(postDeleteCCModel, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderSucScreenVABP(final String order_id, final OnResponseListener onResponseListener){
        try {
            getSuccessScreenVABP(order_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);

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
                    loaderSucScreenVABP(order_id, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderSucScreenWalkthrough(final OnResponseListener onResponseListener){
        try {
            getSuccessScreenWalkthrough(new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String responses = new Gson().toJson(response.body());
                    //Utils.d("res", responses);

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
                    loaderSucScreenWalkthrough(onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderSucScreenCC(final String order_id, final OnResponseListener onResponseListener){
        try {
            getSuccessScreenCC(order_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String responses = new Gson().toJson(response.body());
                    //Utils.d("res", responses);

                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        onResponseListener.onSuccess(response.body());
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    //Utils.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {
                    loaderSucScreenCC(order_id, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderSupport(final OnResponseListener onResponseListener){
        try {
            getSupport(new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String responses = new Gson().toJson(response.body());
                    //Utils.d("res", responses);

                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        onResponseListener.onSuccess(response.body());
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    //Utils.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {
                    loaderSupport(onResponseListener);
                }
            });
        }catch (IOException e){
            //Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderFreePayment(final PostFreePaymentModel postFreePaymentModel, final OnResponseListener onResponseListener){
        try {
            postFreePayment(postFreePaymentModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    //String responses = new Gson().toJson(response.body());
                    //Utils.d("res", response.toString());

                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        if (dataTemp.getResponse() == 1) {
                            onResponseListener.onSuccess(dataTemp);

                        } else {
                            /*if(dataTemp.getType() != null && dataTemp.getType().equals("paid")){
                                onResponseListener.onFailure(dataTemp.getResponse(), dataTemp.getMsg());
                            }*/
                            onResponseListener.onFailure(dataTemp.getResponse(), dataTemp.getMsg());
                        }

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
                    loaderFreePayment(postFreePaymentModel, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
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

    /*public interface OnCustomResponseListener
    {
        public void onSuccess(Object object);
        public void onFailure(int responseCode, String message);
        public void onNeedToRestart();
    }*/

}