package com.jiggie.android.manager;

import android.os.Build;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.BuildConfig;
import com.jiggie.android.api.SocialInterface;
import com.jiggie.android.component.SimpleJSONObject;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.PostLocationModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.SocialModel;
import com.jiggie.android.model.Success2Model;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LTE on 2/5/2016.
 */
public class SocialManager extends BaseManager{

    private static SocialInterface socialInterface;
    public static final String TAG = SocialManager.class.getSimpleName() ;
    public static String lat = Utils.BLANK;
    public static  String lng = Utils.BLANK;

    public static void iniSocialService(){
        socialInterface = getRetrofit().create(SocialInterface.class);
    }

    private static SocialInterface getInstance(){
        if(socialInterface == null)
            iniSocialService();

        return socialInterface;
    }

    private static void getSocialFeed(String fb_id, String gender_interest, Callback callback) throws IOException {
        getInstance().getSocialFeed(fb_id, gender_interest).enqueue(callback);
    }

    private static void getSocialMatch(String fb_id, String from_id, String type, Callback callback) throws IOException {
        getInstance().getSocialMatch(fb_id, from_id, type).enqueue(callback);
    }

    private static void postLocation(PostLocationModel postLocationModel, Callback callback) throws IOException {
        getInstance().postLocation(Utils.URL_POST_LOCATION, postLocationModel).enqueue(callback);
    }

    public static void loaderSocialFeed(String fb_id, String gender_interest){
        /*try {
            getSocialFeed(fb_id, gender_interest, new CustomCallback() {
                @Override
                public void onCustomCallbackReponse(Response response) {
                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    SocialModel dataTemp = (SocialModel) response.body();
                    *//*if(dataTemp!=null&&dataTemp.getData().getSocial_feeds().size()>0){
                        EventBus.getDefault().post(dataTemp);
                    }*//*
                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_FEED, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_FEED, Utils.MSG_EXCEPTION + e.toString()));
        }*/

        try {
            getSocialFeed(fb_id, gender_interest, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Utils.d(TAG, responses);


                    if(response.code()==Utils.CODE_SUCCESS){
                        SocialModel dataTemp = (SocialModel) response.body();
                        EventBus.getDefault().post(dataTemp);
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_FEED, "Empty data"));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_FEED, t));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loaderSocialMatch(String fb_id, String from_id, String type, final OnResponseListener onResponseListener){
        try {
            getSocialMatch(fb_id, from_id, type, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {

                    //String header = String.valueOf(response.code());
                    //String responses = new Gson().toJson(response.body());
                    //Utils.d("res", responses);

                    if (response.code() == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        dataTemp.setFrom(TAG);
                        onResponseListener.onSuccess(dataTemp);
                        //EventBus.getDefault().post(dataTemp);
                    } else {
                        //EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.RESPONSE_FAILED));
                        onResponseListener.onFailure(Utils.CODE_FAILED, Utils.RESPONSE_FAILED);
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    //EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.MSG_EXCEPTION + t.toString()));
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.RESPONSE_FAILED);

                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderSocialMatch(String fb_id, String from_id, String type){
        try {
            getSocialMatch(fb_id, from_id, type, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {

                    //String header = String.valueOf(response.code());
                    //String responses = new Gson().toJson(response.body());
                    //Utils.d("res", responses);

                    if (response.code() == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        dataTemp.setFrom(TAG);
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.MSG_EXCEPTION + e.toString()));
        }
    }


    private static void trackMixPanel(boolean confirm)
    {
        final LoginModel login = AccountManager.loadLogin();
        final SettingModel setting = AccountManager.loadSetting();
        final SimpleJSONObject json = new SimpleJSONObject();
        final App app = App.getInstance();
        if(confirm)
        {
            json.putString("ABTestChat:Connect", "Connect");
            json.putString("name_and_fb_id", String.format("%s_%s_%s", login.getUser_first_name(), login.getUser_last_name(), AccessToken.getCurrentAccessToken().getUserId()));
            json.putString("age", StringUtility.getAge2(login.getBirthday()));
            json.putString("app_version", String.valueOf(BuildConfig.VERSION_CODE));
            json.putString("birthday", login.getBirthday());
            json.putString("device_type", Build.MODEL);
            json.putString("email", login.getEmail());
            json.putString("feed_item_type", "viewed");
            json.putString("first_name", login.getUser_first_name());
            json.putString("gender", setting.getData().getGender());
            json.putString("gender_interest", setting.getData().getGender_interest());
            json.putString("last_name", login.getUser_last_name());
            json.putString("location", login.getLocation());
            json.putString("os_version", app.getDeviceOSName());
            app.trackMixPanelEvent("Accept Feed Item", json);
        }
        else
        {
            app.trackMixPanelEvent("Passed Feed Item");
        }
    }

    public static void loaderSocialMatchAsync(String fb_id, String from_id, final String type, final boolean confirm){
        try {
            getSocialMatch(fb_id, from_id, type, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    trackMixPanel(confirm);
                    if (response.code() == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        dataTemp.setFrom(TAG);
                        //EventBus.getDefault().post(dataTemp);
                    } else {
                        //EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    //Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }catch (IOException e){
            //Log.d("Exception", e.toString());
            //EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderLocation(final PostLocationModel postLocationModel, final OnResponseListener onResponseListener){
        try {
            postLocation(postLocationModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", response.toString());

                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        if (dataTemp.getResponse() == 1) {
                            onResponseListener.onSuccess(dataTemp);

                        } else {
                            onResponseListener.onFailure(dataTemp.getResponse(), dataTemp.getMsg());
                        }

                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static class Type {
        public static final String APPROVED = "approved";
        public static final String VIEWED = "viewed";
        public static final String DENIED = "denied";

        public static boolean isInbound(SocialModel.Data.SocialFeeds value)
        {
            if(value != null)
                return APPROVED.equalsIgnoreCase(value.getType());
            else return false;
        }
    }

    public interface OnResponseListener {
        public void onSuccess(Object object);
        public void onFailure(int responseCode, String message);
    }

}
