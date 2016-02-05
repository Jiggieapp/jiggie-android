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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this._id);
                dest.writeString(this.event_id);
                dest.writeString(this.start_datetime);
                dest.writeString(this.end_datetime);
                dest.writeString(this.venue_id);
                dest.writeString(this.venue_name);
                dest.writeString(this.start_datetime_str);
                dest.writeString(this.end_datetime_str);
                dest.writeString(this.fullfillment_type);
                dest.writeString(this.fullfillment_value);
                dest.writeStringList(this.photos);
                dest.writeString(this.description);
                dest.writeString(this.title);
                dest.writeStringList(this.tags);
                //dest.writeList(this.guests_viewed);
                dest.writeParcelable(this.venue, 0);
            }

            protected EventDetail(Parcel in) {
                this._id = in.readString();
                this.event_id = in.readString();
                this.start_datetime = in.readString();
                this.end_datetime = in.readString();
                this.venue_id = in.readString();
                this.venue_name = in.readString();
                this.start_datetime_str = in.readString();
                this.end_datetime_str = in.readString();
                this.fullfillment_type = in.readString();
                this.fullfillment_value = in.readString();
                this.photos = in.createStringArrayList();
                this.description = in.readString();
                this.title = in.readString();
                this.tags = in.createStringArrayList();
                /*this.guests_viewed = new ArrayList<GuestViewed>();
                in.readList(this.guests_viewed, List.class.getClassLoader());*/
                this.venue = in.readParcelable(Venue.class.getClassLoader());
            }

            public static final Creator<EventDetail> CREATOR = new Creator<EventDetail>() {
                public EventDetail createFromParcel(Parcel source) {
                    return new EventDetail(source);
                }

                public EventDetail[] newArray(int size) {
                    return new EventDetail[size];
                }
            };

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getEvent_id() {
                return event_id;
            }

            public void setEvent_id(String event_id) {
                this.event_id = event_id;
            }

            public String getStart_datetime() {
                return start_datetime;
            }

            public void setStart_datetime(String start_datetime) {
                this.start_datetime = start_datetime;
            }

            public String getEnd_datetime() {
                return end_datetime;
            }

            public void setEnd_datetime(String end_datetime) {
                this.end_datetime = end_datetime;
            }

            public String getVenue_id() {
                return venue_id;
            }

            public void setVenue_id(String venue_id) {
                this.venue_id = venue_id;
            }

            public String getVenue_name() {
                return venue_name;
            }

            public void setVenue_name(String venue_name) {
                this.venue_name = venue_name;
            }

            public String getStart_datetime_str() {
                return start_datetime_str;
            }

            public void setStart_datetime_str(String start_datetime_str) {
                this.start_datetime_str = start_datetime_str;
            }

            public String getEnd_datetime_str() {
                return end_datetime_str;
            }

            public void setEnd_datetime_str(String end_datetime_str) {
                this.end_datetime_str = end_datetime_str;
            }

            public String getFullfillment_type() {
                return fullfillment_type;
            }

            public void setFullfillment_type(String fullfillment_type) {
                this.fullfillment_type = fullfillment_type;
            }

            public String getFullfillment_value() {
                return fullfillment_value;
            }

            public void setFullfillment_value(String fullfillment_value) {
                this.fullfillment_value = fullfillment_value;
            }

            public ArrayList<String> getPhotos() {
                return photos;
            }

            public void setPhotos(ArrayList<String> photos) {
                this.photos = photos;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public ArrayList<String> getTags() {
                return tags;
            }

            public void setTags(ArrayList<String> tags) {
                this.tags = tags;
            }

            public ArrayList<GuestViewed> getGuests_viewed() {
                return guests_viewed;
            }

            public void setGuests_viewed(ArrayList<GuestViewed> guests_viewed) {
                this.guests_viewed = guests_viewed;
            }

            public Venue getVenue() {
                return venue;
            }

            public void setVenue(Venue venue) {
                this.venue = venue;
            }

            public static class GuestViewed{

                String fb_id;
                String first_name;
                String gender;


                public String getFb_id() {
                    return fb_id;
                }

                public void setFb_id(String fb_id) {
                    this.fb_id = fb_id;
                }

                public String getFirst_name() {
                    return first_name;
                }

                public void setFirst_name(String first_name) {
                    this.first_name = first_name;
                }

                public String getGender() {
                    return gender;
                }

                public void setGender(String gender) {
                    this.gender = gender;
                }
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

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getNeighborhood() {
                    return neighborhood;
                }

                public void setNeighborhood(String neighborhood) {
                    this.neighborhood = neighborhood;
                }

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getLon() {
                    return lon;
                }

                public void setLon(String lon) {
                    this.lon = lon;
                }

                public String getLat() {
                    return lat;
                }

                public void setLat(String lat) {
                    this.lat = lat;
                }

                public String getZip() {
                    return zip;
                }

                public void setZip(String zip) {
                    this.zip = zip;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public ArrayList<String> getPhotos() {
                    return photos;
                }

                public void setPhotos(ArrayList<String> photos) {
                    this.photos = photos;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this._id);
                    dest.writeString(this.address);
                    dest.writeString(this.neighborhood);
                    dest.writeString(this.city);
                    dest.writeString(this.description);
                    dest.writeString(this.lon);
                    dest.writeString(this.lat);
                    dest.writeString(this.zip);
                    dest.writeString(this.name);
                    dest.writeStringList(this.photos);
                }

                protected Venue(Parcel in) {
                    this._id = in.readString();
                    this.address = in.readString();
                    this.neighborhood = in.readString();
                    this.city = in.readString();
                    this.description = in.readString();
                    this.lon = in.readString();
                    this.lat = in.readString();
                    this.zip = in.readString();
                    this.name = in.readString();
                    this.photos = in.createStringArrayList();
                }

                public static final Creator<Venue> CREATOR = new Creator<Venue>() {
                    public Venue createFromParcel(Parcel source) {
                        return new Venue(source);
                    }

                    public Venue[] newArray(int size) {
                        return new Venue[size];
                    }
                };
            }


        }

    }

}
