package com.jiggie.android.activity.event;

import android.util.Log;

import com.facebook.AccessToken;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.model.CityModel;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.SettingModel;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit2.Response;

/**
 * Created by Wandy on 6/8/2016.
 */
public class EventPresenterImplementation implements EventPresenter {

    private EventView eventView;
    private final static String TAG = EventPresenterImplementation.class.getSimpleName();

    public EventPresenterImplementation(EventView eventView) {
        this.eventView = eventView;
    }

    @Override
    public void getCities() {
        AccountManager.loaderCityList(new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                SettingModel settingModel = AccountManager.loadSetting();
                CityModel cityModel = (CityModel) object;
                settingModel.getData().setCityList(cityModel.data.citylist);
                AccountManager.saveSetting(settingModel);
                eventView.onFinishGetCities(cityModel.data.citylist);
                loadEvents();
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Utils.d(TAG, "not success");
            }
        });
    }

    @Override
    public void loadEvents() {
        /*EventManager.loaderEvent2(AccessToken.getCurrentAccessToken().getUserId(), new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                EventModel eventModel = (EventModel) object;
                eventView.onf
            }

            @Override
            public void onFailure(int responseCode, String message) {

            }
        });*/

        try{
            if(AccessToken.getCurrentAccessToken().getUserId()!=null){
                EventManager.loaderEvent(AccessToken.getCurrentAccessToken().getUserId());
            }
        }catch (Exception e){
            Log.d(TAG, e.toString());
        }


    }
}
