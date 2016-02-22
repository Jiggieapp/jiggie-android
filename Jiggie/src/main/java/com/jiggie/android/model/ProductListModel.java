package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/22/2016.
 */
public class ProductListModel {

    int response;
    String msg;
    Data data;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
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
            ArrayList<Purchase> purchase;
            ArrayList<Reservation> reservation;

            public String getEvent_id() {
                return event_id;
            }

            public void setEvent_id(String event_id) {
                this.event_id = event_id;
            }

            public String getEvent_name() {
                return event_name;
            }

            public void setEvent_name(String event_name) {
                this.event_name = event_name;
            }

            public ArrayList<Purchase> getPurchase() {
                return purchase;
            }

            public void setPurchase(ArrayList<Purchase> purchase) {
                this.purchase = purchase;
            }

            public ArrayList<Reservation> getReservation() {
                return reservation;
            }

            public void setReservation(ArrayList<Reservation> reservation) {
                this.reservation = reservation;
            }

            public static class Purchase{
                String ticket_id;
                String name;
                String ticket_type;
                String quantity;
                String admin_fee;
                String tax_percent;
                String tax_amount;
                String tip_percent;
                String tip_amount;
                String price;
                String total_price;

                public String getTicket_id() {
                    return ticket_id;
                }

                public void setTicket_id(String ticket_id) {
                    this.ticket_id = ticket_id;
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

                public String getQuantity() {
                    return quantity;
                }

                public void setQuantity(String quantity) {
                    this.quantity = quantity;
                }

                public String getAdmin_fee() {
                    return admin_fee;
                }

                public void setAdmin_fee(String admin_fee) {
                    this.admin_fee = admin_fee;
                }

                public String getTax_percent() {
                    return tax_percent;
                }

                public void setTax_percent(String tax_percent) {
                    this.tax_percent = tax_percent;
                }

                public String getTax_amount() {
                    return tax_amount;
                }

                public void setTax_amount(String tax_amount) {
                    this.tax_amount = tax_amount;
                }

                public String getTip_percent() {
                    return tip_percent;
                }

                public void setTip_percent(String tip_percent) {
                    this.tip_percent = tip_percent;
                }

                public String getTip_amount() {
                    return tip_amount;
                }

                public void setTip_amount(String tip_amount) {
                    this.tip_amount = tip_amount;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getTotal_price() {
                    return total_price;
                }

                public void setTotal_price(String total_price) {
                    this.total_price = total_price;
                }
            }

            public static class Reservation{
                String ticket_id;
                String name;
                String ticket_type;
                String quantity;
                String admin_fee;
                String tax_percent;
                String tax_amount;
                String tip_percent;
                String tip_amount;
                String price;
                String total_price;

                public String getTicket_id() {
                    return ticket_id;
                }

                public void setTicket_id(String ticket_id) {
                    this.ticket_id = ticket_id;
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

                public String getQuantity() {
                    return quantity;
                }

                public void setQuantity(String quantity) {
                    this.quantity = quantity;
                }

                public String getAdmin_fee() {
                    return admin_fee;
                }

                public void setAdmin_fee(String admin_fee) {
                    this.admin_fee = admin_fee;
                }

                public String getTax_percent() {
                    return tax_percent;
                }

                public void setTax_percent(String tax_percent) {
                    this.tax_percent = tax_percent;
                }

                public String getTax_amount() {
                    return tax_amount;
                }

                public void setTax_amount(String tax_amount) {
                    this.tax_amount = tax_amount;
                }

                public String getTip_percent() {
                    return tip_percent;
                }

                public void setTip_percent(String tip_percent) {
                    this.tip_percent = tip_percent;
                }

                public String getTip_amount() {
                    return tip_amount;
                }

                public void setTip_amount(String tip_amount) {
                    this.tip_amount = tip_amount;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getTotal_price() {
                    return total_price;
                }

                public void setTotal_price(String total_price) {
                    this.total_price = total_price;
                }
            }

        }

    }

}
