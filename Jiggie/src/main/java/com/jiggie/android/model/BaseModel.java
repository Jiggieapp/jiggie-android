package com.jiggie.android.model;

/**
 * Created by Wandy on 2/10/2016.
 */
public interface BaseModel {
    /*public String getAccessToken() {
        return App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .getString(Utils.ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .edit()
                .putString(Utils.ACCESS_TOKEN, accessToken)
                .apply();
    }*/

    public String getAccessToken();
    public String setAccessToken();
}
