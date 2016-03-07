package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.CommerceInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.CCModel;
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
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by LTE on 2/22/2016.
 */
public class CommerceManager {

    private static CommerceInterface commerceInterface;

    public static void initCommerceService(){
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
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

    public static void loaderProductList(String event_id){
        try {
            getProductList(event_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    if (response.code() == Utils.CODE_SUCCESS) {
                        ProductListModel dataTemp = (ProductListModel) response.body();
                        //EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PRODUCT_LIST, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PRODUCT_LIST, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PRODUCT_LIST, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderSummary(PostSummaryModel postSummaryModel){
        try {
            postSummary(postSummaryModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    if (response.code() == Utils.CODE_SUCCESS) {
                        SummaryModel dataTemp = (SummaryModel) response.body();
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SUMMARY, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SUMMARY, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SUMMARY, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderPayment(PostPaymentModel postPaymentModel){
        try {
            postPayment(postPaymentModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", response.toString());

                    if (response.code() == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        //EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PAYMENT, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PAYMENT, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PAYMENT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderCCList(final LoadCCListener loadCCListener){

        getInstance().getCC("123456").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<CCModel>() {
            @Override
            public void onCompleted() {
                Log.d("yoo","complete");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("error",e.toString());
            }

            @Override
            public void onNext(CCModel ccModel) {
                loadCCListener.onLoadCC(ccModel);
            }
        });
        /*getInstance().getCC("123456").subscribe(new Action1<CCModel>() {
            @Override
            public void call(CCModel ccModel) {
                loadCCListener.onLoadCC(ccModel);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d("error",throwable.toString());
            }
        });*/
    }

    public interface LoadCCListener{
        void onLoadCC(CCModel ccModel);
    }

}