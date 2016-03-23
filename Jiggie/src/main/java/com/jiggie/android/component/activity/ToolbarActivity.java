package com.jiggie.android.component.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;

import butterknife.Bind;

/**
 * Created by rangg on 19/11/2015.
 */
public abstract class ToolbarActivity extends BaseActivity {
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void bindView() {
        super.bindView();
        super.setSupportActionBar(this.toolbar);
    }

    protected void setToolbarTitle(String title, boolean backEnabled) {
        final ActionBar actionBar = super.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(backEnabled);
            actionBar.setTitle(title);
        }
    }

    protected void setBackEnabled(boolean backEnabled) {
        final ActionBar actionBar = super.getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(backEnabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    protected void redirectToHome()
    {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
