package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 4/27/2016.
 */
public final class CityModel {
    public final int response;
    public final String msg;
    public final Data data;

    public CityModel(int response, String msg, Data data){
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
        public final ArrayList<Citylist> citylist;

        public Data(ArrayList<Citylist> citylist){
            this.citylist = citylist;
        }

        public ArrayList<Citylist> getCitylist() {
            return citylist;
        }

        public static final class Citylist {
            public final String _id;
            public final String country;
            public final String city;
            public final boolean status;
            public final String created_at;

            public Citylist(String _id, String country, String city, boolean status, String created_at){
                this._id = _id;
                this.country = country;
                this.city = city;
                this.status = status;
                this.created_at = created_at;
            }

            public String get_id() {
                return _id;
            }

            public String getCountry() {
                return country;
            }

            public String getCity() {
                return city;
            }

            public boolean isStatus() {
                return status;
            }

            public String getCreated_at() {
                return created_at;
            }
        }
    }
}