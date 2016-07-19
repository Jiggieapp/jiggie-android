package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/16/2016.
 */
public class MemberSettingResultModel {
    public Data data;
    public String msg;
    public long response;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public MemberSettingResultModel(Data data, String msg, long response) {
        this.data = data;
        this.msg = msg;
        this.response = response;
    }

    public static final class Data {
        public MemberSettings getMembersettings() {
            return membersettings;
        }

        public void setMembersettings(MemberSettings membersettings) {
            this.membersettings = membersettings;
        }

        public MemberSettings membersettings;

        public Data(MemberSettings membersettings) {
            this.membersettings = membersettings;
        }

        public static final class MemberSettings {
            public String _id;
            public String account_type;
            public ArrayList<String> experiences;
            public String fb_id;
            public String gender;
            public String gender_interest;
            public Notifications notifications;
            public Payment payment;
            public String phone;
            public String updated_at;
            public int from_age;
            public int to_age;
            public int distance;
            public String area_event;
            public String latlng_location;

            public int getFrom_age() {
                return from_age;
            }

            public void setFrom_age(int from_age) {
                this.from_age = from_age;
            }

            public int getTo_age() {
                return to_age;
            }

            public void setTo_age(int to_age) {
                this.to_age = to_age;
            }

            public int getDistance() {
                return distance;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public String getArea_event() {
                return area_event;
            }

            public void setArea_event(String area_event) {
                this.area_event = area_event;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getAccount_type() {
                return account_type;
            }

            public void setAccount_type(String account_type) {
                this.account_type = account_type;
            }

            public ArrayList<String> getExperiences() {
                return experiences;
            }

            public void setExperiences(ArrayList<String> experiences) {
                this.experiences = experiences;
            }

            public String getFb_id() {
                return fb_id;
            }

            public void setFb_id(String fb_id) {
                this.fb_id = fb_id;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getGender_interest() {
                return gender_interest;
            }

            public void setGender_interest(String gender_interest) {
                this.gender_interest = gender_interest;
            }

            public Notifications getNotifications() {
                return notifications;
            }

            public void setNotifications(Notifications notifications) {
                this.notifications = notifications;
            }

            public Payment getPayment() {
                return payment;
            }

            public void setPayment(Payment payment) {
                this.payment = payment;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public String getLatlng_location() {
                return latlng_location;
            }

            public MemberSettings(String _id, String account_type, ArrayList<String> experiences, String fb_id, String gender, String gender_interest, Notifications notifications, Payment payment, String phone, String updated_at, String latlng_location) {
                this._id = _id;
                this.account_type = account_type;
                this.experiences = experiences;
                this.fb_id = fb_id;
                this.gender = gender;
                this.gender_interest = gender_interest;
                this.notifications = notifications;
                this.payment = payment;
                this.phone = phone;
                this.updated_at = updated_at;
                this.latlng_location = latlng_location;
            }

            public static final class Notifications {
                public boolean chat;
                public boolean feed;
                public boolean location;

                public boolean isLocation() {
                    return location;
                }

                public void setLocation(boolean location) {
                    this.location = location;
                }

                public boolean isChat() {
                    return chat;
                }

                public void setChat(boolean chat) {
                    this.chat = chat;
                }

                public boolean isFeed() {
                    return feed;
                }

                public void setFeed(boolean feed) {
                    this.feed = feed;
                }



                public Notifications(boolean chat, boolean feed, boolean location) {
                    this.chat = chat;
                    this.feed = feed;
                    this.location = location;
                }
            }

            public static final class Payment {
                public Payment() {
                }
            }
        }
    }
}

