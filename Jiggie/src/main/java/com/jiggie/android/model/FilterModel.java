package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public final class FilterModel {
    public final long response;
    public final String msg;
    public final Data data;

    public FilterModel(long response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static final class Data {
        public final Membersettings membersettings;

        public Data(Membersettings membersettings){
            this.membersettings = membersettings;
        }

        public static final class Membersettings {
            public final String _id;
            public final String fb_id;
            public final Notifications notifications;
            public final String account_type;
            public final String gender;
            public final String[] experiences;
            public final String gender_interest;
            public final String updated_at;
            public final boolean matchme;
            public final Payment payment;
            public final String phone;

            public Membersettings(String _id, String fb_id, Notifications notifications, String account_type, String gender, String[] experiences, String gender_interest, String updated_at, boolean matchme, Payment payment, String phone){
                this._id = _id;
                this.fb_id = fb_id;
                this.notifications = notifications;
                this.account_type = account_type;
                this.gender = gender;
                this.experiences = experiences;
                this.gender_interest = gender_interest;
                this.updated_at = updated_at;
                this.matchme = matchme;
                this.payment = payment;
                this.phone = phone;
            }

            public static final class Notifications {
                public final boolean feed;
                public final boolean chat;
                public final boolean location;

                public Notifications(boolean feed, boolean chat, boolean location){
                    this.feed = feed;
                    this.chat = chat;
                    this.location = location;
                }
            }

            public static final class Payment {

                public Payment(){
                }
            }
        }
    }
}
