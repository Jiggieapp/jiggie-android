package com.jiggie.android.model;

/**
 * Created by Wandy on 4/20/2016.
 */
public class GuestInfo {
    public long response;
    public String msg;
    public Data data;

    public GuestInfo(long response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static class Data {
        public Guest_detail guest_detail;

        public Data(Guest_detail guest_detail){
            this.guest_detail = guest_detail;
        }

        public static class Guest_detail {
            public String email;
            public String name;
            public String dial_code;
            public String phone;

            public Guest_detail(String email, String name, String dial_code, String phone){
                this.email = email;
                this.name = name;
                this.dial_code = dial_code;
                this.phone = phone;
            }
        }
    }
}