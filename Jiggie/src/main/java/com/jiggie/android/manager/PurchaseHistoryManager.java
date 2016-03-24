package com.jiggie.android.manager;

import com.jiggie.android.api.OnResponseListener;
import com.jiggie.android.api.PurchaseHistoryInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.PurchaseHistoryModel;

import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Wandy on 3/16/2016.
 */
public class PurchaseHistoryManager extends BaseManager {
    private static final String TAG = PurchaseHistoryManager.class.getSimpleName();
    static PurchaseHistoryInterface purchaseHistoryInterface;

    private static PurchaseHistoryInterface getInstance() {
        if (purchaseHistoryInterface == null)
            purchaseHistoryInterface = getRetrofit().create(PurchaseHistoryInterface.class);
        return purchaseHistoryInterface;
    }

    private static void getOrderList(final String fb_id, CustomCallback callback)
    {
        getInstance().getOrderList(fb_id).enqueue(callback);
    }

    public static void getOrderList(final String fb_id, final OnResponseListener onResponseListener)
    {
        getOrderList(fb_id, new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                PurchaseHistoryModel purchaseHistoryModel = (PurchaseHistoryModel) response.body();
                onResponseListener.onSuccess(purchaseHistoryModel);
            }

            @Override
            public void onCustomCallbackFailure(String t) {
                Utils.d(TAG, "gagal " + t);
                onResponseListener.onFailure(new ExceptionModel(t));
            }
        });
    }
}
