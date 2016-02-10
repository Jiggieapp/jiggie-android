package com.jiggie.android.manager;

import com.google.gson.Gson;
import com.jiggie.android.api.ProductInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ProductModel;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Wandy on 2/10/2016.
 */
public class ProductManager extends BaseManager{

    static ProductInterface productInterface;
    public final static String TAG = ProductManager.class.getSimpleName();

    private static ProductInterface getProductInterfaceInstance()
    {
        if(productInterface == null)
        {
            productInterface = getRetrofit().create(ProductInterface.class);
        }
        return productInterface;
    }

    private static void getProductList(final String eventId, Callback callback)
    {
        getProductInterfaceInstance().getProductList(eventId).enqueue(callback);
    }

    public static void getProductList(final String eventId)
    {
        getProductList(eventId, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                final String responsee = new Gson().toJson(response);
                Utils.d(TAG, "responsee " + responsee);
                ProductModel productModel = (ProductModel) response.body();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
