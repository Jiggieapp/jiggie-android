package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.TrackInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.PostAppsFlyerModel;
import com.jiggie.android.model.PostMixpanelModel;
import com.jiggie.android.model.Success2Model;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by LTE on 2/25/2016.
 */
public class TrackManager extends BaseManager{

    static TrackInterface trackInterface;

    public static void initTrackService(){
        trackInterface = getRetrofit().create(TrackInterface.class);
    }

    private static TrackInterface getInstance(){
        if(trackInterface == null)
            initTrackService();
        return trackInterface;
    }

    private static void postAppsFlyer(PostAppsFlyerModel postAppsFlyerModel, Callback callback) throws IOException {
        getInstance().postAppsFlyer(Utils.URL_APPSFLYER, postAppsFlyerModel).enqueue(callback);
    }

    private static void postMixpanel(String fb_id, PostMixpanelModel postMixpanelModel, Callback callback) throws IOException {
        getInstance().postMixpanel(Utils.URL_MIXPANEL + fb_id, postMixpanelModel).enqueue(callback);
    }

    public static void loaderAppsFlyer(PostAppsFlyerModel postAppsFlyerModel){
        try {
            postAppsFlyer(postAppsFlyerModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    //Utils.d(TAG, responses);

                    if (response.code() == Utils.CODE_SUCCESS) {

                        Success2Model dataTemp = (Success2Model) response.body();

                        //EventBus.getDefault().post(dataTemp);
                        Log.d("Appsflyer", "success sync");
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_APPSFLYER, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_APPSFLYER, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_APPSFLYER, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMixpanel(String fb_id, PostMixpanelModel postMixpanelModel){
        try {
            postMixpanel(fb_id, postMixpanelModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    //Utils.d(TAG, responses);

                    if (response.code() == Utils.CODE_SUCCESS) {

                        Success2Model dataTemp = (Success2Model) response.body();

                        //EventBus.getDefault().post(dataTemp);
                        Log.d("Mixpanel", "success sync");
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MIXPANEL, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MIXPANEL, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MIXPANEL, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

}
