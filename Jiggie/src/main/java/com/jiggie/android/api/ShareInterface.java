package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ShareLinkModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by LTE on 2/5/2016.
 */
public interface ShareInterface {

    @GET(Utils.URL_SHARE_LINK)
    Call<ShareLinkModel> getShareLink(@Query("from_fb_id") String from_fb_id, @Query("type") String type);

}
