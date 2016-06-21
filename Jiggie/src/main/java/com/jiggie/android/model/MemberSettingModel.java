package com.jiggie.android.model;

import android.text.TextUtils;

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
    int distance;
    String area_event;
    String from_age;
    String to_age;

    public String getFrom_age() {
        return from_age;
    }

    public void setFrom_age(String from_age) {
        this.from_age = from_age;
    }

    public String getTo_age() {
        return to_age;
    }

    public void setTo_age(String to_age) {
        this.to_age = to_age;
    }
    /*"from_age":"17",
            "to_age":"30",
            "distance":"10000",
            "area_event":"jakarta"*/


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

    public MemberSettingModel()
    {}

    public MemberSettingModel(MemberSettingResultModel memberSettingResultModel)
    {
        final MemberSettingResultModel.Data data = memberSettingResultModel.getData();
        final MemberSettingResultModel.Data.MemberSettings memberSettings = data.getMembersettings();
        final MemberSettingResultModel.Data.MemberSettings.Notifications notifications = memberSettings.getNotifications();

        ArrayList<String> exp = memberSettings.getExperiences();
        //ArrayList<String> photos = memberSettings.
        final String experiences = TextUtils.join(","
                , exp.toArray(new String[exp.size()]));

        setGender(memberSettingResultModel.getData().getMembersettings().getGender());
        setFb_id(memberSettingResultModel.getData().getMembersettings().getFb_id());
        setAccount_type(memberSettingResultModel.getData().getMembersettings().getAccount_type());
        setChat(memberSettingResultModel.getData().getMembersettings().getNotifications().isChat() ? 1 : 0);
        setExperiences(experiences);
        setFeed(memberSettingResultModel.getData().getMembersettings().getNotifications().isFeed() ? 1 : 0);
        setGender_interest(memberSettingResultModel.getData().getMembersettings().getGender_interest());
        //setPhotos(photos);
        setLocation(memberSettingResultModel.getData().getMembersettings().getNotifications().isLocation()? 1 : 0);
        setFrom_age(memberSettingResultModel.getData().getMembersettings().getFrom_age() + "");
        setTo_age(memberSettingResultModel.getData().getMembersettings().getTo_age() + "");
        setArea_event(memberSettingResultModel.getData().getMembersettings().getArea_event());
        setDistance(memberSettingResultModel.getData().getMembersettings().getDistance());
    }
}