package com.jiggie.android.model;

/**
 * Created by LTE on 6/16/2016.
 */
public class MessagesModel {

    String messageId;
    String fb_id;
    String name;
    String avatar;
    String message;
    long created_at;

    public MessagesModel(String messageId, String fb_id, String name, String avatar, String message, long created_at){
        this.messageId = messageId;
        this.fb_id = fb_id;
        this.name = name;
        this.avatar = avatar;
        this.message = message;
        this.created_at = created_at;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getFb_id() {
        return fb_id;
    }

    public String getMessage() {
        return message;
    }

    public long getCreated_at() {
        return created_at;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
