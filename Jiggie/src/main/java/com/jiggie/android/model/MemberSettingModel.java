package com.jiggie.android.model;

import android.text.TextUtils;

import com.facebook.AccessToken;
import com.jiggie.android.activity.setup.SetupNotificationActivity;
import com.jiggie.android.activity.setup.SetupTagsActivity;

import java.util.ArrayList;

/**
 * Created by LTE on 2/2/2016.
 */
public class MemberSettingModel {

    String gender;
    String fb_id;
    int location;
    int chat;
    String experiences;
    int feed;
    String gender_interest;
    String account_type;
    ArrayList<String> photos;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getChat() {
        return chat;
    }

    public void setChat(int chat) {
        this.chat = chat;
    }

    public String getExperiences() {
        return experiences;
    }

    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }

    public int getFeed() {
        return feed;
    }

    public void setFeed(int feed) {
        this.feed = feed;
    }

    public String getGender_interest() {
        return gender_interest;
    }

    public void setGender_interest(String gender_interest) {
        this.gender_interest = gender_interest;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

}