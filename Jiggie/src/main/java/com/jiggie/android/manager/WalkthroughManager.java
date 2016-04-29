package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.WalkthroughInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.PostWalkthroughModel;
import com.jiggie.android.model.Success2Model;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by LTE on 2/29/2016.
 */
public class WalkthroughManager extends BaseManager{

    private static WalkthroughInterface walkthroughInterface;
    static final String TAG = WalkthroughManager.class.getSimpleName();

    public static void initWalkthroughService(){
        walkthroughInterface = getRetrofit().create(WalkthroughInterface.class);
    }

    private static WalkthroughInterface getInstance(){
        if(walkthroughInterface == null)
            initWalkthroughService();

        return walkthroughInterface;
    }

    private static void postWalkthrough(PostWalkthroughModel postWalkthroughModel, Callback callback) throws IOException {
        getInstance().postWalkthrough(Utils.URL_WALKTHROUGH, postWalkthroughModel).enqueue(callback);
    }

    public static void loaderPostWalkthrough(PostWalkthroughModel postWalkthroughModel){
        try {
            postWalkthrough(postWalkthroughModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);

                    if (response.code() == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        dataTemp.setFrom(TAG);
                        EventBus.getDefault().post(dataTemp);
                    }
                    /*else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_WALKTHROUGH, Utils.RESPONSE_FAILED));
                    }*/
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_WALKTHROUGH, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_WALKTHROUGH, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

}
