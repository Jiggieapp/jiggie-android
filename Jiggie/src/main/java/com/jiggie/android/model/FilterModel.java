package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public final class FilterModel {
    public final boolean success;
    public final Data data;

    public Data getData() {
        return data;
    }

    public FilterModel(boolean success, Data data){
        this.success = success;
        this.data = data;
    }

    public static final class Data implements Parcelable{
        String _id;
        String fb_id;
        String gender;
        Notifications notifications;
        String updated_at;
        String account_type;
        ArrayList<String> experiences;
        String gender_interest;
        boolean matchme;
        Payment payment;
        String phone;

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

        public boolean isMatchme() {
            return matchme;
        }

        public void setMatchme(boolean matchme) {
            this.matchme = matchme;
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

        public static Creator<Data> getCREATOR() {
            return CREATOR;
        }

        public Data(String _id, String fb_id, String gender, Notifications notifications, String updated_at, String account_type, ArrayList<String> experiences, String gender_interest, boolean matchme, Payment payment, String phone){
            this._id = _id;
            this.fb_id = fb_id;
            this.gender = gender;
            this.notifications = notifications;
            this.updated_at = updated_at;
            this.account_type = account_type;
            this.experiences = experiences;
            this.gender_interest = gender_interest;
            this.matchme = matchme;
            this.payment = payment;
            this.phone = phone;
        }

        protected Data(Parcel in) {
            _id = in.readString();
            fb_id = in.readString();
            gender = in.readString();
            updated_at = in.readString();
            account_type = in.readString();
            experiences = in.createStringArrayList();
            gender_interest = in.readString();
            matchme = in.readByte() != 0;
            phone = in.readString();
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(_id);
            dest.writeString(fb_id);
            dest.writeString(gender);
            dest.writeString(updated_at);
            dest.writeString(account_type);
            dest.writeStringList(experiences);
            dest.writeString(gender_interest);
            dest.writeByte((byte) (matchme ? 1 : 0));
            dest.writeString(phone);
        }

        public static final class Notifications {
            public final boolean feed;
            public final boolean chat;

            public Notifications(boolean feed, boolean chat){
                this.feed = feed;
                this.chat = chat;
            }
        }

        public static final class Payment {

            public Payment(){
            }
        }
    }
}
