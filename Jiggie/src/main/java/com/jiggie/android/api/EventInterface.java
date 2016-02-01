package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.EventModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by LTE on 2/1/2016.
 */
public interface EventInterface {

    //@GET("http://52.76.76.3/app/v3/events/list/{fb_id}/{gender_interest}")
    @GET(Utils.GET_EVENTS)
    Call<EventModel> getEventList(@Path("fb_id") String fb_id, @Path("gender_interest") String gender_interest);

    /*@GET("http://52.76.76.3/app/v3/events/details/{_id}/{fb_id}/{gender_interest}")
    Call<EventDetailModel> getEventDetail(@Path("_id") String _id, @Path("fb_id") String fb_id, @Path("gender_interest") String gender_interest);*/

}
