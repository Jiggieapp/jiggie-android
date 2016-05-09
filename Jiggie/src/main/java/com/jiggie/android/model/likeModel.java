package com.jiggie.android.model;

/**
 * Created by LTE on 5/9/2016.
 */
public class likeModel {

    String event_id;
    boolean isLiked;

    public likeModel(String event_id, boolean isLiked){
        this.event_id = event_id;
        this.isLiked = isLiked;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }
}
