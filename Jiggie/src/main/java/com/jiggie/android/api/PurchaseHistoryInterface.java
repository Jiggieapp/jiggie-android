package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.PurchaseHistoryModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Wandy on 3/16/2016.
 */
public interface PurchaseHistoryInterface {

    @GET(Utils.URL_GET_ORDER_LIST)
    Call<PurchaseHistoryModel> getOrderList(@Path("fb_id") String fb_id);
}
