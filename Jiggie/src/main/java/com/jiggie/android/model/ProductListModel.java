package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/22/2016.
 */
public class ProductListModel {

    int response;
    String msg;
    Data data;

    public ProductListModel(int response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public int getResponse() {
        return response;
    }

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }

    public static class Data{
        ProductList product_lists;

        public ProductList getProduct_lists() {
            return product_lists;
        }

        public void setProduct_lists(ProductList product_lists) {
            this.product_lists = product_lists;
        }

        public static class ProductList{

            String event_id;
            String event_name;
            String venue_name;
            String start_datetime;
            ArrayList<Purchase> purchase;
            ArrayList<Reservation> reservation;

            public ProductList(String event_id, String event_name, String venue_name, String start_datetime, ArrayList<Purchase> purchase, ArrayList<Reservation> reservation){
                this.event_id = event_id;
                this.event_name = event_name;
                this.venue_name = venue_name;
                this.start_datetime = start_datetime;
                this.purchase = purchase;
                this.reservation = reservation;
            }

            public String getVenue_name() {
                return venue_name;
            }

            public String getStart_datetime() {
                return start_datetime;
            }

            public String getEvent_id() {
                return event_id;
            }

            public String getEvent_name() {
                return event_name;
            }

            public ArrayList<Purchase> getPurchase() {
                return purchase;
            }

            public ArrayList<Reservation> getReservation() {
                return reservation;
            }

            public static class Purchase{
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
                String description;
                String max_purchase;

                public Purchase(String ticket_id, String name, String ticket_type, int quantity, String admin_fee, String tax_percent, String tax_amount, String tip_percent, String tip_amount, String price, String total_price, String description, String max_purchase){
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
                    this.description = description;
                    this.max_purchase = max_purchase;
                }

                public String getTicket_id() {
                    return ticket_id;
                }

                public String getName() {
                    return name;
                }

                public String getTicket_type() {
                    return ticket_type;
                }

                public int getQuantity() {
                    return quantity;
                }

                public String getAdmin_fee() {
                    return admin_fee;
                }

                public String getTax_percent() {
                    return tax_percent;
                }

                public String getTax_amount() {
                    return tax_amount;
                }

                public String getTip_percent() {
                    return tip_percent;
                }

                public String getTip_amount() {
                    return tip_amount;
                }

                public String getPrice() {
                    return price;
                }

                public String getTotal_price() {
                    return total_price;
                }

                public String getDescription() {
                    return description;
                }

                public String getMax_purchase() {
                    return max_purchase;
                }
            }

            public static class Reservation{
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
                String description;
                String max_guests;

                public Reservation(String ticket_id, String name, String ticket_type, int quantity, String admin_fee, String tax_percent, String tax_amount, String tip_percent, String tip_amount, String price, String total_price, String description, String max_guests){
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
                    this.description = description;
                    this.max_guests = max_guests;
                }

                public String getTicket_id() {
                    return ticket_id;
                }

                public String getName() {
                    return name;
                }

                public String getTicket_type() {
                    return ticket_type;
                }

                public int getQuantity() {
                    return quantity;
                }

                public String getAdmin_fee() {
                    return admin_fee;
                }

                public String getTax_percent() {
                    return tax_percent;
                }

                public String getTax_amount() {
                    return tax_amount;
                }

                public String getTip_percent() {
                    return tip_percent;
                }

                public String getTip_amount() {
                    return tip_amount;
                }

                public String getPrice() {
                    return price;
                }

                public String getTotal_price() {
                    return total_price;
                }

                public String getDescription() {
                    return description;
                }

                public String getMax_guests() {
                    return max_guests;
                }
            }

        }

    }

}
