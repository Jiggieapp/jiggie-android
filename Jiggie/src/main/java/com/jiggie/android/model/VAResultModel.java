package com.jiggie.android.model;

/**
 * Created by LTE on 3/11/2016.
 */
public class VAResultModel {
    public final int response;
    public final String msg;
    public final Data data;

    public VAResultModel(int response, String msg, Data data){
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
        public final Payment_informations payment_informations;

        public Data(Payment_informations payment_informations){
            this.payment_informations = payment_informations;
        }

        public Payment_informations getPayment_informations() {
            return payment_informations;
        }

        public static final class Payment_informations {
            public final boolean success;
            public final String status;
            public final String va_number;
            public final String method;

            public Payment_informations(boolean success, String status, String va_number, String method){
                this.success = success;
                this.status = status;
                this.va_number = va_number;
                this.method = method;
            }

            public boolean isSuccess() {
                return success;
            }

            public String getStatus() {
                return status;
            }

            public String getVa_number() {
                return va_number;
            }

            public String getMethod() {
                return method;
            }
        }
    }
}
