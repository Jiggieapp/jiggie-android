package com.jiggie.android.model;

/**
 * Created by LTE on 2/2/2016.
 */
public class ExceptionModel {

    private final String message;

    public ExceptionModel(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
