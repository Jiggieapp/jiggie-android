package com.jiggie.android.model;

/**
 * Created by Wandy on 5/4/2016.
 */
public class SuccessUploadModel {
    int response;
    String url;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    String from;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String type = null;

    public String getType() {
        return type;
    }
}
