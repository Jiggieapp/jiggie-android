package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.GuestModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SuccessModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by LTE on 2/4/2016.
 */
public interface GuestInterface {

    @GET(Utils.URL_GUEST_INTEREST)
    Call<GuestModel> getGuestInterest(@Path("event_id") String event_id, @Path("fb_id") String fb_id, @Path("gender_interest") String gender_interest);

    @GET(Utils.URL_GUEST_MATCH)
    Call<Success2Model> getGuestConnect(@Path("fb_id") String fb_id, @Path("from_id") String from_id, @Path("type") String type);

}
