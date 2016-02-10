package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.ProductListAdapter;
import com.jiggie.android.manager.ProductManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ProductModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class ProductListActivity extends ToolbarActivity
    implements ViewTreeObserver.OnGlobalLayoutListener
{
    @Bind(R.id.recycler_view_product) RecyclerView recyclerView;
    ProductListAdapter adapter;

    public static final String TAG = ProductListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        super.bindView();

        EventBus.getDefault().register(this);

        final Intent intent = getIntent();
        final String eventId = intent.getStringExtra(Common.FIELD_EVENT_ID);

        adapter = new ProductListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        loadData(eventId);
    }

    private void loadData(final String eventId)
    {
        ProductManager.getProductList(eventId);
    }

    public void onEvent(ProductModel productModel)
    {

        adapter.clear();
        ArrayList<ProductModel.Data.Product_list> product_list
                = productModel.getData().getProduct_lists();
        for(int i=0;i<product_list.size();i++)
        {
            Utils.d(TAG, "product model " + product_list.get(i).getName());
            adapter.add(product_list.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGlobalLayout() {
        this.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
