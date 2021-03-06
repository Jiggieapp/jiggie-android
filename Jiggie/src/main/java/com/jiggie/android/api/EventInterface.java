package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.TagNewModel;
import com.jiggie.android.model.TagsListModel;
import com.jiggie.android.model.ThemePostModel;
import com.jiggie.android.model.ThemeResultModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @GET(Utils.URL_TAGSLIST_NEW)
    Call<TagNewModel> getTagsListNew();

    @GET(Utils.URL_LIKE_EVENT)
    Call<Success2Model> actionLikeEvent(@Path("event_id") String event_id, @Path("fb_id") String fb_id, @Path("action") String action);

    @POST(Utils.URL_GET_THEME)
    Call<ThemeResultModel> getTheme(@Body ThemePostModel themeResultModel);
}