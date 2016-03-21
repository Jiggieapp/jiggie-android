package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

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

            public static class Order implements Parcelable {
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
                public Guest_detail guest_detail;
                public ArrayList<Product_list> product_list;
                public String order_id;
                protected Order(Parcel in) {
                    _id = in.readString();
                    code = in.readString();
                    order_status = in.readString();
                    payment_status = in.readString();
                    fb_id = in.readString();
                    event_id = in.readString();
                    total_tax_amount = in.readString();
                    total_tip_amount = in.readString();
                    total_adminfee = in.readString();
                    total_price = in.readString();
                    created_at = in.readString();
                    guest_detail = (Guest_detail) in.readValue(Guest_detail.class.getClassLoader());
                    if (in.readByte() == 0x01) {
                        product_list = new ArrayList<Product_list>();
                        in.readList(product_list, Product_list.class.getClassLoader());
                    } else {
                        product_list = null;
                    }
                    order_id = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(_id);
                    dest.writeString(code);
                    dest.writeString(order_status);
                    dest.writeString(payment_status);
                    dest.writeString(fb_id);
                    dest.writeString(event_id);
                    dest.writeString(total_tax_amount);
                    dest.writeString(total_tip_amount);
                    dest.writeString(total_adminfee);
                    dest.writeString(total_price);
                    dest.writeString(created_at);
                    dest.writeValue(guest_detail);
                    if (product_list == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(product_list);
                    }
                    dest.writeString(order_id);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
                    @Override
                    public Order createFromParcel(Parcel in) {
                        return new Order(in);
                    }

                    @Override
                    public Order[] newArray(int size) {
                        return new Order[size];
                    }
                };

                public Order(String _id, String code, String order_status, String payment_status, String fb_id, String event_id, String total_tax_amount, String total_tip_amount, String total_adminfee, String total_price, String created_at, Guest_detail guest_detail, ArrayList<Product_list> product_list, String order_id) {
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

                public static class Guest_detail implements Parcelable {
                    public String name;
                    public String email;
                    public String phone;

                    public Guest_detail(String name, String email, String phone) {
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

                public static class Product_list implements Parcelable {
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
                        payment_timelimit = in.readString();
                        _id = in.readString();
                        if (in.readByte() == 0x01) {
                            terms = new ArrayList<Term>();
                            in.readList(terms, Term.class.getClassLoader());
                        } else {
                            terms = null;
                        }
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
                        dest.writeString(payment_timelimit);
                        dest.writeString(_id);
                        if (terms == null) {
                            dest.writeByte((byte) (0x00));
                        } else {
                            dest.writeByte((byte) (0x01));
                            dest.writeList(terms);
                        }
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

                    public static class Term implements Parcelable {
                        public String _id;
                        public String body;
                        public String label;

                        public Term(String _id, String body, String label) {
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

                        protected Term(Parcel in) {
                            _id = in.readString();
                            body = in.readString();
                            label = in.readString();
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {
                            dest.writeString(_id);
                            dest.writeString(body);
                            dest.writeString(label);
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
                }
            }

            public static class Event implements Parcelable {
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
                public String updated_at;
                protected Event(Parcel in) {
                    _id = in.readString();
                    event_type = in.readString();
                    event_id = in.readString();
                    start_date = in.readString();
                    start_time = in.readLong();
                    start_datetime = in.readString();
                    end_date = in.readString();
                    end_time = in.readLong();
                    end_datetime = in.readString();
                    venue_id = in.readString();
                    venue_name = in.readString();
                    date_full = in.readString();
                    start_datetime_str = in.readString();
                    end_datetime_str = in.readString();
                    fullfillment_type = in.readString();
                    fullfillment_value = in.readString();
                    event_time = in.readLong();
                    if (in.readByte() == 0x01) {
                        photos = new ArrayList<Photo>();
                        in.readList(photos, Photo.class.getClassLoader());
                    } else {
                        photos = null;
                    }
                    if (in.readByte() == 0x01) {
                        invited = new ArrayList<Invited>();
                        in.readList(invited, Invited.class.getClassLoader());
                    } else {
                        invited = null;
                    }
                    if (in.readByte() == 0x01) {
                        accepted = new ArrayList<Accepted>();
                        in.readList(accepted, Accepted.class.getClassLoader());
                    } else {
                        accepted = null;
                    }
                    if (in.readByte() == 0x01) {
                        confirmed = new ArrayList<Confirmed>();
                        in.readList(confirmed, Confirmed.class.getClassLoader());
                    } else {
                        confirmed = null;
                    }
                    if (in.readByte() == 0x01) {
                        guestconfirmed = new ArrayList<Guestconfirmed>();
                        in.readList(guestconfirmed, Guestconfirmed.class.getClassLoader());
                    } else {
                        guestconfirmed = null;
                    }
                    if (in.readByte() == 0x01) {
                        hostconfirmed = new ArrayList<Hostconfirmed>();
                        in.readList(hostconfirmed, Hostconfirmed.class.getClassLoader());
                    } else {
                        hostconfirmed = null;
                    }
                    if (in.readByte() == 0x01) {
                        rejected = new ArrayList<Rejected>();
                        in.readList(rejected, Rejected.class.getClassLoader());
                    } else {
                        rejected = null;
                    }
                    if (in.readByte() == 0x01) {
                        cancelled = new ArrayList<Cancelled>();
                        in.readList(cancelled, Cancelled.class.getClassLoader());
                    } else {
                        cancelled = null;
                    }
                    if (in.readByte() == 0x01) {
                        hosts = new ArrayList<Host>();
                        in.readList(hosts, Host.class.getClassLoader());
                    } else {
                        hosts = null;
                    }
                    if (in.readByte() == 0x01) {
                        hosters = new ArrayList<Hoster>();
                        in.readList(hosters, Hoster.class.getClassLoader());
                    } else {
                        hosters = null;
                    }
                    source = in.readString();
                    description = in.readString();
                    location = in.readString();
                    rank = in.readLong();
                    title = in.readString();
                    inherits = in.readByte() != 0x00;
                    status = in.readString();
                    if (in.readByte() == 0x01) {
                        tags = new ArrayList<String>();
                        in.readList(tags, String.class.getClassLoader());
                    } else {
                        tags = null;
                    }
                    end_series_datetime = in.readString();
                    visible = in.readByte() != 0x00;
                    created_at = in.readString();
                    updated_at = in.readString();
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(_id);
                    dest.writeString(event_type);
                    dest.writeString(event_id);
                    dest.writeString(start_date);
                    dest.writeLong(start_time);
                    dest.writeString(start_datetime);
                    dest.writeString(end_date);
                    dest.writeLong(end_time);
                    dest.writeString(end_datetime);
                    dest.writeString(venue_id);
                    dest.writeString(venue_name);
                    dest.writeString(date_full);
                    dest.writeString(start_datetime_str);
                    dest.writeString(end_datetime_str);
                    dest.writeString(fullfillment_type);
                    dest.writeString(fullfillment_value);
                    dest.writeLong(event_time);
                    if (photos == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(photos);
                    }
                    if (invited == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(invited);
                    }
                    if (accepted == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(accepted);
                    }
                    if (confirmed == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(confirmed);
                    }
                    if (guestconfirmed == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(guestconfirmed);
                    }
                    if (hostconfirmed == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(hostconfirmed);
                    }
                    if (rejected == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(rejected);
                    }
                    if (cancelled == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(cancelled);
                    }
                    if (hosts == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(hosts);
                    }
                    if (hosters == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(hosters);
                    }
                    dest.writeString(source);
                    dest.writeString(description);
                    dest.writeString(location);
                    dest.writeLong(rank);
                    dest.writeString(title);
                    dest.writeByte((byte) (inherits ? 0x01 : 0x00));
                    dest.writeString(status);
                    if (tags == null) {
                        dest.writeByte((byte) (0x00));
                    } else {
                        dest.writeByte((byte) (0x01));
                        dest.writeList(tags);
                    }
                    dest.writeString(end_series_datetime);
                    dest.writeByte((byte) (visible ? 0x01 : 0x00));
                    dest.writeString(created_at);
                    dest.writeString(updated_at);
                }

                @SuppressWarnings("unused")
                public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
                    @Override
                    public Event createFromParcel(Parcel in) {
                        return new Event(in);
                    }

                    @Override
                    public Event[] newArray(int size) {
                        return new Event[size];
                    }
                };

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

                public ArrayList<Photo> getPhotos() {
                    return photos;
                }

                public ArrayList<Invited> getInvited() {
                    return invited;
                }

                public ArrayList<Accepted> getAccepted() {
                    return accepted;
                }

                public ArrayList<Confirmed> getConfirmed() {
                    return confirmed;
                }

                public ArrayList<Guestconfirmed> getGuestconfirmed() {
                    return guestconfirmed;
                }

                public ArrayList<Hostconfirmed> getHostconfirmed() {
                    return hostconfirmed;
                }

                public ArrayList<Rejected> getRejected() {
                    return rejected;
                }

                public ArrayList<Cancelled> getCancelled() {
                    return cancelled;
                }

                public ArrayList<Host> getHosts() {
                    return hosts;
                }

                public ArrayList<Hoster> getHosters() {
                    return hosters;
                }

                public String getSource() {
                    return source;
                }

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

                public boolean isVisible() {
                    return visible;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
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