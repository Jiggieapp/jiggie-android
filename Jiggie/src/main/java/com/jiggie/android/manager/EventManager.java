package com.jiggie.android.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.api.API;
import com.jiggie.android.api.EventInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.ExceptionModel;

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
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventInterface = retrofit.create(EventInterface.class);
    }

    private static EventInterface getInstance(){
        if(eventInterface == null)
            initEventService();

        return eventInterface;
    }

    private static void getEventList(String fb_id, Callback callback) throws IOException {
        getInstance().getEventList(fb_id).enqueue(callback);
    }

    private static void getEventDetail(String _id, String fb_id, String gender_interest, Callback callback) throws IOException {
        getInstance().getEventDetail(_id, fb_id, gender_interest).enqueue(callback);
    }

    public static void loaderEvent(String fb_id){
        try {
            getEventList(fb_id, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());

                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);
                    EventModel dataTemp = (EventModel) response.body();


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

    public static void loaderEventDetail(String _id, String fb_id, String gender_interest){
        try {
            getEventDetail(_id, fb_id, gender_interest, new Callback() {
                @Override
                public void onResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());

                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);
                    EventDetailModel dataTemp = (EventDetailModel) response.body();

                    //int size = dataTemp.getData().getEvents().size();

                    EventBus.getDefault().post(dataTemp);
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
