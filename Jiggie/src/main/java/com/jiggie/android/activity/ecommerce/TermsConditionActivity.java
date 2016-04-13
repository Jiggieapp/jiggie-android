package com.jiggie.android.activity.ecommerce;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;

/**
 * Created by LTE on 4/12/2016.
 */
public class TermsConditionActivity extends ToolbarActivity {

    RelativeLayout rel_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        initView();
    }

    private void initView(){
        rel_continue = (RelativeLayout)findViewById(R.id.rel_continue);

        rel_continue.setEnabled(false);
    }
}
