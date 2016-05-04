package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LTE on 3/15/2016.
 */
public class CCScreenModel implements Parcelable {
    public final CCModel.Data.Creditcard_information creditcardInformation;
    public final CardDetails cardDetails;
    public final String name_cc;

    public CCScreenModel(CCModel.Data.Creditcard_information creditcardInformation, CardDetails cardDetails, String name_cc){
        this.creditcardInformation = creditcardInformation;
        this.cardDetails = cardDetails;
        this.name_cc = name_cc;
    }

    public static class CardDetails implements Parcelable {
        String cardNumber;
        String cvv;
        int expMonth;
        int expYear;
        String grossAmount;


        public CardDetails()
        {

        }

        public CardDetails(String cardNumber, String cvv, int expMonth, int expYear, String grossAmount){
            this.cardNumber = cardNumber;
            this.cvv = cvv;
            this.expMonth = expMonth;
            this.expYear = expYear;
            this.grossAmount = grossAmount;
        }

        protected CardDetails(Parcel in) {
            cardNumber = in.readString();
            cvv = in.readString();
            expMonth = in.readInt();
            expYear = in.readInt();
            grossAmount = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(cardNumber);
            dest.writeString(cvv);
            dest.writeInt(expMonth);
            dest.writeInt(expYear);
            dest.writeString(grossAmount);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<CardDetails> CREATOR = new Parcelable.Creator<CardDetails>() {
            @Override
            public CardDetails createFromParcel(Parcel in) {
                return new CardDetails(in);
            }

            @Override
            public CardDetails[] newArray(int size) {
                return new CardDetails[size];
            }
        };

        public String getCardNumber() {
            return cardNumber;
        }

        public String getCvv() {
            return cvv;
        }

        public int getExpMonth() {
            return expMonth;
        }

        public int getExpYear() {
            return expYear;
        }

        public String getGrossAmount() {
            return grossAmount;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public void setGrossAmount(String grossAmount) {
            this.grossAmount = grossAmount;
        }

        public void setCvv(String cvv) {
            this.cvv = cvv;
        }

        public void setExpMonth(int expMonth) {
            this.expMonth = expMonth;
        }

        public void setExpYear(int expYear) {
            this.expYear = expYear;
        }
    }

    protected CCScreenModel(Parcel in) {
        creditcardInformation = (CCModel.Data.Creditcard_information) in.readValue(CCModel.Data.Creditcard_information.class.getClassLoader());
        cardDetails = (CardDetails) in.readValue(CardDetails.class.getClassLoader());
        name_cc = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(creditcardInformation);
        dest.writeValue(cardDetails);
        dest.writeString(name_cc);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CCScreenModel> CREATOR = new Parcelable.Creator<CCScreenModel>() {
        @Override
        public CCScreenModel createFromParcel(Parcel in) {
            return new CCScreenModel(in);
        }

        @Override
        public CCScreenModel[] newArray(int size) {
            return new CCScreenModel[size];
        }
    };

    public CCModel.Data.Creditcard_information getCreditcardInformation() {
        return creditcardInformation;
    }

    public CardDetails getCardDetails() {
        return cardDetails;
    }

    public String getName_cc() {
        return name_cc;
    }
}




