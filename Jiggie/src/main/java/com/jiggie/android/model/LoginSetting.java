package com.jiggie.android.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rangg on 15/01/2016.
 */
public class LoginSetting implements Model {
    public static final String FIELD_GENDER_INTEREST = Common.FIELD_GENDER_INTEREST;
    public static final String FIELD_FACEBOOK_ID = Common.FIELD_FACEBOOK_ID;
    public static final String FIELD_ACCOUNT_TYPE = Common.FIELD_ACCOUNT_TYPE;
    public static final String FIELD_GENDER = Common.FIELD_GENDER;
    public static final String FIELD_LOCATION = Common.FIELD_LOCATION;
    public static final String FIELD_CHAT = Common.FIELD_CHAT;
    public static final String FIELD_FEED = Common.FIELD_FEED;
    public static final String FIELD_EXPERIENCES = "experiences";

    private String experiences;
    private String facebookId;
    private String genderInterest;
    private String gender;
    private String accountType;
    private boolean location;
    private boolean chat;
    private boolean feed;

    @Override
    public JSONObject toJsonObject() throws JSONException {
        final JSONObject json = new JSONObject();
        json.put(FIELD_GENDER_INTEREST, this.genderInterest);
        json.put(FIELD_FACEBOOK_ID, this.facebookId);
        json.put(FIELD_GENDER, this.gender);
        json.put(FIELD_ACCOUNT_TYPE, this.accountType);
        json.put(FIELD_LOCATION, this.location ? 1 : 0);
        json.put(FIELD_CHAT, this.chat ? 1 : 0);
        json.put(FIELD_FEED, this.feed ? 1 : 0);
        json.put(FIELD_EXPERIENCES, this.experiences);
        return json;
    }

    public void setGenderInterest(String genderInterest) { this.genderInterest = genderInterest; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public void setFacebookId(String facebookId) { this.facebookId = facebookId; }
    public void setLocation(boolean location) { this.location = location; }
    public void setExperiences(String value) { this.experiences = value; }
    public void setGender(String gender) { this.gender = gender; }
    public void setChat(boolean chat) { this.chat = chat; }
    public void setFeed(boolean feed) { this.feed = feed; }
}
