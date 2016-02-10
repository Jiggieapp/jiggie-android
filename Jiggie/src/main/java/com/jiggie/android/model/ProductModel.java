package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by Wandy on 2/10/2016.
 */
public class ProductModel {
    long response;
    String msg;
    Data data;

    public long getResponse() {
        return response;
    }

    public void setResponse(long response) {
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public ProductModel(long response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static final class Data {
        public ArrayList<Product_list> getProduct_lists() {
            return product_lists;
        }

        //public final Product_list product_lists[];
        public final ArrayList<Product_list> product_lists;

        public Data(ArrayList<Product_list> product_lists){
            this.product_lists = product_lists;
        }

        public static final class Product_list {
            String ticket_id;
            String event_id;
            String name;

            public String getTicket_id() {
                return ticket_id;
            }

            public void setTicket_id(String ticket_id) {
                this.ticket_id = ticket_id;
            }

            public String getEvent_id() {
                return event_id;
            }

            public void setEvent_id(String event_id) {
                this.event_id = event_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTicket_type() {
                return ticket_type;
            }

            public void setTicket_type(String ticket_type) {
                this.ticket_type = ticket_type;
            }

            public long getQuantity() {
                return quantity;
            }

            public void setQuantity(long quantity) {
                this.quantity = quantity;
            }

            public String getTotal_price() {
                return total_price;
            }

            public void setTotal_price(String total_price) {
                this.total_price = total_price;
            }

            String ticket_type;
            long quantity;
            String total_price;

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