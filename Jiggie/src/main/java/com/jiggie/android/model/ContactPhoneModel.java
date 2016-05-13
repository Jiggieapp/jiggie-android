package com.jiggie.android.model;

/**
 * Created by LTE on 5/12/2016.
 */
public class ContactPhoneModel {

    String id;
    String name;
    String phoneNumber;
    String email;
    String photoThumbnail;

    public ContactPhoneModel(String id, String name, String phoneNumber, String email, String photoThumbnail){
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoThumbnail() {
        return photoThumbnail;
    }
}
