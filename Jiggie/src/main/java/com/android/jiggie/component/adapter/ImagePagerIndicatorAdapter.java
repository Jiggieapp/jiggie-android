package com.android.jiggie.component.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.jiggie.R;
import com.android.jiggie.model.Common;
import com.bumptech.glide.Glide;

/**
 * Created by rangga on 17/08/2014.
 */
public class ImagePagerIndicatorAdapter extends FragmentPagerAdapter {
    private IndicatorAdapter indicatorAdapter;
    private Fragment[] fragments;
    private ViewPager viewPager;
    private String[] images;

    public ImagePagerIndicatorAdapter(FragmentManager fm, final ViewPager viewPager) {
        super(fm);
        this.viewPager = viewPager;
        this.viewPager.setAdapter(this);
        this.indicatorAdapter = new IndicatorAdapter();
        this.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                indicatorAdapter.setSelectedPosition(position);
                super.onPageSelected(position);
            }
        });
    }

    public void setImages(String[] images) {
        this.images = images;
        this.fragments = new Fragment[images == null ? 0 : images.length];
        this.indicatorAdapter.setItemCount(images == null ? 0 : images.length);

        super.notifyDataSetChanged();
        this.viewPager.setOffscreenPageLimit(this.fragments.length);
    }

    @Override
    public int getCount() { return this.images == null ? 0 : this.images.length; }
    public BaseAdapter getIndicatorAdapter() { return this.indicatorAdapter; }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = this.fragments[position];
        final String image = this.images[position];

        if (fragment == null) {
            fragment = new ImageFragment();
            final Bundle bundle = new Bundle();
            bundle.putString(Common.BUNDLE_IMAGE, image);
            fragment.setArguments(bundle);
            this.fragments[position] = fragment;
        }

        return fragment;
    }

    public static class IndicatorAdapter extends BaseAdapter {
        private int selectedPosition;
        private int itemCount;

        public IndicatorAdapter() {}
        public IndicatorAdapter(int itemCount) { this.itemCount = itemCount; }

        public void setItemCount(int itemCount) {
            this.selectedPosition = 0;
            this.itemCount = itemCount;
            super.notifyDataSetChanged();
        }
        public void setSelectedPosition(int position) {
            this.selectedPosition = position;
            super.notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) { return null; }
        @Override
        public int getCount() { return this.itemCount; }
        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View indicator;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pager_indicator, parent, false);
                indicator = convertView.findViewById(R.id.view_indicator);
                convertView.setTag(indicator);
            } else
                indicator = (View) convertView.getTag();
            indicator.setBackgroundResource(position == this.selectedPosition ? R.drawable.bg_pager_indicator_selected : R.drawable.bg_pager_indicator);
            return convertView;
        }
    }

    public static class ImageFragment extends Fragment {
        private ImageView imageView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return this.imageView = new ImageView(container.getContext());
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final String url = super.getArguments().getString(Common.BUNDLE_IMAGE);
            Glide.with(this).load(url).centerCrop().into(this.imageView);
        }
    }
}