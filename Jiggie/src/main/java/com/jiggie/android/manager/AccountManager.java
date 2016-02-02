package com.jiggie.android.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.api.AccountInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.SettingModel;

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

    public static void initLoginService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        accountInterface = retrofit.create(AccountInterface.class);
    }

    private static void postLogin(LoginModel loginRequestModel, Callback callback) throws IOException {
        accountInterface.postLogin(Utils.URL_LOGIN, loginRequestModel).enqueue(callback);
    }

    public static void loaderLogin(LoginModel loginRequestModel){
        try {
            postLogin(loginRequestModel, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    SettingModel dataTemp = (SettingModel)response.body();

                    EventBus.getDefault().post(dataTemp);

                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.MSG_EXCEPTION+t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void saveSetting(Activity a, SettingModel settingModel){
        App.getInstance().getSharedPreferences(a.getClass().getName(), Context.MODE_PRIVATE).edit()
                .putString(Common.FIELD_GENDER, settingModel.getData().getGender())
                .putString(Common.FIELD_GENDER_INTEREST, settingModel.getData().getGender_interest())
                .putString(Common.FIELD_PHONE, settingModel.getData().getPhone())
                .putString(Common.FIELD_ACCOUNT_TYPE, settingModel.getData().getAccount_type())
                .putBoolean(Common.FIELD_MATCH_ME, settingModel.isMatch_me())
                .putBoolean(Common.FIELD_FEED, settingModel.getData().getNotifications().isFeed())
                .putBoolean(Common.FIELD_CHAT, settingModel.getData().getNotifications().isChat())
                .apply();
    }

}
