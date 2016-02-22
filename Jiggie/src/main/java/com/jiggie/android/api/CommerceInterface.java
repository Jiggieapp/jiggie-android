package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ProductListModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by LTE on 2/22/2016.
 */
public interface CommerceInterface {

    @GET(Utils.URL_PRODUCT_LIST)
    Call<ProductListModel> getProductList(@Path("event_id") String event_id);

}
