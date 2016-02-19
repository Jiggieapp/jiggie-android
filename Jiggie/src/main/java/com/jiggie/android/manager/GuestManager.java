package com.jiggie.android.manager;

import android.util.Log;

import com.jiggie.android.api.GuestInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.GuestModel;
import com.jiggie.android.model.Success2Model;

import java.io.IOException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by LTE on 2/4/2016.
 */
public class GuestManager {

    private static GuestInterface eventInterface;
    public static ArrayList<GuestModel.Data.GuestInterests> dataGuestInterest = new ArrayList<>();

    public static void initGuestService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        eventInterface = retrofit.create(GuestInterface.class);
    }

    private static GuestInterface getInstance(){
        if(eventInterface == null)
            initGuestService();

        return eventInterface;
    }

    private static void getGuestInterest(String event_id, String fb_id, String gender_interest, Callback callback) throws IOException {
        getInstance().getGuestInterest(event_id, fb_id, gender_interest).enqueue(callback);
    }

    private static void getGuestConnect(String fb_id, String from_id, Callback callback) throws IOException {
        getInstance().getGuestConnect(fb_id, from_id, "approved").enqueue(callback);
    }

    public static void loaderGuestInterest(String event_id, String fb_id, String gender_interest){
        try {
            getGuestInterest(event_id, fb_id, gender_interest, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    /*String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);*/

                    if(response.code()==Utils.CODE_SUCCESS){
                        GuestModel dataTemp = (GuestModel) response.body();
                        dataGuestInterest = dataTemp.getData().getGuest_interests();
                        EventBus.getDefault().post(dataTemp);
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT_GUEST, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT_GUEST, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT_GUEST, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderGuestConnect(String fb_id, String from_id){
        try {
            getGuestConnect(fb_id, from_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    /*String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);*/

                    if(response.code()==Utils.CODE_SUCCESS){
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(dataTemp);
                    }else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_GUEST_CONNECT, Utils.RESPONSE_FAILED));
                    }

                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_GUEST_CONNECT, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_GUEST_CONNECT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

}
