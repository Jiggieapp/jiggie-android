package com.jiggie.android.model;

/**
 * Created by LTE on 3/11/2016.
 */
public class PostPaymentModel {

    String type;
    String is_new_card;
    long order_id;
    String token_id;
    String name_cc;

    public PostPaymentModel(String type, String is_new_card, long order_id, String token_id, String name_cc){
        this.type = type;
        this.is_new_card = is_new_card;
        this.order_id = order_id;
        this.token_id = token_id;
        this.name_cc = name_cc;
    }

}