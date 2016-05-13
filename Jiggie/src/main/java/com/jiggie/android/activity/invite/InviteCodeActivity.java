package com.jiggie.android.activity.invite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.BaseManager;
import com.jiggie.android.manager.InviteManager;
import com.jiggie.android.model.InviteCodeModel;

import butterknife.Bind;

/**
 * Created by LTE on 5/12/2016.
 */
public class InviteCodeActivity extends ToolbarActivity {

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
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    String invite_url = Utils.BLANK;
    String invite_msg = Utils.BLANK;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_invite_friend_code);
        super.bindView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Invite Friends");

        btnShareCp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InviteCodeActivity.this, InviteFriendsActivity.class));
            }
        });

        btnShareMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, invite_url)
                        .putExtra(Intent.EXTRA_SUBJECT, invite_msg);

                /*if (file != null && file.exists()) {
                    i.putExtra(Intent.EXTRA_STREAM,
                            Uri.parse("file:" + file.getAbsolutePath()));
                }*/

                i.setType("text/plain");
                /*if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();*/

                startActivity(Intent.createChooser(i, getString(R.string.share)));
            }
        });

        btnShareCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(invite_url);
            }
        });

        relContent.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        /*BaseManager.isTokenAlready(new BaseManager.OnExistListener() {
            @Override
            public void onExist(boolean isExist) {
                if(isExist){
                    InviteManager.loaderInviteCode(AccessToken.getCurrentAccessToken().getUserId(), new InviteManager.OnResponseListener() {
                        @Override
                        public void onSuccess(Object object) {
                            InviteCodeModel inviteCodeModel = (InviteCodeModel)object;
                            if(inviteCodeModel!=null){
                                InviteCodeModel.Data.InviteCode inviteCode = inviteCodeModel.getData().getInvite_code();
                                txtCode.setText(inviteCode.getCode());
                                txtDesc.setText(inviteCode.getMsg_invite());
                                invite_url = inviteCode.getInvite_url();
                                invite_msg = inviteCode.getMsg_invite();

                                relContent.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }else{
                                relContent.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onFailure(int responseCode, String message) {
                            relContent.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }else{
                    //do nothing
                    relContent.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });*/

    }

    private void setClipboard(String text) {
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
