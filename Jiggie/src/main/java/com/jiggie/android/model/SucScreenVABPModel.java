package com.jiggie.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by LTE on 3/16/2016.
 */
public final class SucScreenVABPModel
{
    public final int response;
    public final String msg;
    public final Data data;

    public SucScreenVABPModel(int response, String msg, Data data){
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
        public final SuccessScreen success_screen;

        public Data(SuccessScreen success_screen){
            this.success_screen = success_screen;
        }

        public SuccessScreen getSuccess_screen() {
            return success_screen;
        }

        public static class SuccessScreen {

            public String order_id;
            public String order_number;
            public String order_status;
            public String payment_status;
            public String payment_type;
            public String type;
            public String created_at;
            public String timelimit;
            public String amount;
            public String transfer_to;
            public ArrayList<StepPayment> step_payment;

            public SuccessScreen(String order_id, String order_number, String order_status, String payment_status, String payment_type,String type, String created_at, String timelimit, String amount, String transfer_to, ArrayList<StepPayment> step_payment) {
                this.order_id = order_id;
                this.order_number = order_number;
                this.order_status = order_status;
                this.payment_status = payment_status;
                this.payment_type = payment_type;
                this.type = type;
                this.created_at = created_at;
                this.timelimit = timelimit;
                this.amount = amount;
                this.transfer_to = transfer_to;
                this.step_payment = step_payment;
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

            public String getPayment_type() {
                return payment_type;
            }

            public String getType() {
                return type;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getTimelimit() {
                return timelimit;
            }

            public String getAmount() {
                return amount;
            }

            public String getTransfer_to() {
                return transfer_to;
            }

            public ArrayList<StepPayment> getStep_payment() {
                return step_payment;
            }

            public static class StepPayment{

                public String header;
                public ArrayList<String> step = new ArrayList<String>();

                public StepPayment(String header, ArrayList<String> step) {
                    this.header = header;
                    this.step = step;
                }

                public String getHeader() {
                    return header;
                }

                public ArrayList<String> getStep() {
                    return step;
                }
            }
        }
    }


}
