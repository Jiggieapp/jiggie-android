package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/4/2016.
 */
public class MemberInfoModel {

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
        MemberInfo memberinfo;

        public MemberInfo getMemberinfo() {
            return memberinfo;
        }

        public void setMemberinfo(MemberInfo memberinfo) {
            this.memberinfo = memberinfo;
        }

        public class MemberInfo{
            String _id;
            boolean active;
            boolean visible;
            String fb_id;
            String apn_token;
            String user_first_name;
            String user_last_name;
            String first_name;
            String last_name;
            String profile_image_url;
            String gender;
            String email;
            ArrayList<String> photos;
            String about;
            String birthday;
            String location;
            String userId;
            String created_at;
            String last_login;
            String update_at;
            String birth_date;
            String user_ref_name;
            String user_ref_fb_id;
            int ref_count;
            int chat_count;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public boolean isActive() {
                return active;
            }

            public void setActive(boolean active) {
                this.active = active;
            }

            public boolean isVisible() {
                return visible;
            }

            public void setVisible(boolean visible) {
                this.visible = visible;
            }

            public String getFb_id() {
                return fb_id;
            }

            public void setFb_id(String fb_id) {
                this.fb_id = fb_id;
            }

            public String getApn_token() {
                return apn_token;
            }

            public void setApn_token(String apn_token) {
                this.apn_token = apn_token;
            }

            public String getUser_first_name() {
                return user_first_name;
            }

            public void setUser_first_name(String user_first_name) {
                this.user_first_name = user_first_name;
            }

            public String getUser_last_name() {
                return user_last_name;
            }

            public void setUser_last_name(String user_last_name) {
                this.user_last_name = user_last_name;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getLast_name() {
                return last_name;
            }

            public void setLast_name(String last_name) {
                this.last_name = last_name;
            }

            public String getProfile_image_url() {
                return profile_image_url;
            }

            public void setProfile_image_url(String profile_image_url) {
                this.profile_image_url = profile_image_url;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public ArrayList<String> getPhotos() {
                return photos;
            }

            public void setPhotos(ArrayList<String> photos) {
                this.photos = photos;
            }

            public String getAbout() {
                return about;
            }

            public void setAbout(String about) {
                this.about = about;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getLast_login() {
                return last_login;
            }

            public void setLast_login(String last_login) {
                this.last_login = last_login;
            }

            public String getUpdate_at() {
                return update_at;
            }

            public void setUpdate_at(String update_at) {
                this.update_at = update_at;
            }

            public String getBirth_date() {
                return birth_date;
            }

            public void setBirth_date(String birth_date) {
                this.birth_date = birth_date;
            }

            public String getUser_ref_name() {
                return user_ref_name;
            }

            public void setUser_ref_name(String user_ref_name) {
                this.user_ref_name = user_ref_name;
            }

            public String getUser_ref_fb_id() {
                return user_ref_fb_id;
            }

            public void setUser_ref_fb_id(String user_ref_fb_id) {
                this.user_ref_fb_id = user_ref_fb_id;
            }

            public int getRef_count() {
                return ref_count;
            }

            public void setRef_count(int ref_count) {
                this.ref_count = ref_count;
            }

            public int getChat_count() {
                return chat_count;
            }

            public void setChat_count(int chat_count) {
                this.chat_count = chat_count;
            }
        }
    }

}
