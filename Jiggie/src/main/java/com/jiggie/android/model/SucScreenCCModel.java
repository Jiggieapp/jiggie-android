package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 3/19/2016.
 */
public final class SucScreenCCModel {
    public final long response;
    public final String msg;
    public final Data data;

    public SucScreenCCModel(long response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public long getResponse() {
        return response;
    }

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }

    public static final class Data {
        public final Success_screen success_screen;

        public Data(Success_screen success_screen){
            this.success_screen = success_screen;
        }

        public Success_screen getSuccess_screen() {
            return success_screen;
        }

        public static final class Success_screen {
            public final String order_id;
            public final String order_number;
            public final String order_status;
            public final String payment_status;
            public final String type;
            public final String payment_type;
            public final Event event;
            public final Summary summary;
            public final String instructions;
            public final ArrayList<String> ticket_include;
            public final ArrayList<String> fine_print;
            public final Credit credit;
            public final Discount discount;

            public Success_screen(String order_id, String order_number, String order_status, String payment_status, String type, String payment_type, Event event, Summary summary, String instructions, ArrayList<String> ticket_include, ArrayList<String> fine_print, Credit credit, Discount discount){
                this.order_id = order_id;
                this.order_number = order_number;
                this.order_status = order_status;
                this.payment_status = payment_status;
                this.type = type;
                this.payment_type = payment_type;
                this.event = event;
                this.summary = summary;
                this.instructions = instructions;
                this.ticket_include = ticket_include;
                this.fine_print = fine_print;
                this.credit = credit;
                this.discount = discount;
            }

            public String getOrder_id() {
                return order_id;
            }

            public String getOrder_number() {
                return order_number;
            }

            public String getOrder_status() {
                return order_status;
            }

            public String getPayment_status() {
                return payment_status;
            }

            public String getType() {
                return type;
            }

            public String getPayment_type() {
                return payment_type;
            }

            public Event getEvent() {
                return event;
            }

            public Summary getSummary() {
                return summary;
            }

            public String getInstructions() {
                return instructions;
            }

            public ArrayList<String> getTicket_include() {
                return ticket_include;
            }

            public ArrayList<String> getFine_print() {
                return fine_print;
            }

            public Credit getCredit() {
                return credit;
            }

            public Discount getDiscount() {
                return discount;
            }

            public final class Credit {
                public final String credit_left;
                public final String tot_price_after_credit;
                public final String tot_price_before_credit;
                public final String credit_used;
                public final String tot_credit;

                public Credit(String credit_left, String tot_price_after_credit, String tot_price_before_credit, String credit_used, String tot_credit){
                    this.credit_left = credit_left;
                    this.tot_price_after_credit = tot_price_after_credit;
                    this.tot_price_before_credit = tot_price_before_credit;
                    this.credit_used = credit_used;
                    this.tot_credit = tot_credit;
                }

                public String getCredit_left() {
                    return credit_left;
                }

                public String getTot_price_after_credit() {
                    return tot_price_after_credit;
                }

                public String getTot_price_before_credit() {
                    return tot_price_before_credit;
                }

                public String getCredit_used() {
                    return credit_used;
                }

                public String getTot_credit() {
                    return tot_credit;
                }
            }

            public static final class Discount {
                public final String tot_price_after_discount;
                public final String tot_price_before_discount;
                public final String total_discount;
                public final ArrayList<Data_> data;

                public Discount(String tot_price_after_discount, String tot_price_before_discount, String total_discount, ArrayList<Data_> data){
                    this.tot_price_after_discount = tot_price_after_discount;
                    this.tot_price_before_discount = tot_price_before_discount;
                    this.total_discount = total_discount;
                    this.data = data;
                }

                public String getTot_price_after_discount() {
                    return tot_price_after_discount;
                }

                public String getTot_price_before_discount() {
                    return tot_price_before_discount;
                }

                public String getTotal_discount() {
                    return total_discount;
                }

                public ArrayList<Data_> getData() {
                    return data;
                }

                public static final class Data_ {
                    public final String end_date;
                    public final String start_date;
                    public final String amount_used;
                    public final String name;

                    public Data_(String end_date, String start_date, String amount_used, String name){
                        this.end_date = end_date;
                        this.start_date = start_date;
                        this.amount_used = amount_used;
                        this.name = name;
                    }

