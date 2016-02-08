package com.jiggie.android.fragment;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.activity.profile.ProfileSettingActivity;
import com.jiggie.android.activity.setup.SetupTagsActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.MoreTabListAdapter;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.manager.ShareManager;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.ShareLink;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.jiggie.android.model.ShareLinkModel;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by rangg on 21/10/2015.
 */
public class MoreTabFragment extends Fragment implements TabFragment, MoreTabListAdapter.ItemSelectedListener {
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout refreshLayout;
    @Bind(R.id.recycler) RecyclerView recyclerView;

    private MoreTabListAdapter adapter;
    private boolean isTabSelectedOnce;
    private HomeMain homeMain;
    private String title;
    private View rootView;

    ProgressDialog progressDialog = null;
    private ShareLinkModel shareLink;

    @Override
    public void onTabSelected() {
        if (super.getContext() != null) {
            if (!this.isTabSelectedOnce)
                this.adapter.initItems();
            this.adapter.notifyDataSetChanged();
            this.isTabSelectedOnce = true;
        }
    }
    @Override
    public void setHomeMain(HomeMain homeMain) { this.homeMain = homeMain; }
    @Override
    public String getTitle() { return this.title == null ? (this.title = this.homeMain.getContext().getString(R.string.more)) : this.title; }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, this.rootView);

        EventBus.getDefault().register(this);

        this.refreshLayout.setEnabled(false);
        this.recyclerView.setAdapter(this.adapter = new MoreTabListAdapter(this, this));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext()));
    }

    @Override
    public void onItemSelected(int position) {
        Class<?> targetActivity = null;
        if (position == 0)
            targetActivity = ProfileDetailActivity.class;
        else if (position == 1)
            this.inviteFriends();
        else if (position == 2)
            targetActivity = ProfileSettingActivity.class;
        else if (position == 3)
            this.mailSupport();
        else if (position == 4) {
            new AlertDialog.Builder(super.getContext())
                    .setTitle(R.string.logout)
                    .setMessage(R.string.confirmation)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.getSharedPreferences().edit().clear().putBoolean(SetupTagsActivity.PREF_SETUP_COMPLETED, true).apply();
                            LoginManager.getInstance().logOut();
                            getActivity().finish();
                        }
                    }).show();
        }
        if (targetActivity != null)
            super.startActivity(new Intent(super.getActivity(), targetActivity));
    }

    private void inviteFriends() {
        if (this.shareLink != null) {
            String link = String.format("%s\n\n%s", shareLink.getMessage(), shareLink.getUrl());
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, link).setType("text/plain"), getString(R.string.invite)));
            App.getInstance().trackMixPanelEvent("Share App");
        } else {
            progressDialog = App.showProgressDialog(super.getContext());
            ShareManager.loaderShareLink(AccessToken.getCurrentAccessToken().getUserId());
        }
    }

    public void onEvent(ShareLinkModel message){
        App.getInstance().trackMixPanelEvent("Share App");
        if (getContext() != null) {
            String link = String.format("%s\n\n%s", message.getMessage(), message.getUrl());
            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, link).setType("text/plain"), getString(R.string.invite)));
            progressDialog.dismiss();
            shareLink = message;
        }
    }

    public void onEvent(ExceptionModel message){
        if(message.getFrom().equals(Utils.FROM_SHARE_LINK)){
            if (getContext() != null) {
                Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }

    private void mailSupport() {
        final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", super.getString(R.string.support_email), null));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {super.getString(R.string.support_email)}); // hack for android 4.3
        super.startActivity(Intent.createChooser(intent, super.getString(R.string.support)));
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}