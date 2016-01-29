package com.android.jiggie.model;

import org.json.JSONObject;

import com.android.jiggie.component.StringUtility;

/**
 * Created by rangg on 17/11/2015.
 */
public class UserProfile extends Login {
    public static final String FIELD_FIRST_NAME = Common.FIELD_FIRST_NAME;
    public static final String FIELD_LAST_NAME = Common.FIELD_LAST_NAME;
    public static final String FIELD_IMAGE_URL = "profile_image_url";
    public static final String FIELD_DATE_CREATED = "created_at";
    public static final String FIELD_DATE_UPDATED = "updated_at";
    public static final String FIELD_LAST_LOGIN = "last_login";
    public static final String FIELD_FRIENDS = "friendslist";
    public static final String FIELD_VERIFIED = "is_verified_host";
    public static final String FIELD_ACTIVE = "active";
    public static final String FIELD_GENDER_INTEREST = "gender_interest";
    public static final String FIELD_BIRTH_DATE = "birth_date";
    public static final String FIELD_VISIBLE = "visible";
    public static final String FIELD_PHOTOS = "photos";

    private String imageUrl;
    private String dateCreated;
    private String dateUpdated;
    private String lastLogin;
    private String[] friends;
    private boolean verifiedHost;
    private boolean active;
    private String genderInterest;
    private String birthDate;
    private boolean visible;
    private String[] photos;

    public UserProfile() {
    }

    public UserProfile(JSONObject json) {
        super(json);
        super.setFirstName(json.optString(FIELD_FIRST_NAME));
        super.setLastName(json.optString(FIELD_LAST_NAME));
        this.imageUrl = json.optString(FIELD_IMAGE_URL);
        this.dateCreated = json.optString(FIELD_DATE_CREATED);
        this.dateUpdated = json.optString(FIELD_DATE_UPDATED);
        this.lastLogin = json.optString(FIELD_LAST_LOGIN);
        this.friends = StringUtility.toStringArray(json.optJSONArray(FIELD_FRIENDS));
        this.verifiedHost = json.optBoolean(FIELD_VERIFIED);
        this.active = json.optBoolean(FIELD_ACTIVE);
        this.genderInterest = json.optString(FIELD_GENDER_INTEREST);
        this.birthDate = json.optString(FIELD_BIRTH_DATE);
        this.visible = json.optBoolean(FIELD_VISIBLE);
        this.photos = StringUtility.toStringArray(json.optJSONArray(FIELD_PHOTOS));
    }

    public String[] getPhotos() { return this.photos; }
    public String getBirthDate() { return this.birthDate; }

    public void setPhotos(String[] photos) { this.photos = photos; }
}
