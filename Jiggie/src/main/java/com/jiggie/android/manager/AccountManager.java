package com.jiggie.android.manager;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.activity.setup.SetupNotificationActivity;
import com.jiggie.android.activity.setup.SetupTagsActivity;
import com.jiggie.android.api.AccountInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.LoginModel;
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

    public static void initAccountService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        accountInterface = retrofit.create(AccountInterface.class);
    }

    private static void postLogin(LoginModel loginRequestModel, Callback callback) throws IOException {
        accountInterface.postLogin(Utils.URL_LOGIN, loginRequestModel).enqueue(callback);
    }

    private static void postMemberSetting(MemberSettingModel memberSettingModel, Callback callback) throws IOException {
        accountInterface.postMemberSetting(Utils.URL_MEMBER_SETTING, memberSettingModel).enqueue(callback);
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
                    EventBus.getDefault().post(new ExceptionModel(Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMemberSetting(final MemberSettingModel memberSettingModel){
        try {
            postMemberSetting(memberSettingModel, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                   /* String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/
                    Utils.d("AccountManager", memberSettingModel.getExperiences());
                    SuccessModel dataTemp = (SuccessModel) response.body();
                    EventBus.getDefault().post(dataTemp);

                    AccountManager.saveMemberSetting(memberSettingModel);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.MSG_EXCEPTION + e.toString()));
        }
    }



    public static void saveSetting(SettingModel settingModel){
        /*App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE).edit()
                .putString(Common.FIELD_GENDER, settingModel.getData().getGender())
                .putString(Common.FIELD_GENDER_INTEREST, settingModel.getData().getGender_interest())
                .putString(Common.FIELD_PHONE, settingModel.getData().getPhone())
                .putString(Common.FIELD_ACCOUNT_TYPE, settingModel.getData().getAccount_type())
                .putBoolean(Common.FIELD_MATCH_ME, settingModel.isMatch_me())
                .putBoolean(Common.FIELD_FEED, settingModel.getData().getNotifications().isFeed())
                .putBoolean(Common.FIELD_CHAT, settingModel.getData().getNotifications().isChat())
                .apply();*/

        String model = new Gson().toJson(settingModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE).edit()
                .putString(Utils.SETTING_MODEL, model).apply();
    }

    private static void saveMemberSetting(MemberSettingModel memberSettingModel) {
        String model = new Gson().toJson(memberSettingModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE).edit()
                .putString(Utils.MEMBER_SETTING_MODEL, model).apply();
    }

    public static SettingModel loadSetting(){
        SettingModel settingModel = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING,
                Context.MODE_PRIVATE).getString(Utils.SETTING_MODEL, ""), SettingModel.class);
        return settingModel;
    }

    public static MemberSettingModel loadMemberSetting()
    {
        MemberSettingModel memberSettingModel = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING,
                Context.MODE_PRIVATE).getString(Utils.MEMBER_SETTING_MODEL, ""), MemberSettingModel.class);
        return memberSettingModel;
    }




}
