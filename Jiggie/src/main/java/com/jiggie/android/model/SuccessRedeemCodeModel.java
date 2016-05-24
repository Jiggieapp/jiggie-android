package com.jiggie.android.model;

/**
 * Created by LTE on 5/17/2016.
 */
public final class SuccessRedeemCodeModel {
    public final int response;
    public final Data data;

    public SuccessRedeemCodeModel(int response, Data data){
        this.response = response;
        this.data = data;
    }

    public int getResponse() {
        return response;
    }

    public Data getData() {
        return data;
    }

    public static final class Data {
        public final Redeem_code redeem_code;

        public Data(Redeem_code redeem_code){
            this.redeem_code = redeem_code;
        }

        public Redeem_code getRedeem_code() {
            return redeem_code;
        }

        public static final class Redeem_code {
            public final String msg;
            public final boolean is_check;

            public Redeem_code(String msg, boolean is_check){
                this.msg = msg;
                this.is_check = is_check;
            }

            public String getMsg() {
                return msg;
            }

            public boolean is_check() {
                return is_check;
            }
        }
    }
}
