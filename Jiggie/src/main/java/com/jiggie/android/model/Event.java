package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jiggie.android.App;
import com.jiggie.android.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by rangg on 03/11/2015.
 */
public class Event extends IdModel implements Parcelable {
    public static final String FIELD_TAGS = "tags";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_VENUE_NAME = "venue_name";
    public static final String FIELD_START_DATETIME = "start_datetime";
    public static final String FIELD_END_DATETIME = "end_datetime";

    private String title;
    private String[] tags;
    private String imageUrl;
    private String venueName;
    private String startDateTime;
    private String endDateTime;
    private String simpleDate;

    public Event(String id, String title) {
        super(id);
        this.title = title;
    }

    public Event(JSONObject json) {
        super(json);
        this.title = json.optString(FIELD_TITLE);
        this.venueName = json.optString(FIELD_VENUE_NAME);
        this.startDateTime = json.optString(FIELD_START_DATETIME);
        this.endDateTime = json.optString(FIELD_END_DATETIME);

        final JSONArray tagJson= json.optJSONArray(FIELD_TAGS);
        final int tagCount = tagJson == null ? 0 : tagJson.length();
        final String[] tags = new String[tagCount];

        for (int i = 0; i < tagCount; i++)
            tags[i] = tagJson.optString(i);

        this.tags = tags;
    }

    protected Event(Parcel in) {
        super(in);
        this.title = in.readString();
        this.imageUrl = in.readString();
        this.tags = in.createStringArray();
        this.venueName = in.readString();
        this.startDateTime = in.readString();
        this.endDateTime = in.readString();
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getStartDateTime() { return this.startDateTime; }
    public String getEndDateTime() { return this.endDateTime ; }
    public String getVenueName() { return this.venueName; }
    public String getImageUrl() { return this.imageUrl; }
    public String getTitle() { return this.title; }
    public String[] getTags() { return this.tags; }
    public String getSimpleDate() throws ParseException {
        if (this.simpleDate == null) {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(this.startDateTime);
            final Date endDate = Common.ISO8601_DATE_FORMAT_UTC.parse(this.endDateTime);
            this.simpleDate = App.getInstance().getResources().getString(R.string.event_date_format, Common.SERVER_DATE_FORMAT_ALT.format(startDate), Common.SIMPLE_12_HOUR_FORMAT.format(endDate));
        }
        return this.simpleDate;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest);
        dest.writeString(this.title);
        dest.writeString(this.imageUrl);
        dest.writeStringArray(this.tags);
        dest.writeString(this.venueName);
        dest.writeString(this.startDateTime);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) { return new Event(in); }
        @Override
        public Event[] newArray(int size) { return new Event[size]; }
    };
}
