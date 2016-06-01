package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by LTE on 2/4/2016.
 */
public class GuestModel {

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
        ArrayList<GuestInterests> guest_interests;

        public ArrayList<GuestInterests> getGuest_interests() {
            return guest_interests;
        }

        public void setGuest_interests(ArrayList<GuestInterests> guest_interests) {
            this.guest_interests = guest_interests;
        }

        public static class GuestInterests implements Parcelable {
            String fb_id;
            String first_name;
            String gender;
            boolean is_invited;
            boolean is_connected;
            String profile_image;

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

            public boolean is_invited() {
                return is_invited;
            }

            public void setIs_invited(boolean is_invited) {
                this.is_invited = is_invited;
            }

            public boolean is_connected() {
                return is_connected;
            }

            public void setIs_connected(boolean is_connected) {
                this.is_connected = is_connected;
            }

            public String getProfile_image() {
                return profile_image;
            }

            public void setProfile_image(String profile_image) {
                this.profile_image = profile_image;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.fb_id);
                dest.writeString(this.first_name);
                dest.writeString(this.gender);
                dest.writeByte(is_invited ? (byte) 1 : (byte) 0);
                dest.writeByte(is_connected ? (byte) 1 : (byte) 0);
                dest.writeString(this.profile_image);
            }

            public GuestInterests() {
            }

            protected GuestInterests(Parcel in) {
                this.fb_id = in.readString();
                this.first_name = in.readString();
                this.gender = in.readString();
                this.is_invited = in.readByte() != 0;
                this.is_connected = in.readByte() != 0;
                this.profile_image = in.readString();
            }

            public static final Parcelable.Creator<GuestInterests> CREATOR = new Parcelable.Creator<GuestInterests>() {
                public GuestInterests createFromParcel(Parcel source) {
                    return new GuestInterests(source);
                }

                public GuestInterests[] newArray(int size) {
                    return new GuestInterests[size];
                }
            };
        }

    }

}
