package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/22/2016.
 */
public final class SummaryModel {
    public final int response;
    public final String msg;
    public final Data data;

    public SummaryModel(int response, String msg, Data data){
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

    public static final class Data {
        public final Product_summary product_summary;

        public Data(Product_summary product_summary){
            this.product_summary = product_summary;
        }

        public Product_summary getProduct_summary() {
            return product_summary;
        }

        public static final class Product_summary {
            public final String code;
            public final String order_status;
            public final String payment_status;
            public final long order_id;
            public final String fb_id;
            public final String event_id;
            public final String event_name;
            public final ArrayList<Product_list> product_list;
            public final Guest_detail guest_detail;
            public final String total_tax_amount;
            public final String total_tip_amount;
            public final String total_adminfee;
            public final String total_price;
            public final ArrayList<Credit_card> credit_card;

            public Product_summary(String code, String order_status, String payment_status, long order_id, String fb_id, String event_id, String event_name, ArrayList<Product_list> product_list, Guest_detail guest_detail, String total_tax_amount, String total_tip_amount, String total_adminfee, String total_price, ArrayList<Credit_card> credit_card){
                this.code = code;
                this.order_status = order_status;
                this.payment_status = payment_status;
                this.order_id = order_id;
                this.fb_id = fb_id;
                this.event_id = event_id;
                this.event_name = event_name;
                this.product_list = product_list;
                this.guest_detail = guest_detail;
                this.total_tax_amount = total_tax_amount;
                this.total_tip_amount = total_tip_amount;
                this.total_adminfee = total_adminfee;
                this.total_price = total_price;
                this.credit_card = credit_card;
            }

            public String getCode() {
                return code;
            }

            public String getOrder_status() {
                return order_status;
            }

            public String getPayment_status() {
                return payment_status;
            }

            public long getOrder_id() {
                return order_id;
            }

            public String getFb_id() {
                return fb_id;
            }

            public String getEvent_id() {
                return event_id;
            }

            public String getEvent_name() {
                return event_name;
            }

            public ArrayList<Product_list> getProduct_list() {
                return product_list;
            }

            public Guest_detail getGuest_detail() {
                return guest_detail;
            }

            public String getTotal_tax_amount() {
                return total_tax_amount;
            }

            public String getTotal_tip_amount() {
                return total_tip_amount;
            }

            public String getTotal_adminfee() {
                return total_adminfee;
            }

            public String getTotal_price() {
                return total_price;
            }

            public ArrayList<Credit_card> getCredit_card() {
                return credit_card;
            }

            public static final class Product_list {
                public final String ticket_id;
                public final String name;
                public final String ticket_type;
                public final String max_buy;
                public final String quantity;
                public final String admin_fee;
                public final String tax_percent;
                public final String tax_amount;
                public final String tip_percent;
                public final String tip_amount;
                public final String price;
                public final String currency;
                public final String total_price;
                public final String total_price_aftertax;
                public final String num_buy;
                public final String total_price_all;
                public final ArrayList<Term> terms;
                public final int payment_timelimit;

                public Product_list(String ticket_id, String name, String ticket_type, String max_buy, String quantity, String admin_fee, String tax_percent, String tax_amount, String tip_percent, String tip_amount, String price, String currency, String total_price, String total_price_aftertax, String num_buy, String total_price_all, ArrayList<Term> terms, int payment_timelimit){
                    this.ticket_id = ticket_id;
                    this.name = name;
                    this.ticket_type = ticket_type;
                    this.max_buy = max_buy;
                    this.quantity = quantity;
                    this.admin_fee = admin_fee;
                    this.tax_percent = tax_percent;
                    this.tax_amount = tax_amount;
                    this.tip_percent = tip_percent;
                    this.tip_amount = tip_amount;
                    this.price = price;
                    this.currency = currency;
                    this.total_price = total_price;
                    this.total_price_aftertax = total_price_aftertax;
                    this.num_buy = num_buy;
                    this.total_price_all = total_price_all;
                    this.terms = terms;
                    this.payment_timelimit = payment_timelimit;
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

                public String getMax_buy() {
                    return max_buy;
                }

                public String getQuantity() {
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

                public String getCurrency() {
                    return currency;
                }

                public String getTotal_price() {
                    return total_price;
                }

                public String getTotal_price_aftertax() {
                    return total_price_aftertax;
                }

                public String getNum_buy() {
                    return num_buy;
                }

                public String getTotal_price_all() {
                    return total_price_all;
                }

                public ArrayList<Term> getTerms() {
                    return terms;
                }

                public int getPayment_timelimit() {
                    return payment_timelimit;
                }

                public static final class Term {
                    public final String label;
                    public final String body;

                    public Term(String label, String body){
                        this.label = label;
                        this.body = body;
                    }

                    public String getLabel() {
                        return label;
                    }

                    public String getBody() {
                        return body;
                    }
                }
            }

            public static final class Guest_detail {
                public final String name;
                public final String email;
                public final String phone;

                public Guest_detail(String name, String email, String phone){
                    this.name = name;
                    this.email = email;
                    this.phone = phone;
                }

                public String getName() {
                    return name;
                }

                public String getEmail() {
                    return email;
                }

                public String getPhone() {
                    return phone;
                }
            }

            public static final class Credit_card {

                public Credit_card(){
                }
            }
        }
    }
}