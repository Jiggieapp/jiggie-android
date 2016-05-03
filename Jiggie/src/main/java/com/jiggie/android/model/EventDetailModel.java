package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LTE on 2/3/2016.
 */
public class EventDetailModel {

    int response;
    String msg;
    Data data;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public static class Data{
        EventDetail event_detail;

        public EventDetail getEvents_detail() {
            return event_detail;
        }

        public void setEvent_detail(EventDetail events_detail) {
            this.event_detail = events_detail;
        }

        public static class EventDetail implements Parcelable {

            String _id;
            String event_id;
            String start_datetime;
            String end_datetime;
            String venue_id;
            String venue_name;
            String start_datetime_str;
            String end_datetime_str;
            String fullfillment_type;
            String fullfillment_value;
            ArrayList<String> photos;

            String description;
            String title;
            ArrayList<String> tags;

            ArrayList<GuestViewed> guests_viewed;
            Venue venue;

            boolean is_liked;
            int likes;

            public EventDetail(String _id, String event_id, String start_datetime, String end_datetime, String venue_id, String venue_name, String start_datetime_str, String end_datetime_str,
                               String fullfillment_type, String fullfillment_value, ArrayList<String> photos, String description, String title, ArrayList<String> tags,
                               ArrayList<GuestViewed> guests_viewed, Venue venue, boolean is_liked, int likes){
                this._id = _id;
                this.event_id = event_id;
                this.start_datetime = start_datetime;
                this.end_datetime = end_datetime;
                this.venue_id = venue_id;
                this.venue_name = venue_name;
                this.start_datetime_str = start_datetime_str;
                this.end_datetime_str = end_datetime_str;
                this.fullfillment_type = fullfillment_type;
                this.fullfillment_value = fullfillment_value;
                this.photos = photos;
                this.description = description;
                this.title = title;
                this.tags = tags;
                this.guests_viewed = guests_viewed;
                this.venue = venue;
                this.is_liked = is_liked;
                this.likes = likes;
            }

            public String get_id() {
                return _id;
            }

            public String getEvent_id() {
                return event_id;
            }

            public String getStart_datetime() {
                return start_datetime;
            }

            public String getEnd_datetime() {
                return end_datetime;
            }

            public String getVenue_id() {
                return venue_id;
            }

            public String getVenue_name() {
                return venue_name;
            }

            public String getStart_datetime_str() {
                return start_datetime_str;
            }

            public String getEnd_datetime_str() {
                return end_datetime_str;
            }

            public String getFullfillment_type() {
                return fullfillment_type;
            }

            public String getFullfillment_value() {
                return fullfillment_value;
            }

            public ArrayList<String> getPhotos() {
                return photos;
            }

            public String getDescription() {
                return description;
            }

            public String getTitle() {
                return title;
            }

            public ArrayList<String> getTags() {
                return tags;
            }

            public ArrayList<GuestViewed> getGuests_viewed() {
                return guests_viewed;
            }

            public Venue getVenue() {
                return venue;
            }

            public boolean is_liked() {
                return is_liked;
            }

            public int getLikes() {
                return likes;
            }

            protected EventDetail(Parcel in) {
                _id = in.readString();
                event_id = in.readString();
                start_datetime = in.readString();
                end_datetime = in.readString();
                venue_id = in.readString();
                venue_name = in.readString();
                start_datetime_str = in.readString();
                end_datetime_str = in.readString();
                fullfillment_type = in.readString();
                fullfillment_value = in.readString();
                if (in.readByte() == 0x01) {
                    photos = new ArrayList<String>();
                    in.readList(photos, String.class.getClassLoader());
                } else {
                    photos = null;
                }
                description = in.readString();
                title = in.readString();
                if (in.readByte() == 0x01) {
                    tags = new ArrayList<String>();
                    in.readList(tags, String.class.getClassLoader());
                } else {
                    tags = null;
                }
                if (in.readByte() == 0x01) {
                    guests_viewed = new ArrayList<GuestViewed>();
                    in.readList(guests_viewed, GuestViewed.class.getClassLoader());
                } else {
                    guests_viewed = null;
                }
                venue = (Venue) in.readValue(Venue.class.getClassLoader());
                is_liked = in.readByte() != 0x00;
                likes = in.readInt();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(_id);
                dest.writeString(event_id);
                dest.writeString(start_datetime);
                dest.writeString(end_datetime);
                dest.writeString(venue_id);
                dest.writeString(venue_name);
                dest.writeString(start_datetime_str);
                dest.writeString(end_datetime_str);
                dest.writeString(fullfillment_type);
                dest.writeString(fullfillment_value);
                if (photos == null) {
                    dest.writeByte((byte) (0x00));
                } else {
                    dest.writeByte((byte) (0x01));
                    dest.writeList(photos);
                }
                dest.writeString(description);
                dest.writeString(title);
                if (tags == null) {
                    dest.writeByte((byte) (0x00));
                } else {
                    dest.writeByte((byte) (0x01));
                    dest.writeList(tags);
                }
                if (guests_viewed == null) {
                    dest.writeByte((byte) (0x00));
                } else {
                    dest.writeByte((byte) (0x01));
                    dest.writeList(guests_viewed);
                }
                dest.writeValue(venue);
                dest.writeByte((byte) (is_liked ? 0x01 : 0x00));
                dest.writeInt(likes);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<EventDetail> CREATOR = new Parcelable.Creator<EventDetail>() {
                @Override
                public EventDetail createFromParcel(Parcel in) {
                    return new EventDetail(in);
                }

                @Override
                public EventDetail[] newArray(int size) {
                    return new EventDetail[size];
                }
            };

            public static class GuestViewed implements Parcelable {

                String fb_id;
                String first_name;
                String gender;

                public GuestViewed(String fb_id, String first_name, String gender){
                    this.fb_id = fb_id;
                    this.first_name = first_name;
                    this.gender = gender;
                }

                public String getFb_id() {
                    return fb_id;
                }

                public String getFirst_name() {
                    return first_name;
                }

                public String getGender() {
                    return gender;
                }

                protected GuestViewed(Parcel in) {
                    fb_id = in.readString();
                    first_name = in.readString();
                    gender = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(fb_id);
                    dest.writeString(first_name);
                    dest.writeString(gender);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<GuestViewed> CREATOR = new Parcelable.Creator<GuestViewed>() {
                    @Override
                    public GuestViewed createFromParcel(Parcel in) {
                        return new GuestViewed(in);
                    }

                    @Override
                    public GuestViewed[] newArray(int size) {
                        return new GuestViewed[size];
                    }
                };
            }

            public static class Venue implements Parcelable {

                String _id;
                String address;
                String neighborhood;
                String city;
                String description;

                @SerializedName("long")
                String lon;

                String lat;
                String zip;
                String name;
                ArrayList<String> photos;

                public Venue(String _id, String address, String neighborhood, String city, String description, String lon, String lat, String zip, String name, ArrayList<String> photos){
                    this._id = _id;
                    this.address = address;
                    this.neighborhood = neighborhood;
                    this.city = city;
                    this.description = description;
                    this.lon = lon;
                    this.lat = lat;
                    this.zip = zip;
                    this.name = name;
                    this.photos = photos;
                }

                public String get_id() {
                    return _id;
                }

                public String getAddress() {
                    return address;
                }

                public String getNeighborhood() {
                    return neighborhood;
                }

                public String getCity() {
                    return city;
                }

                public String getDescription() {
                    return description;
                }

                public String getLon() {
                    return lon;
                }

                public String getLat() {
                    return lat;
                }

                public String getZip() {
                    return zip;
                }

                public String getName() {
                    return name;
                }

                public ArrayList<String> getPhotos() {
                    return photos;
                }

                protected Venue(Parcel in) {
                    _id = in.readString();
                    address = in.readString();
                    neighborhood = in.readString();
                    city = in.readString();
                    description = in.readString();
                    lon = in.readString();
                    lat = in.readString();
                    zip = in.readString();
                    name = in.readString();
                    if (in.readByte() == 0x01) {
                        photos = new ArrayList<String>();
                        in.readList(photos, String.class.getClassLoader());
                    } else {
                        photos = null;
                    }
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(_id);
                    dest.writeString(address);
                    dest.writeString(neighborhood);
                    dest.writeString(city);
                    dest.writeString(description);
                    dest.writeString(lon);
                    dest.writeString(lat);
                    dest.writeString(zip);
                    dest.writeString(name);
                    if (photos == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(photos);
                    }
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<Venue> CREATOR = new Parcelable.Creator<Venue>() {
                    @Override
                    public Venue createFromParcel(Parcel in) {
                        return new Venue(in);
                    }

                    @Override
                    public Venue[] newArray(int size) {
                        return new Venue[size];
                    }
                };
            }
        }

    }

}
