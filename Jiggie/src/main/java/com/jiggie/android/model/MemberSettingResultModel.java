package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/16/2016.
 */
public class MemberSettingResultModel {

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

    public class Data{

        MemberSettings membersettings;

        public MemberSettings getMembersettings() {
            return membersettings;
        }

        public void setMembersettings(MemberSettings membersettings) {
            this.membersettings = membersettings;
        }

        public class MemberSettings{
            String _id;
            String fb_id;
            String gender;
            Notifications notifications;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
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

            public Notifications getNotifications() {
                return notifications;
            }

            public void setNotifications(Notifications notifications) {
                this.notifications = notifications;
            }

            public class Notifications{
                boolean chat;
                boolean feed;
                boolean location;

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

                public boolean isLocation() {
                    return location;
                }

                public void setLocation(boolean location) {
                    this.location = location;
                }
            }

            String updated_at;
            String account_type;
            ArrayList<String> experiences;
            String gender_interest;
            Payment payment;
            String phone;

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
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

            public String getGender_interest() {
                return gender_interest;
            }

            public void setGender_interest(String gender_interest) {
                this.gender_interest = gender_interest;
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

            public class Payment{

            }



        }

    }

}
