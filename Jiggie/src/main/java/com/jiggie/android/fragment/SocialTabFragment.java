package com.jiggie.android.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.BuildConfig;
import com.jiggie.android.R;
import com.jiggie.android.activity.chat.ChatActivity;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.activity.profile.ProfileSettingActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.SimpleJSONObject;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.manager.SocialManager;
import com.jiggie.android.manager.WalkthroughManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.Conversation;
import com.jiggie.android.model.EventDetailModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.PostWalkthroughModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.SocialModel;
import com.jiggie.android.model.Success2Model;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

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

    //private SocialMatch current;
    private HomeMain homeMain;
    private View rootView;
    private String title;

    private SocialModel.Data.SocialFeeds current;
    boolean confirm;
    SettingModel currentSetting;
    private int socialSize;
    public static final String TAG = SocialTabFragment.class.getSimpleName();
    private Dialog dialogWalkthrough;

    @Override
    public String getTitle() {
        return this.title == null ? (this.title = this.homeMain.getContext().getString(R.string.social)) : this.title;
    }

    @Override
    public int getIcon()
    {
        return R.drawable.ic_visibility_white_24dp;
    }

    @Override
    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public void onTabSelected() {

        currentSetting = AccountManager.loadSetting();

        boolean a = AccountManager.anySettingChange;

        if (this.current == null){
            if (switchSocialize.isChecked()) {
                txtSocialize.setText(R.string.socialize_description);
                this.onRefresh();
            }else{
                this.layoutSocialize.setVisibility(View.VISIBLE);
                txtSocialize.setText(R.string.socialize_description_off);
            }

            if (App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_SOCIAL, false)) {
                showWalkthroughDialog();
            }
        }

        if (switchSocialize.isChecked()&&AccountManager.anySettingChange) {
            txtSocialize.setText(R.string.socialize_description);
            this.onRefresh();
            AccountManager.anySettingChange = false;
        }

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

        EventBus.getDefault().register(this);

        this.layoutSocialize.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.GONE);
        this.cardGeneral.setVisibility(View.GONE);
        this.cardInbound.setVisibility(View.GONE);
        this.cardEmpty.setVisibility(View.GONE);
        this.card.setVisibility(View.GONE);

        currentSetting = AccountManager.loadSetting();

        //wandy 22-02-2016
        //currentSetting = null;

        if(currentSetting != null)
        {
            this.switchSocialize.setChecked(currentSetting.isMatchme());
         }
        else
        {
            this.switchSocialize.setChecked(true);
        }
        this.switchSocialize.setOnCheckedChangeListener(this.socializeChanged);
        App.getInstance().registerReceiver(this.socialReceiver, new IntentFilter(super.getString(R.string.broadcast_social)));
    }

    private void onRefresh() {
        if(!AccountManager.isInSettingPage){
            if (super.getContext() == null) {
                // fragment already destroyed.
                return;
            } else if (this.progressBar.getVisibility() == View.VISIBLE) {
                // refreshing is ongoing.
                return;
            }

        /*if(current==null){
            this.layoutSocialize.setVisibility(View.GONE);
        }*/
            this.progressBar.setVisibility(View.VISIBLE);

            //showProgressDialog();
            SocialManager.loaderSocialFeed(AccessToken.getCurrentAccessToken().getUserId(), currentSetting.getData().getGender_interest());
        }

    }

    public void onEvent(SocialModel message){
        current = null;
        socialSize = 0;
        for(SocialModel.Data.SocialFeeds item : message.getData().getSocial_feeds())
        {
            if(SocialManager.Type.isInbound(item))
            {
                socialSize++;
            }
        }
        dismissProgressDialog();

        for (int i = 0; i < message.getData().getSocial_feeds().size(); i++) {
            final SocialModel.Data.SocialFeeds item = message.getData().getSocial_feeds().get(i);
            //if(item..equalsIgnoreCase(Common.SOCIAL_FEED_TYPE_APPROVED))
            if (SocialManager.Type.isInbound(item)) {
                current = item;
                break;
            } else if (current == null)
                current = item;
        }
        setHomeTitle();
        openDetail(current);
    }

    public void onEvent(ExceptionModel message){
        String ex = message.getMessage();
        if(message.getFrom().equals(Utils.FROM_SOCIAL_FEED)||message.getFrom().equals(Utils.FROM_SOCIAL_MATCH)||message.getFrom().equals(Utils.FROM_EVENT_DETAIL)){
            if(ex.equals(Utils.RESPONSE_FAILED+" "+"empty data")){
                this.layoutSocialize.setVisibility(View.GONE);
                this.cardEmpty.setVisibility(View.VISIBLE);
                this.cardGeneral.setVisibility(View.GONE);
                this.cardInbound.setVisibility(View.GONE);
                this.progressBar.setVisibility(View.GONE);
                this.card.setVisibility(View.GONE);
            }else{
                if (getContext() != null) {
                    dismissProgressDialog();
                    Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    if(message.getFrom().equals(Utils.FROM_SOCIAL_MATCH)||message.getFrom().equals(Utils.FROM_EVENT_DETAIL)){
                        enableButton(true);
                    }
                }
            }

        }
    }

    private void openDetail(SocialModel.Data.SocialFeeds value) {
        this.current = value;

        if ((super.getContext() != null) && (value == null)) {
            this.layoutSocialize.setVisibility(View.GONE);
            this.cardEmpty.setVisibility(View.VISIBLE);
            this.cardGeneral.setVisibility(View.GONE);
            this.cardInbound.setVisibility(View.GONE);
            this.progressBar.setVisibility(View.GONE);
            this.card.setVisibility(View.GONE);
        } else if (super.getContext() != null) {
            final boolean isInbound = SocialManager.Type.isInbound(value);
            this.cardInbound.setVisibility(isInbound ? View.VISIBLE : View.GONE);
            this.cardGeneral.setVisibility(isInbound ? View.GONE : View.VISIBLE);
            this.layoutSocialize.setVisibility(View.VISIBLE);
            this.cardEmpty.setVisibility(View.GONE);
            this.card.setVisibility(View.VISIBLE);

            final String image = App.getFacebookImage(value.getFrom_fb_id(), generalImage.getWidth() * 2);
            final DrawableTypeRequest<String> glideRequest = Glide.with(SocialTabFragment.this).load(image);

            App.getInstance().trackMixPanelEvent("View Feed Item");

            if (isInbound) {
                this.progressBar.setVisibility(View.GONE);
                this.inboundTxtEvent.setText(value.getEvent_name());
                this.inboundTxtUser.setText(super.getString(R.string.wants_to_go_with, value.getFrom_first_name()));

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
                this.generalTxtEvent.setText(value.getEvent_name());
                this.generalTxtUser.setText(super.getString(R.string.user_viewing, value.getFrom_first_name()));
                this.generalTxtConnect.setText(super.getString(R.string.connect_with, value.getFrom_first_name()));
                this.layoutSocialize.setVisibility(View.VISIBLE);

                // we need to get venue name from event detail api
                this.progressBar.setVisibility(View.VISIBLE);

                EventManager.loaderEventDetail(current.getEvent_id()
                        , AccessToken.getCurrentAccessToken().getUserId()
                        , currentSetting.getData().getGender_interest()
                        ,TAG);
            }
        }
    }

    public void onEvent(EventDetailModel message){
        if (getContext() != null && message.getFrom().equalsIgnoreCase(TAG)) {
            dismissProgressDialog();
            generalTxtEvent.setText(getString(R.string.location_viewing, message.getData().getEvents_detail().getTitle(), message.getData().getEvents_detail().getVenue_name()));
            progressBar.setVisibility(View.GONE);
            enableButton(true);
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
                    currentSetting.setMatchme(isChecked);
                    AccountManager.saveSetting(currentSetting);
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

    @SuppressWarnings("unused")
    @OnClick(R.id.imageUserGeneral)
    void imageUserGeneralOnClick() {
        this.imageUserOnClick();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.imageUserInbound)
    void imageUserOnClick() {
        super.startActivity(new Intent(super.getContext(), ProfileDetailActivity.class).putExtra(Common.FIELD_FACEBOOK_ID, current.getFrom_fb_id()));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.cardGeneral)
    void cardGeneralOnClick() {
        this.cardOnClick();
    }

    @OnClick(R.id.cardInbound)
    void cardOnClick() {
        Intent i = new Intent(super.getContext(), EventDetailActivity.class);
        i.putExtra(Common.FIELD_EVENT_ID, current.getEvent_id());
        i.putExtra(Common.FIELD_EVENT_NAME, current.getEvent_name());
        super.startActivity(i);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnYesInbound)
    void btnYesInboundOnClick() {
        this.btnYesOnClick();
        socialSize-=1;
        setHomeTitle();
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
        socialSize-=1;
        setHomeTitle();
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

    private ProgressDialog progressDialog;

    private void showProgressDialog()
    {
        if(progressDialog == null)
        {
            progressDialog = ProgressDialog.show(getActivity(), "",
                    getResources().getString(R.string.wait));
            progressDialog.setCancelable(false);
        }

        if(!progressDialog.isShowing())
            progressDialog.show();
    }

    private void dismissProgressDialog()
    {
        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void match(final boolean confirms) {
        this.progressBar.setVisibility(View.VISIBLE);
        this.enableButton(false);

        confirm = confirms;
        showProgressDialog();
        if(current!=null)
            SocialManager.loaderSocialMatch(AccessToken.getCurrentAccessToken().getUserId(), this.current.getFrom_fb_id(), confirm ? "approved" : "denied");
    }

    public void onEvent(Success2Model message) {
        if(message.getFrom().equals(SocialManager.TAG))
        {
            final App app = App.getInstance();
            final Context context = getContext();
            final SocialModel.Data.SocialFeeds socialMatch = current;
            final LoginModel login = AccountManager.loadLogin();
            final SettingModel setting = AccountManager.loadSetting();

            progressBar.setVisibility(View.GONE);
            enableButton(true);
            current = null;

            onRefresh();

            if (confirm) {
                final SimpleJSONObject json = new SimpleJSONObject();
                json.putString("ABTestChat:Connect", "Connect");
                json.putString("name_and_fb_id", String.format("%s_%s_%s", login.getUser_first_name(), login.getUser_last_name(), AccessToken.getCurrentAccessToken().getUserId()));
                json.putString("age", StringUtility.getAge2(login.getBirthday()));
                json.putString("app_version", String.valueOf(BuildConfig.VERSION_CODE));
                json.putString("birthday", login.getBirthday());
                json.putString("device_type", Build.MODEL);
                json.putString("email", login.getEmail());
                json.putString("feed_item_type", "viewed");
                json.putString("first_name", login.getUser_first_name());
                json.putString("gender", setting.getData().getGender());
                json.putString("gender_interest", setting.getData().getGender_interest());
                json.putString("last_name", login.getUser_last_name());
                json.putString("location", login.getLocation());
                json.putString("os_version", app.getDeviceOSName());
                app.trackMixPanelEvent("Accept Feed Item", json);

                if ((SocialManager.Type.isInbound(socialMatch)) && (context != null)) {
                    final Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(Conversation.FIELD_FROM_NAME, socialMatch.getFrom_first_name());
                    intent.putExtra(Conversation.FIELD_FACEBOOK_ID, socialMatch.getFrom_fb_id());
                    context.sendBroadcast(new Intent(getString(R.string.broadcast_social_chat)));
                    startActivity(intent);
                }

                dismissProgressDialog();
            }
            else
            {
                app.trackMixPanelEvent("Passed Feed Item");
            }
        }
    }

    @Override
    public void onDestroy() {
        App.getInstance().unregisterReceiver(this.socialReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private BroadcastReceiver socialReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getContext() != null) {
                //SocialManager.NEED_REFRESH = true;

                if (switchSocialize.isChecked()) {
                    txtSocialize.setText(R.string.socialize_description);
                    onRefresh();
                }else{
                    layoutSocialize.setVisibility(View.VISIBLE);
                    txtSocialize.setText(R.string.socialize_description_off);
                }

                //onRefresh();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setHomeTitle() {
        if (this.homeMain != null) {
            //final int unreadCount = this.adapter.countUnread();
            if (socialSize > 0)
            {
                if(socialSize >= 99)
                    this.title =  "99";
                else
                    this.title = socialSize + "";

            }
            else if(socialSize <= 0)
            {
                socialSize = 0;
                this.title="0";
            }
            this.homeMain.onTabTitleChanged(this);
        }
    }

    private void showWalkthroughDialog() {
        dialogWalkthrough = new Dialog(getActivity());
        dialogWalkthrough.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWalkthrough.setContentView(R.layout.walkthrough_screen);
        dialogWalkthrough.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogWalkthrough.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        RelativeLayout layout = (RelativeLayout)dialogWalkthrough.findViewById(R.id.layout_walkthrough);
        final ImageView imgWk = (ImageView)dialogWalkthrough.findViewById(R.id.img_wk);
        final TextView txtWkAction = (TextView)dialogWalkthrough.findViewById(R.id.txt_wk_action);
        final TextView txtWkTitle = (TextView)dialogWalkthrough.findViewById(R.id.txt_wk_title);
        final TextView txtWkDesc = (TextView)dialogWalkthrough.findViewById(R.id.txt_wk_desc);
        imgWk.setImageResource(R.drawable.wk_img_suggestion);
        txtWkAction.setPadding(0, 0, Utils.myPixel(getActivity(), 27), Utils.myPixel(getActivity(), 22));
        txtWkAction.setText(getString(R.string.wk_match_action));
        txtWkTitle.setText(R.string.wk_match_title);
        txtWkDesc.setText(getResources().getText(R.string.wk_match_desc));

        dialogWalkthrough.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (txtWkTitle.getText().toString().equals(getString(R.string.wk_request_title))) {
                    Utils.SHOW_WALKTHROUGH_SOCIAL = false;
                    App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_SOCIAL, false).commit();
                    dismissWalkthroughDialog();

                    PostWalkthroughModel postWalkthroughModel = new PostWalkthroughModel(AccessToken.getCurrentAccessToken().getUserId(), Utils.TAB_SOCIAL, Utils.DEVICE_ID);
                    WalkthroughManager.loaderPostWalkthrough(postWalkthroughModel);

                } else {
                    imgWk.setImageResource(R.drawable.wk_img_connection);
                    txtWkAction.setPadding(0, 0, Utils.myPixel(getActivity(), 30), Utils.myPixel(getActivity(), 22));
                    txtWkAction.setText(getString(R.string.wk_request_action));
                    txtWkTitle.setText(R.string.wk_request_title);
                    txtWkDesc.setText(getResources().getText(R.string.wk_request_desc));
                    showAgainWalkthroughDialog();
                }

            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtWkTitle.getText().toString().equals(getString(R.string.wk_request_title))) {
                    Utils.SHOW_WALKTHROUGH_SOCIAL = false;
                    App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_SOCIAL, false).commit();
                    dismissWalkthroughDialog();

                    PostWalkthroughModel postWalkthroughModel = new PostWalkthroughModel(AccessToken.getCurrentAccessToken().getUserId(), Utils.TAB_SOCIAL, Utils.DEVICE_ID);
                    WalkthroughManager.loaderPostWalkthrough(postWalkthroughModel);

                } else {
                    imgWk.setImageResource(R.drawable.wk_img_connection);
                    txtWkAction.setPadding(0, 0, Utils.myPixel(getActivity(), 30), Utils.myPixel(getActivity(), 22));
                    txtWkAction.setText(getString(R.string.wk_request_action));
                    txtWkTitle.setText(R.string.wk_request_title);
                    txtWkDesc.setText(getResources().getText(R.string.wk_request_desc));
                    //showAgainWalkthroughDialog();
                }
            }
        });

        dialogWalkthrough.setCanceledOnTouchOutside(true);
        dialogWalkthrough.setCancelable(true);
        dialogWalkthrough.show();
    }

    private void showAgainWalkthroughDialog(){
        if(dialogWalkthrough!=null&&!dialogWalkthrough.isShowing()){
            dialogWalkthrough.show();
        }
    }

    private void dismissWalkthroughDialog(){
        if(dialogWalkthrough!=null&&dialogWalkthrough.isShowing()){
            dialogWalkthrough.dismiss();
        }
    }
}

