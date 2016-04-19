package com.jiggie.android.activity.setup.country;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;

import java.util.HashMap;

import butterknife.Bind;

public class CountryActivity extends ToolbarActivity implements CountryCodeAdapter.ViewSelectedListener {

    @Bind(R.id.countryList)
    RecyclerView countryList;

    CountryCodeAdapter adapter;
    CountryModel countryModel;
    private final static String TAG = CountryActivity.class.getSimpleName();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        super.bindView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_country));

        countryModel = new CountryModel(this);
        adapter = countryModel.getCountryCodeAdapter(this);
        countryList.setLayoutManager(new LinearLayoutManager(this));
        countryList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_country, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) this.getSystemService
                (Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        final Handler handler = new Handler();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Utils.d(TAG, "searchtext " + searchText);
                        if (newText != null) {
                            adapter.filter(newText);
                        }
                    }
                }, getResources().getInteger(R.integer.event_search_delay));
                return true;
            }
        });
        return true;
    }

    @Override
    public void onViewSelected(int position, HashMap<String, String> object) {
        Utils.d(TAG, "object click " + object.get("name"));
        Intent i = new Intent();
        i.putExtra("name", object.get("name"));
        i.putExtra("dial_code", object.get("dial_code"));
        setResult(RESULT_OK, i);
        finish();
    }
}
