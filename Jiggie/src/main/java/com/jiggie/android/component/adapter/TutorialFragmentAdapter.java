package com.jiggie.android.component.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rangg on 19/01/2016.
 */
public class TutorialFragmentAdapter extends FragmentPagerAdapter {
    private ImagePagerIndicatorAdapter.IndicatorAdapter indicatorAdapter;
    private Fragment[] fragments;

    public TutorialFragmentAdapter(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        this.fragments = new Fragment[6];
        this.indicatorAdapter = new ImagePagerIndicatorAdapter.IndicatorAdapter(this.fragments.length);

        viewPager.setAdapter(this);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                indicatorAdapter.setSelectedPosition(position);
                super.onPageSelected(position);
            }
        });
        viewPager.setOffscreenPageLimit(this.fragments.length);
    }

    public BaseAdapter getIndicatorAdapter() { return this.indicatorAdapter; }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = this.fragments[position];

        if (fragment == null) {
            fragment = new TutorialFragment();
            final Bundle arg = new Bundle();
            final App app = App.getInstance();

            if (position == 0) {
                arg.putString(TutorialFragment.ARG_TITLE, app.getString(R.string.app_name));
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.see_whats_going));
            } else if (position == 1) {
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.msg_carr_1));
                arg.putInt(TutorialFragment.ARG_IMAGE, R.drawable.ss_car_1);
            } else if (position == 2) {
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.msg_carr_2));
                arg.putInt(TutorialFragment.ARG_IMAGE, R.drawable.ss_car_2);
            } else if (position == 3) {
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.msg_carr_3));
                arg.putInt(TutorialFragment.ARG_IMAGE, R.drawable.ss_car_3);
            } else if (position == 4) {
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.msg_carr_4));
                arg.putInt(TutorialFragment.ARG_IMAGE, R.drawable.ss_car_4);
            }else if (position == 5) {
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.msg_carr_5));
                arg.putInt(TutorialFragment.ARG_IMAGE, R.drawable.ss_car_5);
            }

            fragment.setArguments(arg);
            this.fragments[position] = fragment;
        }

        return fragment;
    }

    @Override
    public int getCount() { return this.fragments.length; }

    public static class TutorialFragment extends Fragment {
        public static final String ARG_TITLE = "arg-title";
        public static final String ARG_IMAGE = "arg-image";
        public static final String ARG_DESC = "arg-desc";

        @Bind(R.id.txtDescription)
        TextView txtDescription;
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.contentView)
        View contentView;
        @Bind(R.id.txtTitle) TextView txtTitle;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_turorial, container, false);
            ButterKnife.bind(this, rootView);
            return rootView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            final Bundle arg = super.getArguments();
            this.txtDescription.setText(arg.getString(ARG_DESC));
            this.txtTitle.setText(arg.getString(ARG_TITLE));

            if (this.txtTitle.getText().length() == 0) {
                this.txtTitle.setVisibility(View.GONE);
                this.imageView.setImageResource(arg.getInt(ARG_IMAGE));
                this.txtDescription.setGravity(Gravity.CENTER_HORIZONTAL);
            } else {
                this.txtDescription.setGravity(Gravity.START);
                this.imageView.setVisibility(View.GONE);

                final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.contentView.getLayoutParams();
                params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                params.setMargins(super.getResources().getDimensionPixelSize(R.dimen.padding_standard), 0, 0, 0);
            }
        }
    }
}

