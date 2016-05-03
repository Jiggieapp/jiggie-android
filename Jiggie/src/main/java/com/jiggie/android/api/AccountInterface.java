package com.jiggie.android.api;

import com.jiggie.android.component.Utils;
import com.jiggie.android.model.AboutModel;
import com.jiggie.android.model.AccessTokenModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.LoginResultModel;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.MemberSettingResultModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SuccessTokenModel;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by LTE on 2/1/2016.
 */
public interface AccountInterface{

    @POST
    Call<LoginResultModel> postLogin(@Url String url, @Body LoginModel loginModel);

    @POST
    Call<Success2Model> postMemberSetting(@Url String url, @Body MemberSettingModel memberSettingModel);

    @GET(Utils.URL_MEMBER_SETTING)
    Call<MemberSettingResultModel> getUserTagList(@Query("fb_id") String fb_id);
    
    @GET(Utils.URL_MEMBER_INFO)
    Call<MemberInfoModel> getMemberInfo(@Path("fb_id") String fb_id);

    @GET(Utils.URL_GET_SETTING)
    Call<MemberSettingResultModel> getSetting(@Query("fb_id") String fb_id);

    @POST
    Call<Success2Model> postEditAbout(@Url String url, @Body AboutModel aboutModel);

    @POST
    Call<SuccessTokenModel> getAccessToken(@Url String url, @Body AccessTokenModel accessTokenModel);

    @GET(Utils.URL_VERIFY_PHONE_NUMBER)
    Call<Success2Model> verifyPhoneNumber(@Path("fb_id") String fb_id, @Path("phone") String phone);

    @GET(Utils.URL_VERIFY_VERIFICATION_CODE)
    Call<Success2Model> verifyVerificationCode(@Path("fb_id") String fb_id, @Path("token") String token);

    @Multipart
    @POST(Utils.URL_UPLOAD)
    Call<Success2Model> upload(@Part("filefield") File file, @Part("fb_id") String fb_id);

    @Multipart
    @POST(Utils.URL_UPLOAD)
    Call<Success2Model> upload(@Part("filefield") RequestBody requestBody);

    @Multipart
    @POST(Utils.URL_UPLOAD)
    Call<Success2Model> upload3(@Part("filefield") RequestBody photo, @Part("fb_id") RequestBody fb_id);

    @Multipart
    @POST(Utils.URL_UPLOAD)
    Call<Success2Model> upload4(@Part("filefield") MultipartBody.Part photo, @Part("fb_id") RequestBody fb_id);


}
