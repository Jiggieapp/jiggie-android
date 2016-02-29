package com.jiggie.android.api;

import com.jiggie.android.model.PostWalkthroughModel;
import com.jiggie.android.model.Success2Model;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Url;

/**
 * Created by LTE on 2/29/2016.
 */
public interface WalkthroughInterface {

    @POST
    Call<Success2Model> postWalkthrough(@Url String url, @Body PostWalkthroughModel postWalkthroughModel);

}
