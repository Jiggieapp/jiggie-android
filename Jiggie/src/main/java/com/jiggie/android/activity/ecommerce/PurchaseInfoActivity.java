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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.activity.ToolbarWithDotActivity;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.SummaryModel;
import com.jiggie.android.view.TermsItemView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

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

    SummaryModel.Data.Product_summary productSummary;
    @Bind(R.id.txt_event_name)
    TextView txtEventName;
    @Bind(R.id.txt_event_info)
    TextView txtEventInfo;
    @Bind(R.id.txt_tik_title)
    TextView txtTikTitle;
    @Bind(R.id.txt_tik_fill)
    TextView txtTikFill;
    @Bind(R.id.txt_fee_fill)
    TextView txtFeeFill;
    @Bind(R.id.txt_tax_fill)
    TextView txtTaxFill;
    @Bind(R.id.txt_total_fill)
    TextView txtTotalFill;
    @Bind(R.id.img_payment)
    ImageView imgPayment;
    @Bind(R.id.txt_payment)
    TextView txtPayment;

    String eventId, eventName, venueName, startTime;
    @Bind(R.id.lin_terms)
    LinearLayout linTerms;
    ArrayList<TermsItemView> arrTermItemView = new ArrayList<>();

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_purchase_info);
        ButterKnife.bind(this);

        preDefined();

        relPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PurchaseInfoActivity.this, PaymentMethodActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }

    private void preDefined() {
        pagerSlide.setAdapter(new SlideAdapter(getSupportFragmentManager(), pagerSlide));
        pagerSlide.setCurrentItem(1);

        Intent a = getIntent();
        eventId = a.getStringExtra(Common.FIELD_EVENT_ID);
        eventName = a.getStringExtra(Common.FIELD_EVENT_NAME);
        venueName = a.getStringExtra(Common.FIELD_VENUE_NAME);
        startTime = a.getStringExtra(Common.FIELD_STARTTIME);
        productSummary = a.getParcelableExtra(SummaryModel.Data.Product_summary.class.getName());
        SummaryModel.Data.Product_summary.Product_list dataProduct = productSummary.getProduct_list().get(0);

        txtEventName.setText(eventName);
        try {
            final Date startDate = Common.ISO8601_DATE_FORMAT_UTC.parse(startTime);
            txtEventInfo.setText(Common.SERVER_DATE_FORMAT_COMM.format(startDate) + " - " + venueName);
        } catch (ParseException e) {
            throw new RuntimeException(App.getErrorMessage(e), e);
        }
        txtTikTitle.setText(dataProduct.getName() + " Ticket (" + dataProduct.getNum_buy() + ")");
        txtTikFill.setText(StringUtility.getRupiahFormat(dataProduct.getTotal_price()));
        txtFeeFill.setText(StringUtility.getRupiahFormat(dataProduct.getAdmin_fee()));
        txtTaxFill.setText(StringUtility.getRupiahFormat(dataProduct.getTax_amount()));
        txtTotalFill.setText(StringUtility.getRupiahFormat(dataProduct.getTotal_price_all()));

        initTermView(dataProduct);
    }

    private void initTermView(SummaryModel.Data.Product_summary.Product_list dataProduct){
        int size = dataProduct.getTerms().size();
        for(int i=0; i<dataProduct.getTerms().size();i++){
            TermsItemView termsItemView = new TermsItemView(PurchaseInfoActivity.this, dataProduct.getTerms().get(i).getBody());
            linTerms.addView(termsItemView);
            arrTermItemView.add(termsItemView);
        }
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
                        //startActivityForResult();
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
