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
import com.jiggie.android.model.FilterModel;
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

    private static void postLogin(LoginModel loginRequestModel, Callback callback) throws IOException {
        getAccountInterface().postLogin(Utils.URL_LOGIN, loginRequestModel).enqueue(callback);
    }

    private static void postMemberSetting(MemberSettingModel memberSettingModel, Callback callback) throws IOException {
        getAccountInterface().postMemberSetting(Utils.URL_MEMBER_SETTING, memberSettingModel).enqueue(callback);
    }

    private static void getUserTagList(Callback callback)
    {
        getAccountInterface().getUserTagList("10153418311072858"
               /* AccessToken.getCurrentAccessToken().getUserId()*/).enqueue(callback);
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
                    SuccessModel dataTemp = (SuccessModel) response.body();
                    EventBus.getDefault().post(dataTemp);

                    AccountManager.saveMemberSetting(memberSettingModel);
                }

                @Override
                public void onFailure(Throwable t) {
                    Utils.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.MSG_EXCEPTION + e.toString()));
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

    public static MemberSettingModel loadMemberSetting()
    {
        MemberSettingModel memberSettingModel = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING,
                Context.MODE_PRIVATE).getString(Utils.MEMBER_SETTING_MODEL, ""), MemberSettingModel.class);
        return memberSettingModel;
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


}
