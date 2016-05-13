package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 5/12/2016.
 */
public final class PostContactModel {
    public final String fb_id;
    public final String device_type;
    public final ArrayList<Contact> contact;

    public PostContactModel(String fb_id, String device_type, ArrayList<Contact> contact){
        this.fb_id = fb_id;
        this.device_type = device_type;
        this.contact = contact;
    }

    public static final class Contact {
        public final String record_id;
        public final String name;
        public final ArrayList<String> email;
        public final ArrayList<String> phone;

        public Contact(String record_id, String name, ArrayList<String> email, ArrayList<String> phone){
            this.record_id = record_id;
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

    }
}