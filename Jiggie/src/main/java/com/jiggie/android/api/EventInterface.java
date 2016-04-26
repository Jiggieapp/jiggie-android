package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.TagsListModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by LTE on 2/1/2016.
 */
public interface EventInterface {

    @GET(Utils.URL_EVENTS)
    Call<EventModel> getEventList(@Path("fb_id") String fb_id);

    @GET(Utils.URL_EVENT_DETAIL)
    Call<EventDetailModel> getEventDetail(@Path("id") String _id, @Path("fb_id") String fb_id, @Path("gender_interest") String gender_interest);

    @GET(Utils.URL_TAGSLIST)
    Call<TagsListModel> getTagsList();

    @GET(Utils.URL_LIKE_EVENT)
    Call<Success2Model> actionLikeEvent(@Path("event_id") String event_id, @Path("fb_id") String fb_id, @Path("action") String action);
}