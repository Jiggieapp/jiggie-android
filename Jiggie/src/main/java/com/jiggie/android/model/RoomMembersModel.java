package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 6/16/2016.
 */
public class RoomMembersModel {

    String key;
    String roomMemberId;
    boolean isAvailable;

    public RoomMembersModel(String key, String roomMemberId, boolean isAvailable){
        this.key = key;
        this.roomMemberId = roomMemberId;
        this.isAvailable = isAvailable;
    }

    public String getRoomMemberId() {
        return roomMemberId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getKey() {
        return key;
    }
}
