package com.jiggie.android.activity.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;

public class ProductListActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
    }
}
