package com.jiggie.android.model;

/**
 * Created by wandywijayanto on 2/9/16.
 */
public class AccessTokenModel {
    String token;
    public void setToken(String token) {
        this.token = token;
    }

    /*public AccessTokenModel(boolean success, String token){
        this.success = success;
        this.token = token;
    }*/

    public String getToken() {
        return token;
    }
}
