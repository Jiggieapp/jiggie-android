package com.jiggie.android.component.adapter;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiggie.android.R;
import com.jiggie.android.model.Common;

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

            /*if(position % 2 == 0)
            {
                bundle.putString(Common.BUNDLE_IMAGE, image);
            }
            else
            {
                bundle.putString(Common.BUNDLE_IMAGE,"https://scontent.xx.fbcdn.net/hphotos-xal1/t31.0-8/12370850_10203839778713508_3736034773002736547_o.jpg");
            }*/

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
            String url = super.getArguments().getString(Common.BUNDLE_IMAGE);
            //url = "https://img.jiggieapp.com/image/10204456507851351/0?imgid=https://scontent.xx.fbcdn.net/hphotos-xal1/t31.0-8/12370850_10203839778713508_3736034773002736547_o.jpg";
            Glide.with(this)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(this.imageView);
                    /*.into(new GlideDrawableImageViewTarget(this.imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            //never called
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            Utils.d(TAG, "errorglide ");
                            //never called
                            //e.printStackTrace();
                        }
                    });*/
        }
    }

    public static final String TAG = ImagePagerIndicatorAdapter.class.getSimpleName() ;
}