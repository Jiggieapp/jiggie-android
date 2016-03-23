package com.jiggie.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 3/22/2016.
 */
public class SlideFragment extends Fragment {
    public static final String ARG_TITLE = "arg-title";
    @Bind(R.id.txt_pay)
    TextView txtPay;
    @Bind(R.id.rel_slide_pay)
    RelativeLayout relSlidePay;

    public static Fragment newInstance() {
        SlideFragment en = new SlideFragment();

        return en;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slidepay, container, false);
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
