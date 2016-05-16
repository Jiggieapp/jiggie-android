package com.jiggie.android.api;

import com.jiggie.android.model.PostRedeemCodeModel;
import com.jiggie.android.model.Success2Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by LTE on 5/16/2016.
 */
public interface RedeemCodeInterface {
    @POST
    Call<Success2Model> postRedeemCode(@Url String url, @Body PostRedeemCodeModel postRedeemCodeModel);
}
