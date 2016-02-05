package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.SocialInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.SocialModel;

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

    public static void loaderSocialFeed(String fb_id, String gender_interest){
        try {
            getSocialFeed(fb_id, gender_interest, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());

                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);
                    SocialModel dataTemp = (SocialModel) response.body();


                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.MSG_EXCEPTION + e.toString()));
        }
    }

}
