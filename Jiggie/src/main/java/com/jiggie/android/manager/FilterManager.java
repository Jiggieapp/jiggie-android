package com.jiggie.android.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.api.API;
import com.jiggie.android.api.FilterInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.model.FilterModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Filter;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by wandywijayanto on 2/1/16.
 */
public class FilterManager {

    private static FilterInterface filterInterface;
    public final static String TAG = FilterManager.class.getSimpleName();

    public static void initFilter()
    {
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://52.77.222.216")
                //.baseUrl(Utils.URL)
                .baseUrl(Utils.OLD_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        filterInterface = retrofit.create(FilterInterface.class);
    }

    private static void loadUserTagList(Callback callback)
    {
        /*API.loadUserTagList(new API.OnResponseCompleted() {
            @Override
            public void onResponse(String[] values) {

            }
        });*/

        filterInterface.getUserTagList(
                AccessToken.getCurrentAccessToken().getUserId()).enqueue(callback);
    }

    public static void getUserTagList()
    {
        if(filterInterface == null)
            initFilter();
        loadUserTagList(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                //String[] result = (String[]) response.body();
                //String r = new Gson().toJson(response.body());
                //JSONObject r = (JSONObject) response.body();
                FilterModel filterMode = (FilterModel) response.body();
                EventBus.getDefault().post(filterMode.getData().getExperiences());
            }

            @Override
            public void onFailure(Throwable t) {
                Utils.d(TAG, "response fail" + t.getMessage());
            }
        });

        /*VolleyHandler.getInstance().createVolleyArrayRequest("user/tagslist", new VolleyRequestListener<String[], JSONArray>() {
            @Override
            public String[] onResponseAsync(JSONArray jsonArray) {
                final int length = jsonArray.length();
                final String[] values = new String[length];
                for (int i = 0; i < length; i++)
                    values[i] = jsonArray.optString(i);
                return values;
            }

            @Override
            public void onResponseCompleted(String[] values) {
                for(String val : values)
                {
                    Utils.d(TAG, "val " + val);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });*/
    }
}
