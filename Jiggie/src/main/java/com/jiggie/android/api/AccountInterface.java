package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.AboutModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SuccessModel;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Url;

/**
 * Created by LTE on 2/1/2016.
 */
public interface AccountInterface {

    @POST
    Call<SettingModel> postLogin(@Url String url, @Body LoginModel loginModel);

    @POST
    Call<SuccessModel> postMemberSetting(@Url String url, @Body MemberSettingModel memberSettingModel);

    @GET(Utils.URL_MEMBER_INFO)
    Call<MemberInfoModel> getMemberInfo(@Path("fb_id") String fb_id);

    @GET(Utils.URL_GET_SETTING)
    Call<SettingModel> getSetting(@Query("fb_id") String fb_id);

    @POST
    Call<Success2Model> postEditAbout(@Url String url, @Body AboutModel aboutModel);

}
