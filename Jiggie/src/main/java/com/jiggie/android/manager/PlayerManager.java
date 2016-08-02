package com.jiggie.android.manager;

import com.jiggie.android.api.SoundcloudInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.model.SoundcloudModel;

import retrofit2.Response;

/**
 * Created by Wandy on 7/19/2016.
 */
public class PlayerManager extends BaseManager {
    private static SoundcloudInterface instance;
    public static SoundcloudInterface getInstance()
    {
        if(instance == null)
            instance = getRetrofit().create(SoundcloudInterface.class);
        return instance;
    }

    public static void getTrackDetail(final String trackId, final OnResponseListener onResponseListener)
    {
        getTrackDetail(trackId, new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response) {
                Utils.d("bruh " + trackId, Utils.print(response));
                onResponseListener.onSuccess(response.body());
            }

            @Override
            public void onCustomCallbackFailure(String t) {
            }

            @Override
            public void onNeedToRestart() {

            }
        });
    }

    private static void getTrackDetail(final String trackId, CustomCallback callback)
    {
        getInstance().getSoundcloudDetail(trackId).enqueue(callback);
    }
}
