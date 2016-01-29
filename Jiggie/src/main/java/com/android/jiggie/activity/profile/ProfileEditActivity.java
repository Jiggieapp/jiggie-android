package com.android.jiggie.activity.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jiggie.component.activity.ToolbarActivity;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import com.android.jiggie.App;
import com.android.jiggie.R;
import com.android.jiggie.component.volley.VolleyHandler;
import com.android.jiggie.component.volley.VolleyRequestListener;
import com.android.jiggie.model.UserProfile;

/**
 * Created by rangg on 17/11/2015.
 */
public class ProfileEditActivity extends ToolbarActivity {
    @Bind(R.id.textView) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile_edit);
        super.bindView();
        super.setBackEnabled(true);

        final String value = super.getIntent().getStringExtra(UserProfile.FIELD_ABOUT);
        this.textView.setText(value);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save)
            this.save();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.root)
    void rootOnClick() { this.textView.requestFocus(); }

    private void save() {
        final ProgressDialog dialog = App.showProgressDialog(this);
        final UserProfile profile = new UserProfile();
        profile.setFacebookId(AccessToken.getCurrentAccessToken().getUserId());
        profile.setAbout(this.textView.getText().toString());

        VolleyHandler.getInstance().createVolleyRequest("updateuserabout", profile, new VolleyRequestListener<String, JSONObject>() {
            @Override
            public String onResponseAsync(JSONObject jsonObject) { return profile.getAbout(); }

            @Override
            public void onResponseCompleted(String value) {
                setResult(RESULT_OK, new Intent().putExtra(UserProfile.FIELD_ABOUT, value));
                App.getInstance().trackMixPanelEvent("MyProfile Update");
                dialog.dismiss();
                finish();
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileEditActivity.this, App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
