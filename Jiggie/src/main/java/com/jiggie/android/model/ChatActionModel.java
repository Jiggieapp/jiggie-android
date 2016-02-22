package com.jiggie.android.model;

/**
 * Created by LTE on 2/12/2016.
 */
public class ChatActionModel {

    String from;
    Success2Model success2Model;

    public ChatActionModel(String from, Success2Model success2Model){
        this.from = from;
        this.success2Model = success2Model;
    }

    public String getFrom() {
        return from;
    }

    public Success2Model getSuccess2Model() {
        return success2Model;
    }
}
