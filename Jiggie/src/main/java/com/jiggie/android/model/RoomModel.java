package com.jiggie.android.model;

import com.jiggie.android.component.Utils;

/**
 * Created by LTE on 6/15/2016.
 */
public class RoomModel {
    public Room room;

    public RoomModel(){

    }

    public RoomModel(Room room){
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public static class Room {
        public final String id;
        public final String type;
        public final String name;

        public Room(String id, String type, String name){
            this.id = id;
            this.type = type;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }
}