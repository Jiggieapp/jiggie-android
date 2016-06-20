package com.jiggie.android.model;

import com.jiggie.android.component.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LTE on 6/15/2016.
 */
public class RoomModel {
    public String key;
    public Info info;
    public long type;

    public RoomModel(String key, Info info, long type){
        this.key = key;
        this.info = info;
        this.type = type;
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

    public static class Info {
        public String name;
        public String avatar;
        public String event;
        //public String identifier;
        public String last_message;
        public long created_at;
        public long updated_at;
        public int unread;

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

        public int getUnread() {
            return unread;
        }

        public void setUnread(int unread) {
            this.unread = unread;
        }
    }
}