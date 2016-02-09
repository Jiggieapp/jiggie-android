package com.jiggie.android.model;

/**
 * Created by wandywijayanto on 2/9/16.
 */
public final class AccessTokenModel {
    public final boolean success;
    public final String token;

    public AccessTokenModel(boolean success, String token){
        this.success = success;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }
}
