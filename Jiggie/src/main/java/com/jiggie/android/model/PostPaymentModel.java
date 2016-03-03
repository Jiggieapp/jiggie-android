package com.jiggie.android.model;

/**
 * Created by LTE on 3/3/2016.
 */
public class PostPaymentModel {

    String type;
    String is_new_card;
    String order_id;
    String token_id;

    public PostPaymentModel(String type, String is_new_card, String order_id, String token_id){
        this.type = type;
        this.is_new_card = is_new_card;
        this.order_id = order_id;
        this.token_id = token_id;
    }

}
