package com.jiggie.android.model;

/**
 * Created by LTE on 6/20/2016.
 */
public class CollectionRoomMemberModel {

    String fb_id;
    boolean isAvailable;

    public CollectionRoomMemberModel(String fb_id, boolean isAvailable){
        this.fb_id = fb_id;
        this.isAvailable = isAvailable;
    }

    public String getFb_id() {
        return fb_id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
