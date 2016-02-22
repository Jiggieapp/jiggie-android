package com.jiggie.android.activity.commerce;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.ProductListModel;

import de.greenrobot.event.EventBus;

/**
 * Created by LTE on 2/22/2016.
 */
public class ProductListActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        CommerceManager.loaderProductList("56b1a0bf89bfed03005c50f0");
    }

    public void onEvent(ProductListModel message) {
        String sd = String.valueOf(new Gson().toJson(message));
        Log.d("sd",sd);
    }

    public void onEvent(ExceptionModel message){
        if(message.getFrom().equals(Utils.FROM_PRODUCT_LIST)){

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
