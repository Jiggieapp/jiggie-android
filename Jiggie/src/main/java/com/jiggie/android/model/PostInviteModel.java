package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 5/12/2016.
 */
public final class PostInviteModel {
    public final String fb_id;
    public final Contact contact;

    public PostInviteModel(String fb_id, Contact contact){
        this.fb_id = fb_id;
        this.contact = contact;
    }

    public static final class Contact {
        public final String name;
        public final ArrayList<String> email;
        public final String uniq_id;

        public Contact(String name, ArrayList<String> email, String uniq_id){
            this.name = name;
            this.email = email;
            this.uniq_id = uniq_id;
        }
    }
}