package com.jiggie.android.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.SplashActivity;
import com.jiggie.android.activity.ecommerce.PurchaseHistoryActivity;
import com.jiggie.android.activity.invite.InviteCodeActivity;
import com.jiggie.android.activity.profile.EditProfileActivity;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.activity.profile.ProfileDetailPresenterImplementation;
import com.jiggie.android.activity.profile.ProfileDetailView;
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
import com.jiggie.android.model.Common;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.model.SuccessCreditBalanceModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 5/25/2016.
 */
public class MoreFragment extends Fragment implements TabFragment, MoreTabListAdapter.ItemSelectedListener, ProfileDetailView {

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.imgEditProfile)
    ImageView imgEditProfile;
    @Bind(R.id.txtUser)
    TextView txtUser;
    @Bind(R.id.textCredit)
    TextView textCredit;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private MoreTabListAdapter adapter;
    private boolean isTabSelectedOnce;
    //private ShareLink shareLink;
    private HomeMain homeMain;
    private String title;
    private View rootView;
    private String strCredit = Utils.BLANK;

    private ProfileDetailPresenterImplementation profilePresenter;

    @Override
    public String getTitle() {
        return this.title == null ? (this.title = this.homeMain.getContext().getString(R.string.more)) : this.title;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_more;
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
        View view = this.rootView = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, this.rootView);

        preDefine();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadCredit();
    }

    private void preDefine(){
        final String userImage = App.getSharedPreferences().getString(Common.PREF_IMAGE, null);
        final String dataPath = App.getInstance().getDataPath(Common.PREF_IMAGES);
        final String imagePath;

        txtUser.setText(App.getSharedPreferences().getString(Common.PREF_FACEBOOK_NAME, null));

        //Added by Aga 12-2-2016
        if (userImage != null) {
            imagePath = String.format("file:///%s%s", dataPath, userImage);
        } else {
            final int width = imageView.getWidth() * 2;
            imagePath = App.getFacebookImage(AccessToken.getCurrentAccessToken().getUserId(), width);
        }
        //--------
        profilePresenter = new ProfileDetailPresenterImplementation(this);
        fetchDetail();

        /*Glide.with(this).load(imagePath).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                super.getView().setImageDrawable(circularBitmapDrawable);
            }
        });*/

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open new Profile detail
                Intent intent = new Intent(getActivity(), ProfileDetailActivity.class);
                intent.putExtra(Common.FIELD_FACEBOOK_ID, AccessToken.getCurrentAccessToken().getUserId());
                getActivity().startActivity(intent);
            }
        });

        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        setMoreAdapter();
    }

    private void loadCredit() {
        CreditBalanceManager.loaderCreditBalance(AccessToken.getCurrentAccessToken().getUserId(), new CreditBalanceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                SuccessCreditBalanceModel successCreditBalanceModel = (SuccessCreditBalanceModel) object;
                strCredit = "Credit: " + StringUtility.getCreditBalanceFormat(successCreditBalanceModel.getData().getBalance_credit().getTot_credit_active());
                textCredit.setText(strCredit);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                textCredit.setText("Credit: Rp. -");
            }
        });
    }

    private void setMoreAdapter() {
        this.refreshLayout.setEnabled(false);
        this.recyclerView.setAdapter(this.adapter = new MoreTabListAdapter(this, this));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
    }

    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(getActivity(), PurchaseHistoryActivity.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(), InviteCodeActivity.class));
                break;
            case 2:
                startActivity(new Intent(getActivity(), PromotionsActivity.class));
                break;
            case 3:
                startActivity(new Intent(getActivity(), ProfileSettingActivity.class));
                break;
            case 4:
                mailSupport();
                break;
            case 5:
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
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{super.getString(R.string.support_email)}); // hack for android 4.3
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.support));
        super.startActivity(Intent.createChooser(intent, super.getString(R.string.support)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void loadImages(ArrayList<String> photos) {

    }

    @Override
    public void onFailUpload(int position) {

    }

    @Override
    public void doCrop(Uri filepath, Uri destination) {

    }

    @Override
    public void loadImageToCertainView(String tempUrl, int position) {

    }

    @Override
    public void onFailure() {

    }

    @Override
    public Bitmap onFinishTakePhoto(int requestCode, Uri uri) {
        return null;
    }

    @Override
    public ImageView makeTransparent(int position) {
        return null;
    }

    @Override
    public void getPhoto() {

    }

    @Override
    public void onFinishUpload(int position) {

    }

    @Override
    public ImageView removeTransparent(int position) {
        return null;
    }

    @Override
    public void onSuccess(MemberInfoModel memberInfoModel) {
        MemberInfoModel.Data.MemberInfo memberInfo = memberInfoModel.getData().getMemberinfo();
        Glide.with(this).load(memberInfo.getPhotos().get(0)).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                super.getView().setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void fetchDetail() {
        profilePresenter.fetchMemberInfo(AccessToken.getCurrentAccessToken().getUserId());
    }
}
