package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.ShareInterface;
import com.jiggie.android.component.Utils;
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

    private static void getShareLink(String fb_id, Callback callback) throws IOException {
        getInstance().getShareLink(fb_id, "general").enqueue(callback);
    }

    public static void loaderShareLink(String fb_id){
        try {
            getShareLink(fb_id, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    /*String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);*/
                    ShareLinkModel dataTemp = (ShareLinkModel) response.body();


                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onFailure(Throwable t) {
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
