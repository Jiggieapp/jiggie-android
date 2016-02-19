package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.SocialInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.SocialModel;
import com.jiggie.android.model.Success2Model;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by LTE on 2/5/2016.
 */
public class SocialManager {

    private static SocialInterface socialInterface;
    //

    public static void iniSocialService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        socialInterface = retrofit.create(SocialInterface.class);

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

    public static void loaderSocialFeed(String fb_id, String gender_interest){
        /*try {
            getSocialFeed(fb_id, gender_interest, new CustomCallback() {
                @Override
                public void onCustomCallbackReponse(Response response, Retrofit retrofit) {
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
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);

                    int response_code = response.code();

                    if(response.code()==Utils.CODE_SUCCESS){
                        SocialModel dataTemp = (SocialModel) response.body();
                        EventBus.getDefault().post(dataTemp);
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_FEED, Utils.RESPONSE_FAILED+" "+"empty data"));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_FEED, t));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loaderSocialMatch(String fb_id, String from_id, String type){
        try {
            getSocialMatch(fb_id, from_id, type, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    if(response.code()==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(dataTemp);
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SOCIAL_MATCH, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static class Type {
        public static final String APPROVED = "approved";
        public static final String VIEWED = "viewed";
        public static final String DENIED = "denied";

        public static boolean isInbound(SocialModel.Data.SocialFeeds value)
        {
            return APPROVED.equalsIgnoreCase(value.getType());
        }
    }

}
