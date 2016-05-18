package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 5/12/2016.
 */
public class ContactPhoneModel {

    String id;
    String name;
    ArrayList<String> phoneNumber;
    ArrayList<String> email;
    String photoThumbnail;

    public ContactPhoneModel(String id, String name, ArrayList<String> phoneNumber, ArrayList<String> email, String photoThumbnail){
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.photoThumbnail = photoThumbnail;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoThumbnail() {
        return photoThumbnail;
    }

    public ArrayList<String> getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<String> getEmail() {
        return email;
    }
}
