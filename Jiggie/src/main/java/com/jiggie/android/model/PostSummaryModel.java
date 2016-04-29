package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/22/2016.
 */
public final class PostSummaryModel{
    public final String fb_id;
    public final String event_id;
    public final ArrayList<Product_list> product_list;
    public final Guest_detail guest_detail;

    public PostSummaryModel(String fb_id, String event_id, ArrayList<Product_list> product_list, Guest_detail guest_detail){
        this.fb_id = fb_id;
        this.event_id = event_id;
        this.product_list = product_list;
        this.guest_detail = guest_detail;
    }

    public static final class Product_list {
        public final String ticket_id;
        public final int num_buy;

        public Product_list(String ticket_id, int num_buy){
            this.ticket_id = ticket_id;
            this.num_buy = num_buy;
        }
    }

    public static final class Guest_detail {
        public final String name;
        public final String email;
        public final String phone;
        public final String dial_code;

        public Guest_detail(String name, String email, String phone, String dial_code){
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.dial_code = dial_code;
        }
    }
}