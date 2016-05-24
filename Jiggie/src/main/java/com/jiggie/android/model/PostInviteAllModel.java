package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 5/13/2016.
 */
public final class PostInviteAllModel {
    public final String fb_id;
    public final ArrayList<Contact> contact;

    public PostInviteAllModel(String fb_id, ArrayList<Contact> contact){
        this.fb_id = fb_id;
        this.contact = contact;
    }

    public static final class Contact {
        public final String name;
        public final ArrayList<String> phone;
        public final ArrayList<String> email;
        public final String uniq_id;

        public Contact(String name, ArrayList<String> phone, ArrayList<String> email, String uniq_id){
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.uniq_id = uniq_id;
        }
    }
}