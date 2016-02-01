package com.jiggie.android.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.jiggie.android.App;
import com.jiggie.android.component.StringUtility;
import com.facebook.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rangg on 18/11/2015.
 */
public class Setting extends IdModel {
    private static final String[] GENDERS = new String[] { "male", "female" };
    private static final String[] GENDER_INTERESTS = new String[] { "both", "male", "female" };

    public static final String FIELD_GENDER_INTEREST = Common.FIELD_GENDER_INTEREST;
    public static final String FIELD_ACCOUNT_TYPE = Common.FIELD_ACCOUNT_TYPE;
    public static final String FIELD_GENDER = Common.FIELD_GENDER;
    public static final String FIELD_NOTIFICATION = "notifications";
    public static final String FIELD_MATCH_ME = "matchme";
    public static final String FIELD_PHONE = "phone";

    private int genderInterest;
    private int gender;

    private Notification notification;
    private String accountType;
    private String phone;
    private boolean matchMe;
    private String facebookId;

    private static Setting current;

    public static Setting getCurrentSetting() {
        return current != null ? current : (current = new Setting(App.getInstance().getSharedPreferences(Setting.class.getName(), Context.MODE_PRIVATE)));
    }

    private Setting() { }

    private Setting(SharedPreferences pref) {
        this.notification = new Notification(pref);
        this.gender = pref.getInt(FIELD_GENDER, 0);
        this.phone = pref.getString(FIELD_PHONE, null);
        this.matchMe = pref.getBoolean(FIELD_MATCH_ME, true);
        this.accountType = pref.getString(FIELD_ACCOUNT_TYPE, null);
        this.genderInterest = pref.getInt(FIELD_GENDER_INTEREST, 0);
        this.facebookId = AccessToken.getCurrentAccessToken().getUserId();
    }

    public Setting(JSONObject json) {
        this.phone = json.optString(FIELD_PHONE);
        this.accountType = json.optString(FIELD_ACCOUNT_TYPE);
        this.notification = new Notification(json.optJSONObject(FIELD_NOTIFICATION));
        this.gender = StringUtility.indexOf(GENDERS, json.optString(FIELD_GENDER), true);
        this.genderInterest = StringUtility.indexOf(GENDER_INTERESTS, json.optString(FIELD_GENDER_INTEREST), true);
        this.matchMe = json.optBoolean(FIELD_MATCH_ME, true);
        this.facebookId = getCurrentSetting().getFacebookId();

        this.gender = this.gender < 0 ? 0 : this.gender;
        this.genderInterest = this.genderInterest < 0 ? 0 : this.genderInterest;
    }

    public void save() {
        this.notification.save(App.getInstance().getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE).edit()
                .putInt(FIELD_GENDER, this.gender)
                .putInt(FIELD_GENDER_INTEREST, this.genderInterest)
                .putString(FIELD_PHONE, this.phone)
                .putString(FIELD_ACCOUNT_TYPE, this.accountType)
                .putBoolean(FIELD_MATCH_ME, this.matchMe)
        );
        current = this;
    }

    public int getGender() { return this.gender; }
    public int getGenderInterest() { return this.genderInterest; }
    public boolean isMatchMe() { return this.matchMe; }
    public String getFacebookId() { return this.facebookId; }
    public String getAccountType() { return this.accountType; }

    public String getGenderString() { return GENDERS[this.gender]; }
    public String getGenderInterestString() { return GENDER_INTERESTS[this.genderInterest]; }

    public Notification getNotification() { return this.notification; }

    public void setGender(int gender) { this.gender = gender; }
    public void setMatchMe(boolean matchMe) { this.matchMe = matchMe; }
    public void setGenderInterest(int genderInterest) { this.genderInterest = genderInterest; }

    public Setting duplicate() {
        final Setting setting = new Setting();
        setting.notification = new Notification(this.notification.isFeed(), this.notification.isChat());
        setting.genderInterest = this.genderInterest;
        setting.accountType = this.accountType;
        setting.facebookId = this.facebookId;
        setting.matchMe = this.matchMe;
        setting.gender = this.gender;
        setting.phone = this.phone;
        return setting;
    }

    @Override
    public JSONObject toJsonObject() throws JSONException {
        final JSONObject json = new JSONObject();
        json.put(Notification.FIELD_FEED, this.notification.feed);
        json.put(Notification.FIELD_CHAT, this.notification.chat);
        json.put(FIELD_GENDER_INTEREST, this.getGenderInterestString());
        json.put(FIELD_GENDER, this.getGenderString());
        json.put(Common.FIELD_FACEBOOK_ID, this.facebookId);
        json.put(FIELD_ACCOUNT_TYPE, this.accountType);
        json.put(FIELD_PHONE, this.phone);
        return json;
    }

    public static class Notification implements Model {
        public static final String FIELD_FEED = Common.FIELD_FEED;
        public static final String FIELD_CHAT = Common.FIELD_CHAT;

        private boolean feed;
        private boolean chat;

        private Notification(JSONObject json) {
            this.feed = json.optBoolean(FIELD_FEED);
            this.chat = json.optBoolean(FIELD_CHAT);
        }

        private Notification(SharedPreferences pref) {
            this.feed = pref.getBoolean(FIELD_FEED, true);
            this.chat = pref.getBoolean(FIELD_CHAT, true);
        }

        private Notification(boolean feed, boolean chat) {
            this.feed = feed;
            this.chat = chat;
        }

        public boolean isFeed() { return feed; }
        public boolean isChat() { return chat; }

        public void setFeed(boolean feed) { this.feed = feed; }
        public void setChat(boolean chat) { this.chat = chat; }

        public void save(SharedPreferences.Editor editor) {
            editor.putBoolean(FIELD_FEED, this.feed);
            editor.putBoolean(FIELD_CHAT, this.chat);
            editor.apply();
        }

        @Override
        public JSONObject toJsonObject() throws JSONException {
            final JSONObject json = new JSONObject();
            json.put(FIELD_CHAT, this.chat);
            json.put(FIELD_FEED, this.feed);
            return json;
        }
    }
}
