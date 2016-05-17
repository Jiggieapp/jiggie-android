package com.jiggie.android.activity.invite;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.InviteManager;
import com.jiggie.android.model.InviteCodeResultModel;
import com.jiggie.android.model.ReferEventMixpanelModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LTE on 5/12/2016.
 */
public class InviteCodeActivity extends ToolbarActivity implements InviteCodeView {

    @Bind(R.id.appBar)
    AppBarLayout appBar;
    @Bind(R.id.txt_share_promote)
    TextView txtSharePromote;
    @Bind(R.id.txt_code)
    TextView txtCode;
    @Bind(R.id.txt_desc)
    TextView txtDesc;
    @Bind(R.id.btn_share_fb)
    Button btnShareFb;
    @Bind(R.id.btn_share_cp)
    Button btnShareCp;
    @Bind(R.id.btn_share_msg)
    Button btnShareMsg;
    @Bind(R.id.btn_share_copy)
    Button btnShareCopy;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rel_content)
    RelativeLayout relContent;
    /*@Bind(R.id.progressBar)
    ProgressBar progressBar;*/

    private static String TAG = InviteCodeActivity.class.getSimpleName();
    InviteCodePresenterImplementation inviteCodePresenterImplementation;
    private ProgressDialog progressDialog;
    InviteCodeResultModel inviteCodeResultModel;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    private void showProgressDialog()
    {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog()
    {
        if(progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_invite_friend_code);
        super.bindView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Invite Friends");

        inviteCodePresenterImplementation = new InviteCodePresenterImplementation(this);
        if(getInviteCodeResultModel() == null)
        {
            showProgressDialog();
            inviteCodePresenterImplementation.getInviteCode();
        }
        else
        {
            Utils.d(TAG, "masuk sini?");
            initView();
        }
        //initView();
    }

    private void initView()
    {
        txtCode.setText(inviteCodeResultModel.getData().getInvite_code().getCode());
        btnShareCp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getInstance().trackMixPanelReferral(Utils.REFERRAL_PHONE, new ReferEventMixpanelModel(InviteManager.referEventMixpanelModel.getPromo_code(), InviteManager.referEventMixpanelModel.getPromo_url()));
                startActivity(new Intent(InviteCodeActivity.this, InviteFriendsActivity.class));
            }
        });
    }


    @OnClick(R.id.btn_share_fb)
    public void shareToFb() {
        if(getInviteCodeResultModel() == null)
        {
            //inviteCodePresenterImplementation.getInviteCode();
        }
        else
        {
            App.getInstance().trackMixPanelReferral(Utils.REFERRAL_FACEBOOK, new ReferEventMixpanelModel(InviteManager.referEventMixpanelModel.getPromo_code(), InviteManager.referEventMixpanelModel.getPromo_url()));
            FacebookSdk.sdkInitialize(this);
            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(this);
            // this part is optional
            //shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() { ... });
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(getInviteCodeResultModel().getData().getInvite_code().getMsg_share())
                        .setContentDescription(
                                getInviteCodeResultModel().getData().getInvite_code().getMsg_invite())
                        .setContentUrl(Uri.parse(getInviteCodeResultModel().getData().getInvite_code().getInvite_url()))
                        .build();

                shareDialog.show(linkContent);
            }
        }

    }

    private InviteCodeResultModel getInviteCodeResultModel() {
        final String inv = AccountManager.getInviteCodeFromPreference();
        if(inv.isEmpty())
            return null;
        else
        {
             inviteCodeResultModel
                    = new Gson().fromJson(inv, InviteCodeResultModel.class);
            return inviteCodeResultModel;
        }
    }


    private void setInviteCodeResultModel(InviteCodeResultModel inviteCodeResultModel) {
        AccountManager.setInviteCodeToPreferences(new Gson().toJson(inviteCodeResultModel).toString());
        inviteCodeResultModel = getInviteCodeResultModel();
        InviteManager.referEventMixpanelModel = new ReferEventMixpanelModel(InviteManager.referEventMixpanelModel.getPromo_code(), InviteManager.referEventMixpanelModel.getPromo_url());
        initView();
    }

    @Override
    public void onFinishGetInviteCode(InviteCodeResultModel inviteCodeResultModel) {
        dismissProgressDialog();
        setInviteCodeResultModel(inviteCodeResultModel);

    }

    @Override
    public void onFailedToGetInviteCode(String message) {
        dismissProgressDialog();
    }

    @OnClick(R.id.btn_share_msg)
    public void onBtnShareMessageClick()
    {
        if(getInviteCodeResultModel() == null)
        {
            //inviteCodePresenterImplementation.getInviteCode();
        }
        else
        {
            App.getInstance().trackMixPanelReferral(Utils.REFERRAL_MESSAGE, new ReferEventMixpanelModel(InviteManager.referEventMixpanelModel.getPromo_code(), InviteManager.referEventMixpanelModel.getPromo_url()));
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            final String textInvite = inviteCodeResultModel.getData().getInvite_code().getMsg_share()
                + "\n" + inviteCodeResultModel.getData().getInvite_code().getMsg_invite();
            sendIntent.putExtra(Intent.EXTRA_TEXT, textInvite);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.lets_go_out));
            sendIntent.setType("text/plain");
            //startActivity(sendIntent);
            startActivity(Intent.createChooser(sendIntent, getString(R.string.share)));
        }
    }

    @OnClick(R.id.btn_share_copy)
    public void onBtnShareCopyClick() {
        setClipboard(inviteCodeResultModel.getData().getInvite_code().getInvite_url());
        Toast.makeText(this
                , getResources().getString(R.string.invite_code_has_been_copied)
                , Toast.LENGTH_SHORT);
    }

    private void setClipboard(String text) {
        App.getInstance().trackMixPanelReferral(Utils.REFERRAL_COPY, new ReferEventMixpanelModel(InviteManager.referEventMixpanelModel.getPromo_code(), InviteManager.referEventMixpanelModel.getPromo_url()));
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied referral link", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}
