package com.jiggie.android.api;

import com.facebook.AccessToken;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.FilterModel;

import org.json.JSONObject;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by wandywijayanto on 2/1/16.
 */
public interface FilterInterface {
    @GET(Utils.GET_USER_TAG_LIST)
   Call<FilterModel> getUserTagList(@Path("user_id") String fb_id);
    //Call<JSONObject> getUserTagList(@Path("user_id") String user_id);
}
