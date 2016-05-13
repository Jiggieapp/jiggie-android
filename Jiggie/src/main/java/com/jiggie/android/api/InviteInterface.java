package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.InviteCodeModel;
import com.jiggie.android.model.PostContactModel;
import com.jiggie.android.model.PostInviteAllModel;
import com.jiggie.android.model.PostInviteModel;
import com.jiggie.android.model.ResponseContactModel;
import com.jiggie.android.model.Success2Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by LTE on 5/12/2016.
 */
public interface InviteInterface {

    //@Headers("Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmYl90b2tlbiI6IkNBQUw4ZGs2blpDQ3NCQU82WkFpd21uaEhBSnNaQmNScXJvcHFOdG5FZDFjd1hZb0wwdHlwZVpDbDBGZkVkWkJ3WkJaQmxEUGtmWkIyNEUzWEJCZk9JczFGNVpDVzZYd2M2SEYyc3F2cGpYV25aQko2OVl3blFTb3o4TXBDNncxYWl2NlVpWUhIWTYxWkFLZmRaQ252QU00T01sS0RscDNiODhkWkJNMGJwTmt2UGh0OUhsV1dVOEJ1dHpJSkdIZW1mTXpvRVBNU25VZjhtdjhUOXRtajR6YUpKU0Z3Um4wNHhLMFlMYlBrTTNIUmJtczVZRXdaRFpEIiwiaWF0IjoxNDYzMTEwMjg3LCJleHAiOjE0NjMxMTc0ODd9.eZ38oywHSjD9mf3je_Co-NwQqisCkGLiJCZSOqIMxD4")
    @POST
    Call<ResponseContactModel> postContact(@Url String url, @Body PostContactModel postSummaryModel);

    @POST
    Call<Success2Model> postInvite(@Url String url, @Body PostInviteModel postInviteModel);

    @POST
    Call<Success2Model> postInviteAll(@Url String url, @Body PostInviteAllModel postInviteModel);

    @GET(Utils.URL_INVITE_CODE)
    Call<InviteCodeModel> getInviteCode(@Path("fb_id") String fb_id);

}
