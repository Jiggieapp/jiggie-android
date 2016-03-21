package com.jiggie.android.model;

/**
 * Created by LTE on 2/2/2016.
 */
public class ExceptionModel {

    private final String from;
    private final String message;

    public ExceptionModel(String from, String message){
        this.from = from;
        this.message = message;
    }

    public ExceptionModel(String message)
    {
        this.message = message;
        this.from = "";
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }
}
