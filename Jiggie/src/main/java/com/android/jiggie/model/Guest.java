package com.android.jiggie.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.android.jiggie.component.StringUtility;

import org.json.JSONObject;

/**
 * Created by rangg on 11/11/2015.
 */
public class Guest implements Parcelable {
    public static final String FIELD_FACEBOOK_ID = Common.FIELD_FACEBOOK_ID;
    public static final String FIELD_FIRST_NAME = Common.FIELD_FIRST_NAME;
    public static final String FIELD_LAST_NAME = Common.FIELD_LAST_NAME;
    public static final String FIELD_CONNECTED = "is_connected";
    public static final String FIELD_INVITED = "is_invited";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_ABOUT = "about";

    private String facebookId;
    private String firstName;
    private String lastName;
    private String gender;
    private String about;
    private boolean connected;
    private boolean invited;

    public Guest(String facebookId, String firstName) {
        this.facebookId = facebookId;
        this.firstName = firstName;
        this.lastName = "";
    }

    public Guest(JSONObject json) {
        this.facebookId = json.optString(FIELD_FACEBOOK_ID);
        this.firstName = StringUtility.formatCharacterCase(json.optString(FIELD_FIRST_NAME));
        this.lastName = StringUtility.formatCharacterCase(json.optString(FIELD_LAST_NAME));
        this.connected = json.optBoolean(FIELD_CONNECTED);
        this.invited = json.optBoolean(FIELD_INVITED);
        this.gender = json.optString(FIELD_GENDER);
        this.about = json.optString(FIELD_ABOUT);
    }

    protected Guest(Parcel in) {
        this.connected = in.readByte() != 0;
        this.invited = in.readByte() != 0;
        this.facebookId = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.gender = in.readString();
        this.about = in.readString();
    }

    public static final Creator<Guest> CREATOR = new Creator<Guest>() {
        @Override
        public Guest createFromParcel(Parcel in) { return new Guest(in); }
        @Override
        public Guest[] newArray(int size) { return new Guest[size]; }
    };

    public String getLastName() { return this.lastName; }
    public String getFacebookId() { return this.facebookId; }
    public String getFirstName() { return this.firstName; }
    public boolean isConnected() { return this.connected; }
    public boolean isInvited() { return this.invited; }
    public String getGender() { return this.gender; }
    public String getAbout() { return this.about; }

    public void setConnected(boolean connected) { this.connected = connected; }
    public void setInvited(boolean invited) { this.invited = invited; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (connected ? 1 : 0));
        dest.writeByte((byte) (invited ? 1 : 0));
        dest.writeString(facebookId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(gender);
        dest.writeString(about);
    }

    public String getName() { return String.format("%s %s", this.firstName, this.lastName); }
}
