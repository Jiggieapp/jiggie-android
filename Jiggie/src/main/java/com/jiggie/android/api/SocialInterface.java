package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.PostLocationModel;
import com.jiggie.android.model.SocialModel;
import com.jiggie.android.model.Success2Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by LTE on 2/5/2016.
 */
public interface SocialInterface {

    @GET(Utils.URL_SOCIAL_FEED)
    Call<SocialModel> getSocialFeed(@Path("fb_id") String fb_id, @Path("gender_interest") String gender_interest);

    @GET(Utils.URL_SOCIAL_MATCH)
    Call<Success2Model> getSocialMatch(@Path("fb_id") String fb_id, @Path("from_id") String from_id, @Path("type") String type);

    @POST
    Call<Success2Model> postLocation(@Url String url, @Body PostLocationModel postLocationModel);

}
