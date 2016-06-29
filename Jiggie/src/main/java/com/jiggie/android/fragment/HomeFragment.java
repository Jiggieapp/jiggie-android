package com.jiggie.android.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.activity.event.EventPresenterImplementation;
import com.jiggie.android.activity.event.EventView;
import com.jiggie.android.activity.invite.InviteFriendsActivity;
import com.jiggie.android.component.FlowLayout;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.manager.FirebaseChatManager;
import com.jiggie.android.manager.SocialManager;
import com.jiggie.android.manager.TooltipsManager;
import com.jiggie.android.model.CityModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.MemberSettingResultModel;
import com.jiggie.android.model.PostLocationModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.TagNewModel;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Created by rangg on 21/10/2015.
 */
public class HomeFragment extends Fragment
        implements ViewPager.OnPageChangeListener, ViewTreeObserver.OnGlobalLayoutListener, HomeMain, EventView {
    @Nullable
    @Bind(R.id.appBar)
    AppBarLayout appBarLayout;
    @Bind(R.id.viewpagerw)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab)
    TabLayout tab;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.fab_invite)
    FloatingActionButton fabInvite;
    @Bind(R.id.flowLayoutHome)
    FlowLayout flowLayout;
    @Bind(R.id.txt_place)
    TextView txtPlace;
    @Bind(R.id.rel_place)
    RelativeLayout relPlace;
    @Bind(R.id.view_shadow)
    View viewShadow;
    @Bind(R.id.img_drop)
    ImageView imgDrop;
    @Bind(R.id.city_container)
    LinearLayout cityContainer;

    private TabFragment lastSelectedFragment;
    private PageAdapter adapter;
    private View rootView;
    public final String TAG = HomeFragment.class.getSimpleName();
    Animation makeInAnimation, makeOutAnimation, makeInAnimationInvite, makeOutAnimationInvite;

    final int EVENT_TAB = 0;
    final int SOCIAL_TAB = 1;
    final int CHAT_TAB = 2;
    final int MORE_TAB = 3;

    private int currentPosition = 0;
    private int temp = -1;
    boolean isAlreadyExpand = false;
    private ArrayList<String> selectedItems = new ArrayList<>();
    private ArrayList<String> latestSelectedItems = new ArrayList<>();
    private boolean hasChanged;
    ProgressDialog progressDialog;
    boolean isFirstClick = true;
    View bottomSheet;
    AppCompatActivity activity;
    EventPresenterImplementation eventPresenterImplementation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = this.rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    private boolean isNeedToBeRedirected() {
        final SharedPreferences pref = App.getSharedPreferences();
        boolean isNeedToBeRedirected = pref.getBoolean(Utils.IS_NEED_TO_BE_REDIRECTED_TO_EVENT_DETAIL, true);
        if (isNeedToBeRedirected) {
            String afSub1 = Utils.AFsub2;
            pref.edit().putBoolean(Utils.IS_NEED_TO_BE_REDIRECTED_TO_EVENT_DETAIL, false).commit();
            return (isNeedToBeRedirected && !afSub1.isEmpty() && !afSub1.equalsIgnoreCase("null"));
        } else {
            return false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            eventsFragment = (EventsFragment) getFragmentManager().getFragment(savedInstanceState, "eventsfragment");
            socialTabFragment = (SocialTabFragment) getFragmentManager().getFragment(savedInstanceState, "socialtabfragment");
            friendsFragment = (FriendsFragment) getFragmentManager().getFragment(savedInstanceState, "friendsfragment");
            moreFragment = (MoreFragment) getFragmentManager().getFragment(savedInstanceState, "morefragment");
            temp = savedInstanceState.getInt("position");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setRetainInstance(true);
        ButterKnife.bind(this, this.rootView);
        activity = (AppCompatActivity) super.getActivity();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        /*imgView = (ImageView) toolbar.findViewById(R.id.logo_image);
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.logo));*/
        addLogoImage();
        this.toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);

        /*activity = (AppCompatActivity) super.getActivity();
        //this.toolbar.setNavigationIcon(R.drawable.logo);
        //this.toolbar.getLogo().set;
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        imgView = (ImageView) toolbar.findViewById(R.id.logo_image);
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        this.toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);*/

        //addLogoImage();
        this.adapter = new PageAdapter(this, activity.getSupportFragmentManager() /*getChildFragmentManager()*/);
        this.viewPager.setOffscreenPageLimit(this.adapter.getCount());
        this.viewPager.setAdapter(this.adapter);
        this.tab.setupWithViewPager(this.viewPager);
        /*this.tab.getTabAt(1).setCustomView(R.layout.tab_custom_with_badge);
        TextView txtView = (TextView) tab.findViewById(R.id.tab_badge);
        txtView.setText("99");*/
        this.viewPager.addOnPageChangeListener(this);

        final int tabCount = this.adapter.getCount();

        for (int i = 0; i < tabCount; i++) {
            final Fragment fragment = this.adapter.getItem(i);
            if (fragment instanceof AppBarLayout.OnOffsetChangedListener)
                this.appBarLayout.addOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener) fragment);
        }

        super.setHasOptionsMenu(true);
        this.viewPager.getViewTreeObserver().addOnGlobalLayoutListener(this);
        if (isNeedToBeRedirected()) {
            Intent i = new Intent(super.getActivity(), EventDetailActivity.class);
            i.putExtra(Common.FIELD_EVENT_ID
                    , Utils.AFsub2 /*"56cbf750acbe12030016860d"*/);
            //i.putExtra(Common.FIELD_EVENT_NAME, event.getTitle());
            super.startActivity(i);
        } else //app invite
        {

        }

        //PART OF BOTTOM SHEET FILTER=================
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.cl_main);
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    /*boolean isShow = bottomSheet.isShown();
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);*/
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstClick) {
                    isFirstClick = false;
                    behavior.setPeekHeight(Utils.myPixel(getActivity(), 156));
                }

                if (isAlreadyExpand) {
                    fab.setImageResource(R.drawable.ic_filter_list_white_24dp);
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    isAlreadyExpand = false;
                    viewShadow.setVisibility(View.GONE);
                    //changeTags();
                    if (shouldCheckTags()) {
                        latestSelectedItems.clear();
                        for (int i = 0; i < selectedItems.size(); i++) {
                            latestSelectedItems.add(selectedItems.get(i));
                        }
                        changeTags();
                    }
                } else {
                    fab.setImageResource(R.drawable.ic_action_cancel);
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    isAlreadyExpand = true;
                    viewShadow.setVisibility(View.VISIBLE);
                }
            }
        });

        fabInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), InviteFriendsActivity.class));
            }
        });

        viewShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });

        /*EventManager.loaderTags(new EventManager.OnResponseEventListener() {
            @Override
            public void onSuccess(Object object) {
                TagsListModel dataTemp = (TagsListModel) object;

                setTags(dataTemp);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
            }
        });*/

        EventManager.loaderTagsNew(new EventManager.OnResponseEventListener() {
            @Override
            public void onSuccess(Object object) {
                TagNewModel dataTemp = (TagNewModel) object;
                setTagsNew(dataTemp);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
            }
        });

        //END OF PART OF BOTTOM SHEET FILTER=================

        EventBus.getDefault().register(this);
        relPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(getActivity(), CityActivity.class);
                startActivityForResult(i, Utils.REQUEST_CODE_CHOOSE_COUNTRY);*/
            }
        });

        //TOOLTIP PART===============
        if (tab.getSelectedTabPosition() == EVENT_TAB) {
            if (TooltipsManager.canShowTooltipAt(TooltipsManager.TOOLTIP_EVENT_LIST)) {
                TooltipsManager.initTooltipWithPoint(getActivity(), new Point(TooltipsManager.getCenterPoint(getActivity())[0],
                        TooltipsManager.getCenterPoint(getActivity())[1]), getActivity().getString(R.string.tooltip_event_list), Utils.myPixel(getActivity(), 380), Tooltip.Gravity.BOTTOM);
                TooltipsManager.setAlreadyShowTooltips(TooltipsManager.ALREADY_TOOLTIP_EVENT_LIST, true);
            }

            if (SocialManager.countData > 0) {
                if (TooltipsManager.canShowTooltipAt(TooltipsManager.TOOLTIP_SOCIAL_TAB)) {
                    TooltipsManager.initTooltipWithPoint(getActivity(), new Point(TooltipsManager.getCenterPoint(getActivity())[0] - Utils.myPixel(getActivity(), 48),
                            TooltipsManager.getCenterPoint(getActivity())[1] / 3), getActivity().getString(R.string.tooltip_social_tab), Utils.myPixel(getActivity(), 380), Tooltip.Gravity.BOTTOM);
                    TooltipsManager.setAlreadyShowTooltips(TooltipsManager.ALREADY_TOOLTIP_SOCIAL_TAB, true);
                }
            }
        }
        //END OF TOOLTIP PART===============

        eventPresenterImplementation = new EventPresenterImplementation(this);
        /*SettingModel.Data data = AccountManager.loadSetting().getData();
        //wandy 08-06-2016
        if (data.getCityList() == null
                || data.getCityList().size() == 0) {
            showProgressDialog();
            eventPresenterImplementation.getCities();
        } else {
            onFinishGetCities(AccountManager.loadSetting().getData().getCityList());
        }*/
    }

    private void addLogoImage() {
        int[] center = TooltipsManager.getCenterPoint(getActivity());
        ImageView imgLogo = new ImageView(getActivity());
        imgLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(Utils.myPixel(getActivity(), 63), Utils.myPixel(getActivity(), 24));
        param.leftMargin = center[0] - Utils.myPixel(getActivity(), 32);
        param.addRule(RelativeLayout.CENTER_VERTICAL);
        relPlace.addView(imgLogo, param);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private boolean shouldCheckTags() {
        boolean shouldCheckTags = false;

        int currentSizeSelected = selectedItems.size();
        int latestSizeSelected = latestSelectedItems.size();

        if (currentSizeSelected != latestSizeSelected) {
            shouldCheckTags = true;
        } else {
            for (int i = 0; i < currentSizeSelected; i++) {
                String tagA = selectedItems.get(i);
                boolean isFounded = false;

                for (int j = 0; j < latestSizeSelected; j++) {
                    String tagB = latestSelectedItems.get(j);

                    if (tagA.equals(tagB)) {
                        isFounded = true;
                        break;
                    }
                }

                if (!isFounded) {
                    shouldCheckTags = true;
                    break;
                }
            }
        }

        return shouldCheckTags;
    }


    private void changeTags() {
        if (selectedItems.size() > 0) {
            MemberSettingModel memberSettingModel = AccountManager.loadMemberSetting();
            //MemberSettingModel memberSettingModel = new MemberSettingModel();
            //MemberSettingResultModel memberSettingModel = AccountManager.loadMemberSetting();

            final String experiences = TextUtils.join(",", selectedItems.toArray(new String[this.selectedItems.size()]));
            Utils.d(TAG, "experiences " + experiences);
            memberSettingModel.setExperiences(experiences);
            //memberSettingModel.getData().getMembersettings().setExperiences(selectedItems);
            showProgressDialog();
            AccountManager.loaderMemberSetting2(memberSettingModel, new AccountManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    hideProgressDialog();
                    EventBus.getDefault().post(EventsFragment.TAG);
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    hideProgressDialog();
                }
            });
        } else {
            showConfirmationDialog();
        }
    }

    public void onEvent(ExceptionModel exceptionModel) {
        //Toast.makeText(this, exceptionModel.getMessage(), Toast.LENGTH_SHORT).show();
        if (exceptionModel.getMessage()
                .equalsIgnoreCase(Utils.MSG_EMPTY_DATA)) {
            showToast(getResources().getString(R.string.preferred_experience));
        } else {
            if (!exceptionModel.getMessage().contains("no card"))
                showToast(exceptionModel.getMessage());
        }
        //this.progressDialog.setVisibility(View.GONE);
        //this.failedView.setVisibility(View.VISIBLE);

        hideProgressDialog();
    }

    private void showToast(final String message) {
        Toast.makeText(getActivity()
                , message
                , Toast.LENGTH_SHORT).show();
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.please_wait), false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public Set<String> getTags() {
        Set<String> tags = App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .getStringSet(Utils.TAGS_LIST, null);
        return tags;
        //return null;
    }

    /*private void setTags(TagsListModel tagsListModel) {
        EventManager.saveTags(tagsListModel.getData().getTagslist());
        AccountManager.getUserTags(new AccountManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                MemberSettingResultModel memberSettingResultModel = (MemberSettingResultModel) object;
                ArrayList<String> result = memberSettingResultModel.getData().getMembersettings().getExperiences();

                for (String res : getTags()) {
                    if (getActivity() != null) {
                        final View view = getActivity().getLayoutInflater().inflate(R.layout.item_setup_tag, flowLayout, false);
                        final ViewHolder holder = new ViewHolder(getActivity(), view, res);

                        holder.textView.setText(holder.text);

                        flowLayout.addView(view);

                        if (result.contains(res)) {
                            selectedItems.add(res);
                            //holder.checkView.setVisibility(View.GONE);
                            holder.checkView.setVisibility(View.INVISIBLE);
                        } else {
                            //setSelected(holder.container, holder.textView, false);
                            holder.checkView.setVisibility(View.VISIBLE);
                        }
                        //onTagClick(holder);
                        setSelected(holder, holder.checkView.getVisibility() != View.VISIBLE, res);
                        hasChanged = false;

                        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                holder.container.setMinimumWidth(holder.container.getMeasuredWidth());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
            }
        });
    }*/

    private void setTagsNew(final TagNewModel tagNewModel) {
        //EventManager.saveTags(tagsListModel.getData().getTagslist());
        EventManager.saveTagsListNew(tagNewModel);
        AccountManager.getUserTags(new AccountManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                MemberSettingResultModel memberSettingResultModel = (MemberSettingResultModel) object;
                ArrayList<String> result = memberSettingResultModel.getData().getMembersettings().getExperiences();

                /*for (String res : getTags()) {
                    if(getActivity() != null)
                    {
                        final View view = getActivity().getLayoutInflater().inflate(R.layout.item_setup_tag, flowLayout, false);
                        final ViewHolder holder = new ViewHolder(getActivity(), view, res);

                        holder.textView.setText(holder.text);

                        flowLayout.addView(view);

                        if (result.contains(res)) {
                            selectedItems.add(res);
                            //holder.checkView.setVisibility(View.GONE);
                            holder.checkView.setVisibility(View.INVISIBLE);
                        } else {
                            //setSelected(holder.container, holder.textView, false);
                            holder.checkView.setVisibility(View.VISIBLE);
                        }
                        //onTagClick(holder);
                        setSelected(holder, holder.checkView.getVisibility() != View.VISIBLE, res);
                        hasChanged = false;

                        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                holder.container.setMinimumWidth(holder.container.getMeasuredWidth());
                            }
                        });
                    }
                }*/

                int sizeTag = tagNewModel.getData().getTagslist().size();
                for (int i = 0; i < sizeTag; i++) {
                    if (getActivity() != null) {
                        String res = tagNewModel.getData().getTagslist().get(i).getName();
                        String res2 = "\u2713\u0009" + tagNewModel.getData().getTagslist().get(i).getName();
                        final View view = getActivity().getLayoutInflater().inflate(R.layout.item_setup_tag, flowLayout, false);
                        final ViewHolder holder = new ViewHolder(getActivity(), view, res2, i);

                        holder.textView.setText(holder.text);

                        flowLayout.addView(view);

                        if (result.contains(res)) {
                            selectedItems.add(res);
                            latestSelectedItems.add(res);
                            setSelected(holder, true, res, i);
                        } else {
                            setSelected(holder, false, res, i);
                        }
                        holder.checkView.setVisibility(View.GONE);
                        //onTagClick(holder);
                        //setSelected(holder, holder.checkView.getVisibility() != View.VISIBLE, res, i);
                        hasChanged = false;

                        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                holder.container.setMinimumWidth(holder.container.getMeasuredWidth());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(int responseCode, String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
            }
        });
    }

    class ViewHolder {
        @Bind(R.id.textView)
        TextView textView;
        @Bind(R.id.checkView)
        ImageView checkView;
        @Bind(R.id.container)
        View container;

        View parent;
        String text;
        int position;

        public ViewHolder(final FragmentActivity activity, View parent, String text, final int position) {
            ButterKnife.bind(this, parent);
            this.textView.setText(text);
            this.parent = parent;
            this.text = text;
            this.position = position;

            this.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity != null)
                        onTagClick(ViewHolder.this, position);
                }
            });
        }
    }

    private void onTagClick(ViewHolder holder, int position) {

        //boolean selected = holder.checkView.getVisibility() != View.VISIBLE;
        TagNewModel tagNewModel = EventManager.loadTagsListNew();
        boolean selected = false;
        if (holder.textView.getCurrentTextColor() == getResources().getColor(R.color.divider_pantone)) {
            selected = true;
        } else {
            selected = false;
        }

        boolean doNothing = false;

        if (selected) {
            //this.selectedItems.add(holder.text);
            this.selectedItems.add(tagNewModel.getData().getTagslist().get(position).getName());
        } else {
            if (this.selectedItems.size() == 1) {
                doNothing = true;
                selected = false;
            } else {
                this.selectedItems.remove(tagNewModel.getData().getTagslist().get(position).getName());
            }
        }

        if (!doNothing) {
            //holder.checkView.setVisibility(selected ? View.VISIBLE : View.GONE);
            setSelected(holder, selected, holder.text, position);
        }
    }

    private void setSelected(ViewHolder holder, boolean selected, String text, int position) {
        TagNewModel dataTag = EventManager.loadTagsListNew();
        /*if (selected) {
            if (text.equalsIgnoreCase("Art & Culture")) {
                holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_red_f));
                //holder.textView.setTextColor(getResources().getColor(R.color.pink));
                holder.checkView.setImageResource(R.drawable.ic_tick_pink);
                //holder.checkView.setImageResource(R.mipmap.ic_check);

                *//*for(int i=0;i<sizeTag;i++){
                    String nameTag = dataTag.getData().getTagslist().get(i).getName();
                    if(nameTag.equalsIgnoreCase("Art & Culture")){
                        holder.textView.setTextColor(Color.parseColor(dataTag.getData().getTagslist().get(i).getColor()));
                        break;
                    }
                }*//*

            } else if (text.equalsIgnoreCase("Fashion")) {
                holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_green));
                holder.textView.setTextColor(getResources().getColor(R.color.green_tag));
                holder.checkView.setImageResource(R.drawable.ic_tick_green);
                //holder.checkView.setImageResource(R.mipmap.ic_check);

            } else if (text.equalsIgnoreCase("Nightlife")) {
                holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_greydark));
                holder.textView.setTextColor(getResources().getColor(R.color.greydark_tag));
                holder.checkView.setImageResource(R.drawable.ic_tick_greydark);
                //holder.checkView.setImageResource(R.mipmap.ic_check);

            } else if (text.equalsIgnoreCase("Music")) {
                holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_blues));
                holder.textView.setTextColor(getResources().getColor(R.color.bluedark_tag));
                holder.checkView.setImageResource(R.drawable.ic_tick_blue);
                //holder.checkView.setImageResource(R.mipmap.ic_check);


            } else if (text.equalsIgnoreCase("Food & Drink")) {
                holder.container.setBackground(getResources().getDrawable(R.drawable.btn_yellow_tag_f));
                holder.textView.setTextColor(getResources().getColor(R.color.yellow_warning));
                holder.checkView.setImageResource(R.drawable.ic_tick_yellow);
                //holder.checkView.setImageResource(R.mipmap.ic_check);

            } else if (text.equalsIgnoreCase("Featured")) {
                *//*holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_blue));
                holder.textView.setTextColor(getResources().getColor(R.color.bluedark_tag));*//*
            }

        } else {
            holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_grey_f));
            holder.textView.setTextColor(getResources().getColor(R.color.divider_pantone));
            //holder.checkView.setImageResource(R.drawable.ic_tick_grey);
            //holder.checkView.setImageResource(R.drawable.ic_tick_yellow);
        }*/

        if (selected) {
            holder.container.setBackground(setDrawableTag(getActivity(), getResources().getColor(R.color.background), Color.parseColor(dataTag.getData().getTagslist().get(position).getColor())));
            holder.textView.setTextColor(Color.parseColor(dataTag.getData().getTagslist().get(position).getColor()));
            //holder.checkView.setImageResource(R.drawable.ic_tick_grey);
            //holder.checkView.setImageResource(R.mipmap.ic_check);
        } else {
            holder.container.setBackground(setDrawableTag(getActivity(), getResources().getColor(R.color.background), getResources().getColor(R.color.divider_pantone)));
            holder.textView.setTextColor(getResources().getColor(R.color.divider_pantone));
        }


        //holder.checkView.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
        holder.checkView.setVisibility(View.GONE);
        hasChanged = true;
    }

    public static GradientDrawable setDrawableTag(Activity a, int backgroundColor, int borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        //shape.setCornerRadii(new float[]{Utils.myPixel(a, 100), Utils.myPixel(a, 100), Utils.myPixel(a, 100), Utils.myPixel(a, 100), 0, 0, 0, 0});
        shape.setCornerRadius(Utils.myPixel(a, 100));
        //shape.setColor(backgroundColor);
        shape.setStroke(Utils.myPixel(a, 1), borderColor);
        return shape;
    }

    private void showConfirmationDialog() {
        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                //.setTitle(getAct)
                .setMessage(getResources().getString(R.string.choose_one_experience))
                .setPositiveButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //listener.onNextAction(ACTION, data);
                    }
                })
                /*.setNegativeButton(get().getResources().getString(R.string.edit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })*/
                .create();
        builder.show();
    }

    public static void sendLocationInfo(final boolean isLogin) {
        //PART of postLocation
        //PostLocationModel postLocationModel = new PostLocationModel(AccessToken.getCurrentAccessToken().getUserId(), "-6.2216706", "106.8401574");
        if (AccessToken.getCurrentAccessToken() != null && AccessToken.getCurrentAccessToken() != null) {
            final String userId = AccessToken.getCurrentAccessToken().getUserId();

            if (userId != null && SocialManager.lat != null && SocialManager.lng != null) {
                //PART of postLocation
                PostLocationModel postLocationModel = new PostLocationModel(userId, SocialManager.lat, SocialManager.lng, isLogin);
                //PostLocationModel postLocationModel = new PostLocationModel(AccessToken.getCurrentAccessToken().getUserId(), "-6.2216706", "106.8401574");
                /*String responses = new Gson().toJson(postLocationModel);
                Utils.d("res", responses);*/

                SocialManager.loaderLocation(postLocationModel, new SocialManager.OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {
                        Utils.d("location", "post location success");
                    }

                    @Override
                    public void onFailure(int responseCode, String message) {

                    }
                });
                //end here
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        this.viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        this.onPageSelected(currentPosition);
        setupTabIcons();
        if ((super.getArguments() != null) && (super.getArguments().getBoolean("chat", false)))
            this.viewPager.setCurrentItem(2);
        else if ((super.getArguments() != null)
                && (super.getArguments().getBoolean(Common.TO_TAB_SOCIAL, false)))
            this.viewPager.setCurrentItem(1);
        else if ((super.getArguments() != null)
                && (super.getArguments().getBoolean(Common.TO_TAB_CHAT, false)))
            this.viewPager.setCurrentItem(2);
        /*else if((super.getArguments() != null)
                && (!super.getArguments().getString(Common.FIELD_EVENT_ID, "").equals("")))
        {
            Intent i = new Intent(super.getActivity(), EventDetailActivity.class);
            final String eventId = super.getArguments().getString(Common.FIELD_EVENT_ID);
            i.putExtra(Common.FIELD_EVENT_ID, eventId);
            super.startActivity(i);
        }*/
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    @Override
    public void onPageSelected(int position) {

        /*if(temp > -1)
        {
            Utils.d(TAG, "temp value " + temp);
            position = temp;
            temp = -1; //kembalikan awal
            viewPager.setCurrentItem(position);
        }
        else
        {*/
        if (position == 0) {
            cityContainer.setVisibility(View.VISIBLE);
        } else {
            cityContainer.setVisibility(View.GONE);
        }

        if (position == CHAT_TAB) {
            startFetchChat();
        } else {
            stopFetchChat();
        }

        if (position == CHAT_TAB) {
            showToolbar();
            //fab.startAnimation(makeOutAnimation);
            fab.setVisibility(View.GONE);
            //fabInvite.startAnimation(makeInAnimation);
            fabInvite.setVisibility(View.VISIBLE);
            bottomSheet.setVisibility(View.GONE);
            SocialManager.isInSocial = false;
        } else if (position == SOCIAL_TAB) {
            showToolbar();

            fab.setVisibility(View.GONE);
            fabInvite.setVisibility(View.GONE);


            TooltipsManager.setCanShowTooltips(TooltipsManager.TOOLTIP_SOCIAL_TAB, false);
            SocialManager.isInSocial = true;
            SocialTabFragment sc = (SocialTabFragment) this.adapter.fragments[position];
            sc.checkTooltipsInSug();

            //sc.refreshCard();
            //Log.d("", "");
            Utils.d(TAG, "socialtabfragment");
            bottomSheet.setVisibility(View.GONE);
        } else if (position == MORE_TAB) {
            showToolbar();
            fab.setVisibility(View.GONE);
            fabInvite.setVisibility(View.GONE);
            bottomSheet.setVisibility(View.GONE);

            //getMoreFragment().onTabSelected();
        } else if (position == EVENT_TAB) {
            //fab.startAnimation(makeInAnimation);
            fab.setVisibility(View.VISIBLE);
            //fabInvite.startAnimation( makeOutAnimation);
            fabInvite.setVisibility(View.GONE);

            SocialManager.isInSocial = false;
            bottomSheet.setVisibility(View.VISIBLE);

            //adapter.onClick(position);
            //getMoreFragment().onTabSelected();
        }
        //}
        //currentPosition = position;
        this.lastSelectedFragment = (TabFragment) this.adapter.getItem(position);
        this.lastSelectedFragment.onTabSelected();

    }

    private void showToolbar() {
        CoordinatorLayout coordinator = (CoordinatorLayout) getActivity().findViewById(R.id.cl_main);
        AppBarLayout appbar = (AppBarLayout) getActivity().findViewById(R.id.appBar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();

        //int[] consumed = new int[2];
        //behavior.onNestedPreScroll(coordinator, appbar, null, 0, -1000, consumed);
        behavior.onNestedFling(coordinator, appbar, null, 0, -1000, true);
    }

    private void startFetchChat() {
        if (AccountManager.loadMemberSetting() != null
                && AccountManager.loadMemberSetting().getChat() == 0)
            ((FriendsFragment) this.adapter.getItem(CHAT_TAB)).startRepeatingTask();
    }

    private void stopFetchChat() {
        ((FriendsFragment) this.adapter.getItem(CHAT_TAB)).stopRepeatingTask();
    }

    @Override
    public void onTabTitleChanged(TabFragment fragment) {
        final int position = this.adapter.getFragmentPosition(fragment);
        final TabLayout.Tab tab = position >= 0
                ? this.tab.getTabAt(position) : null;

        if (tab != null) {
            if (position == EVENT_TAB) {
                tab.setText(fragment.getTitle());
            }
            /*else if(position == 1)
            {

            }*/
            else {
                TextView lblBadge = (TextView) tab.getCustomView().findViewById(R.id.tab_badge);
                //final String badgeCount = fragment.getTitle() /*"13"*/;
                final String badgeCount = FirebaseChatManager.badgeChat;

                if (badgeCount.equals("0")) {
                    lblBadge.setVisibility(View.GONE);
                } else {
                    lblBadge.setVisibility(View.VISIBLE);
                    lblBadge.setText(badgeCount);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private static EventsFragment eventsFragment;
    private static SocialTabFragment socialTabFragment;
    private static FriendsFragment friendsFragment;
    private static MoreFragment moreFragment;

    public static SocialTabFragment getSocialTabFragment() {
        if (socialTabFragment == null)
            socialTabFragment = new SocialTabFragment();
        return socialTabFragment;
    }

    public static FriendsFragment getFriendsFragment() {
        if (friendsFragment == null)
            friendsFragment = new FriendsFragment();
        return friendsFragment;
    }

    public static MoreFragment getMoreFragment() {
        if (moreFragment == null)
            moreFragment = new MoreFragment();
        return moreFragment;
        //return new MoreFragment();
    }

    private static EventsFragment getEventsFragment() {
        if (eventsFragment == null) {
            eventsFragment = new EventsFragment();
        }
        return eventsFragment;
    }

    private static class PageAdapter extends FragmentPagerAdapter {
        private final Fragment[] fragments;

        public PageAdapter(HomeMain homeMain, FragmentManager fm) {
            super(fm);
            this.fragments = new Fragment[]{
                    getEventsFragment()
                    , getSocialTabFragment()
                    , getFriendsFragment()
                    , getMoreFragment()
            };
            /*((TabFragment) getEventsFragment()).setHomeMain(homeMain);
            ((TabFragment) getSocialTabFragment()).setHomeMain(homeMain);
            ((TabFragment) getFriendsFragment()).setHomeMain(homeMain);
            ((TabFragment) getMoreFragment()).setHomeMain(homeMain);*/

            /*getEventsFragment().setHomeMain(homeMain);
            getSocialTabFragment().setHomeMain(homeMain);
            getFriendsFragment().setHomeMain(homeMain);
            getMoreFragment().setHomeMain(homeMain);*/


            ((TabFragment) fragments[0]).setHomeMain(homeMain);
            ((TabFragment) fragments[1]).setHomeMain(homeMain);
            ((TabFragment) fragments[2]).setHomeMain(homeMain);
            ((TabFragment) fragments[3]).setHomeMain(homeMain);
        }

        public void onClick(int position) {
            switch (position) {
                case 0:
                    getEventsFragment().onTabSelected();
                    break;
                case 1:
                    getSocialTabFragment().onTabSelected();
                    break;
                case 2:
                    getFriendsFragment().onTabSelected();
                    break;
                case 3:
                    getMoreFragment().onTabSelected();
                    break;
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return getEventsFragment();
                case 1:
                    return getSocialTabFragment();
                case 2:
                    return getFriendsFragment();
                case 3:
                    return getMoreFragment();
                default:
                    return null;
            }

            //return this.fragments[position];
        }

        @Override
        public int getCount() {
            return this.fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ((TabFragment) this.fragments[position]).getTitle();
        }

        public int getIcon(int position) {
            return ((TabFragment) this.fragments[position]).getIcon();
        }

        public int getFragmentPosition(Object fragment) {
            final int length = this.fragments.length;

            for (int i = 0; i < length; i++) {
                if (fragment == this.fragments[i])
                    return i;
            }
            return -1;
        }

        /*@Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Yet another bug in FragmentStatePagerAdapter that destroyItem is called on fragment that hasnt been added. Need to catch
            try {
                super.destroyItem(container, position, object);
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }*/
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom, null);
        tabOne.setText(adapter.getPageTitle(0));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getIcon(0), 0, 0);
        tab.getTabAt(EVENT_TAB).setCustomView(tabOne);

        View tabTwo = LayoutInflater.from(getActivity())
                .inflate(R.layout.tab_custom_with_badge, null);
        TextView tabTwoTitle = (TextView) tabTwo.findViewById(R.id.tab_title);
        tabTwoTitle.setText(adapter.getPageTitle(1));
        //tabTwoTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_chat_white_24dp, 0, 0);
        tabTwoTitle.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getIcon(1), 0, 0);
        TextView tabTwoBadge = (TextView) tabTwo.findViewById(R.id.tab_badge);
        tabTwoBadge.setText("99");
        tab.getTabAt(SOCIAL_TAB).setCustomView(tabTwo);

        View tabThree = LayoutInflater.from(getActivity())
                .inflate(R.layout.tab_custom_with_badge, null);
        TextView tabThreeTitle = (TextView) tabThree.findViewById(R.id.tab_title);
        tabThreeTitle.setText(adapter.getPageTitle(2));
        tabThreeTitle.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getIcon(2), 0, 0);
        TextView tabThreeBadge = (TextView) tabThree.findViewById(R.id.tab_badge);
        tabThreeBadge.setText("99");
        tab.getTabAt(CHAT_TAB).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom, null);
        tabFour.setText(adapter.getPageTitle(3));
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getIcon(3), 0, 0);
        tabFour.setPadding(0, Utils.myPixel(getActivity(), 3), 0, 0);
        tab.getTabAt(MORE_TAB).setCustomView(tabFour);
    }

    /*@OnClick(R.id.fab)
    void onFabClick()
    {
        Intent i = new Intent(this.getActivity(), FilterActivity.class);
        startActivity(i);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(fetchChatReceiver
                , new IntentFilter(Utils.FETCH_CHAT_RECEIVER));

        if (TooltipsManager.canShowTooltipAt(TooltipsManager.TOOLTIP_SOCIAL_TAB)) {
            TooltipsManager.initTooltipWithPoint(getActivity(), new Point(TooltipsManager.getCenterPoint(getActivity())[0] - Utils.myPixel(getActivity(), 48),
                    TooltipsManager.getCenterPoint(getActivity())[1] / 3), getActivity().getString(R.string.tooltip_social_tab), Utils.myPixel(getActivity(), 380), Tooltip.Gravity.BOTTOM);
            TooltipsManager.setAlreadyShowTooltips(TooltipsManager.ALREADY_TOOLTIP_SOCIAL_TAB, true);
        }
    }

    @Override
    public void onDestroy() {
        Utils.d(TAG, "ondestroy");
        super.onDestroy();
        eventsFragment = null;
        socialTabFragment = null;
        friendsFragment = null;
        moreFragment = null;
        try {
            getActivity().unregisterReceiver(fetchChatReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver fetchChatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(Utils.IS_ON, true)) {
                stopFetchChat();
            } else startFetchChat();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_CODE_CHOOSE_COUNTRY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getStringExtra("cityname").equalsIgnoreCase("jakarta"))
                    txtPlace.setText("JKT");
                //EventBus.getDefault().post(EventsFragment.TAG);
            }
        }
    }

    PopupMenu popup;

    @OnClick(R.id.city_container)
    public void openContextualMenu() {
        //registerForContextMenu(imgDrop);
        if (popup != null)
            popup.show();
    }

    int lastSelected = 0;

    void onEvent(final ArrayList<CityModel.Data.Citylist> cityLists)
    {
        SettingModel settingModel = AccountManager.loadSetting();
        settingModel.getData().getCityList().clear();
        AccountManager.saveSetting(settingModel);
        getPopupMenu().getMenu().clear();
        onFinishGetCities(cityLists);
    }

    @Override
    public void onFinishGetCities(final ArrayList<CityModel.Data.Citylist> cityLists) {
        //final ArrayList<CityModel.Data.Citylist> cityLists = cityModel.data.citylist;
        hideProgressDialog();
        final String currentAreaEvent = AccountManager.loadMemberSetting().getArea_event();
        int citySize = cityLists.size();
        for (int i = 0; i < citySize; i++) {
            Utils.d(TAG, currentAreaEvent + " cityy " + cityLists.get(i).getCity());
            if (currentAreaEvent != null && currentAreaEvent.equalsIgnoreCase(cityLists.get(i).getCity())) {
                txtPlace.setText(cityLists.get(i).getInitial());
                getPopupMenu().getMenu().add(0, i, i, "\u2713\u0009 " + cityLists.get(i).getCity());
                lastSelected = i;
            } else {
                getPopupMenu().getMenu().add(0, i, i, "  " + cityLists.get(i).getCity());
            }
        }
        if (citySize > 1) {
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final int position = item.getOrder();

                    showProgressDialog();
                    MemberSettingModel memberSettingModel = AccountManager.loadMemberSetting();
                    memberSettingModel.setArea_event(cityLists.get(position).getCity());
                    AccountManager.loaderMemberSetting(memberSettingModel, new OnResponseListener() {
                        @Override
                        public void onSuccess(Object object) {
                            txtPlace.setText(cityLists.get(position).getInitial());

                            if (lastSelected > -1) {
                                popup.getMenu().removeItem(lastSelected);
                                popup.getMenu().add(0, lastSelected, lastSelected, " " + cityLists.get(lastSelected).getCity());
                            }
                            lastSelected = position;
                            popup.getMenu().removeItem(position);
                            popup.getMenu().add(0, position, position, "\u2713\u0009 " + cityLists.get(position).getCity());
                            hideProgressDialog();
                            eventsFragment.onRefresh();

                            App.getInstance().trackMixPanelChangeCity(Utils.CHANGE_CITY, cityLists.get(position).getCity(), cityLists.get(position).getInitial());
                        }

                        @Override
                        public void onFailure(int responseCode, String message) {
                            hideProgressDialog();
                        }
                    });
                    return false;
                }
            });
        } else {
            imgDrop.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onFinishGetEvents(EventModel eventModel) {

    }

    private PopupMenu getPopupMenu() {
        if (popup == null)
            popup = new PopupMenu(this.getActivity(), cityContainer);
        return popup;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getEventsFragment() != null) {
            outState.putInt("position", currentPosition);
            getFragmentManager().putFragment(outState, "eventsfragment", getEventsFragment());
            getFragmentManager().putFragment(outState, "socialtabfragment", getSocialTabFragment());
            getFragmentManager().putFragment(outState, "friendsfragment", getFriendsFragment());
            getFragmentManager().putFragment(outState, "morefragment", getMoreFragment());
        }
    }
}