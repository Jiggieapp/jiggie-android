package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 2/9/2016.
 */
public class TagsListModel {

    int response;
    String msg;
    Data data;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        ArrayList<String> tagslist;

        public ArrayList<String> getTagslist() {
            return tagslist;
        }

        public void setTagslist(ArrayList<String> tagslist) {
            this.tagslist = tagslist;
        }
    }

}
