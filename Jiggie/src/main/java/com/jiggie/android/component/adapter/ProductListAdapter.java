package com.jiggie.android.component.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.ProductModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wandywijayanto on 2/11/16.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{

    public static final String TAG = ProductListAdapter.class.getSimpleName();

    private ArrayList<ProductModel.Data.Product_lists.Purchase> items;
    private ArrayList<ProductModel.Data.Product_lists.Purchase> purchases;
    private ArrayList<ProductModel.Data.Product_lists.Reservation> reservations;

    public ProductListAdapter()
    {
        items = new ArrayList<>();
        purchases = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public void clear()
    {
        this.purchases.clear();
        this.reservations.clear();
    }

    public void addPurchases(ArrayList<ProductModel.Data.Product_lists.Purchase> data)
    {
        items.addAll(data);
        purchases.addAll(data);
    }
    public void add(ProductModel.Data.Product_lists.Purchase data)
    {
        purchases.add(data);
        items.add(data);
    }

    public void addReservations(ArrayList<ProductModel.Data.Product_lists.Reservation> data)
    {
        //reservations.addAll(data);
        for(int i=0;i<data.size();i++)
        {

            ProductModel.Data.Product_lists.Purchase temp
                    = new ProductModel.Data.Product_lists.Purchase(data.get(i));
            //temp.setEvent_id(reservations.g);
            Utils.d(TAG, "data get i " + temp.getName()
                    + " " + temp.getTicket_type());
            items.add(temp);
            notifyDataSetChanged();
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_product_list, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProductModel.Data.Product_lists.Purchase product
                = items.get(position);
        holder.productName.setText(product.getName());
    }

    @Override
    public int getItemCount() {
        return purchases.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.product_name)
        TextView productName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
