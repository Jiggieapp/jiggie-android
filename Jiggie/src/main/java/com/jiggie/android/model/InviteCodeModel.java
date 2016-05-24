package com.jiggie.android.model;

/**
 * Created by LTE on 5/13/2016.
 */
public final class InviteCodeModel {
    public final int response;
    public final String msg;
    public final Data data;

    public InviteCodeModel(int response, String msg, Data data){
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

        public InviteCode invite_code;

        public InviteCode getInvite_code() {
            return invite_code;
        }

        public static final class InviteCode {
            public final String code;
            public final String msg_invite;
            public final String msg_share;
            public final String invite_url;

            public InviteCode(String code, String msg_invite, String msg_share, String invite_url){
                this.code = code;
                this.msg_invite = msg_invite;
                this.msg_share = msg_share;
                this.invite_url = invite_url;
            }

            public String getCode() {
                return code;
            }

            public String getMsg_invite() {
                return msg_invite;
            }

            public String getMsg_share() {
                return msg_share;
            }

            public String getInvite_url() {
                return invite_url;
            }
        }
    }
}