package com.jiggie.android.activity.setup.country;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.jiggie.android.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wandy on 4/19/2016.
 */
public class CountryModel {
    private Country country;
    private Context context;

    public Country getCountry() {
        return country;
    }

    public CountryModel(Context context)
    {
        this.context = context;
    }
    private CountryCodeAdapter codeAdapter;
    public void setCountry(final String countryCode, final String countryName) {
        //country = new Country(countryCode, countryName);
    }

    public CountryCodeAdapter getCountryCodeAdapter(CountryCodeAdapter.ViewSelectedListener listener) {
        //String rawCodes = load("country_code.txt");
        //Country countryCodes = new Gson().fromJson(rawCodes, Country.class);
        codeAdapter = new CountryCodeAdapter
                (context, load("country_code.txt"), listener);
        return codeAdapter;
    }

    public void filter(String query)
    {
        codeAdapter.filter(query);
    }

    public String loadJSONFromAsset(final String assetName) {
        String json = null;
        try {
            InputStream is = App.getInstance().getAssets()
                    .open(assetName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public ArrayList<HashMap<String, String>> load(final String assetName) {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(assetName));
            JSONArray m_jArry = obj.getJSONArray("data");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                //Log.d("Details-->", jo_inside.getString("formule"));
                String formula_value = jo_inside.getString("dial_code");
                String url_value = jo_inside.getString("name");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("dial_code", formula_value);
                m_li.put("name", url_value);

                //Country country = new Country(formula_value, url_value);
                formList.add(m_li);
            }
            return formList;
        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
