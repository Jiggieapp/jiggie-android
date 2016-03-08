package com.jiggie.android.manager;

import android.content.Context;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    public static final String TAG = EventManager.class.getSimpleName();

    public static class FullfillmentTypes {
        public static final String PHONE_NUMBER = "phone_number";
        public static final String RESERVATION = "reservation";
        public static final String PURCHASE = "purchase";
        public static final String LINK = "link";
        public static final String NONE = "none";
        public static final String TICKET = "ticket";
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
                    Utils.d("res", responses);*/
                    if(response.code()==Utils.CODE_SUCCESS){
                        EventModel dataTemp = (EventModel) response.body();
                        EventBus.getDefault().post(dataTemp);
                    }
                    else if(response.code() == Utils.CODE_EMPTY_DATA)
                    {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT, Utils.MSG_EMPTY_DATA));
                    }
                    else{
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d(TAG, e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderEventDetail(String _id, String fb_id, String gender_interest, final String TAG){
        try {
            getEventDetail(_id, fb_id, gender_interest, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    if (response.code() == Utils.CODE_SUCCESS) {
                        EventDetailModel dataTemp = (EventDetailModel) response.body();
                        dataTemp.setFrom(TAG);
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT_DETAIL, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT_DETAIL, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT_DETAIL, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderTagsList(){
        try {
            getTagsList(new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response, Retrofit retrofit) {
                    //String header = String.valueOf(response.code());
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/

                    if (response.code() == Utils.CODE_SUCCESS) {
                        TagsListModel dataTemp = (TagsListModel) response.body();
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SETUP_TAGS, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SETUP_TAGS, Utils.MSG_EXCEPTION + t.toString()));
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
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

    public static void saveTags(ArrayList<String> jsonArray)
    {
        final int length = jsonArray.size();
        final String[] values = new String[length];
        final Set<String> setValues = new HashSet<String>();
        for (int i = 0; i < length; i++)
            values[i] = jsonArray.get(i);

        for(String temp : values)
        {
            setValues.add(temp);
        }

        App.getInstance().savePreference(Utils.TAGS_LIST, setValues);
    }

}
