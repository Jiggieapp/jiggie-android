package com.jiggie.android.model;

import java.util.ArrayList;

/**
 * Created by wandywijayanto on 6/17/16.
 */
public final class ThemePostModel {
    public ArrayList<String> themes_id;

    public ThemePostModel(ArrayList<String> themes_id){
        this.themes_id = themes_id;
    }

    public ThemePostModel()
    {
        themes_id = new ArrayList<String>();
    }
}
