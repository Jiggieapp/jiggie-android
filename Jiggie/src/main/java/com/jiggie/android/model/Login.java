package com.jiggie.android.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.jiggie.android.App;
import com.jiggie.android.component.StringUtility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rangg on 02/11/2015.
 */
public class Login extends IdModel {
    public static final String FIELD_FACEBOOK_ID = Common.FIELD_FACEBOOK_ID;
    public static final String FIELD_APN_TOKEN = "apn_token";
    public static final String FIELD_USER_FIRST_NAME = "user_first_name";
    public static final String FIELD_USER_LAST_NAME = "user_last_name";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_BIRTHDAY = "birthday";
    public static final String FIELD_ABOUT = "about";

    private String facebookId;
    private String apnToken;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String location;
    private String birthday;
    private String about;

    public Login() { }
    public Login(SharedPreferences pref) {
        super(pref.getString(FIELD_ID, null));
        this.firstName = pref.getString(FIELD_USER_FIRST_NAME, null);
        this.lastName = pref.getString(FIELD_USER_LAST_NAME, null);
        this.apnToken = pref.getString(FIELD_APN_TOKEN, null);
        this.birthday = pref.getString(FIELD_BIRTHDAY, null);
        this.gender = pref.getString(FIELD_GENDER, null);
        this.about = pref.getString(FIELD_ABOUT, null);
        this.email = pref.getString(FIELD_EMAIL, null);
    }
    protected Login(JSONObject json) {
        this.facebookId = json.optString(FIELD_FACEBOOK_ID);
        this.setFirstName(json.optString(FIELD_USER_FIRST_NAME));
        this.setLastName(json.optString(FIELD_USER_LAST_NAME));
        this.apnToken = json.optString(FIELD_APN_TOKEN);
        this.gender = json.optString(FIELD_GENDER);
        this.email = json.optString(FIELD_EMAIL);
        this.location = json.optString(FIELD_LOCATION);
        this.birthday = json.optString(FIELD_BIRTHDAY);
        this.about = json.optString(FIELD_ABOUT);
    }

    @Override
    public JSONObject toJsonObject() throws JSONException {
        final JSONObject json = super.toJsonObject();
        json.put(FIELD_FACEBOOK_ID, this.facebookId);
        json.put(FIELD_APN_TOKEN, this.apnToken);
        json.put(FIELD_USER_FIRST_NAME, this.firstName);
        json.put(FIELD_USER_LAST_NAME, this.lastName);
        json.put(FIELD_GENDER, this.gender);
        json.put(FIELD_EMAIL, this.email);
        json.put(FIELD_LOCATION, this.location);
        json.put(FIELD_BIRTHDAY, this.birthday);
        json.put(FIELD_ABOUT, this.about);
        return json;
    }

    public void save(Context context) {
        final SharedPreferences.Editor editor = context.getSharedPreferences(Login.class.getName(), Context.MODE_PRIVATE).edit();
        editor.putString(FIELD_USER_FIRST_NAME, this.firstName);
        editor.putString(FIELD_USER_LAST_NAME, this.lastName);
        editor.putString(FIELD_APN_TOKEN, this.apnToken);
        editor.putString(FIELD_LOCATION, this.location);
        editor.putString(FIELD_BIRTHDAY, this.birthday);
        editor.putString(FIELD_GENDER, this.gender);
        editor.putString(FIELD_ABOUT, this.about);
        editor.putString(FIELD_EMAIL, this.email);
        editor.apply();
    }

    private static Login currentLogin;
    public static Login getCurrentLogin() {
        if (currentLogin == null) {
            final SharedPreferences pref = App.getInstance().getSharedPreferences(Login.class.getName(), Context.MODE_PRIVATE);
            currentLogin = pref.getString(FIELD_USER_FIRST_NAME, null) == null ? null : new Login(pref);
        }
        return currentLogin;
    }

    public void setFacebookId(String facebookId) { this.facebookId = facebookId; }
    public void setApnToken(String apnToken) { this.apnToken = apnToken; }
    public void setFirstName(String firstName) { this.firstName = StringUtility.formatCharacterCase(firstName); }
    public void setLastName(String lastName) { this.lastName = StringUtility.formatCharacterCase(lastName); }
    public void setGender(String gender) { this.gender = gender; }
    public void setEmail(String email) { this.email = email; }
    public void setLocation(String location) { this.location = location; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setAbout(String about) { this.about = about; }

    public String getEmail() { return this.email; }
    public String getAbout() { return this.about; }
    public String getBirthday() { return this.birthday; }
    public String getLocation() { return this.location; }
    public String getLastName() { return this.lastName; }
    public String getFirstName() { return this.firstName; }
    public String getFacebookId() { return this.facebookId; }
    public String getName() { return this.firstName + " " + this.lastName; }
}