                    public String getEnd_date() {
                        return end_date;
                    }

                    public String getStart_date() {
                        return start_date;
                    }

                    public String getAmount_used() {
                        return amount_used;
                    }

                    public String getName() {
                        return name;
                    }
                }
            }

            public static final class Event {
                public final String _id;
                public final String event_type;
                public final String event_id;
                public final String start_date;
                public final long start_time;
                public final String start_datetime;
                public final String end_date;
                public final long end_time;
                public final String end_datetime;
                public final String venue_id;
                public final String venue_name;
                public final String date_full;
                public final String start_datetime_str;
                public final String end_datetime_str;
                public final String fullfillment_type;
                public final String fullfillment_value;
                public final long event_time;
                public final Invited invited[];
                public final Accepted accepted[];
                public final Confirmed confirmed[];
                public final Guestconfirmed guestconfirmed[];
                public final Hostconfirmed hostconfirmed[];
                public final Rejected rejected[];
                public final Cancelled cancelled[];
                public final Host hosts[];
                public final Hoster hosters[];
                //public final String source;
                public final String description;
                public final String location;
                public final long rank;
                public final String title;
                public final boolean inherits;
                public final String status;
                public final ArrayList<String> tags;
                public final String end_series_datetime;
                public final String created_at;
                public final String updated_at;

                public Event(String _id, String event_type, String event_id, String start_date, long start_time, String start_datetime, String end_date, long end_time, String end_datetime, String venue_id, String venue_name, String date_full, String start_datetime_str, String end_datetime_str, String fullfillment_type, String fullfillment_value, long event_time, Invited[] invited, Accepted[] accepted, Confirmed[] confirmed, Guestconfirmed[] guestconfirmed, Hostconfirmed[] hostconfirmed, Rejected[] rejected, Cancelled[] cancelled, Host[] hosts, Hoster[] hosters/*, String source*/, String description, String location, long rank, String title, boolean inherits, String status, ArrayList<String> tags, String end_series_datetime, String created_at, String updated_at){
                    this._id = _id;
                    this.event_type = event_type;
                    this.event_id = event_id;
                    this.start_date = start_date;
                    this.start_time = start_time;
                    this.start_datetime = start_datetime;
                    this.end_date = end_date;
                    this.end_time = end_time;
                    this.end_datetime = end_datetime;
                    this.venue_id = venue_id;
                    this.venue_name = venue_name;
                    this.date_full = date_full;
                    this.start_datetime_str = start_datetime_str;
                    this.end_datetime_str = end_datetime_str;
                    this.fullfillment_type = fullfillment_type;
                    this.fullfillment_value = fullfillment_value;
                    this.event_time = event_time;
                    this.invited = invited;
                    this.accepted = accepted;
                    this.confirmed = confirmed;
                    this.guestconfirmed = guestconfirmed;
                    this.hostconfirmed = hostconfirmed;
                    this.rejected = rejected;
                    this.cancelled = cancelled;
                    this.hosts = hosts;
                    this.hosters = hosters;
                    //this.source = source;
                    this.description = description;
                    this.location = location;
                    this.rank = rank;
                    this.title = title;
                    this.inherits = inherits;
                    this.status = status;
                    this.tags = tags;
                    this.end_series_datetime = end_series_datetime;
                    this.created_at = created_at;
                    this.updated_at = updated_at;
                }

                public String get_id() {
                    return _id;
                }

                public String getEvent_type() {
                    return event_type;
                }

                public String getEvent_id() {
                    return event_id;
                }

                public String getStart_date() {
                    return start_date;
                }

                public long getStart_time() {
                    return start_time;
                }

                public String getStart_datetime() {
                    return start_datetime;
                }

                public String getEnd_date() {
                    return end_date;
                }

                public long getEnd_time() {
                    return end_time;
                }

                public String getEnd_datetime() {
                    return end_datetime;
                }

                public String getVenue_id() {
                    return venue_id;
                }

                public String getVenue_name() {
                    return venue_name;
                }

                public String getDate_full() {
                    return date_full;
                }

                public String getStart_datetime_str() {
                    return start_datetime_str;
                }

