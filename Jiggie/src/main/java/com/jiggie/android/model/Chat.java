package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by rangg on 21/12/2015.
 */
public class Chat implements Model, Parcelable, BaseColumns {
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_IS_FROM_YOU = "isFromYou";
    public static final String FIELD_FROM_NAME = "fromName";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_FROM_ID = "fromId";
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_TOID = "toId";

    private long _id;
    private boolean fromYou;
    private String createdAt;
    private String fromName;
    private String fromId;
    private String header;
    private String message;
    private String toId;
    private String simpleDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public Chat() { }
    public Chat(JSONObject json) {
        this.header = json.optString(FIELD_HEADER);
        this.fromId = json.optString(FIELD_FROM_ID);
        this.message = json.optString(FIELD_MESSAGE);
        this.fromYou = json.optBoolean(FIELD_IS_FROM_YOU);
        this.createdAt = json.optString(FIELD_CREATED_AT);
    }

    public Chat(ChatConversationModel.Data.ChatConversations.Messages data, String fromId) {
        /*this.header = data.getHeader();
        this.fromId = fromId;
        this.message = data.getMessage();
        this.fromYou = data.isFromYou();
        this.createdAt = data.getCreated_at();*/

        setData(data, fromId, "");
    }

    public Chat(ChatConversationModel.Data.ChatConversations.Messages data
            , String fromId, String title) {
        /*this.header = data.getHeader();
        this.fromId = fromId;
        this.message = data.getMessage();
        this.fromYou = data.isFromYou();
        this.createdAt = data.getCreated_at();
        this.title = title;*/
        setData(data, fromId, title);
    }

    private void setData(ChatConversationModel.Data.ChatConversations.Messages data
            , String fromId, String title)
    {
        if(data != null)
        {
            this.message = data.getMessage();
            this.fromYou = data.isFromYou();
            this.header = data.getHeader();
            this.createdAt = data.getCreated_at();
        }

        this.fromId = fromId;
        this.title = title;
    }

    protected Chat(Parcel in) {
        this.fromYou = in.readByte() != 0;
        this.createdAt = in.readString();
        this.fromName = in.readString();
        this.fromId = in.readString();
        this.header = in.readString();
        this.message = in.readString();
        this.toId = in.readString();
        this._id = in.readLong();
    }

    public long getRowId() { return this._id; }
    public String getToId() { return this.toId; }
    public String getFromId() { return this.fromId; }
    public boolean isFromYou() { return this.fromYou; }
    public String getMessage() { return this.message; }
    public String getFromName() { return this.fromName; }
    public String getCreatedAt() { return this.createdAt; }
    public String getSimpleDate() throws ParseException {
        if (this.simpleDate == null) {
            final Date date = Common.ISO8601_DATE_FORMAT_UTC.parse(this.createdAt);
            this.simpleDate = Common.SIMPLE_12_HOUR_FORMAT.format(date);
        }
        return this.simpleDate;
    }

    public void setRowId(long id) { this._id = id; }
    public void setToId(String toId) { this.toId = toId; }
    public void setFromId(String fromId) { this.fromId = fromId; }
    public void setFromYou(boolean value) { this.fromYou = value; }
    public void setMessage(String message) { this.message = message; }
    public void setFromName(String fromName) { this.fromName = fromName; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public JSONObject toJsonObject() throws JSONException {
        final JSONObject json = new JSONObject();
        json.put(FIELD_FROM_NAME, this.fromName);
        json.put(FIELD_MESSAGE, this.message);
        json.put(FIELD_FROM_ID, this.fromId);
        json.put(FIELD_HEADER, this.header);
        json.put(FIELD_TOID, this.toId);
        json.put("hosting_id", "");
        json.put("header", "");
        return json;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (this.fromYou ? 1 : 0));
        dest.writeString(this.createdAt);
        dest.writeString(this.fromName);
        dest.writeString(this.fromId);
        dest.writeString(this.header);
        dest.writeString(this.message);
        dest.writeString(this.toId);
        dest.writeLong(this._id);
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) { return new Chat(in); }
        @Override
        public Chat[] newArray(int size) { return new Chat[size]; }
    };
}
