package com.jiggie.android.activity.setup;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.CityAdapter;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.CityModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 4/27/2016.
 */
public class CityActivity extends ToolbarActivity implements CityAdapter.ViewSelectedListener {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    CityAdapter adapter;
    private final static String TAG = CityActivity.class.getSimpleName();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        super.bindView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_city));

        AccountManager.loaderCityList(new AccountManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                CityModel cityModel = (CityModel)object;
                setCityAdapter(cityModel.getData().getCitylist());
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Toast.makeText(CityActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setCityAdapter(ArrayList<CityModel.Data.Citylist> arrCityList){
        adapter = new CityAdapter(CityActivity.this, arrCityList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
    public void onViewSelected(int position, CityModel.Data.Citylist citylist) {
        Utils.d(TAG, "object click " + citylist.getCity());
        Intent i = new Intent();
        i.putExtra("cityname", citylist.getCity());
        setResult(RESULT_OK, i);
        finish();
    }
}
