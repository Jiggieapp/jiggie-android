package com.jiggie.android.model;

/**
 * Created by LTE on 2/22/2016.
 */
public final class SummaryModel {
    public final long response;
    public final String msg;
    public final Data data;

    public long getResponse() {
        return response;
    }

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }

    public SummaryModel(long response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static final class Data {
        public final Product_summary product_summary;

        public Data(Product_summary product_summary){
            this.product_summary = product_summary;
        }

        public static final class Product_summary {
            public final String code;
            public final String order_status;
            public final String payment_status;
            public final long order_id;
            public final String fb_id;
            public final String event_id;
            public final String event_name;
            public final Product_list product_list[];
            public final String total_tax_amount;
            public final String total_tip_amount;
            public final String total_adminfee;
            public final String total_price;
            public final Credit_card credit_card[];

            public Product_summary(String code, String order_status, String payment_status, long order_id, String fb_id, String event_id, String event_name, Product_list[] product_list, String total_tax_amount, String total_tip_amount, String total_adminfee, String total_price, Credit_card[] credit_card){
                this.code = code;
                this.order_status = order_status;
                this.payment_status = payment_status;
                this.order_id = order_id;
                this.fb_id = fb_id;
                this.event_id = event_id;
                this.event_name = event_name;
                this.product_list = product_list;
                this.total_tax_amount = total_tax_amount;
                this.total_tip_amount = total_tip_amount;
                this.total_adminfee = total_adminfee;
                this.total_price = total_price;
                this.credit_card = credit_card;
            }

            public static final class Product_list {
                public final String ticket_id;
                public final String name;
                public final String ticket_type;
                public final String quantity;
                public final String admin_fee;
                public final String tax_percent;
                public final String tax_amount;
                public final String tip_percent;
                public final String tip_amount;
                public final String price;
                public final String total_price;
                public final String num_buy;
                public final String total_price_all;
                public final Term terms[];

                public Product_list(String ticket_id, String name, String ticket_type, String quantity, String admin_fee, String tax_percent, String tax_amount, String tip_percent, String tip_amount, String price, String total_price, String num_buy, String total_price_all, Term[] terms){
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
                    this.total_price_all = total_price_all;
                    this.terms = terms;
                }

                public static final class Term {
                    public final String label;
                    public final String body;

                    public Term(String label, String body){
                        this.label = label;
                        this.body = body;
                    }
                }
            }

            public static final class Credit_card {

                public Credit_card(){
                }
            }
        }
    }
}
