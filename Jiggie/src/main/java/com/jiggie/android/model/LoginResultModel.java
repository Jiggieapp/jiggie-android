package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/16/2016.
 */
public class LoginResultModel {

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

        Login login;

        public Login getLogin() {
            return login;
        }

        public void setLogin(Login login) {
            this.login = login;
        }

        public class Login{
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

            boolean is_new_user;
            String help_phone;
            boolean matchme;
            int device_type;
            boolean show_walkthrough;
            ShowWalkthroughNew show_walkthrough_new;

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

            public ShowWalkthroughNew getShow_walkthrough_new() {
                return show_walkthrough_new;
            }

            public void setShow_walkthrough_new(ShowWalkthroughNew show_walkthrough_new) {
                this.show_walkthrough_new = show_walkthrough_new;
            }

            public class ShowWalkthroughNew{
                boolean event;
                boolean chat;
                boolean social;

                public boolean isEvent() {
                    return event;
                }

                public void setEvent(boolean event) {
                    this.event = event;
                }

                public boolean isChat() {
                    return chat;
                }

                public void setChat(boolean chat) {
                    this.chat = chat;
                }

                public boolean isSocial() {
                    return social;
                }

                public void setSocial(boolean social) {
                    this.social = social;
                }
            }

        }

    }

}
