package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 5/12/2016.
 */
public class ContactPhoneModel implements Comparable {

    String record_id;
    String name;
    ArrayList<String> phone;
    ArrayList<String> email;
    String photoThumbnail;

    public ContactPhoneModel(String id, String name, ArrayList<String> phone, ArrayList<String> email, String photoThumbnail){
        this.record_id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.photoThumbnail = photoThumbnail;
    }

    /*this.record_id = record_id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.is_active = is_active;
    this.uniq_id = uniq_id;
    this.credit = credit;*/

    public String getId() {
        return record_id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoThumbnail() {
        return photoThumbnail;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public ArrayList<String> getEmail() {
        return email;
    }

    public int compareTo(ContactPhoneModel another) {
        return 0;
    }

    @Override
    public int compareTo(Object another) {
        //int compare=Integer.parseInt(((ContactPhoneModel)another).getId());
        //return Integer.parseInt(getId()) - compare;

        return name.compareTo(((ContactPhoneModel)another).getName());
    }
}
