package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/2/2016.
 */
public class SettingModel {

    boolean success;
    Data data;

    public SettingModel(boolean success, Data data){
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        String _id;
        String fb_id;
        String gender;
        Notifications notifications;

        public Data(String _id, String fb_id, String gender, Notifications notifications, String update_at, String account_type, ArrayList<String> experiences,
                    String gender_interest, Payment payment, String phone){
            this._id = _id;
            this.fb_id = fb_id;
            this.gender = gender;
            this.notifications = notifications;
            this.updated_at = update_at;
            this.account_type = account_type;
            this.experiences = experiences;
            this.gender_interest = gender_interest;
            this.payment = payment;
            this.phone = phone;
        }

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

        public static class Notifications{
            boolean chat;
            boolean feed;
            boolean location;

            public Notifications(boolean chat, boolean feed, boolean location){
                this.chat = chat;
                this.feed = feed;
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

        public static class Payment{
            public Payment(){

            }
        }

    }

    boolean is_new_user;
    String help_phone;
    boolean matchme;
    int device_type;
    boolean show_walkthrough;

    public boolean is_new_user() {
        return is_new_user;
    }

    public void setIs_new_user(boolean is_new_user) {
        this.is_new_user = is_new_user;
    }

    public String getHelp_phone() {
        return help_phone;
    }

    public void setHelp_phone(String help_phone) {
        this.help_phone = help_phone;
    }

    public boolean isMatchme() {
        return matchme;
    }

    public void setMatchme(boolean matchme) {
        this.matchme = matchme;
    }

    public int getDevice_type() {
        return device_type;
    }

    public void setDevice_type(int device_type) {
        this.device_type = device_type;
    }

    public boolean isShow_walkthrough() {
        return show_walkthrough;
    }

    public void setShow_walkthrough(boolean show_walkthrough) {
        this.show_walkthrough = show_walkthrough;
    }
}
