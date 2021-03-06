package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.CCModel;
import com.jiggie.android.model.GuestInfo;
import com.jiggie.android.model.PaymentMethod;
import com.jiggie.android.model.PostCCModel;
import com.jiggie.android.model.PostDeleteCCModel;
import com.jiggie.android.model.PostFreePaymentModel;
import com.jiggie.android.model.PostPaymentModel;
import com.jiggie.android.model.PostSummaryModel;
import com.jiggie.android.model.ProductListModel;
import com.jiggie.android.model.PurchaseHistoryModel;
import com.jiggie.android.model.SucScreenCCModel;
import com.jiggie.android.model.SucScreenVABPModel;
import com.jiggie.android.model.SucScreenWalkthroughModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SummaryModel;
import com.jiggie.android.model.SupportModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

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

    @GET(Utils.URL_GET_CC)
    Call<CCModel> getCC(@Path("fb_id") String fb_id);

    @GET(Utils.URL_PAYMENT_METHOD)
    Call<PaymentMethod> getPaymentMethod();

    @GET(Utils.URL_GUEST_INFO)
    Call<GuestInfo> getGuest(@Path("fb_id") String fb_id);

    @POST
    Call<Success2Model> postCC(@Url String url, @Body PostCCModel postPaymentModel);

    @POST
    Call<Success2Model> deleteCC(@Url String url, @Body PostDeleteCCModel postDeleteCCModel);

    @GET(Utils.URL_SUCCESS_SCREEN_VABP)
    Call<SucScreenVABPModel> getSucScreenVABP(@Path("order_id") String order_id);

    @GET(Utils.URL_SUCCESS_SCREEN_VABP)
    Call<SucScreenCCModel> getSucScreenCC(@Path("order_id") String order_id);

    @GET(Utils.URL_SUCCESS_SCREEN_WALKTHROUGH)
    Call<SucScreenWalkthroughModel> getSucScreenWalkthrough();

    @GET(Utils.URL_SUPPORT)
    Call<SupportModel> getSupport();

    @POST
    Call<Success2Model> postFreePayment(@Url String url, @Body PostFreePaymentModel postFreePaymentModel);

    @GET(Utils.URL_GET_ORDER_LIST)
    Call<PurchaseHistoryModel> getOrderList(@Path("fb_id") String fb_id);

}
