package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/22/2016.
 */
public class PostSummaryModel {

    String fb_id;
    String event_id;
    ArrayList<ProductList> product_list;

    public PostSummaryModel(String fb_id, String event_id, ArrayList<ProductList> product_list){
        this.fb_id = fb_id;
        this.event_id = event_id;
        this.product_list = product_list;
    }

    public static class ProductList{
        String ticket_id;
        String name;
        String ticket_type;
        int quantity;
        String admin_fee;
        String tax_percent;
        String tax_amount;
        String tip_percent;
        String tip_amount;
        String price;
        String total_price;
        int num_buy;

        public ProductList(String ticket_id, String name, String ticket_type, int quantity, String admin_fee, String tax_percent, String tax_amount, String tip_percent,
                           String tip_amount, String price, String total_price, int num_buy){

            this.ticket_id = ticket_id;
            this.name = name;
            this.ticket_type = ticket_type;
            this.quantity = quantity;
            this.admin_fee = admin_fee;
            this.tax_percent = tax_percent;
            this.tax_amount = tax_amount;
            this.tip_percent = tip_percent;
            this.tip_amount = tip_amount;
            this.price = price;
            this.total_price = total_price;
            this.num_buy = num_buy;

        }
    }

}
