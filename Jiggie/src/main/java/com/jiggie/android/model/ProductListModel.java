package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

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

            public static class Purchase implements Parcelable{
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
                int payment_timelimit;
                String summary;
                String status;

                public Purchase(String ticket_id, String name, String ticket_type, int quantity, String admin_fee, String tax_percent, String tax_amount, String tip_percent, String tip_amount, String price, String total_price, String description, String max_purchase, int payment_timelimit, String summary, String status){
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
                    this.payment_timelimit = payment_timelimit;
                    this.summary = summary;
                    this.status = status;
                }

                protected Purchase(Parcel in) {
                    this.ticket_id = in.readString();
                    this.name = in.readString();
                    this.ticket_type = in.readString();
                    this.quantity = in.readInt();
                    this.admin_fee = in.readString();
                    this.tax_percent = in.readString();
                    this.tax_amount = in.readString();
                    this.tip_percent = in.readString();
                    this.tip_amount = in.readString();
                    this.price = in.readString();
                    this.total_price = in.readString();
                    this.description = in.readString();
                    this.max_purchase = in.readString();
                    this.payment_timelimit = in.readInt();
                    this.summary = in.readString();
                    this.status = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.ticket_id);
                    dest.writeString(this.name);
                    dest.writeString(this.ticket_type);
                    dest.writeInt(this.quantity);
                    dest.writeString(this.admin_fee);
                    dest.writeString(this.tax_percent);
                    dest.writeString(this.tax_amount);
                    dest.writeString(this.tip_percent);
                    dest.writeString(this.tip_amount);
                    dest.writeString(this.price);
                    dest.writeString(this.total_price);
                    dest.writeString(this.description);
                    dest.writeString(this.max_purchase);
                    dest.writeInt(this.payment_timelimit);
                    dest.writeString(this.summary);
                    dest.writeString(this.status);
                }

                public static final Creator<Purchase> CREATOR = new Creator<Purchase>() {
                    @Override
                    public Purchase createFromParcel(Parcel in) { return new Purchase(in); }
                    @Override
                    public Purchase[] newArray(int size) { return new Purchase[size]; }
                };

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

                public int getPayment_timelimit() {
                    return payment_timelimit;
                }

                public String getSummary() {
                    return summary;
                }

                public String getStatus() {
                    return status;
                }
            }

            public static class Reservation implements Parcelable{
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
                int payment_timelimit;
                String summary;
                String min_deposit_percent;
                String min_deposit_amount;
                String status;
                String extra_charge;
                String sale_type;

                public Reservation(String ticket_id, String name, String ticket_type, int quantity, String admin_fee, String tax_percent, String tax_amount, String tip_percent, String tip_amount, String price, String total_price, String description, String max_guests, int payment_timelimit, String summary, String min_deposit_percent, String min_deposit_amount, String status, String extra_charge, String sale_type){
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
                    this.payment_timelimit = payment_timelimit;
                    this.summary = summary;
                    this.min_deposit_percent = min_deposit_percent;
                    this.min_deposit_amount = min_deposit_amount;
                    this.status = status;
                    this.extra_charge = extra_charge;
                    this.sale_type = sale_type;
                }

                protected Reservation(Parcel in) {
                    this.ticket_id = in.readString();
                    this.name = in.readString();
                    this.ticket_type = in.readString();
                    this.quantity = in.readInt();
                    this.admin_fee = in.readString();
                    this.tax_percent = in.readString();
                    this.tax_amount = in.readString();
                    this.tip_percent = in.readString();
                    this.tip_amount = in.readString();
                    this.price = in.readString();
                    this.total_price = in.readString();
                    this.description = in.readString();
                    this.max_guests = in.readString();
                    this.payment_timelimit = in.readInt();
                    this.summary = in.readString();
                    this.min_deposit_percent = in.readString();
                    this.min_deposit_amount = in.readString();
                    this.status = in.readString();
                    this.extra_charge = in.readString();
                    this.sale_type = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.ticket_id);
                    dest.writeString(this.name);
                    dest.writeString(this.ticket_type);
                    dest.writeInt(this.quantity);
                    dest.writeString(this.admin_fee);
                    dest.writeString(this.tax_percent);
                    dest.writeString(this.tax_amount);
                    dest.writeString(this.tip_percent);
                    dest.writeString(this.tip_amount);
                    dest.writeString(this.price);
                    dest.writeString(this.total_price);
                    dest.writeString(this.description);
                    dest.writeString(this.max_guests);
                    dest.writeInt(this.payment_timelimit);
                    dest.writeString(this.summary);
                    dest.writeString(this.min_deposit_percent);
                    dest.writeString(this.min_deposit_amount);
                    dest.writeString(this.status);
                    dest.writeString(this.extra_charge);
                    dest.writeString(this.sale_type);
                }

                public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
                    @Override
                    public Reservation createFromParcel(Parcel in) { return new Reservation(in); }
                    @Override
                    public Reservation[] newArray(int size) { return new Reservation[size]; }
                };

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

                public int getPayment_timelimit() {
                    return payment_timelimit;
                }

                public String getSummary() {
                    return summary;
                }

                public String getMin_deposit_percent() {
                    return min_deposit_percent;
                }

                public String getMin_deposit_amount() {
                    return min_deposit_amount;
                }

                public String getStatus() {
                    return status;
                }

                public String getExtra_charge() {
                    return extra_charge;
                }

                public String getSale_type() {
                    return sale_type;
                }
            }

        }

    }

}
