package com.jiggie.android.activity.event;

import com.jiggie.android.model.CityModel;

import java.util.ArrayList;

/**
 * Created by Wandy on 6/8/2016.
 */
public interface EventView {
    //void onFinishGetCities(CityModel cityModel);
    void onFinishGetCities(ArrayList<CityModel.Data.Citylist> citylist);
}
