package com.jiggie.android.model;

/**
 * Created by LTE on 5/26/2016.
 */
public class PromoMixpanelModel {

    String code;
    String status;
    String message;

    public PromoMixpanelModel(String code, String status, String message){
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
