package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.ShareInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.ShareLinkModel;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by LTE on 2/5/2016.
 */
public class ShareManager {

    private static ShareInterface shareInterface;

    public static void initShareService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        shareInterface = retrofit.create(ShareInterface.class);
    }

    private static ShareInterface getInstance(){
        if(shareInterface == null)
            initShareService();

        return shareInterface;
    }

    private static void getShareApps(String fb_id, Callback callback) throws IOException {
        getInstance().getShareApps(fb_id, "general").enqueue(callback);
    }

    private static void getShareEvent(String event_id, String fb_id, String venue_name, Callback callback) throws IOException {
        getInstance().getShareEvent(event_id, fb_id, "event", venue_name).enqueue(callback);
    }

    public static void loaderShareApps(String fb_id){
        try {
            getShareApps(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    /*String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);*/

                    if(response.code()==Utils.CODE_SUCCESS){
                        ShareLinkModel dataTemp = (ShareLinkModel) response.body();
                        EventBus.getDefault().post(dataTemp);
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SHARE_LINK, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SHARE_LINK, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SHARE_LINK, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderShareEvent(String event_id, String fb_id, String venue_name){
        try {
            getShareEvent(event_id, fb_id, venue_name, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    if(response.code()==Utils.CODE_SUCCESS){
                        ShareLinkModel dataTemp = (ShareLinkModel) response.body();
                        EventBus.getDefault().post(dataTemp);
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SHARE_LINK, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SHARE_LINK, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SHARE_LINK, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

}