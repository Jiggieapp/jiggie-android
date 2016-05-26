package com.jiggie.android.api;

import com.jiggie.android.model.PostWalkthroughModel;
import com.jiggie.android.model.Success2Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by LTE on 2/29/2016.
 */
public interface WalkthroughInterface {

    @POST
    Call<Success2Model> postWalkthrough(@Url String url, @Body PostWalkthroughModel postWalkthroughModel);

}
