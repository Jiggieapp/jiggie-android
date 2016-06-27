package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/5/2016.
 */
public class SocialModel {

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

        ArrayList<SocialFeeds> social_feeds;

        public ArrayList<SocialFeeds> getSocial_feeds() {
            return social_feeds;
        }

        public void setSocial_feeds(ArrayList<SocialFeeds> social_feeds) {
            this.social_feeds = social_feeds;
        }

        public static class SocialFeeds{
            String fb_id;
            String from_fb_id;
            String from_first_name;
            String event_id;
            String event_name;
            String type;
            int type_rank;
            String last_updated;
            String image;
            boolean badge_booking;
            boolean badge_ticket;
            int type_feed;

            public int getType_feed() {
                return type_feed;
            }

            public void setType_feed(int type_feed) {
                this.type_feed = type_feed;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getFb_id() {
                return fb_id;
            }

            public void setFb_id(String fb_id) {
                this.fb_id = fb_id;
            }

            public String getFrom_fb_id() {
                return from_fb_id;
            }

            public void setFrom_fb_id(String from_fb_id) {
                this.from_fb_id = from_fb_id;
            }

            public String getFrom_first_name() {
                from_first_name = from_first_name.substring(0,1).toUpperCase() + from_first_name.substring(1);
                return from_first_name;
            }

            public void setFrom_first_name(String from_first_name) {
                this.from_first_name = from_first_name;
            }

            public String getEvent_id() {
                return event_id;
            }

            public boolean getBadge_booking() {
                return badge_booking;
            }

            public void setBadge_booking(boolean badge_booking) {
                this.badge_booking = badge_booking;
            }

            public boolean getBadge_ticket() {
                return badge_ticket;
            }

            public void setBadge_ticket(boolean badge_ticket) {
                this.badge_ticket = badge_ticket;
            }

            public void setEvent_id(String event_id) {
                this.event_id = event_id;
            }

            public String getEvent_name() {
                return event_name;
            }

            public void setEvent_name(String event_name) {
                this.event_name = event_name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getType_rank() {
                return type_rank;
            }

            public void setType_rank(int type_rank) {
                this.type_rank = type_rank;
            }

            public String getLast_updated() {
                return last_updated;
            }

            public void setLast_updated(String last_updated) {
                this.last_updated = last_updated;
            }
        }

    }

}
