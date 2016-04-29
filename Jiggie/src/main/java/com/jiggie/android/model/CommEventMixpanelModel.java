package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 3/18/2016.
 */
public class CommEventMixpanelModel {
    String eventName;
    String eventVenueName;
    String eventVenueCity;
    String eventStartDate;
    String eventEndDate;
    ArrayList<String> tag;
    String eventDescription;
    String ticketName;
    String ticketType;
    String ticketPrice;
    String ticketMaxPerGuest;
    String dateTime;
    String purchaseQuantity;
    String purchaseAmount;
    String purchaseDiscount;
    String purchasePayment;
    String totalGuest;
    boolean isReservation;

    public CommEventMixpanelModel(String eventName, String eventVenueName, String eventVenueCity, String eventStartDate, String eventEndDate, ArrayList<String> tag, String eventDescription){
        this.eventName = eventName;
        this.eventVenueName = eventVenueName;
        this.eventVenueCity = eventVenueCity;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.tag = tag;
        this.eventDescription = eventDescription;
    }

    public CommEventMixpanelModel(String eventName, String eventVenueName, String eventVenueCity, String eventStartDate, String eventEndDate, ArrayList<String> tag, String eventDescription,
                                  String ticketName, String ticketType, String ticketPrice, String ticketMaxPerGuest){
        this.eventName = eventName;
        this.eventVenueName = eventVenueName;
        this.eventVenueCity = eventVenueCity;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.tag = tag;
        this.eventDescription = eventDescription;
        this.ticketName = ticketName;
        this.ticketType = ticketType;
        this.ticketPrice = ticketPrice;
        this.ticketMaxPerGuest = ticketMaxPerGuest;
    }

    public CommEventMixpanelModel(String eventName, String eventVenueName, String eventVenueCity, String eventStartDate, String eventEndDate, ArrayList<String> tag, String eventDescription,
                                  String ticketName, String ticketType, String ticketPrice, String ticketMaxPerGuest, String dateTime, String purchaseQuantity, String purchaseAmount,
                                  String purchaseDiscount, String purchasePayment, String totalGuest, boolean isResevation){
        this.eventName = eventName;
        this.eventVenueName = eventVenueName;
        this.eventVenueCity = eventVenueCity;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.tag = tag;
        this.eventDescription = eventDescription;
        this.ticketName = ticketName;
        this.ticketType = ticketType;
        this.ticketPrice = ticketPrice;
        this.ticketMaxPerGuest = ticketMaxPerGuest;
        this.dateTime = dateTime;
        this.purchaseQuantity = purchaseQuantity;
        this.purchaseAmount = purchaseAmount;
        this.purchaseDiscount = purchaseDiscount;
        this.purchasePayment = purchasePayment;
        this.totalGuest = totalGuest;
        this.isReservation = isResevation;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventVenueName() {
        return eventVenueName;
    }

    public String getEventVenueCity() {
        return eventVenueCity;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getTicketName() {
        return ticketName;
    }

    public String getTicketType() {
        return ticketType;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public String getTicketMaxPerGuest() {
        return ticketMaxPerGuest;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public String getPurchaseDiscount() {
        return purchaseDiscount;
    }

    public String getPurchasePayment() {
        return purchasePayment;
    }

    public String getTotalGuest() {
        return totalGuest;
    }

    public boolean isReservation() {
        return isReservation;
    }
}
