package com.jiggie.android.activity.event;

import com.jiggie.android.component.Utils;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.CityModel;

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
                eventView.onFinishGetCities((CityModel) object);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Utils.d(TAG, "not success");
            }
        });
    }
}
