package com.jiggie.android.model;

import com.jiggie.android.component.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LTE on 6/15/2016.
 */
public class RoomModel {
    public String key;
    public Info info;
    public long type;
    public ArrayList<Unread> unreads;

    public RoomModel(String key, Info info, long type, ArrayList<Unread> unreads){
        this.key = key;
        this.info = info;
        this.type = type;
        this.unreads = unreads;
    }

    public String getKey() {
        return key;
    }

    public Info getInfo() {
        return info;
    }

    public long getType() {
        return type;
    }

    public ArrayList<Unread> getUnreads() {
        return unreads;
    }

    public static class Info {
        public String name;
        public String avatar;
        public String event;
        //public String identifier;
        public String last_message;
        public long created_at;
        public long updated_at;

        public Info(String name, String avatar, String event, String last_message, long created_at, long updated_at){
            this.name = name;
            this.avatar = avatar;
            this.event = event;
            this.last_message = last_message;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }

        public String getName() {
            return name;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getEvent() {
            return event;
        }

        public String getLast_message() {
            return last_message;
        }

        public long getCreated_at() {
            return created_at;
        }

        public long getUpdated_at() {
            return updated_at;
        }
    }

    public static class Unread {
        String fb_id;
        long counter;

        public Unread(String fb_id, long counter){
            this.fb_id = fb_id;
            this.counter = counter;
        }

        public String getFb_id() {
            return fb_id;
        }

        public long getCounter() {
            return counter;
        }
    }
}