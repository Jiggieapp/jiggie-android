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

    private final ArrayList<ProductModel.Data.Product_list> items;
    public ProductListAdapter()
    {
        items = new ArrayList<>();
    }

    public void clear() { this.items.clear(); }
    public void addAll(ArrayList<ProductModel.Data.Product_list> data)
    {
        items.addAll(data);
    }
    public void add(ProductModel.Data.Product_list data)
    {
        items.add(data);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProductModel.Data.Product_list productList
                = items.get(position);
        Utils.d(TAG, productList.getName());
        holder.productName.setText(productList.getName());
    }


    @Override
    public int getItemCount() {
        return items.size();
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
