package com.jiggie.android.activity.commerce;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.PostSummaryModel;
import com.jiggie.android.model.SummaryModel;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by LTE on 2/23/2016.
 */
public class SummaryActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);


        String ticket_id = "56b1a12e89bfed03005c50f1";
        String name = "Silver";
        String ticket_type = "purchase";
        int quantity = 5;
        String admin_fee = "2";
        String tax_percent = "10";
        String tax_amount = "1.00";
        String tip_percent = "0";
        String tip_amount = "0.00";
        String price = "10";
        String total_price = "13.00";
        int num_buy = 4;

        PostSummaryModel.ProductList productList = new PostSummaryModel.ProductList(ticket_id, name, ticket_type, quantity, admin_fee, tax_percent, tax_amount, tip_percent, tip_amount, price, total_price, num_buy);
        ArrayList<PostSummaryModel.ProductList> arrproductList= new ArrayList<>();
        arrproductList.add(productList);

        PostSummaryModel postSummaryModel = new PostSummaryModel("123456", "56b1a0bf89bfed03005c50f0", arrproductList);
        CommerceManager.loaderSummary(postSummaryModel);
    }

    public void onEvent(SummaryModel message) {
        String sd = String.valueOf(new Gson().toJson(message));
        Log.d("sd", sd);
    }

    public void onEvent(ExceptionModel message){
        if(message.getFrom().equals(Utils.FROM_SUMMARY)){

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
