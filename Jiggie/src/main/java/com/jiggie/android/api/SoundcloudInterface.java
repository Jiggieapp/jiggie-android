package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.SoundcloudModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Wandy on 7/19/2016.
 */
public interface SoundcloudInterface {
    @GET(Utils.URL_SOUNDCLOUD_TRACK_URL)
    Call<SoundcloudModel> getSoundcloudDetail(@Path("track_id") String trackId);
}
