package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ProductModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Wandy on 2/10/2016.
 */
public interface ProductInterface {
    /*GET /app/v3/userlogin => for get Token

    Header Authorization {{token}}
    GET /app/v3/product/list/56b1a0bf89bfed03005c50f0 => for get Product List*/

    @GET(Utils.URL_GET_PRODUCT_LIST)
    Call<ProductModel> getProductList(@Path("event_id") String eventId);
}
