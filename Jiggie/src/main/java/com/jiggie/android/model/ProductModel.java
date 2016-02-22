package com.jiggie.android.model;

import java.util.ArrayList;

public final class ProductModel {
    public final long response;

    public long getResponse() {
        return response;
    }

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }

    public final String msg;
    public final Data data;

    public ProductModel(long response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static final class Data {
        public Product_lists getProduct_lists() {
            return product_lists;
        }

        public final Product_lists product_lists;

        public Data(Product_lists product_lists){
            this.product_lists = product_lists;
        }

        public static final class Product_lists {
            //public final Purchase purchase[];
            //public final Reservation reservation[];

            public final ArrayList<Purchase> purchase;
            public final ArrayList<Reservation> reservation;

            public Product_lists(ArrayList<Purchase> purchase, ArrayList<Reservation> reservation){
                this.purchase = purchase;
                this.reservation = reservation;
            }



            public static final class Purchase {
                  String ticket_id;
                  String event_id;
                  String name;
                  String ticket_type;
                  long quantity;
                  String total_price;

                public void setTicket_id(String ticket_id) {
                    this.ticket_id = ticket_id;
                }

                public void setEvent_id(String event_id) {
                    this.event_id = event_id;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public void setTicket_type(String ticket_type) {
                    this.ticket_type = ticket_type;
                }

                public void setQuantity(long quantity) {
                    this.quantity = quantity;
                }

                public void setTotal_price(String total_price) {
                    this.total_price = total_price;
                }

                public Purchase(String ticket_id, String event_id, String name, String ticket_type, long quantity, String total_price){
                    this.ticket_id = ticket_id;
                    this.event_id = event_id;
                    this.name = name;
                    this.ticket_type = ticket_type;
                    this.quantity = quantity;
                    this.total_price = total_price;
                }

                public Purchase(Reservation reservation)
                {
                    setEvent_id(reservation.getEvent_id());
                    setName(reservation.getName());
                    setQuantity(reservation.getQuantity());
                    setTicket_id(reservation.getTicket_id());
                    setTicket_type(reservation.getTicket_type());
                    setTotal_price(reservation.getTotal_price());
                }

                public String getEvent_id() {
                    return event_id;
                }

                public String getName() {
                    return name;
                }

                public String getTicket_type() {
                    return ticket_type;
                }

                public long getQuantity() {
                    return quantity;
                }

                public String getTotal_price() {
                    return total_price;
                }

                public String getTicket_id() {
                    return ticket_id;
                }
            }

            public static final class Reservation {
                  String ticket_id;
                  String event_id;
                  String name;
                  String ticket_type;
                  long quantity;
                  String total_price;

                public void setTicket_id(String ticket_id) {
                    this.ticket_id = ticket_id;
                }

                public void setEvent_id(String event_id) {
                    this.event_id = event_id;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public void setTicket_type(String ticket_type) {
                    this.ticket_type = ticket_type;
                }

                public void setQuantity(long quantity) {
                    this.quantity = quantity;
                }

                public void setTotal_price(String total_price) {
                    this.total_price = total_price;
                }

                public Reservation(String ticket_id, String event_id, String name, String ticket_type, long quantity, String total_price){
                    this.ticket_id = ticket_id;
                    this.event_id = event_id;
                    this.name = name;
                    this.ticket_type = ticket_type;
                    this.quantity = quantity;
                    this.total_price = total_price;
                }

                public String getTicket_id() {
                    return ticket_id;
                }

                public String getEvent_id() {
                    return event_id;
                }

                public String getName() {
                    return name;
                }

                public String getTicket_type() {
                    return ticket_type;
                }

                public long getQuantity() {
                    return quantity;
                }

                public String getTotal_price() {
                    return total_price;
                }
            }

            public ArrayList<Purchase> getPurchase() {
                return purchase;
            }

            public ArrayList<Reservation> getReservation() {
                return reservation;
            }
        }
    }
}