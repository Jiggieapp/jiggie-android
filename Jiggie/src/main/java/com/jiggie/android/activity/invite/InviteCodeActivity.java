package com.jiggie.android.activity.invite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;

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

    @Override
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
                        .putExtra(Intent.EXTRA_TEXT, "link referral here..")
                        .putExtra(Intent.EXTRA_SUBJECT, "Lets Go Out With Jiggie");

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
    }
}
