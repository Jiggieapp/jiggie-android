package com.jiggie.android.model;

/**
 * Created by Wandy on 6/29/2016.
 */
public class SuccessLocationModel {

    int response;
    String msg;
    Data data;

    public Data getData() {
        return data;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static final class Data {
        public final City city;

        public Data(City city) {
            this.city = city;
        }

        public static final class City {
            public final String country;
            public final String city;
            public final boolean status;

            public City(String country, String city, boolean status) {
                this.country = country;
                this.city = city;
                this.status = status;
            }
        }
    }
}
