package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 4/27/2016.
 */
public final class CityModel {
    public final long response;
    public final String msg;
    public final Data data;

    public CityModel(long response, String msg, Data data) {
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static final class Data {
        public final ArrayList<Citylist> citylist;

        public Data(ArrayList<Citylist> citylist) {
            this.citylist = citylist;
        }

        public static final class Citylist {
            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getCity() {
                city = city.substring(0,1).toUpperCase() + city.substring(1);
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getInitial() {
                return initial;
            }

            public void setInitial(String initial) {
                this.initial = initial;
            }

            private String _id;
            private String country;
            private String city;
            //public final boolean status;
            private String initial;
            //public final long tz;
            //public final String tz_string;
            //public final String created_at;

            public Citylist(String _id
                    , String country
                    , String city
                            //, boolean status
                    , String initial
                            //, long tz
                            //, String tz_string
                            //, String created_at
            ) {
                this._id = _id;
                this.country = country;
                this.city = city;
                //this.status = status;
                this.initial = initial;
                //this.tz = tz;
                //this.tz_string = tz_string;
                //this.created_at = created_at;
            }
        }
    }
}