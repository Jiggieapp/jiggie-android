package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ShareLinkModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by LTE on 2/5/2016.
 */
public interface ShareInterface {
    @GET(Utils.URL_SHARE_APPS)
    Call<ShareLinkModel> getShareApps(@Query("from_fb_id") String from_fb_id, @Query("type") String type);

    @GET(Utils.URL_SHARE_EVENT)
    Call<ShareLinkModel> getShareEvent(@Query("event_id") String event_id, @Query("from_fb_id") String from_fb_id, @Query("type") String type, @Query("venue_name") String venue_name);
}
