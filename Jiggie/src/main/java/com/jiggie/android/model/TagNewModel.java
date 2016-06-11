package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by LTE on 6/9/2016.
 */
public final class TagNewModel {
    public final int response;
    public final String msg;
    public final Data data;

    public TagNewModel(int response, String msg, Data data){
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
        public final ArrayList<Tagslist> tagslist;

        public Data(ArrayList<Tagslist> tagslist){
            this.tagslist = tagslist;
        }

        public ArrayList<Tagslist> getTagslist() {
            return tagslist;
        }

        public static final class Tagslist {
            public final String name;
            public final String color;

            public Tagslist(String name, String color){
                this.name = name;
                this.color = color;
            }

            public String getName() {
                return name;
            }

            public String getColor() {
                return color;
            }
        }
    }
}