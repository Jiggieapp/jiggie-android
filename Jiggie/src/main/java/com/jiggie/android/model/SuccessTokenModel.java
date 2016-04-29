package com.jiggie.android.model;

/**
 * Created by Wandy on 4/25/2016.
 */
public class SuccessTokenModel {
    public long response;
    public String msg;
    public Data data;

    public SuccessTokenModel(long response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static class Data {
        public boolean success;
        public String token;

        public Data(boolean success, String token){
            this.success = success;
            this.token = token;
        }
    }
}