                public String getEnd_datetime_str() {
                    return end_datetime_str;
                }

                public String getFullfillment_type() {
                    return fullfillment_type;
                }

                public String getFullfillment_value() {
                    return fullfillment_value;
                }

                public long getEvent_time() {
                    return event_time;
                }

                public Invited[] getInvited() {
                    return invited;
                }

                public Accepted[] getAccepted() {
                    return accepted;
                }

                public Confirmed[] getConfirmed() {
                    return confirmed;
                }

                public Guestconfirmed[] getGuestconfirmed() {
                    return guestconfirmed;
                }

                public Hostconfirmed[] getHostconfirmed() {
                    return hostconfirmed;
                }

                public Rejected[] getRejected() {
                    return rejected;
                }

                public Cancelled[] getCancelled() {
                    return cancelled;
                }

                public Host[] getHosts() {
                    return hosts;
                }

                public Hoster[] getHosters() {
                    return hosters;
                }

                /*public String getSource() {
                    return source;
                }*/

                public String getDescription() {
                    return description;
                }

                public String getLocation() {
                    return location;
                }

                public long getRank() {
                    return rank;
                }

                public String getTitle() {
                    return title;
                }

                public boolean isInherits() {
                    return inherits;
                }

                public String getStatus() {
                    return status;
                }

                public ArrayList<String> getTags() {
                    return tags;
                }

                public String getEnd_series_datetime() {
                    return end_series_datetime;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public static final class Invited {

                    public Invited(){
                    }
                }

                public static final class Accepted {

                    public Accepted(){
                    }
                }

                public static final class Confirmed {

                    public Confirmed(){
                    }
                }

                public static final class Guestconfirmed {

                    public Guestconfirmed(){
                    }
                }

                public static final class Hostconfirmed {

                    public Hostconfirmed(){
                    }
                }

                public static final class Rejected {

                    public Rejected(){
                    }
                }

                public static final class Cancelled {

                    public Cancelled(){
                    }
                }

                public static final class Host {

                    public Host(){
                    }
                }

                public static final class Hoster {

                    public Hoster(){
                    }
                }
            }

            public static final class Summary {
                public final String _id;
                public final String code;
                public final String order_status;
                public final String payment_status;
                public final String fb_id;
                public final String event_id;
                public final String total_tax_amount;
                public final String total_tip_amount;
                public final String total_adminfee;
                public final String total_price;
                public final String created_at;
                public final Guest_detail guest_detail;
                public final ArrayList<Product_list> product_list;
                public final String order_id;
                public final Vt_response vt_response;
                public final String pay_deposit;

                public Summary(String _id, String code, String order_status, String payment_status, String fb_id, String event_id, String total_tax_amount, String total_tip_amount, String total_adminfee, String total_price, String created_at, Guest_detail guest_detail, ArrayList<Product_list> product_list, String order_id, Vt_response vt_response, String pay_deposit){
                    this._id = _id;
                    this.code = code;
                    this.order_status = order_status;
                    this.payment_status = payment_status;
                    this.fb_id = fb_id;
                    this.event_id = event_id;
                    this.total_tax_amount = total_tax_amount;
                    this.total_tip_amount = total_tip_amount;
                    this.total_adminfee = total_adminfee;
                    this.total_price = total_price;
                    this.created_at = created_at;
                    this.guest_detail = guest_detail;
                    this.product_list = product_list;
                    this.order_id = order_id;
                    this.vt_response = vt_response;
                    this.pay_deposit = pay_deposit;
                }

