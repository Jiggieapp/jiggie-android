package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.CCModel;
import com.jiggie.android.model.PostPaymentModel;
import com.jiggie.android.model.PostSummaryModel;
import com.jiggie.android.model.ProductListModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SummaryModel;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Url;
import rx.Observable;

/**
 * Created by LTE on 2/22/2016.
 */
public interface CommerceInterface {

    @GET(Utils.URL_PRODUCT_LIST)
    Call<ProductListModel> getProductList(@Path("event_id") String event_id);

    @POST
    Call<SummaryModel> postSummary(@Url String url, @Body PostSummaryModel postSummaryModel);

    @POST
    Call<Success2Model> postPayment(@Url String url, @Body PostPaymentModel postPaymentModel);

    /*@GET(Utils.URL_GET_CC)
    Observable<CCModel> getCC(@Path("fb_id") String fb_id);*/

    @GET(Utils.URL_GET_CC)
    Call<CCModel> getCC(@Path("fb_id") String fb_id);

}
