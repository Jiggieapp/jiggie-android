package com.jiggie.android.model;

/**
 * Created by LTE on 5/19/2016.
 */
public final class SuccessCreditBalanceModel {
    public final int response;
    public final String msg;
    public final Data data;

    public SuccessCreditBalanceModel(int response, String msg, Data data){
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
        public final Balance_credit balance_credit;

        public Data(Balance_credit balance_credit){
            this.balance_credit = balance_credit;
        }

        public static final class Balance_credit {
            public final String tot_credit_active;

            public Balance_credit(String tot_credit_active){
                this.tot_credit_active = tot_credit_active;
            }

            public String getTot_credit_active() {
                return tot_credit_active;
            }
        }

        public Balance_credit getBalance_credit() {
            return balance_credit;
        }
    }
}