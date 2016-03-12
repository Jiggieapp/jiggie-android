package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 3/11/2016.
 */
public class CCModel {

    public final long response;
    public final String msg;
    public final Data data;

    public CCModel(long response, String msg, Data data){
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
        public final ArrayList<Creditcard_information> creditcard_informations;

        public Data(ArrayList<Creditcard_information> creditcard_informations){
            this.creditcard_informations = creditcard_informations;
        }

        public ArrayList<Creditcard_information> getCreditcard_informations() {
            return creditcard_informations;
        }

        public static final class Creditcard_information {
            public final String masked_card;
            public final String saved_token_id;
            public final String saved_token_id_expired_at;
            public final String payment_type;

            public Creditcard_information(String masked_card, String saved_token_id, String saved_token_id_expired_at, String payment_type){
                this.masked_card = masked_card;
                this.saved_token_id = saved_token_id;
                this.saved_token_id_expired_at = saved_token_id_expired_at;
                this.payment_type = payment_type;
            }

            public String getMasked_card() {
                return masked_card;
            }

            public String getSaved_token_id() {
                return saved_token_id;
            }

            public String getSaved_token_id_expired_at() {
                return saved_token_id_expired_at;
            }

            public String getPayment_type() {
                return payment_type;
            }
        }
    }

}
