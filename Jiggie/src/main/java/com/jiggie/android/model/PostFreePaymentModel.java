package com.jiggie.android.model;

/**
 * Created by LTE on 4/20/2016.
 */
public final class PostFreePaymentModel {
    public final String order_id;
    public final String pay_deposit;

    public PostFreePaymentModel(String order_id, String pay_deposit){
        this.order_id = order_id;
        this.pay_deposit = pay_deposit;
    }
}