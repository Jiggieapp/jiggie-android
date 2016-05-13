package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 5/13/2016.
 */
public final class ResponseContactModel {
    public final int response;
    public final String msg;
    public final Data data;

    public ResponseContactModel(int response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public int getResponse() {
        return response;
    }

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }

    public static final class Data {
        public final ArrayList<Contact> contact;

        public Data(ArrayList<Contact> contact){
            this.contact = contact;
        }

        public ArrayList<Contact> getContact() {
            return contact;
        }

        public static final class Contact {
            public final String record_id;
            public final String name;
            public final ArrayList<String> email;
            public final ArrayList<String> phone;
            public final boolean is_active;
            public final String uniq_id;

            public Contact(String record_id, String name, ArrayList<String> email, ArrayList<String> phone, boolean is_active, String uniq_id){
                this.record_id = record_id;
                this.name = name;
                this.email = email;
                this.phone = phone;
                this.is_active = is_active;
                this.uniq_id = uniq_id;
            }

            public String getRecord_id() {
                return record_id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<String> getEmail() {
                return email;
            }

            public ArrayList<String> getPhone() {
                return phone;
            }

            public boolean is_active() {
                return is_active;
            }

            public String getUniq_id() {
                return uniq_id;
            }
        }
    }
}