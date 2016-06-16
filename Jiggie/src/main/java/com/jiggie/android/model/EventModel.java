package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by LTE on 2/1/2016.
 */
public class EventModel {
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        ArrayList<Events> events;

        public ArrayList<Events> getEvents() {
            return events;
        }

        public void setEvents(ArrayList<Events> events) {
            this.events = events;
        }

        ArrayList<Theme> themes;

        public ArrayList<Theme> getThemes() {
            return themes;
        }

        public void setThemes(ArrayList<Theme> themes) {
            this.themes = themes;
        }

        public static final class Theme {
            public String _id;
            public String name;
            public String desc;
            public String image;
            public String day;
            public String status;

            public Theme(String _id, String name, String desc, String image, String day, String status){
                this._id = _id;
                this.name = name;
                this.desc = desc;
                this.image = image;
                this.day = day;
                this.status = status;
            }
        }

        public static class Events implements Parcelable {
            String _id;
            int rank;
            String title;
            String venue_name;
            String start_datetime;
            String end_datetime;
            String special_type;
            ArrayList<String> tags;
            int likes;
            String date_day;
            String description;
            ArrayList<String> photos;
            Integer lowest_price;
            String fullfillment_type;
            String tz;
            boolean isEvent;

            public String getTz() {
                return tz;
            }

            public void setTz(String tz) {
                this.tz = tz;
            }

            public Integer getLowest_price() {
                return lowest_price;
            }

            public void setLowest_price(Integer lowest_price) {
                this.lowest_price = lowest_price;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            protected Events(Parcel in) {
                //super(in);
                this._id = in.readString();
                this.rank = in.readInt();
                this.title = in.readString();
                this.venue_name = in.readString();
                this.start_datetime = in.readString();
                this.end_datetime = in.readString();
                this.special_type = in.readString();
                this.tags = in.readArrayList(null);
                this.likes = in.readInt();
                this.date_day = in.readString();
                this.photos = in.readArrayList(null);
                this.description = in.readString();
                this.lowest_price = in.readInt();
                this.fullfillment_type = in.readString();
            }

            public Events(Theme theme)
            {
                this._id = theme._id;
                //this.rank = in.readInt();
                this.title = theme.name;
                this.venue_name = "";
                //this.start_datetime = in.readString();
                //this.end_datetime = in.readString();
                //this.special_type = in.readString();
                this.tags = new ArrayList<>();
                //this.likes = in.readInt();
                //this.date_day = in.readString();
                //this.photos = in.readArrayList(null);
                this.description = theme.desc;
                this.photos = new ArrayList<>();
                photos.add(theme.image);
                this.lowest_price = 0;
                //this.fullfillment_type = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public String getFullfillment_type() {
                return fullfillment_type;
            }

            public void setFullfillment_type(String fullfillment_type) {
                this.fullfillment_type = fullfillment_type;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                //super.writeToParcel(dest);
                dest.writeString(this._id);
                dest.writeInt(rank);
                dest.writeString(this.title);
                dest.writeString(this.venue_name);
                dest.writeString(this.start_datetime);
                dest.writeString(this.end_datetime);
                dest.writeString(this.special_type);
                dest.writeList(this.tags);
                dest.writeInt(this.likes);
                dest.writeString(this.date_day);
                dest.writeList(this.photos);
                dest.writeString(this.description);
                dest.writeInt(this.lowest_price);
                dest.writeString(this.fullfillment_type);
            }

            public static final Creator<Events> CREATOR = new Creator<Events>() {
                @Override
                public Events createFromParcel(Parcel in) {
                    return new Events(in);
                }

                @Override
                public Events[] newArray(int size) {
                    return new Events[size];
                }
            };

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVenue_name() {
                return venue_name;
            }

            public void setVenue_name(String venue_name) {
                this.venue_name = venue_name;
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

            public String getSpecial_type() {
                return special_type;
            }

            public void setSpecial_type(String special_type) {
                this.special_type = special_type;
            }

            public ArrayList<String> getTags() {
                return tags;
            }

            public void setTags(ArrayList<String> tags) {
                this.tags = tags;
            }

            public int getLikes() {
                return likes;
            }

            public void setLikes(int likes) {
                this.likes = likes;
            }

            public String getDate_day() {
                return date_day;
            }

            public void setDate_day(String date_day) {
                this.date_day = date_day;
            }

            public ArrayList<String> getPhotos() {
                return photos;
            }

            public void setPhotos(ArrayList<String> photos) {
                this.photos = photos;
            }
        }


    }

}
