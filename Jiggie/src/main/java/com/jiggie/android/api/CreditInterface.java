package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.SuccessCreditBalanceModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by LTE on 5/19/2016.
 */
public interface CreditInterface {
    @GET(Utils.URL_CREDIT_BALANCE)
    Call<SuccessCreditBalanceModel> getCreditBalance(@Path("fb_id") String fb_id);
}
