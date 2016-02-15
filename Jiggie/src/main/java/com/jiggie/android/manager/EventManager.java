package com.jiggie.android.manager;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.api.EventInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.TagsListModel;

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
    public final static String TAG = EventManager.class.getSimpleName();

    public static class FullfillmentTypes {
        public static final String PHONE_NUMBER = "phone_number";
        public static final String RESERVATION = "reservation";
        public static final String PURCHASE = "purchase";
        public static final String LINK = "link";
        public static final String NONE = "none";
    }

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

    private static void getTagsList(Callback callback) throws IOException {
        getInstance().getTagsList().enqueue(callback);
    }

    public static void loaderEvent(String fb_id){
        try {
            getEventList(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    /*String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);*/

                    EventModel dataTemp = (EventModel) response.body();


                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderEventDetail(String _id, String fb_id, String gender_interest){
        try {
            getEventDetail(_id, fb_id, gender_interest, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    String responses = new Gson().toJson(response.body());
                    Utils.d(TAG, responses);
                    EventDetailModel dataTemp = (EventDetailModel) response.body();
                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT_DETAIL, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT_DETAIL, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderTagsList(){
        try {
            getTagsList(new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {

                    //String header = String.valueOf(response.code());
                    String responses = new Gson().toJson(response.body());
                    Log.d("res", responses);
                    TagsListModel dataTemp = (TagsListModel) response.body();

                    EventBus.getDefault().post(dataTemp);
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Log.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SETUP_TAGS, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Log.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SETUP_TAGS, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void saveTagsList(TagsListModel tagsListModel){

        String model = new Gson().toJson(tagsListModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_TAGLIST, Context.MODE_PRIVATE).edit()
                .putString(Utils.TAGLIST_MODEL, model).apply();

    }

    public static TagsListModel loadTagsList(){

        TagsListModel tagsListModel = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_TAGLIST,
                Context.MODE_PRIVATE).getString(Utils.TAGLIST_MODEL, ""), TagsListModel.class);

        return tagsListModel;
    }

}
