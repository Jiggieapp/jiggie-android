package com.android.jiggie.model;

import android.os.Parcel;

import com.android.jiggie.component.StringUtility;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by rangg on 11/11/2015.
 */
public class EventDetail extends Event {
    public static final String FIELD_FULLFILLMENT_VALUE = "fullfillment_value";
    public static final String FIELD_FULLFILLMENT_TYPE = "fullfillment_type";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_GUESTS = "guests_viewed";
    public static final String FIELD_PHOTOS = "photos";
    public static final String FIELD_VENUE = "venue";

    private String fullfillmentValue;
    private String fullfillmentType;
    private String description;
    private Guest[] guests;
    private String[] photos;
    private Venue venue;

    public EventDetail(JSONObject json) {
        super(json);
        this.description = json.optString(FIELD_DESCRIPTION);
        this.venue = new Venue(json.optJSONObject(FIELD_VENUE));
        this.fullfillmentType = json.optString(FIELD_FULLFILLMENT_TYPE);
        this.fullfillmentValue = json.optString(FIELD_FULLFILLMENT_VALUE);

        final JSONArray guestArray = json.optJSONArray(FIELD_GUESTS);
        final int length = guestArray == null ? 0 : guestArray.length();
        final Guest[] guests = new Guest[length];

        for (int i = 0; i < length; i++)
            guests[i] = new Guest(guestArray.optJSONObject(i));

        this.guests = guests;
        this.photos = StringUtility.toStringArray(json.optJSONArray(FIELD_PHOTOS));
    }

    private EventDetail(Parcel parcel) { super(parcel); }

    public String getFieldFullfillmentValue() { return this.fullfillmentValue; }
    public String getFullfillmentType() { return this.fullfillmentType; }
    public String getDescrption() { return this.description; }
    public String[] getPhotos() { return this.photos; }
    public Guest[] getGuests() { return this.guests; }
    public Venue getVenue() { return this.venue; }

    public static final Creator<EventDetail> CREATOR = new Creator<EventDetail>() {
        @Override
        public EventDetail createFromParcel(Parcel in) { return new EventDetail(in); }
        @Override
        public EventDetail[] newArray(int size) { return new EventDetail[size]; }
    };

    public static class FullfillmentTypes {
        public static final String PHONE_NUMBER = "phone_number";
        public static final String RESERVATION = "reservation";
        public static final String PURCHASE = "purchase";
        public static final String LINK = "link";
        public static final String NONE = "none";
    }
}
