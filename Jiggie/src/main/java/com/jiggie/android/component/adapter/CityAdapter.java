package com.jiggie.android.component.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.model.CityModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 4/27/2016.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CityModel.Data.Citylist> arrCityList;
    private ArrayList<CityModel.Data.Citylist> arrCityListFiltered;
    private final ViewSelectedListener listener;

    public CityAdapter(Context context, ArrayList<CityModel.Data.Citylist> arrCityList, ViewSelectedListener listener) {
        this.context = context;
        this.arrCityList = arrCityList;
        this.arrCityListFiltered = arrCityList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(v, this.listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       /* holder.citylist = arrCityList.get(position);
        holder.city = holder.citylist.getCity();

        if(holder.city.equalsIgnoreCase("jakarta")){
            holder.txtCityName.setTextColor(context.getResources().getColor(R.color.textDarkGray));
        }else{
            holder.txtCityName.setTextColor(context.getResources().getColor(R.color.divider_pantone));
        }

        holder.txtCityName.setText(holder.city);
        holder.position = position;*/
    }

    @Override
    public int getItemCount() {
        return arrCityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewSelectedListener listener;
        private int position;
        private CityModel.Data.Citylist citylist;
        private String city;

        @Bind(R.id.txt_city_name)
        TextView txtCityName;
        @Bind(R.id.rel_city)
        RelativeLayout relCity;

        public ViewHolder(View itemView, ViewSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.listener = listener;

            relCity.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                if(city.equalsIgnoreCase("jakarta")){
                    listener.onViewSelected(this.position, this.citylist);
                }
            }
        }
    }

    public interface ViewSelectedListener {
        void onViewSelected(int position, CityModel.Data.Citylist citylist);
    }

    public void filter(String query) {
        /*ArrayList<CityModel.Data.Citylist> temp = new ArrayList<>();
        for (CityModel.Data.Citylist citylist : arrCityListFiltered) {
            if (citylist.getCity().toLowerCase().contains(query)) {
                temp.add(citylist);
            }
        }
        arrCityList = temp;
        notifyDataSetChanged();*/
    }

}
