package com.jiggie.android.model;

/**
 * Created by LTE on 4/15/2016.
 */
public final class SupportModel {
    public final int response;
    public final String msg;
    public final Data data;

    public SupportModel(int response, String msg, Data data){
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
        public final Support support;

        public Support getSupport() {
            return support;
        }

        public Data(Support support){
            this.support = support;
        }

        public static final class Support {
            public final String email;
            public final String telp;

            public Support(String email, String telp){
                this.email = email;
                this.telp = telp;
            }

            public String getEmail() {
                return email;
            }

            public String getTelp() {
                return telp;
            }
        }
    }
}
