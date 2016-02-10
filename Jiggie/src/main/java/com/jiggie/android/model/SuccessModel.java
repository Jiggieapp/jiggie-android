package com.jiggie.android.model;

/**
 * Created by LTE on 2/2/2016.
 */
public class SuccessModel {

    boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    //wandy 10-02-2015
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
