package com.jiggie.android.model;

/**
 * Created by LTE on 5/13/2016.
 */
public final class ResponseContactModel {
    public final long response;
    public final String msg;
    public final Data data;

    public ResponseContactModel(long response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static final class Data {
        public final Contact contact[];

        public Data(Contact[] contact){
            this.contact = contact;
        }

        public static final class Contact {
            public final String record_id;
            public final String name;
            public final String[] email;
            public final String[] phone;
            public final boolean is_active;
            public final String uniq_id;

            public Contact(String record_id, String name, String[] email, String[] phone, boolean is_active, String uniq_id){
                this.record_id = record_id;
                this.name = name;
                this.email = email;
                this.phone = phone;
                this.is_active = is_active;
                this.uniq_id = uniq_id;
            }
        }
    }
}