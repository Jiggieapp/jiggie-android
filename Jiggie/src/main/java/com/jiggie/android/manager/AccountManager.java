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
import com.jiggie.android.model.LoginResultModel;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.MemberSettingResultModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.Success2Model;
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

                    if (response.code() == Utils.CODE_SUCCESS) {

                        LoginResultModel dataLogin = (LoginResultModel) response.body();
                        SettingModel dataTemp = setSettingModelFromLogin(dataLogin);

                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SIGN_IN, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SIGN_IN, Utils.MSG_EXCEPTION + t.toString()));
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

                    if (response.code() == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MEMBER_SETTING, Utils.RESPONSE_FAILED));
                    }

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
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    if (response.code() == Utils.CODE_SUCCESS) {
                        MemberInfoModel dataTemp = (MemberInfoModel) response.body();
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_DETAIL, Utils.RESPONSE_FAILED));
                    }


                }

                @Override
                public void onCustomCallbackFailure(String t) {
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

                    if (response.code() == Utils.CODE_SUCCESS) {
                        MemberSettingResultModel data = (MemberSettingResultModel) response.body();
                        SettingModel dataTemp = setSettingModelFromMemberSetting(data);
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
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
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    if (response.code() == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_EDIT, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
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

    private static SettingModel setSettingModelFromLogin(LoginResultModel data){
        boolean success = true;


        LoginResultModel.Data.Login login = data.getData().getLogin();

        SettingModel.Data.Notifications notifications = new SettingModel.Data.Notifications(login.getNotifications().isChat(), login.getNotifications().isFeed(),
                login.getNotifications().isLocation());
        //payment still empty
        SettingModel.Data.Payment payment = new SettingModel.Data.Payment();

        SettingModel.Data settingData = new SettingModel.Data(login.get_id(), login.getFb_id(), login.getGender(), notifications,
                login.getUpdated_at(), login.getAccount_type(), login.getExperiences(), login.getGender_interest(), payment, login.getPhone());




        SettingModel model = new SettingModel(success, settingData);
        model.setIs_new_user(login.is_new_user());
        model.setHelp_phone(login.getHelp_phone());
        model.setMatchme(login.isMatchme());
        model.setDevice_type(login.getDevice_type());
        model.setShow_walkthrough(login.isShow_walkthrough());

        return model;
    }

    private static SettingModel setSettingModelFromMemberSetting(MemberSettingResultModel data){
        boolean success = true;


        MemberSettingResultModel.Data.MemberSettings memberSettingResultModel = data.getData().getMembersettings();

        SettingModel.Data.Notifications notifications = new SettingModel.Data.Notifications(memberSettingResultModel.getNotifications().isChat(), memberSettingResultModel.getNotifications().isFeed(),
                memberSettingResultModel.getNotifications().isLocation());
        //payment still empty
        SettingModel.Data.Payment payment = new SettingModel.Data.Payment();

        SettingModel.Data settingData = new SettingModel.Data(memberSettingResultModel.get_id(), memberSettingResultModel.getFb_id(), memberSettingResultModel.getGender(), notifications,
                memberSettingResultModel.getUpdated_at(), memberSettingResultModel.getAccount_type(), memberSettingResultModel.getExperiences(), memberSettingResultModel.getGender_interest(), payment, memberSettingResultModel.getPhone());

        SettingModel model = new SettingModel(success, settingData);

        return model;
    }

}
