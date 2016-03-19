package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 3/18/2016.
 */
public final class SucScreenWalkthroughModel {
    public final int response;
    public final String msg;
    public final Data data;

    public SucScreenWalkthroughModel(int response, String msg, Data data){
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
        public final WalkthroughPayment walkthrough_payment;

        public Data(WalkthroughPayment walkthrough_payment){
            this.walkthrough_payment = walkthrough_payment;
        }

        public WalkthroughPayment getWalkthrough_payment() {
            return walkthrough_payment;
        }

        public static final class WalkthroughPayment {
            public final VaStep va_step;
            public final BpStep bp_step;

            public WalkthroughPayment(VaStep va_step, BpStep bp_step){
                this.va_step = va_step;
                this.bp_step = bp_step;
            }

            public VaStep getVa_step() {
                return va_step;
            }

            public BpStep getBp_step() {
                return bp_step;
            }

            public static final class VaStep {
                public final String type;
                public final ArrayList<Step_payment> step_payment;

                public VaStep(String type, ArrayList<Step_payment> step_payment){
                    this.type = type;
                    this.step_payment = step_payment;
                }

                public String getType() {
                    return type;
                }

                public ArrayList<Step_payment> getStep_payment() {
                    return step_payment;
                }

                public static final class Step_payment {
                    public final String header;
                    public final ArrayList<String> step;

                    public Step_payment(String header, ArrayList<String> step){
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

            public static final class BpStep {
                public final String type;
                public final ArrayList<Step_payment> step_payment;

                public BpStep(String type, ArrayList<Step_payment> step_payment){
                    this.type = type;
                    this.step_payment = step_payment;
                }

                public String getType() {
                    return type;
                }

                public ArrayList<Step_payment> getStep_payment() {
                    return step_payment;
                }

                public static final class Step_payment {
                    public final String header;
                    public final ArrayList<String> step;

                    public Step_payment(String header, ArrayList<String> step){
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
}
