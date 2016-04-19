package com.jiggie.android.activity.setup.country;

/**
 * Created by Wandy on 4/19/2016.
 */
public class Country {
    public Data data[];

    public Country(Data[] data) {
        this.data = data;
    }

    public Country(String formula_value, String url_value) {

    }

    public static class Data {
        public String name;
        public String dial_code;
        public String code;

        public Data(String name, String dial_code, String code) {
            this.name = name;
            this.dial_code = dial_code;
            this.code = code;
        }
    }

    public void getCountryCodeModelList() {
    }
}
