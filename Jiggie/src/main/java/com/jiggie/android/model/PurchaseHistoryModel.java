package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by Wandy on 3/16/2016.
 */

public class PurchaseHistoryModel {
    public long response;
    public String msg;
    public Data data;

    public long getResponse() {
        return response;
    }

    public void setResponse(long response) {
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

    public PurchaseHistoryModel(long response, String msg, Data data) {
        this.response = response;
        this.msg = msg;
        this.data = data;

    }

    public static class Data {
        public ArrayList<Order_list> order_lists;

        public Data(ArrayList<Order_list> order_lists) {
            this.order_lists = order_lists;
        }

        public ArrayList<Order_list> getOrder_lists() {
            return order_lists;
        }

        public void setOrder_lists(ArrayList<Order_list> order_lists) {
            this.order_lists = order_lists;
        }

        public static class Order_list {
            public Order order;
            public Event event;

            public Order getOrder() {
                return order;
            }

            public void setOrder(Order order) {
                this.order = order;
            }

            public Event getEvent() {
                return event;
            }

            public void setEvent(Event event) {
                this.event = event;
            }

            public Order_list(Order order, Event event) {
                this.order = order;
                this.event = event;
            }

            public static class Order {
                public String _id;
                public String code;
                public String order_status;
                public String payment_status;
                public String fb_id;
                public String event_id;
                public String total_tax_amount;
                public String total_tip_amount;
                public String total_adminfee;
                public String total_price;
                public String created_at;

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getOrder_status() {
                    return order_status;
                }

                public void setOrder_status(String order_status) {
                    this.order_status = order_status;
                }

                public String getPayment_status() {
                    return payment_status;
                }

                public void setPayment_status(String payment_status) {
                    this.payment_status = payment_status;
                }

                public String getFb_id() {
                    return fb_id;
                }

                public void setFb_id(String fb_id) {
                    this.fb_id = fb_id;
                }

                public String getEvent_id() {
                    return event_id;
                }

                public void setEvent_id(String event_id) {
                    this.event_id = event_id;
                }

                public String getTotal_tax_amount() {
                    return total_tax_amount;
                }

                public void setTotal_tax_amount(String total_tax_amount) {
                    this.total_tax_amount = total_tax_amount;
                }

                public String getTotal_tip_amount() {
                    return total_tip_amount;
                }

                public void setTotal_tip_amount(String total_tip_amount) {
                    this.total_tip_amount = total_tip_amount;
                }

                public String getTotal_adminfee() {
                    return total_adminfee;
                }

                public void setTotal_adminfee(String total_adminfee) {
                    this.total_adminfee = total_adminfee;
                }

                public String getTotal_price() {
                    return total_price;
                }

                public void setTotal_price(String total_price) {
                    this.total_price = total_price;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public Guest_detail getGuest_detail() {
                    return guest_detail;
                }

                public void setGuest_detail(Guest_detail guest_detail) {
                    this.guest_detail = guest_detail;
                }

                public Product_list[] getProduct_list() {
                    return product_list;
                }

                public void setProduct_list(Product_list[] product_list) {
                    this.product_list = product_list;
                }

                public String getOrder_id() {
                    return order_id;
                }

                public void setOrder_id(String order_id) {
                    this.order_id = order_id;
                }

                public Guest_detail guest_detail;
                public Product_list product_list[];
                public String order_id;

                public Order(String _id, String code, String order_status, String payment_status, String fb_id, String event_id, String total_tax_amount, String total_tip_amount, String total_adminfee, String total_price, String created_at, Guest_detail guest_detail, Product_list[] product_list, String order_id) {
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
                }

                public static class Guest_detail {
                    public String name;
                    public String email;
                    public String phone;

                    public Guest_detail(String name, String email, String phone) {
                        this.name = name;
                        this.email = email;
                        this.phone = phone;
                    }
                }

                public static class Product_list {
                    public String ticket_id;
                    public String name;
                    public String ticket_type;
                    public String max_buy;
                    public String quantity;
                    public String admin_fee;
                    public String tax_percent;
                    public String tax_amount;
                    public String tip_percent;
                    public String tip_amount;
                    public String price;
                    public String currency;
                    public String total_price;
                    public String total_price_aftertax;
                    public String num_buy;
                    public String total_price_all;
                    public String payment_timelimit;
                    public String _id;
                    public ArrayList<Term> terms;

                    public Product_list(String ticket_id, String name, String ticket_type, String max_buy, String quantity, String admin_fee, String tax_percent, String tax_amount, String tip_percent, String tip_amount, String price, String currency, String total_price, String total_price_aftertax, String num_buy, String total_price_all, String payment_timelimit, String _id, ArrayList<Term> terms) {
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
                    }

                    public static class Term {
                        public String _id;
                        public String body;
                        public String label;

                        public Term(String _id, String body, String label) {
                            this._id = _id;
                            this.body = body;
                            this.label = label;
                        }
                    }
                }
            }

            public static class Event {
                public String _id;
                public String event_type;
                public String event_id;
                public String start_date;
                public long start_time;
                public String start_datetime;
                public String end_date;
                public long end_time;
                public String end_datetime;
                public String venue_id;
                public String venue_name;
                public String date_full;
                public String start_datetime_str;
                public String end_datetime_str;
                public String fullfillment_type;
                public String fullfillment_value;
                public long event_time;
                public ArrayList<Photo> photos;
                public ArrayList<Invited> invited;
                public ArrayList<Accepted> accepted;
                public ArrayList<Confirmed> confirmed;
                public ArrayList<Guestconfirmed> guestconfirmed;
                public ArrayList<Hostconfirmed> hostconfirmed;
                public ArrayList<Rejected> rejected;
                public ArrayList<Cancelled> cancelled;
                public ArrayList<Host> hosts;
                public ArrayList<Hoster> hosters;
                public String source;
                public String description;
                public String location;
                public long rank;
                public String title;
                public boolean inherits;
                public String status;
                public ArrayList<String> tags;
                public String end_series_datetime;
                public boolean visible;
                public String created_at;

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String getEvent_type() {
                    return event_type;
                }

                public void setEvent_type(String event_type) {
                    this.event_type = event_type;
                }

                public String getEvent_id() {
                    return event_id;
                }

                public void setEvent_id(String event_id) {
                    this.event_id = event_id;
                }

                public String getStart_date() {
                    return start_date;
                }

                public void setStart_date(String start_date) {
                    this.start_date = start_date;
                }

                public long getStart_time() {
                    return start_time;
                }

                public void setStart_time(long start_time) {
                    this.start_time = start_time;
                }

                public String getStart_datetime() {
                    return start_datetime;
                }

                public void setStart_datetime(String start_datetime) {
                    this.start_datetime = start_datetime;
                }

                public String getEnd_date() {
                    return end_date;
                }

                public void setEnd_date(String end_date) {
                    this.end_date = end_date;
                }

                public long getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(long end_time) {
                    this.end_time = end_time;
                }

                public String getEnd_datetime() {
                    return end_datetime;
                }

                public void setEnd_datetime(String end_datetime) {
                    this.end_datetime = end_datetime;
                }

                public String getVenue_id() {
                    return venue_id;
                }

                public void setVenue_id(String venue_id) {
                    this.venue_id = venue_id;
                }

                public String getVenue_name() {
                    return venue_name;
                }

                public void setVenue_name(String venue_name) {
                    this.venue_name = venue_name;
                }

                public String getDate_full() {
                    return date_full;
                }

                public void setDate_full(String date_full) {
                    this.date_full = date_full;
                }

                public String getStart_datetime_str() {
                    return start_datetime_str;
                }

                public void setStart_datetime_str(String start_datetime_str) {
                    this.start_datetime_str = start_datetime_str;
                }

                public String getEnd_datetime_str() {
                    return end_datetime_str;
                }

                public void setEnd_datetime_str(String end_datetime_str) {
                    this.end_datetime_str = end_datetime_str;
                }

                public String getFullfillment_type() {
                    return fullfillment_type;
                }

                public void setFullfillment_type(String fullfillment_type) {
                    this.fullfillment_type = fullfillment_type;
                }

                public String getFullfillment_value() {
                    return fullfillment_value;
                }

                public void setFullfillment_value(String fullfillment_value) {
                    this.fullfillment_value = fullfillment_value;
                }

                public long getEvent_time() {
                    return event_time;
                }

                public void setEvent_time(long event_time) {
                    this.event_time = event_time;
                }

                public ArrayList<Photo> getPhotos() {
                    return photos;
                }

                public void setPhotos(ArrayList<Photo> photos) {
                    this.photos = photos;
                }

                public ArrayList<Invited> getInvited() {
                    return invited;
                }

                public void setInvited(ArrayList<Invited> invited) {
                    this.invited = invited;
                }

                public ArrayList<Accepted> getAccepted() {
                    return accepted;
                }

                public void setAccepted(ArrayList<Accepted> accepted) {
                    this.accepted = accepted;
                }

                public ArrayList<Confirmed> getConfirmed() {
                    return confirmed;
                }

                public void setConfirmed(ArrayList<Confirmed> confirmed) {
                    this.confirmed = confirmed;
                }

                public ArrayList<Guestconfirmed> getGuestconfirmed() {
                    return guestconfirmed;
                }

                public void setGuestconfirmed(ArrayList<Guestconfirmed> guestconfirmed) {
                    this.guestconfirmed = guestconfirmed;
                }

                public ArrayList<Hostconfirmed> getHostconfirmed() {
                    return hostconfirmed;
                }

                public void setHostconfirmed(ArrayList<Hostconfirmed> hostconfirmed) {
                    this.hostconfirmed = hostconfirmed;
                }

                public ArrayList<Rejected> getRejected() {
                    return rejected;
                }

                public void setRejected(ArrayList<Rejected> rejected) {
                    this.rejected = rejected;
                }

                public ArrayList<Cancelled> getCancelled() {
                    return cancelled;
                }

                public void setCancelled(ArrayList<Cancelled> cancelled) {
                    this.cancelled = cancelled;
                }

                public ArrayList<Host> getHosts() {
                    return hosts;
                }

                public void setHosts(ArrayList<Host> hosts) {
                    this.hosts = hosts;
                }

                public ArrayList<Hoster> getHosters() {
                    return hosters;
                }

                public void setHosters(ArrayList<Hoster> hosters) {
                    this.hosters = hosters;
                }

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public long getRank() {
                    return rank;
                }

                public void setRank(long rank) {
                    this.rank = rank;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public boolean isInherits() {
                    return inherits;
                }

                public void setInherits(boolean inherits) {
                    this.inherits = inherits;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public ArrayList<String> getTags() {
                    return tags;
                }

                public void setTags(ArrayList<String> tags) {
                    this.tags = tags;
                }

                public String getEnd_series_datetime() {
                    return end_series_datetime;
                }

                public void setEnd_series_datetime(String end_series_datetime) {
                    this.end_series_datetime = end_series_datetime;
                }

                public boolean isVisible() {
                    return visible;
                }

                public void setVisible(boolean visible) {
                    this.visible = visible;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }

                public String updated_at;

                public Event(String _id, String event_type, String event_id, String start_date, long start_time, String start_datetime, String end_date, long end_time, String end_datetime, String venue_id, String venue_name, String date_full, String start_datetime_str, String end_datetime_str, String fullfillment_type, String fullfillment_value, long event_time, ArrayList<Photo> photos, ArrayList<Invited> invited, ArrayList<Accepted> accepted, ArrayList<Confirmed> confirmed, ArrayList<Guestconfirmed> guestconfirmed, ArrayList<Hostconfirmed> hostconfirmed, ArrayList<Rejected> rejected, ArrayList<Cancelled> cancelled, ArrayList<Host> hosts, ArrayList<Hoster> hosters, String source, String description, String location, long rank, String title, boolean inherits, String status, ArrayList<String> tags, String end_series_datetime, boolean visible, String created_at, String updated_at) {
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
                    this.photos = photos;
                    this.invited = invited;
                    this.accepted = accepted;
                    this.confirmed = confirmed;
                    this.guestconfirmed = guestconfirmed;
                    this.hostconfirmed = hostconfirmed;
                    this.rejected = rejected;
                    this.cancelled = cancelled;
                    this.hosts = hosts;
                    this.hosters = hosters;
                    this.source = source;
                    this.description = description;
                    this.location = location;
                    this.rank = rank;
                    this.title = title;
                    this.inherits = inherits;
                    this.status = status;
                    this.tags = tags;
                    this.end_series_datetime = end_series_datetime;
                    this.visible = visible;
                    this.created_at = created_at;
                    this.updated_at = updated_at;
                }

                public static class Photo {

                    public Photo() {
                    }
                }

                public static class Invited {

                    public Invited() {
                    }
                }

                public static class Accepted {

                    public Accepted() {
                    }
                }

                public static class Confirmed {

                    public Confirmed() {
                    }
                }

                public static class Guestconfirmed {

                    public Guestconfirmed() {
                    }
                }

                public static class Hostconfirmed {

                    public Hostconfirmed() {
                    }
                }

                public static class Rejected {

                    public Rejected() {
                    }
                }

                public static class Cancelled {

                    public Cancelled() {
                    }
                }

                public static class Host {

                    public Host() {
                    }
                }

                public static class Hoster {

                    public Hoster() {
                    }
                }
            }
        }
    }
}