package com.jiggie.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.Utils;

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
    @Bind(R.id.r1)
    ImageView r1;
    @Bind(R.id.r2)
    ImageView r2;

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
        if(arg.getString(ARG_TITLE).equals(Utils.BLANK)){
            this.r1.setVisibility(View.GONE);
            this.r2.setVisibility(View.GONE);
        }else{
            this.r1.setVisibility(View.VISIBLE);
            this.r2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
