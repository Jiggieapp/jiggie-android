package com.jiggie.android.model;

import com.jiggie.android.component.StringUtility;

import org.json.JSONObject;

/**
 * Created by rangg on 23/11/2015.
 */
public class SocialMatch extends IdModel {
    public static final String FIELD_FACEBOOK_ID = Common.FIELD_FACEBOOK_ID;
    public static final String FIELD_FROM_FACEBOOK_ID = "from_fb_id";
    public static final String FIELD_FROM_FIRST_NAME = "from_first_name";
    public static final String FIELD_FROM_GENDER = "from_gender";
    public static final String FIELD_EVENT_ID = "event_id";
    public static final String FIELD_EVENT_NAME = "event_name";
    public static final String FIELD_ACTIVE = "active";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_MATCH = "didMatch";

    private String facebookId;
    private String fromFacebookId;
    private String fromGender;
    private String fromFirstName;
    private String eventId;
    private String eventName;
    private String type;
    private boolean match;
    private boolean active;

    public SocialMatch(JSONObject json) {
        super(json);
        this.facebookId = json.optString(FIELD_FACEBOOK_ID);
        this.fromFacebookId = json.optString(FIELD_FROM_FACEBOOK_ID);
        this.fromGender = json.optString(FIELD_FROM_GENDER);
        this.fromFirstName = StringUtility.formatCharacterCase(json.optString(FIELD_FROM_FIRST_NAME));
        this.eventId = json.optString(FIELD_EVENT_ID);
        this.eventName = json.optString(FIELD_EVENT_NAME);
        this.type = json.optString(FIELD_TYPE);
        this.match = json.optBoolean(FIELD_MATCH);
        this.active = json.optBoolean(FIELD_ACTIVE);
    }

    public String getFacebookId() { return facebookId; }
    public String getFromFacebookId() { return fromFacebookId; }
    public String getFromGender() { return fromGender; }
    public String getFromFirstName() { return fromFirstName; }
    public String getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public String getType() { return type; }
    public boolean isMatch() { return match; }
    public boolean isActive() { return active; }

    public static class Type {
        public static final String APPROVED = "approved";
        public static final String VIEWED = "viewed";
        public static final String DENIED = "denied";

        public static boolean isInbound(SocialMatch value) { return APPROVED.equalsIgnoreCase(value.getType()); }
    }
}
