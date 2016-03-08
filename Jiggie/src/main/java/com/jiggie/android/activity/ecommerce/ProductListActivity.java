package com.jiggie.android.activity.ecommerce;

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
        /*sections.add(new SimpleSectionedRecycleViewAdapter.Section(12,"Section 3"));
        sections.add(new SimpleSectionedRecycleViewAdapter.Section(14,"Section 4"));
        sections.add(new SimpleSectionedRecycleViewAdapter.Section(20,"Section 5"));
        */
        //Add your adapter to the sectionAdapter
        SimpleSectionedRecycleViewAdapter.Section[] dummy = new SimpleSectionedRecycleViewAdapter.Section[sections.size()];
        SimpleSectionedRecycleViewAdapter mSectionedAdapter = new
                SimpleSectionedRecycleViewAdapter(this,R.layout.section,R.id.section_text, adapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        recyclerView.setAdapter(mSectionedAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
