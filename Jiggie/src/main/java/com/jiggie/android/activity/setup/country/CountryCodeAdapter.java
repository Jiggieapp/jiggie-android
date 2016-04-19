package com.jiggie.android.activity.setup.country;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wandy on 4/19/2016.
 */
public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.ViewHolder> {
    private final static String TAG = CountryCodeAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<HashMap<String, String>> countryCodes;
    private ArrayList<HashMap<String, String>> countryCodesFiltered;
    private final ViewSelectedListener listener;

    public CountryCodeAdapter(Context context, ArrayList<HashMap<String, String>> countryCodes, ViewSelectedListener listener) {
        this.context = context;
        this.countryCodes = countryCodes;
        this.countryCodesFiltered = countryCodes;
        this.listener = listener;
    }

    @Override
    public CountryCodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new ViewHolder(v, this.listener);
    }

    @Override
    public void onBindViewHolder(CountryCodeAdapter.ViewHolder holder, int position) {
        holder.country = countryCodes.get(position);
        holder.lblCountryName.setText(holder.country.get("name"));
        holder.lblCountryCode.setText(holder.country.get("dial_code"));
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return countryCodes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewSelectedListener listener;
        private int position;
        private HashMap<String, String> country;

        @Bind(R.id.lbl_country_name)
        TextView lblCountryName;

        @Bind(R.id.lbl_country_code)
        TextView lblCountryCode;

        @Bind(R.id.country_container)
        RelativeLayout countryContainer;

        public ViewHolder(View itemView, ViewSelectedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.listener = listener;

            countryContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener != null)
            {
                listener.onViewSelected(this.position, this.country);
            }
        }
    }

    public interface ViewSelectedListener {
        void onViewSelected(int position, HashMap<String, String> object);
    }

    public void filter(String query) {
        //countryCodes.clear();
        ArrayList<HashMap<String, String>> temp = new ArrayList<>();
        for (HashMap<String, String> country : countryCodesFiltered) {
            if (country.get("name").toLowerCase().contains(query)) {
                temp.add(country);
            }
        }
        countryCodes = temp;
        notifyDataSetChanged();
        //countryCodesFiltered
    }


}
