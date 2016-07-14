package com.jiggie.android.model;

/**
 * Created by LTE on 4/20/2016.
 */
public final class PostLocationModel {
    public final String fb_id;
    public final String longitude;
    public final String latitude;
    public final boolean is_login;

    public PostLocationModel(String fb_id, String latitude, String longitude, boolean is_login){
        this.fb_id = fb_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.is_login = is_login;
    }
}
