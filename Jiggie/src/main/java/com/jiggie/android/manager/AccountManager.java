package com.jiggie.android.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.api.AccountInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.AboutModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
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

    public static void initAccountService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        accountInterface = retrofit.create(AccountInterface.class);

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
            postLogin(loginRequestModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);

                    SettingModel dataTemp = (SettingModel)response.body();
                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SIGN_IN, Utils.MSG_EXCEPTION+t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SIGN_IN, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMemberSetting(MemberSettingModel memberSettingModel){
        try {
            postMemberSetting(memberSettingModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);

                    SuccessModel dataTemp = (SuccessModel) response.body();
                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MEMBER_SETTING, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MEMBER_SETTING, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMemberInfo(String fb_id){
        try {
            getMemberInfo(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);

                    MemberInfoModel dataTemp = (MemberInfoModel) response.body();
                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_DETAIL, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_DETAIL, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderSetting(String fb_id){
        try {
            getSetting(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);

                    SettingModel dataTemp = (SettingModel) response.body();
                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderEditAbout(AboutModel aboutModel){
        try {
            postEditAbout(aboutModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);

                    SuccessModel dataTemp = (SuccessModel) response.body();
                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onCustomCallbackFailure(String  t) {
                    Utils.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_EDIT, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_EDIT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void saveSetting(SettingModel settingModel){
        String model = new Gson().toJson(settingModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE).edit()
                .putString(Utils.SETTING_MODEL, model).apply();
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

}
