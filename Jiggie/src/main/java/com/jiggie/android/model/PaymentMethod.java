package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by Wandy on 4/15/2016.
 */
public final class PaymentMethod {
    public final long response;
    public final String msg;
    public final Data data;

    public PaymentMethod(long response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static final class Data {
        public final ArrayList<Paymentmethod> paymentmethod;

        public Data(ArrayList<Paymentmethod> paymentmethod){
            this.paymentmethod = paymentmethod;
        }

        public static final class Paymentmethod {
            public  String _id;
            public  String type;
            public  boolean status;

            public Paymentmethod(String _id, String type, boolean status){
                this._id = _id;
                this.type = type;
                this.status = status;
            }
        }
    }
}