package com.jiggie.android.activity.event;

import com.facebook.AccessToken;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.AccountManager;
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

    public EventPresenterImplementation(EventView eventView)
    {
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
                //eventView.onFinishGetCities((CityModel) object);
                eventView.onFinishGetCities(cityModel.data.citylist);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Utils.d(TAG, "not success");
            }
        });
    }

    @Override
    public void loadEvents() {
        try {
            getEventList(AccessToken.getCurrentAccessToken().getUserId(), new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
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

                @Override
                public void onNeedToRestart() {

                }
            });
        }catch (IOException e){
            Utils.d(TAG, e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_EVENT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }
}
