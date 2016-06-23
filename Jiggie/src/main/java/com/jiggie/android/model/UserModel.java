package com.jiggie.android.model;

/**
 * Created by LTE on 6/16/2016.
 */
public class UserModel {

    String key;
    String fb_id;
    String name;
    String avatar;

    public UserModel(String key, String fb_id, String name, String avatar){
        this.key = key;
        this.fb_id = fb_id;
        this.name = name;
        this.avatar = avatar;
    }

    public String getKey() {
        return key;
    }

    public String getFb_id() {
        return fb_id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
