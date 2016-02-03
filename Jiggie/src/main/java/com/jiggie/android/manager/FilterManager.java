package com.jiggie.android.manager;

import com.facebook.AccessToken;
import com.jiggie.android.api.AccountInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.FilterModel;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by wandywijayanto on 2/1/16.
 */
public class FilterManager {

    private static AccountInterface accountInterface;
    public final static String TAG = FilterManager.class.getSimpleName();

    public static void initFilter()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        accountInterface = retrofit.create(AccountInterface.class);
    }

    private static void loadUserTagList(Callback callback)
    {
        /*API.loadUserTagList(new API.OnResponseCompleted() {
            @Override
            public void onResponse(String[] values) {

            }
        });*/

        accountInterface.getUserTagList("10153418311072858"
               /* AccessToken.getCurrentAccessToken().getUserId()*/).enqueue(callback);
    }

    public static void getUserTagList()
    {
        if(accountInterface == null)
            initFilter();
        loadUserTagList(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                //String[] result = (String[]) response.body();
                //String r = new Gson().toJson(response.body());
                //JSONObject r = (JSONObject) response.body();
                FilterModel filterMode = (FilterModel) response.body();
                //for(String res : filterMode.getData().getExperiences())
                    //Utils.d(TAG, "res filter model " + filterMode.getData().getExperiences());
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
