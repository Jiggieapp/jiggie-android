package com.jiggie.android.api;

import com.jiggie.android.model.PostAppsFlyerModel;
import com.jiggie.android.model.PostMixpanelModel;
import com.jiggie.android.model.Success2Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by LTE on 2/25/2016.
 */
public interface TrackInterface {

    @POST
    Call<Success2Model> postAppsFlyer(@Url String url, @Body PostAppsFlyerModel loginModel);

    @POST
    Call<Success2Model> postMixpanel(@Url String url, @Body PostMixpanelModel loginModel);

}
