package com.jiggie.android.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.BuildConfig;
import com.jiggie.android.R;
import com.jiggie.android.activity.chat.ChatActivity;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.SimpleJSONObject;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.model.Conversation;
import com.jiggie.android.model.Event;
import com.jiggie.android.model.EventDetail;
import com.jiggie.android.model.Guest;
import com.jiggie.android.model.Login;
import com.jiggie.android.model.Setting;
import com.jiggie.android.model.SocialMatch;
import com.android.volley.VolleyError;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rangg on 21/10/2015.
 */
public class SocialTabFragment extends Fragment implements TabFragment {
    @Bind(R.id.switchSocialize)
    Switch switchSocialize;
    @Bind(R.id.layoutSocialize)
    View layoutSocialize;
    @Bind(R.id.txtSocialize)
    TextView txtSocialize;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.cardGeneral)
    View cardGeneral;
    @Bind(R.id.cardInbound)
    View cardInbound;
    @Bind(R.id.cardEmpty)
    View cardEmpty;
    @Bind(R.id.card)
    View card;

    @Bind(R.id.txtConnectGeneral)
    TextView generalTxtConnect;
    @Bind(R.id.txtEventGeneral)
    TextView generalTxtEvent;
    @Bind(R.id.imageUserGeneral)
    ImageView generalImage;
    @Bind(R.id.txtUserGeneral)
    TextView generalTxtUser;
    @Bind(R.id.btnYesGeneral)
    Button generalBtnYes;
    @Bind(R.id.btnNoGeneral)
    Button generalBtnNo;

    @Bind(R.id.txtEventInbound)
    TextView inboundTxtEvent;
    @Bind(R.id.imageUserInbound)
    ImageView inboundImage;
    @Bind(R.id.txtUserInbound)
    TextView inboundTxtUser;
    @Bind(R.id.btnYesInbound)
    Button inboundBtnYes;
    @Bind(R.id.btnNoInbound)
    Button inboundBtnNo;
    @Bind(R.id.img_wk)
    ImageView imgWk;
    @Bind(R.id.txt_wk_action)
    TextView txtWkAction;
    @Bind(R.id.txt_wk_title)
    TextView txtWkTitle;
    @Bind(R.id.txt_wk_desc)
    TextView txtWkDesc;
    @Bind(R.id.layout_walkthrough)
    FrameLayout layoutWalkthrough;

    private SocialMatch current;
    private HomeMain homeMain;
    private View rootView;
    private String title;

    @Override
    public String getTitle() {
        return this.title == null ? (this.title = this.homeMain.getContext().getString(R.string.social)) : this.title;
    }

    @Override
    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public void onTabSelected() {
        if (this.current == null)
            this.onRefresh();
        App.getInstance().trackMixPanelEvent("View Social Feed");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = this.rootView = inflater.inflate(R.layout.fragment_social, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, this.rootView);

        this.layoutSocialize.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.GONE);
        this.cardGeneral.setVisibility(View.GONE);
        this.cardInbound.setVisibility(View.GONE);
        this.cardEmpty.setVisibility(View.GONE);
        this.card.setVisibility(View.GONE);

        this.switchSocialize.setChecked(Setting.getCurrentSetting().isMatchMe());
        this.switchSocialize.setOnCheckedChangeListener(this.socializeChanged);
        App.getInstance().registerReceiver(this.socialReceiver, new IntentFilter(super.getString(R.string.broadcast_social)));

        if (App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_EVENT, false)) {
            layoutWalkthrough.setVisibility(View.VISIBLE);
            imgWk.setImageResource(R.drawable.wk_img_suggestion);
            txtWkAction.setPadding(0, 0, Utils.myPixel(getActivity(), 37), Utils.myPixel(getActivity(), 22));
            txtWkAction.setText(getString(R.string.wk_match_action));
            txtWkTitle.setText(R.string.wk_match_title);
            txtWkDesc.setText(getResources().getText(R.string.wk_match_desc));
        }

    }

    private void onRefresh() {
        if (super.getContext() == null) {
            // fragment already destroyed.
            return;
        } else if (this.progressBar.getVisibility() == View.VISIBLE) {
            // refreshing is ongoing.
            return;
        }
        this.progressBar.setVisibility(View.VISIBLE);
        this.layoutSocialize.setVisibility(View.GONE);
        final String url = String.format("partyfeed/list/%s/%s", AccessToken.getCurrentAccessToken().getUserId(), Setting.getCurrentSetting().getGenderInterestString());

        VolleyHandler.getInstance().createVolleyArrayRequest(url, new VolleyRequestListener<SocialMatch, JSONArray>() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public SocialMatch onResponseAsync(JSONArray jsonArray) {
                final int length = jsonArray.length();
                SocialMatch current = null;

                for (int i = 0; i < length; i++) {
                    final SocialMatch item = new SocialMatch(jsonArray.optJSONObject(i));

                    if (SocialMatch.Type.isInbound(item)) {
                        current = item;
                        break;
                    } else if (current == null)
                        current = item;
                }

                return current;
            }

            @Override
            public void onResponseCompleted(SocialMatch value) {
                openDetail(value);
            }
        });
    }

    private void openDetail(SocialMatch value) {
        this.current = value;

        if ((super.getContext() != null) && (value == null)) {
            this.layoutSocialize.setVisibility(View.GONE);
            this.cardEmpty.setVisibility(View.VISIBLE);
            this.cardGeneral.setVisibility(View.GONE);
            this.cardInbound.setVisibility(View.GONE);
            this.progressBar.setVisibility(View.GONE);
            this.card.setVisibility(View.GONE);
        } else if (super.getContext() != null) {
            final boolean isInbound = SocialMatch.Type.isInbound(value);
            this.cardInbound.setVisibility(isInbound ? View.VISIBLE : View.GONE);
            this.cardGeneral.setVisibility(isInbound ? View.GONE : View.VISIBLE);
            this.layoutSocialize.setVisibility(View.VISIBLE);
            this.cardEmpty.setVisibility(View.GONE);
            this.card.setVisibility(View.VISIBLE);

            final String image = App.getFacebookImage(value.getFromFacebookId(), generalImage.getWidth() * 2);
            final DrawableTypeRequest<String> glideRequest = Glide.with(SocialTabFragment.this).load(image);

            App.getInstance().trackMixPanelEvent("View Feed Item");

            if (isInbound) {
                this.progressBar.setVisibility(View.GONE);
                this.inboundTxtEvent.setText(value.getEventName());
                this.inboundTxtUser.setText(super.getString(R.string.wants_to_go_with, value.getFromFirstName()));

                glideRequest.asBitmap().centerCrop().into(new BitmapImageViewTarget(inboundImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        super.getView().setImageDrawable(circularBitmapDrawable);
                    }
                });
            } else {
                Glide.with(SocialTabFragment.this).load(image).into(generalImage);
                this.enableButton(false);
                this.generalTxtEvent.setText(value.getEventName());
                this.generalTxtUser.setText(super.getString(R.string.user_viewing, value.getFromFirstName()));
                this.generalTxtConnect.setText(super.getString(R.string.connect_with, value.getFromFirstName()));
                this.layoutSocialize.setVisibility(View.VISIBLE);

                // we need to get venue name from event detail api
                this.progressBar.setVisibility(View.VISIBLE);
                final String url = String.format("event/details/%s/%s/%s", current.getEventId(), AccessToken.getCurrentAccessToken().getUserId(), Setting.getCurrentSetting().getGenderInterestString());

                VolleyHandler.getInstance().createVolleyRequest(url, new VolleyRequestListener<EventDetail, JSONObject>() {
                    @Override
                    public EventDetail onResponseAsync(JSONObject jsonObject) {
                        return new EventDetail(jsonObject);
                    }

                    @Override
                    public void onResponseCompleted(EventDetail value) {
                        if (getContext() != null) {
                            generalTxtEvent.setText(getString(R.string.location_viewing, value.getTitle(), value.getVenueName()));
                            progressBar.setVisibility(View.GONE);
                            enableButton(true);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            enableButton(true);
                        }
                    }
                });
            }
        }
    }

    private CompoundButton.OnCheckedChangeListener socializeChanged = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            final String url = String.format("partyfeed/settings/%s/%s", AccessToken.getCurrentAccessToken().getUserId(), isChecked ? "yes" : "no");
            final ProgressDialog dialog = App.showProgressDialog(getContext());

            VolleyHandler.getInstance().createVolleyRequest(url, new VolleyRequestListener<Void, JSONObject>() {
                @Override
                public Void onResponseAsync(JSONObject jsonObject) {
                    Setting.getCurrentSetting().setMatchMe(isChecked);
                    Setting.getCurrentSetting().save();
                    return null;
                }

                @Override
                public void onResponseCompleted(Void value) {
                    if (getContext() != null) {
                        dialog.dismiss();
                        if (!isChecked) {
                            txtSocialize.setText(R.string.socialize_description_off);
                            cardEmpty.setVisibility(View.GONE);
                            card.setVisibility(View.GONE);
                        } else {
                            txtSocialize.setText(R.string.socialize_description);
                            onRefresh();
                        }
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                        switchSocialize.setOnCheckedChangeListener(null);
                        switchSocialize.setChecked(!isChecked);
                        switchSocialize.setOnCheckedChangeListener(socializeChanged);
                        txtSocialize.setText(isChecked ? R.string.socialize_description_off : R.string.socialize_description);
                        dialog.dismiss();
                    }
                }
            });
        }
    };

    @OnClick(R.id.layout_walkthrough)
    void walkthroughOnClick() {
        if (txtWkTitle.getText().toString().equals(getString(R.string.wk_request_title))) {
            Utils.SHOW_WALKTHROUGH_SOCIAL = false;
            App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_SOCIAL, false).commit();
            layoutWalkthrough.setVisibility(View.GONE);
        }

        imgWk.setImageResource(R.drawable.wk_img_connection);
        txtWkAction.setPadding(0, 0, Utils.myPixel(getActivity(), 32), Utils.myPixel(getActivity(), 22));
        txtWkAction.setText(getString(R.string.wk_request_action));
        txtWkTitle.setText(R.string.wk_request_title);
        txtWkDesc.setText(getResources().getText(R.string.wk_request_desc));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.imageUserGeneral)
    void imageUserGeneralOnClick() {
        this.imageUserOnClick();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.imageUserInbound)
    void imageUserOnClick() {
        final Guest guest = new Guest(this.current.getFromFacebookId(), this.current.getFromFirstName());
        super.startActivity(new Intent(super.getContext(), ProfileDetailActivity.class).putExtra(guest.getClass().getName(), guest));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.cardGeneral)
    void cardGeneralOnClick() {
        this.cardOnClick();
    }

    @OnClick(R.id.cardInbound)
    void cardOnClick() {
        final Event event = new Event(this.current.getEventId(), this.current.getEventName());
        super.startActivity(new Intent(super.getContext(), EventDetailActivity.class).putExtra(event.getClass().getName(), event));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnYesInbound)
    void btnYesInboundOnClick() {
        this.btnYesOnClick();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnYesGeneral)
    void btnYesOnClick() {
        this.match(true);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnNoInbound)
    void btnNoInboundONClick() {
        this.btnNoOnClick();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnNoGeneral)
    void btnNoOnClick() {
        this.match(false);
    }

    private void enableButton(boolean isEnabled) {
        this.generalBtnYes.setEnabled(isEnabled);
        this.generalBtnNo.setEnabled(isEnabled);
        this.inboundBtnYes.setEnabled(isEnabled);
        this.inboundBtnNo.setEnabled(isEnabled);
    }

    private void match(final boolean confirm) {
        final String url = String.format("partyfeed/match/%s/%s/%s", AccessToken.getCurrentAccessToken().getUserId(), this.current.getFromFacebookId(), confirm ? "approved" : "denied");
        this.progressBar.setVisibility(View.VISIBLE);
        this.enableButton(false);

        VolleyHandler.getInstance().createVolleyRequest(url, new VolleyRequestListener<Void, JSONObject>() {
            @Override
            public Void onResponseAsync(JSONObject jsonObject) {
                return null;
            }

            @Override
            public void onResponseCompleted(Void value) {
                final App app = App.getInstance();
                final Context context = getContext();
                final SocialMatch socialMatch = current;
                final Login login = Login.getCurrentLogin();
                final Setting setting = Setting.getCurrentSetting();

                progressBar.setVisibility(View.GONE);
                enableButton(true);
                current = null;
                onRefresh();

                if (confirm) {
                    final SimpleJSONObject json = new SimpleJSONObject();
                    json.putString("ABTestChat:Connect", "Connect");
                    json.putString("name_and_fb_id", String.format("%s_%s_%s", login.getFirstName(), login.getLastName(), AccessToken.getCurrentAccessToken().getUserId()));
                    json.putString("age", StringUtility.getAge(login.getBirthday()));
                    json.putString("app_version", String.valueOf(BuildConfig.VERSION_CODE));
                    json.putString("birthday", login.getBirthday());
                    json.putString("device_type", Build.MODEL);
                    json.putString("email", login.getEmail());
                    json.putString("feed_item_type", "viewed");
                    json.putString("first_name", login.getFirstName());
                    json.putString("gender", setting.getGenderString());
                    json.putString("gender_interest", setting.getGenderInterestString());
                    json.putString("last_name", login.getLastName());
                    json.putString("location", login.getLocation());
                    json.putString("os_version", app.getDeviceOSName());
                    app.trackMixPanelEvent("Accept Feed Item", json);

                    if ((SocialMatch.Type.isInbound(socialMatch)) && (context != null)) {
                        final Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra(Conversation.FIELD_FROM_NAME, socialMatch.getFromFirstName());
                        intent.putExtra(Conversation.FIELD_FACEBOOK_ID, socialMatch.getFromFacebookId());
                        context.sendBroadcast(new Intent(getString(R.string.broadcast_social_chat)));
                        startActivity(intent);
                    }
                } else
                    app.trackMixPanelEvent("Passed Feed Item");
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    enableButton(true);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        App.getInstance().unregisterReceiver(this.socialReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver socialReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
