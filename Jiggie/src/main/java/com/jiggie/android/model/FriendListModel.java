package com.jiggie.android.model;

/**
 * Created by Wandy on 5/12/2016.
 */
public class FriendListModel {
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


    public class Data {
        private List_social_friends[] list_social_friends;

        public List_social_friends[] getList_social_friends() {
            return list_social_friends;
        }

        public void setList_social_friends(List_social_friends[] list_social_friends) {
            this.list_social_friends = list_social_friends;
        }


        public class List_social_friends {
            private String img_url;

            private String first_name;

            private String fb_id;

            private String about;

            private String last_name;

            private String is_connect;

            private String credit;

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getFb_id() {
                return fb_id;
            }

            public void setFb_id(String fb_id) {
                this.fb_id = fb_id;
            }

            public String getAbout() {
                return about;
            }

            public void setAbout(String about) {
                this.about = about;
            }

            public String getLast_name() {
                return last_name;
            }

            public void setLast_name(String last_name) {
                this.last_name = last_name;
            }

            public String getIs_connect() {
                return is_connect;
            }

            public void setIs_connect(String is_connect) {
                this.is_connect = is_connect;
            }

            public String getCredit() {
                return credit;
            }

            public void setCredit(String credit) {
                this.credit = credit;
            }

        }
    }


}