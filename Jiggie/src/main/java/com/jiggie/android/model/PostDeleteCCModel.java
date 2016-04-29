package com.jiggie.android.model;

/**
 * Created by LTE on 3/15/2016.
 */
public class PostDeleteCCModel {
    String fb_id;
    String masked_card;

    public PostDeleteCCModel(String fb_id, String masked_card){
        this.fb_id = fb_id;
        this.masked_card = masked_card;
    }
}
