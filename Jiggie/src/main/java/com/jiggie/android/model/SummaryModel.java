package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by LTE on 2/22/2016.
 */
public final class SummaryModel{
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

        public static final class Product_summary implements Parcelable{
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
            public final Credit_card credit_card;

            public Product_summary(String code, String order_status, String payment_status, long order_id, String fb_id, String event_id, String event_name, ArrayList<Product_list> product_list, Guest_detail guest_detail, String total_tax_amount, String total_tip_amount, String total_adminfee, String total_price, Credit_card credit_card){
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

            public Credit_card getCredit_card() {
                return credit_card;
            }

            public static final class Product_list implements Parcelable {
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

                public static final class Term implements Parcelable {
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

                    protected Term(Parcel in) {
                        label = in.readString();
                        body = in.readString();
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(label);
                        dest.writeString(body);
                    }

                    @SuppressWarnings("unused")
                    public static final Parcelable.Creator<Term> CREATOR = new Parcelable.Creator<Term>() {
                        @Override
                        public Term createFromParcel(Parcel in) {
                            return new Term(in);
                        }

                        @Override
                        public Term[] newArray(int size) {
                            return new Term[size];
                        }
                    };
                }

                protected Product_list(Parcel in) {
                    ticket_id = in.readString();
                    name = in.readString();
                    ticket_type = in.readString();
                    max_buy = in.readString();
                    quantity = in.readString();
                    admin_fee = in.readString();
                    tax_percent = in.readString();
                    tax_amount = in.readString();
                    tip_percent = in.readString();
                    tip_amount = in.readString();
                    price = in.readString();
                    currency = in.readString();
                    total_price = in.readString();
                    total_price_aftertax = in.readString();
                    num_buy = in.readString();
                    total_price_all = in.readString();
                    if (in.readByte() == 0x01) {
                        terms = new ArrayList<Term>();
                        in.readList(terms, Term.class.getClassLoader());
                    } else {
                        terms = null;
                    }
                    payment_timelimit = in.readInt();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(ticket_id);
                    dest.writeString(name);
                    dest.writeString(ticket_type);
                    dest.writeString(max_buy);
                    dest.writeString(quantity);
                    dest.writeString(admin_fee);
                    dest.writeString(tax_percent);
                    dest.writeString(tax_amount);
                    dest.writeString(tip_percent);
                    dest.writeString(tip_amount);
                    dest.writeString(price);
                    dest.writeString(currency);
                    dest.writeString(total_price);
                    dest.writeString(total_price_aftertax);
                    dest.writeString(num_buy);
                    dest.writeString(total_price_all);
                    if (terms == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(terms);
                    }
                    dest.writeInt(payment_timelimit);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<Product_list> CREATOR = new Parcelable.Creator<Product_list>() {
                    @Override
                    public Product_list createFromParcel(Parcel in) {
                        return new Product_list(in);
                    }

                    @Override
                    public Product_list[] newArray(int size) {
                        return new Product_list[size];
                    }
                };
            }

            public static final class Guest_detail implements Parcelable {
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

                protected Guest_detail(Parcel in) {
                    name = in.readString();
                    email = in.readString();
                    phone = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(name);
                    dest.writeString(email);
                    dest.writeString(phone);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<Guest_detail> CREATOR = new Parcelable.Creator<Guest_detail>() {
                    @Override
                    public Guest_detail createFromParcel(Parcel in) {
                        return new Guest_detail(in);
                    }

                    @Override
                    public Guest_detail[] newArray(int size) {
                        return new Guest_detail[size];
                    }
                };
            }

            public static final class Credit_card implements Parcelable {

                public Credit_card(){
                }



                protected Credit_card(Parcel in) {

                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {

                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<Credit_card> CREATOR = new Parcelable.Creator<Credit_card>() {
                    @Override
                    public Credit_card createFromParcel(Parcel in) {
                        return new Credit_card(in);
                    }

                    @Override
                    public Credit_card[] newArray(int size) {
                        return new Credit_card[size];
                    }
                };
            }

            protected Product_summary(Parcel in) {
                code = in.readString();
                order_status = in.readString();
                payment_status = in.readString();
                order_id = in.readLong();
                fb_id = in.readString();
                event_id = in.readString();
                event_name = in.readString();
                if (in.readByte() == 0x01) {
                    product_list = new ArrayList<Product_list>();
                    in.readList(product_list, Product_list.class.getClassLoader());
                } else {
                    product_list = null;
                }
                guest_detail = (Guest_detail) in.readValue(Guest_detail.class.getClassLoader());
                total_tax_amount = in.readString();
                total_tip_amount = in.readString();
                total_adminfee = in.readString();
                total_price = in.readString();
                credit_card = (Credit_card) in.readValue(Credit_card.class.getClassLoader());
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(code);
                dest.writeString(order_status);
                dest.writeString(payment_status);
                dest.writeLong(order_id);
                dest.writeString(fb_id);
                dest.writeString(event_id);
                dest.writeString(event_name);
                if (product_list == null) {
                    dest.writeByte((byte) (0x00));
                } else {
                    dest.writeByte((byte) (0x01));
                    dest.writeList(product_list);
                }
                dest.writeValue(guest_detail);
                dest.writeString(total_tax_amount);
                dest.writeString(total_tip_amount);
                dest.writeString(total_adminfee);
                dest.writeString(total_price);
                dest.writeValue(credit_card);
            }

            @SuppressWarnings("unused")
            public static final Parcelable.Creator<Product_summary> CREATOR = new Parcelable.Creator<Product_summary>() {
                @Override
                public Product_summary createFromParcel(Parcel in) {
                    return new Product_summary(in);
                }

                @Override
                public Product_summary[] newArray(int size) {
                    return new Product_summary[size];
                }
            };
        }
    }
}