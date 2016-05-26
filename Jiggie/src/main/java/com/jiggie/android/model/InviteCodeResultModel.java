package com.jiggie.android.model;

/**
 * Created by Wandy on 5/13/2016.
 */
public class InviteCodeResultModel {
    private String response;

    private Data data;

    private String msg;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", data = " + data + ", msg = " + msg + "]";
    }

    public class Data {
        private Invite_code invite_code;

        public Invite_code getInvite_code() {
            return invite_code;
        }

        public void setInvite_code(Invite_code invite_code) {
            this.invite_code = invite_code;
        }

        @Override
        public String toString() {
            return "ClassPojo [invite_code = " + invite_code + "]";
        }


        public class Invite_code {
            private String msg_share;
            private String msg_invite;
            private String invite_url;
            private String code;
            private String rewards_inviter;

            public String getRewards_inviter() {
                return rewards_inviter;
            }

            public void setRewards_inviter(String rewards_inviter) {
                this.rewards_inviter = rewards_inviter;
            }

            public String getMsg_share() {
                return msg_share;
            }
            public void setMsg_share(String msg_share) {
                this.msg_share = msg_share;
            }
            public String getMsg_invite() {
                return msg_invite;
            }
            public void setMsg_invite(String msg_invite) {
                this.msg_invite = msg_invite;
            }
            public String getInvite_url() {
                return invite_url;
            }
            public void setInvite_url(String invite_url) {
                this.invite_url = invite_url;
            }
            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            @Override
            public String toString() {
                return "ClassPojo [msg_share = " + msg_share + ", msg_invite = " + msg_invite + ", invite_url = " + invite_url + ", code = " + code + "]";
            }
        }

    }
}
