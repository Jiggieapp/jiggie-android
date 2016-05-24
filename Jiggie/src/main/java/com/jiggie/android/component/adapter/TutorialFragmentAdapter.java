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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiggie.android.App;
import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rangg on 19/01/2016.
 */
public class TutorialFragmentAdapter extends FragmentPagerAdapter {
    //private ImagePagerIndicatorAdapter.IndicatorAdapter indicatorAdapter;
    private Fragment[] fragments;

    public TutorialFragmentAdapter(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        this.fragments = new Fragment[6];
        //this.indicatorAdapter = new ImagePagerIndicatorAdapter.IndicatorAdapter(this.fragments.length);

        viewPager.setAdapter(this);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //indicatorAdapter.setSelectedPosition(position);
                super.onPageSelected(position);
            }
        });
        viewPager.setOffscreenPageLimit(this.fragments.length);
    }

    //public BaseAdapter getIndicatorAdapter() { return this.indicatorAdapter; }
    public int getIndicatorLength() {
        return (this.fragments.length - 1);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = this.fragments[position];

        if (fragment == null) {
            fragment = new TutorialFragment();
            final Bundle arg = new Bundle();
            final App app = App.getInstance();

            arg.putInt(TutorialFragment.ARG_POSITION, position);

            if (position == 0) {
                arg.putString(TutorialFragment.ARG_TITLE, app.getString(R.string.title_carr_1_new));
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.msg_carr_1));
                arg.putInt(TutorialFragment.ARG_IMAGE, R.drawable.pic1carr_min);
            } else if (position == 1) {
                arg.putString(TutorialFragment.ARG_TITLE, app.getString(R.string.title_carr_2_new));
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.msg_carr_2));
                arg.putInt(TutorialFragment.ARG_IMAGE, R.drawable.pic2carr_min);
            } else if (position == 2) {
                arg.putString(TutorialFragment.ARG_TITLE, app.getString(R.string.title_carr_3_new));
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.msg_carr_3));
                arg.putInt(TutorialFragment.ARG_IMAGE, R.drawable.pic3carr_min);
            } else if (position == 3) {
                arg.putString(TutorialFragment.ARG_TITLE, app.getString(R.string.title_carr_4_new));
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.msg_carr_4));
                arg.putInt(TutorialFragment.ARG_IMAGE, R.drawable.pic4carr_min);
            } else if (position == 4) {
                arg.putString(TutorialFragment.ARG_TITLE, app.getString(R.string.title_carr_5_new));
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.msg_carr_5));
                arg.putInt(TutorialFragment.ARG_IMAGE, R.drawable.pic5carr_min);
            } else if (position == 5) {
                arg.putString(TutorialFragment.ARG_TITLE, app.getString(R.string.app_name));
                arg.putString(TutorialFragment.ARG_DESC, app.getString(R.string.see_whats_going));
            }

            fragment.setArguments(arg);
            this.fragments[position] = fragment;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return this.fragments.length;
    }

    public static class TutorialFragment extends Fragment {
        public static final String ARG_TITLE = "arg-title";
        public static final String ARG_IMAGE = "arg-image";
        public static final String ARG_DESC = "arg-desc";
        public static final String ARG_POSITION = "arg-pos";

        @Bind(R.id.txtDescription)
        TextView txtDescription;
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.contentView)
        View contentView;
        @Bind(R.id.txtTitle)
        TextView txtTitle;
        @Bind(R.id.img_help)
        ImageView imgHelp;
        @Bind(R.id.btnSignIn)
        Button btnSignIn;
        @Bind(R.id.txt_we_dont)
        TextView txtWeDont;
        @Bind(R.id.txtLogo)
        TextView txtLogo;
        @Bind(R.id.img_logo)
        ImageView imgLogo;

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

            if (!this.txtTitle.getText().toString().contains("Jiggie")) {
                this.txtTitle.setVisibility(View.VISIBLE);
                //Glide.with(getActivity()).load(arg.getInt(ARG_IMAGE)).into(imageView);
                Glide.with(getActivity()).load(arg.getInt(ARG_IMAGE)).into(this.imageView);
                //this.imageView.setImageResource(arg.getInt(ARG_IMAGE));
                this.contentView.setVisibility(View.VISIBLE);

                this.btnSignIn.setVisibility(View.GONE);
                this.txtWeDont.setVisibility(View.GONE);
                this.imgLogo.setVisibility(View.GONE);
                this.txtLogo.setVisibility(View.GONE);

            } else {
                this.imageView.setVisibility(View.GONE);
                this.contentView.setVisibility(View.GONE);

                this.btnSignIn.setVisibility(View.VISIBLE);
                this.txtWeDont.setVisibility(View.VISIBLE);
                this.imgLogo.setVisibility(View.VISIBLE);
                this.txtLogo.setVisibility(View.VISIBLE);

            }

            if(arg.getInt(ARG_POSITION)==4){

            }
        }

        public View getContentView() {
            return this.contentView;
        }

        public ImageView getImageViews() {
            return this.imageView;
        }

        public ImageView getImageHelps() {
            return this.imgHelp;
        }

        public Button getBtnSignIn() {
            return this.btnSignIn;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }
    }
}

