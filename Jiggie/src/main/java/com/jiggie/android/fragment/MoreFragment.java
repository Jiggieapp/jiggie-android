package com.jiggie.android.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.SplashActivity;
import com.jiggie.android.activity.ecommerce.PurchaseHistoryActivity;
import com.jiggie.android.activity.invite.InviteCodeActivity;
import com.jiggie.android.activity.profile.ProfileSettingActivity;
import com.jiggie.android.activity.promo.PromotionsActivity;
import com.jiggie.android.activity.setup.SetupTagsActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.MoreTabListAdapter;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.CreditBalanceManager;
import com.jiggie.android.model.SuccessCreditBalanceModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 5/25/2016.
 */
public class MoreFragment extends Fragment implements TabFragment, MoreTabListAdapter.ItemSelectedListener {

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;

    private MoreTabListAdapter adapter;
    private boolean isTabSelectedOnce;
    //private ShareLink shareLink;
    private HomeMain homeMain;
    private String title;
    private View rootView;
    private String strCredit = Utils.BLANK;

    @Override
    public String getTitle() {
        return this.title == null ? (this.title = this.homeMain.getContext().getString(R.string.more)) : this.title;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_more_horiz_white_24dp;
    }

    @Override
    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public void onTabSelected() {
        if (super.getContext() != null) {
            if (!this.isTabSelectedOnce)
                this.adapter.initItems();
            this.adapter.notifyDataSetChanged();
            this.isTabSelectedOnce = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, this.rootView);

        loadCredit();

    }

    private void loadCredit(){
        CreditBalanceManager.loaderCreditBalance(AccessToken.getCurrentAccessToken().getUserId(), new CreditBalanceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                SuccessCreditBalanceModel successCreditBalanceModel = (SuccessCreditBalanceModel) object;
                strCredit = "Credit: " + StringUtility.getCreditBalanceFormat(successCreditBalanceModel.getData().getBalance_credit().getTot_credit_active());
                setMoreAdapter(strCredit);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                setMoreAdapter("Credit: Rp. -");
            }
        });
    }

    private void setMoreAdapter(String credit){
        this.refreshLayout.setEnabled(false);
        this.recyclerView.setAdapter(this.adapter = new MoreTabListAdapter(this, this, credit));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
    }

    @Override
    public void onItemSelected(int position) {
        switch (position)
        {
            case 0:
                startActivity(new Intent(getActivity(), PurchaseHistoryActivity.class));
                break;
            case 1:
                //do nothing
                break;
            case 2:
                startActivity(new Intent(getActivity(), InviteCodeActivity.class));
                break;
            case 3:
                startActivity(new Intent(getActivity(), PromotionsActivity.class));
                break;
            case 4:
                startActivity(new Intent(getActivity(), ProfileSettingActivity.class));
                break;
            case 5:
                mailSupport();
                break;
            case 6:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.logout)
                        .setMessage(R.string.confirmation)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                App.getSharedPreferences().edit().clear().putBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, true).apply();
                                App.getSharedPreferences().edit().clear().apply();
                                LoginManager.getInstance().logOut();

                                AccountManager.onLogout();

                                //getActivity().finish();

                                //added by Aga 22-1-2016
                                Intent i = new Intent(getActivity(), SplashActivity.class);
                                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                                getActivity().finish();

                                /*Intent intent = getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);*/
                                //finish();

                                //----------------------

                            }
                        }).show();
                break;
        }
    }

    @Override
    public void onVerifyPhoneNumberSelected() {

    }

    private void mailSupport() {
        final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", super.getString(R.string.support_email), null));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {super.getString(R.string.support_email)}); // hack for android 4.3
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.support));
        super.startActivity(Intent.createChooser(intent, super.getString(R.string.support)));
    }
}
