package com.jiggie.android.model;

/**
 * Created by Wandy on 2/10/2016.
 */
public final class ProductModel {
    long response;
    String msg;
    Data data;

    public ProductModel(long response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static final class Data {
        public final Product_list product_lists[];

        public Data(Product_list[] product_lists){
            this.product_lists = product_lists;
        }

        public static final class Product_list {
            public final String ticket_id;
            public final String event_id;
            public final String name;
            public final String ticket_type;
            public final long quantity;
            public final String total_price;

            public Product_list(String ticket_id, String event_id, String name, String ticket_type, long quantity, String total_price){
                this.ticket_id = ticket_id;
                this.event_id = event_id;
                this.name = name;
                this.ticket_type = ticket_type;
                this.quantity = quantity;
                this.total_price = total_price;
            }
        }
    }
}