package com.jiggie.android.activity.invite;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.InviteCodeResultModel;

import org.json.JSONException;
import org.json.JSONObject;

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

    private static String TAG = InviteCodeActivity.class.getSimpleName();
    InviteCodePresenterImplementation inviteCodePresenterImplementation;
    private ProgressDialog progressDialog;
    InviteCodeResultModel inviteCodeResultModel;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    private void showProgressDialog()
    {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.please_wait));
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
            initView();
        }
    }

    private void initView()
    {
        txtCode.setText(inviteCodeResultModel.getData().getInvite_code().getCode());
        btnShareCp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InviteCodeActivity.this, InviteFriendsActivity.class));
            }
        });

        /*btnShareMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, "link referral here..")
                        .putExtra(Intent.EXTRA_SUBJECT, "Lets Go Out With Jiggie");

                i.setType("text/plain");

                startActivity(Intent.createChooser(i, getString(R.string.share)));
            }
        });*/
    }



    @OnClick(R.id.btn_share_fb)
    public void shareToFb() {
        if(getInviteCodeResultModel() == null)
        {
            //inviteCodePresenterImplementation.getInviteCode();
        }
        else
        {
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
    public void onBtnShareCopyClick()
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        final String textInvite = inviteCodeResultModel.getData().getInvite_code().getMsg_share()
                + "\n" + inviteCodeResultModel.getData().getInvite_code().getMsg_invite();
        ClipData clip = ClipData.newPlainText("Jiggie", textInvite);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Your invite code has been copied", Toast.LENGTH_SHORT).show();
    }
}
