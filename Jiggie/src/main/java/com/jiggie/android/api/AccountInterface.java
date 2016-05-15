package com.jiggie.android.api;

import com.jiggie.android.activity.profile.ProfileDetailModel;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.AboutModel;
import com.jiggie.android.model.AccessTokenModel;
import com.jiggie.android.model.CityModel;
import com.jiggie.android.model.FriendListModel;
import com.jiggie.android.model.InviteCodeResultModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.LoginResultModel;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.MemberSettingResultModel;
import com.jiggie.android.model.PostFriendModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SuccessTokenModel;
import com.jiggie.android.model.SuccessUploadModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
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
    Call<SuccessUploadModel> upload4(@Part MultipartBody.Part photo, @Part("fb_id") RequestBody fb_id);

    @GET(Utils.URL_CITY)
    Call<CityModel> getCityList();

    @POST(Utils.URL_DELETE_PHOTO)
    Call<Success2Model> deletePhoto(@Body ProfileDetailModel profileDetailModel);

    @POST(Utils.URL_POST_FRIEND_LIST)
    Call<Success2Model> postFriendList(@Path("fb_id") String fb_id, @Path("friends_fb_id") ArrayList<String> friendsFbId);

    @GET(Utils.URL_GET_INVITE_CODE)
    Call<InviteCodeResultModel> getInviteCode(@Path("fb_id") String fbId);
}
