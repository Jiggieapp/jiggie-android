package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 5/16/2016.
 */
public class ReferEventMixpanelModel {

    String promo_code;
    String promo_url;
    String contact_fullname;
    ArrayList<String> contact_email = new ArrayList<>();
    ArrayList<String> contact_phone = new ArrayList<>();

    public ReferEventMixpanelModel(String promo_code){
        this.promo_code = promo_code;
    }

    public ReferEventMixpanelModel(String promo_code, String promo_url){
        this.promo_code = promo_code;
        this.promo_url = promo_url;
    }

    public ReferEventMixpanelModel(String promo_code, String promo_url, String contact_fullname, ArrayList<String> contact_email, ArrayList<String> contact_phone){
        this.promo_code = promo_code;
        this.promo_url = promo_url;
        this.contact_fullname = contact_fullname;
        this.contact_email = contact_email;
        this.contact_phone = contact_phone;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public String getPromo_url() {
        return promo_url;
    }

    public String getContact_fullname() {
        return contact_fullname;
    }

    public ArrayList<String> getContact_email() {
        return contact_email;
    }

    public ArrayList<String> getContact_phone() {
        return contact_phone;
    }
}
