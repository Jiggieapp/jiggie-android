package com.jiggie.android.model;

/**
 * Created by LTE on 2/12/2016.
 */
public class ChatAddModel {

    String fromId;
    String header;
    String fromName;
    String message;
    String hosting_id;
    String key;
    String toId;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHosting_id() {
        return hosting_id;
    }

    public void setHosting_id(String hosting_id) {
        this.hosting_id = hosting_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }
}
