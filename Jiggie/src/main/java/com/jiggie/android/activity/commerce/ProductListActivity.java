package com.jiggie.android.activity.commerce;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ProductListAdapter;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.ProductListModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by LTE on 2/22/2016.
 */
public class ProductListActivity extends ToolbarActivity implements ProductListAdapter.ViewSelectedListener {


    @Bind(R.id.recycler_view_ticket)
    RecyclerView recyclerViewTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //CommerceManager.loaderProductList("56b1a0bf89bfed03005c50f0");

        this.recyclerViewTicket.setAdapter(new ProductListAdapter(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerViewTicket.setLayoutManager(layoutManager);

    }

    public void onEvent(ProductListModel message) {
        String sd = String.valueOf(new Gson().toJson(message));
        Log.d("sd", sd);
    }

    public void onEvent(ExceptionModel message) {
        if (message.getFrom().equals(Utils.FROM_PRODUCT_LIST)) {

        }
    }

    @Override
    public void onViewSelected() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