                public String get_id() {
                    return _id;
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

                public String getFb_id() {
                    return fb_id;
                }

                public String getEvent_id() {
                    return event_id;
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

                public String getCreated_at() {
                    return created_at;
                }

                public Guest_detail getGuest_detail() {
                    return guest_detail;
                }

                public ArrayList<Product_list> getProduct_list() {
                    return product_list;
                }

                public String getOrder_id() {
                    return order_id;
                }

                public Vt_response getVt_response() {
                    return vt_response;
                }

                public String getPay_deposit() {
                    return pay_deposit;
                }

                public static final class Guest_detail {
                    public final String email;
                    public final String name;
                    public final String phone;

                    public Guest_detail(String email, String name, String phone){
                        this.email = email;
                        this.name = name;
                        this.phone = phone;
                    }

                    public String getEmail() {
                        return email;
                    }

                    public String getName() {
                        return name;
                    }

                    public String getPhone() {
                        return phone;
                    }
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
                    public final String payment_timelimit;
                    public final String _id;
                    public final ArrayList<Term> terms;
                    public final String extra_charge;
                    public final String sale_type;

                    public Product_list(String ticket_id, String name, String ticket_type, String max_buy, String quantity, String admin_fee, String tax_percent, String tax_amount, String tip_percent, String tip_amount, String price, String currency, String total_price, String total_price_aftertax, String num_buy, String total_price_all, String payment_timelimit, String _id, ArrayList<Term> terms, String extra_charge, String sale_type){
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
                        this.payment_timelimit = payment_timelimit;
                        this._id = _id;
                        this.terms = terms;
                        this.extra_charge = extra_charge;
                        this.sale_type = sale_type;
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

                    public String getPayment_timelimit() {
                        return payment_timelimit;
                    }

                    public String get_id() {
                        return _id;
                    }

                    public ArrayList<Term> getTerms() {
                        return terms;
                    }

                    public String getExtra_charge() {
                        return extra_charge;
                    }

                    public String getSale_type() {
                        return sale_type;
                    }

                    public static final class Term {
                        public final String _id;
                        public final String body;
                        public final String label;

                        public Term(String _id, String body, String label){
                            this._id = _id;
                            this.body = body;
                            this.label = label;
                        }

                        public String get_id() {
                            return _id;
                        }

                        public String getBody() {
                            return body;
                        }

                        public String getLabel() {
                            return label;
                        }
                    }
                }

                public static final class Vt_response {
                    public final String status_code;
                    public final String status_message;
                    public final String transaction_id;
                    public final String saved_token_id;
                    public final String masked_card;
                    public final String order_id;
                    public final String gross_amount;
                    public final String payment_type;
                    public final String transaction_time;
                    public final String transaction_status;
                    public final String fraud_status;
                    public final String saved_token_id_expired_at;
                    public final String approval_code;
                    public final boolean secure_token;
                    public final String bank;
                    public final String eci;

                    public Vt_response(String status_code, String status_message, String transaction_id, String saved_token_id, String masked_card, String order_id, String gross_amount, String payment_type, String transaction_time, String transaction_status, String fraud_status, String saved_token_id_expired_at, String approval_code, boolean secure_token, String bank, String eci){
                        this.status_code = status_code;
                        this.status_message = status_message;
                        this.transaction_id = transaction_id;
                        this.saved_token_id = saved_token_id;
                        this.masked_card = masked_card;
                        this.order_id = order_id;
                        this.gross_amount = gross_amount;
                        this.payment_type = payment_type;
                        this.transaction_time = transaction_time;
                        this.transaction_status = transaction_status;
                        this.fraud_status = fraud_status;
                        this.saved_token_id_expired_at = saved_token_id_expired_at;
                        this.approval_code = approval_code;
                        this.secure_token = secure_token;
                        this.bank = bank;
                        this.eci = eci;
                    }

                    public String getStatus_code() {
                        return status_code;
                    }

                    public String getStatus_message() {
                        return status_message;
                    }

                    public String getTransaction_id() {
                        return transaction_id;
                    }

                    public String getSaved_token_id() {
                        return saved_token_id;
                    }

                    public String getMasked_card() {
                        return masked_card;
                    }

                    public String getOrder_id() {
                        return order_id;
                    }

                    public String getGross_amount() {
                        return gross_amount;
                    }

                    public String getPayment_type() {
                        return payment_type;
                    }

                    public String getTransaction_time() {
                        return transaction_time;
                    }

                    public String getTransaction_status() {
                        return transaction_status;
                    }

                    public String getFraud_status() {
                        return fraud_status;
                    }

                    public String getSaved_token_id_expired_at() {
                        return saved_token_id_expired_at;
                    }

                    public String getApproval_code() {
                        return approval_code;
                    }

                    public boolean isSecure_token() {
                        return secure_token;
                    }

                    public String getBank() {
                        return bank;
                    }

                    public String getEci() {
                        return eci;
                    }
                }
            }
        }
    }
}
