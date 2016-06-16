package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 6/16/2016.
 */
public class RoomMembersModel {

    String key;
    ArrayList<Integer> roomMembersId;

    public RoomMembersModel(String key, ArrayList<Integer> roomMembersId){
        this.key = key;
        this.roomMembersId = roomMembersId;
    }

    public String getKey() {
        return key;
    }

    public ArrayList<Integer> getRoomMembersId() {
        return roomMembersId;
    }
}
