package com.jiggie.android.model;

/**
 * Created by LTE on 3/15/2016.
 */
public class PostCCModel {
    String fb_id;
    String token_id;
    String card_id;

    public PostCCModel(String fb_id, String token_id, String card_id){
        this.fb_id = fb_id;
        this.token_id = token_id;
        this.card_id = card_id;
    }
}
