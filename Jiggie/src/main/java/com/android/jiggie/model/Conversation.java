package com.android.jiggie.model;

import com.android.jiggie.component.StringUtility;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by rangg on 18/12/2015.
 */
public class Conversation {
    public static final String FIELD_FROM_ID = "fromId";
    public static final String FIELD_FROM_NAME = "fromName";
    public static final String FIELD_PROFILE_IMAGE = "profile_image";
    public static final String FIELD_LAST_MESSAGE = "last_message";
    public static final String FIELD_LAST_UPDATED = "last_updated";
    public static final String FIELD_UNREAD = "unread";
    public static final String FIELD_REPLIED = "hasreplied";
    public static final String FIELD_FACEBOOK_ID = Common.FIELD_FACEBOOK_ID;

    private String fromId;
    private String fromName;
    private String profileImage;
    private String lastMessage;
    private String lastUpdated;
    private int unread;
    private boolean replied;
    private String facebookId;
    private String simpleDate;

    public Conversation(JSONObject json) {
        this.fromId = json.optString(FIELD_FROM_ID);
        this.fromName = StringUtility.formatCharacterCase(json.optString(FIELD_FROM_NAME));
        this.profileImage = json.optString(FIELD_PROFILE_IMAGE);
        this.lastMessage = json.optString(FIELD_LAST_MESSAGE);
        this.unread = json.optInt(FIELD_UNREAD);
        this.replied = json.optBoolean(FIELD_REPLIED);
        this.facebookId = json.optString(FIELD_FACEBOOK_ID);
        this.lastUpdated = json.optString(FIELD_LAST_UPDATED);
    }

    public String getFromId() { return fromId; }
    public String getFromName() { return fromName; }
    public String getProfileImage() { return profileImage; }
    public String getLastMessage() { return lastMessage; }
    public String getLastUpdated() { return lastUpdated; }
    public int getUnread() { return unread; }
    public boolean isReplied() { return replied; }
    public String getFacebookId() { return facebookId; }
    public String getSimpleDate() throws ParseException {
        if (this.simpleDate == null) {
            final Date date = Common.ISO8601_DATE_FORMAT_UTC.parse(this.lastUpdated);
            this.simpleDate = Common.SIMPLE_12_HOUR_FORMAT.format(date).replace("AM", "").replace("PM", "").trim();
        }
        return this.simpleDate;
    }

    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public void setUnread(int unread) { this.unread = unread; }
    public void setFieldLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
        this.simpleDate = null;
    }
}
