package com.jiggie.android.model;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wandywijayanto on 6/17/16.
 */
public final class ThemeResultModel {
    public final long response;
    public final String msg;
    public final Data data;

    public ThemeResultModel(long response, String msg, Data data) {
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static final class Data {
        public final Themes themes;
        public final ArrayList<EventModel.Data.Events> events;

        public Data(Themes themes, ArrayList<EventModel.Data.Events> events) {
            this.themes = themes;
            this.events = events;
        }

        public static final class Themes {
            public final String _id;
            public final String name;
            public final String desc;
            public final String image;
            public final String day;

            public Themes(String _id, String name, String desc, String image, String day) {
                this._id = _id;
                this.name = name;
                this.desc = desc;
                this.image = image;
                this.day = day;
            }
        }

        /*public static class Event {
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
            //ArrayList<String> themes_id;
            public boolean isEvent;
        }*/
    }
}
