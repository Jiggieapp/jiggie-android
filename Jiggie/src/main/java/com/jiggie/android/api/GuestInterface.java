package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.GuestModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by LTE on 2/4/2016.
 */
public interface GuestInterface {

    @GET(Utils.URL_GUEST_INTEREST)
    Call<GuestModel> getGuestInterest(@Path("event_id") String event_id, @Path("fb_id") String fb_id, @Path("gender_interest") String gender_interest);

}
