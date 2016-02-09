package com.jiggie.android.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.api.AccountInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.AboutModel;
import com.jiggie.android.model.AccessTokenModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.FilterModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.SuccessModel;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by LTE on 2/1/2016.
 */
public class AccountManager {

    static AccountInterface accountInterface;
    public final static String TAG = AccountManager.class.getSimpleName();

    private static void initAccountService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        accountInterface = retrofit.create(AccountInterface.class);
    }

    public static AccountInterface getAccountInterface()
    {
        if(accountInterface == null)
            initAccountService();
        return accountInterface;
    }

    private static AccountInterface getInstance(){
        if(accountInterface == null)
            initAccountService();

        return accountInterface;
    }

    private static void postLogin(LoginModel loginRequestModel, Callback callback) throws IOException {
        getInstance().postLogin(Utils.URL_LOGIN, loginRequestModel).enqueue(callback);
    }

    private static void postMemberSetting(MemberSettingModel memberSettingModel, Callback callback) throws IOException {
        getInstance().postMemberSetting(Utils.URL_MEMBER_SETTING, memberSettingModel).enqueue(callback);
    }

    private static void getMemberInfo(String fb_id, Callback callback) throws IOException {
        getInstance().getMemberInfo(fb_id).enqueue(callback);
    }

    private static void getSetting(String fb_id, Callback callback) throws IOException {
        getInstance().getSetting(fb_id).enqueue(callback);
    }

    private static void postEditAbout(AboutModel aboutModel, Callback callback) throws IOException {
        getInstance().postEditAbout(Utils.URL_EDIT_ABOUT, aboutModel).enqueue(callback);
    }

    public static void loaderLogin(LoginModel loginRequestModel){
        try {
            postLogin(loginRequestModel, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    SettingModel dataTemp = (SettingModel) response.body();
                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SIGN_IN, Utils.MSG_EXCEPTION+t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SIGN_IN, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMemberSetting(final MemberSettingModel memberSettingModel){
        try {
            postMemberSetting(memberSettingModel, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    /* String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/
                    SuccessModel dataTemp = (SuccessModel) response.body();
                    EventBus.getDefault().post(dataTemp);

                    AccountManager.saveMemberSetting(memberSettingModel);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MEMBER_SETTING, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MEMBER_SETTING, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMemberInfo(String fb_id){
        try {
            getMemberInfo(fb_id, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    MemberInfoModel dataTemp = (MemberInfoModel) response.body();

                    EventBus.getDefault().post(dataTemp);

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_DETAIL, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_DETAIL, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderSetting(String fb_id){
        try {
            getSetting(fb_id, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    SettingModel dataTemp = (SettingModel) response.body();

                    EventBus.getDefault().post(dataTemp);

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderEditAbout(AboutModel aboutModel){
        try {
            postEditAbout(aboutModel, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    SuccessModel dataTemp = (SuccessModel) response.body();
                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_EDIT, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_EDIT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void saveSetting(SettingModel settingModel){

        String model = new Gson().toJson(settingModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE).edit()
                .putString(Utils.SETTING_MODEL, model).apply();
    }

    private static void saveMemberSetting(MemberSettingModel memberSettingModel) {
        String model = new Gson().toJson(memberSettingModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE).edit()
                .putString(Utils.MEMBER_SETTING_MODEL, model).apply();
    }

    private static void saveTagsList()
    {

    }

    public static SettingModel loadSetting(){
        SettingModel settingModel = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING,
                Context.MODE_PRIVATE).getString(Utils.SETTING_MODEL, ""), SettingModel.class);
        return settingModel;
    }

    public static void saveLogin(LoginModel loginModel){

        String model = new Gson().toJson(loginModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_LOGIN, Context.MODE_PRIVATE).edit()
                .putString(Utils.LOGIN_MODEL, model).apply();

    }

    public static LoginModel loadLogin(){

        LoginModel loginModel = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_LOGIN,
                Context.MODE_PRIVATE).getString(Utils.LOGIN_MODEL, ""), LoginModel.class);

        return loginModel;
    }

    public static MemberSettingModel loadMemberSetting()
    {
        MemberSettingModel memberSettingModel = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING,
                Context.MODE_PRIVATE).getString(Utils.MEMBER_SETTING_MODEL, ""), MemberSettingModel.class);
        return memberSettingModel;
    }

    private static void getUserTagList(Callback callback)
    {
        getAccountInterface().getUserTagList(AccessToken.getCurrentAccessToken()
                .getUserId()).enqueue(callback);
    }



    public static void getUserTagList()
    {
        getUserTagList(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                //String[] result = (String[]) response.body();
                //String r = new Gson().toJson(response.body());
                //JSONObject r = (JSONObject) response.body();
                FilterModel filterMode = (FilterModel) response.body();
                //for(String res : filterMode.getData().getExperiences())
                //Utils.d(TAG, "res filter model " + filterMode.getData().getExperiences());
                EventBus.getDefault().post(filterMode.getData().getExperiences());
            }

            @Override
            public void onFailure(Throwable t) {
                Utils.d(TAG, "response fail" + t.getMessage());
            }
        });

        /*VolleyHandler.getAccountInterface().createVolleyArrayRequest("user/tagslist", new VolleyRequestListener<String[], JSONArray>() {
            @Override
            public String[] onResponseAsync(JSONArray jsonArray) {
                final int length = jsonArray.length();
                final String[] values = new String[length];
                for (int i = 0; i < length; i++)
                    values[i] = jsonArray.optString(i);
                return values;
            }

            @Override
            public void onResponseCompleted(String[] values) {
                for(String val : values)
                {
                    Utils.d(TAG, "val " + val);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });*/
    }

    public static void getAccessToken()
    {
        getAccessToken(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String responses = new Gson().toJson(response.body());
                Utils.d(TAG, "response " + responses);

                AccessTokenModel accessTokenModel = (AccessTokenModel) response.body();
                Utils.d(TAG, accessTokenModel.getToken());
            }

            @Override
            public void onFailure(Throwable t) {
                Utils.d(TAG, "failure " + t.toString());
            }
        });
    }

    private static void getAccessToken(Callback callback)
    {
        final String fb_token = AccessToken.getCurrentAccessToken().getToken();
        Utils.d(TAG, "fb_token " + fb_token);
        getAccountInterface().getAccessToken(Utils.URL_GET_ACCESS_TOKEN,
                fb_token).enqueue(callback);
    }
}
