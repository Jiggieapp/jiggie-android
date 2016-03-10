package com.jiggie.android.activity.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarWithDotActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/9/2016.
 */
public class PurchaseInfoActivity extends ToolbarWithDotActivity {

    @Bind(R.id.pager_slide)
    ViewPager pagerSlide;
    @Bind(R.id.rel_payment)
    RelativeLayout relPayment;


    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_purchase_info);
        ButterKnife.bind(this);

        pagerSlide.setAdapter(new SlideAdapter(getSupportFragmentManager(), pagerSlide));
        pagerSlide.setCurrentItem(1);

        relPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PurchaseInfoActivity.this, PaymentMethodActivity.class));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        pagerSlide.setCurrentItem(1);
    }

    public class SlideAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;

        public SlideAdapter(FragmentManager fm, ViewPager viewPager) {
            super(fm);
            this.fragments = new Fragment[2];
            viewPager.setAdapter(this);
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    //action
                    if (position == 0) {
                        startActivity(new Intent(PurchaseInfoActivity.this, HowToPayActivity.class));
                    }

                    super.onPageSelected(position);
                }
            });
            viewPager.setOffscreenPageLimit(this.fragments.length);
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = this.fragments[position];

            if (fragment == null) {
                fragment = new SlideFragment();
                final Bundle arg = new Bundle();
                final App app = App.getInstance();

                if (position == 0) {
                    arg.putString(SlideFragment.ARG_TITLE, "");
                } else if (position == 1) {
                    arg.putString(SlideFragment.ARG_TITLE, app.getString(R.string.pci_slide));
                }

                fragment.setArguments(arg);
                this.fragments[position] = fragment;
            }

            return fragment;
        }
    }

    public static class SlideFragment extends Fragment {
        public static final String ARG_TITLE = "arg-title";
        @Bind(R.id.txt_pay)
        TextView txtPay;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_slidepay, container, false);
            ButterKnife.bind(this, rootView);
            return rootView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            final Bundle arg = super.getArguments();
            this.txtPay.setText(arg.getString(ARG_TITLE));
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }
    }

    @Override
    protected int getCurrentStep() {
        return 3;
    }

    @Override
    protected String getToolbarTitle() {
        return "PURCHASE INFO";
    }
}
