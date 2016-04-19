package com.jiggie.android.model;

/**
 * Created by LTE on 3/11/2016.
 */
public class PostPaymentModel {

    String type = null;
    String is_new_card;
    long order_id;
    String token_id;
    String name_cc;
    String pay_deposit;

    public PostPaymentModel(String type, String is_new_card, long order_id, String token_id, String name_cc, String pay_deposit){
        this.type = type;
        this.is_new_card = is_new_card;
        this.order_id = order_id;
        this.token_id = token_id;
        this.name_cc = name_cc;
        this.pay_deposit = pay_deposit;
    }

    public String getType() {
        return type;
    }
}