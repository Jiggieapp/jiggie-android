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
            public final LastPayment last_payment;
            public final Credit credit;
            public final Discount discount;

            public Product_summary(String code, String order_status, String payment_status, long order_id, String fb_id, String event_id, String event_name, ArrayList<Product_list> product_list, Guest_detail guest_detail, String total_tax_amount, String total_tip_amount, String total_adminfee, String total_price, LastPayment last_payment, Credit credit, Discount discount){
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
                this.last_payment = last_payment;
                this.credit = credit;
                this.discount = discount;
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

            public LastPayment getLast_payment() {
                return last_payment;
            }

            public Credit getCredit() {
                return credit;
            }

            public Discount getDiscount() {
                return discount;
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
                last_payment = (LastPayment) in.readValue(LastPayment.class.getClassLoader());
                credit = (Credit) in.readValue(Credit.class.getClassLoader());
                discount = (Discount) in.readValue(Discount.class.getClassLoader());
            }

            public static class LastPayment implements Parcelable {
                String masked_card;
                String saved_token_id;
                String payment_type;
                String saved_token_id_expired_at;

                public LastPayment(String masked_card, String saved_token_id, String payment_type, String saved_token_id_expired_at){
                    this.masked_card = masked_card;
                    this.saved_token_id = saved_token_id;
                    this.payment_type = payment_type;
                    this.saved_token_id_expired_at = saved_token_id_expired_at;
                }

                public String getMasked_card() {
                    return masked_card;
                }

                public String getSaved_token_id() {
                    return saved_token_id;
                }

                public void setPayment_type(String payment_type)
                {
                    this.payment_type = payment_type;
                }

                public String getPayment_type() {
                    return payment_type;
                }

                public String getSaved_token_id_expired_at() {
                    return saved_token_id_expired_at;
                }

                protected LastPayment(Parcel in) {
                    masked_card = in.readString();
                    saved_token_id = in.readString();
                    payment_type = in.readString();
                    saved_token_id_expired_at = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(masked_card);
                    dest.writeString(saved_token_id);
                    dest.writeString(payment_type);
                    dest.writeString(saved_token_id_expired_at);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<LastPayment> CREATOR = new Parcelable.Creator<LastPayment>() {
                    @Override
                    public LastPayment createFromParcel(Parcel in) {
                        return new LastPayment(in);
                    }

                    @Override
                    public LastPayment[] newArray(int size) {
                        return new LastPayment[size];
                    }
                };

                public boolean isEmpty(){
                    boolean empty = false;
                    if(payment_type==null){
                        empty = true;
                    }
                    return empty;
                }
            }

            public static class Credit implements Parcelable {
                int credit_used;

                public Credit(int credit_used){
                    this.credit_used = credit_used;
                }

                public int getCredit_used() {
                    return credit_used;
                }

                protected Credit(Parcel in) {
                    credit_used = in.readInt();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(credit_used);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<Credit> CREATOR = new Parcelable.Creator<Credit>() {
                    @Override
                    public Credit createFromParcel(Parcel in) {
                        return new Credit(in);
                    }

                    @Override
                    public Credit[] newArray(int size) {
                        return new Credit[size];
                    }
                };
            }

            public static final class Discount implements Parcelable {
                public final ArrayList<Data_> data;
                public final int total_discount;
                public final int tot_price_before_discount;
                public final int tot_price_after_discount;

                public Discount(ArrayList<Data_> data, int total_discount, int tot_price_before_discount, int tot_price_after_discount){
                    this.data = data;
                    this.total_discount = total_discount;
                    this.tot_price_before_discount = tot_price_before_discount;
                    this.tot_price_after_discount = tot_price_after_discount;
                }

                public ArrayList<Data_> getData() {
                    return data;
                }

                public int getTotal_discount() {
                    return total_discount;
                }

                public int getTot_price_before_discount() {
                    return tot_price_before_discount;
                }

                public int getTot_price_after_discount() {
                    return tot_price_after_discount;
                }

                public static final class Data_ implements Parcelable {
                    public final String name;
                    public final int amount_used;
                    public final String start_date;
                    public final String end_date;

                    public Data_(String name, int amount_used, String start_date, String end_date){
                        this.name = name;
                        this.amount_used = amount_used;
                        this.start_date = start_date;
                        this.end_date = end_date;
                    }

                    public String getName() {
                        return name;
                    }

                    public int getAmount_used() {
                        return amount_used;
                    }

                    public String getStart_date() {
                        return start_date;
                    }

                    public String getEnd_date() {
                        return end_date;
                    }

                    protected Data_(Parcel in) {
                        name = in.readString();
                        amount_used = in.readInt();
                        start_date = in.readString();
                        end_date = in.readString();
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(name);
                        dest.writeInt(amount_used);
                        dest.writeString(start_date);
                        dest.writeString(end_date);
                    }

                    @SuppressWarnings("unused")
                    public static final Parcelable.Creator<Data_> CREATOR = new Parcelable.Creator<Data_>() {
                        @Override
                        public Data_ createFromParcel(Parcel in) {
                            return new Data_(in);
                        }

                        @Override
                        public Data_[] newArray(int size) {
                            return new Data_[size];
                        }
                    };
                }

                protected Discount(Parcel in) {
                    if (in.readByte() == 0x01) {
                        data = new ArrayList<Data_>();
                        in.readList(data, Data_.class.getClassLoader());
                    } else {
                        data = null;
                    }
                    total_discount = in.readInt();
                    tot_price_before_discount = in.readInt();
                    tot_price_after_discount = in.readInt();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    if (data == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(data);
                    }
                    dest.writeInt(total_discount);
                    dest.writeInt(tot_price_before_discount);
                    dest.writeInt(tot_price_after_discount);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<Discount> CREATOR = new Parcelable.Creator<Discount>() {
                    @Override
                    public Discount createFromParcel(Parcel in) {
                        return new Discount(in);
                    }

                    @Override
                    public Discount[] newArray(int size) {
                        return new Discount[size];
                    }
                };
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
                dest.writeValue(last_payment);
                dest.writeValue(credit);
                dest.writeValue(discount);
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