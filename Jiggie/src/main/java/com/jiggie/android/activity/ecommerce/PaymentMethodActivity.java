package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.PaymentMethodAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 2/25/2016.
 */
public class PaymentMethodActivity extends ToolbarActivity implements PaymentMethodAdapter.ViewSelectedListener{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private int section2Start = 3;
    private PaymentMethodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_payment_method);
        super.bindView();
        super.setToolbarTitle(getResources().getString(R.string.select_payment), true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(adapter = new PaymentMethodAdapter(PaymentMethodActivity.this, this, section2Start));


    }

    @Override
    public void onViewSelected(int position) {
        if(position==(section2Start-1)){
            startActivity(new Intent(PaymentMethodActivity.this, AddCreditCardActivity.class));
        }
    }
}
