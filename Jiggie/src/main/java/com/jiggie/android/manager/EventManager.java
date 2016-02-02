package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.EventInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.EventModel;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by LTE on 2/1/2016.
 */
public class EventManager {

    private static EventInterface eventInterface;

    public static void initEventService(){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://52.77.222.216")
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventInterface = retrofit.create(EventInterface.class);
    }

    private static void getEventList(String fb_id, String gender_interest, Callback callback) throws IOException {
        eventInterface.getEventList(fb_id, gender_interest).enqueue(callback);
    }

    public static void loaderEvent(String fb_id, String gender_interest){
        try {
            getEventList(fb_id, gender_interest, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);
                    EventModel dataTemp = (EventModel) response.body();

                    EventBus.getDefault().post(dataTemp.getData().getEvents());
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Failure", t.toString());
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
        }
    }

}
