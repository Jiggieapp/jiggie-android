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
import android.graphics.Point;
import android.os.Bundle;
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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.activity.setup.CityActivity;
import com.jiggie.android.component.FlowLayout;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.manager.SocialManager;
import com.jiggie.android.manager.TooltipsManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.MemberSettingResultModel;
import com.jiggie.android.model.PostLocationModel;
import com.jiggie.android.model.TagsListModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Created by rangg on 21/10/2015.
 */
public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener, ViewTreeObserver.OnGlobalLayoutListener, HomeMain {
    @Nullable @Bind(R.id.appBar)
    AppBarLayout appBarLayout;
    @Bind(R.id.viewpagerw)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab)
    TabLayout tab;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.flowLayout)
    FlowLayout flowLayout;
    @Bind(R.id.txt_place)
    TextView txtPlace;
    @Bind(R.id.rel_place)
    RelativeLayout relPlace;
    @Bind(R.id.view_shadow)
    View viewShadow;

    private TabFragment lastSelectedFragment;
    private PageAdapter adapter;
    private View rootView;
    public final String TAG = HomeFragment.class.getSimpleName();
    Animation makeInAnimation, makeOutAnimation;

    final int EVENT_TAB = 0;
    final int SOCIAL_TAB = 1;
    final int CHAT_TAB = 2;

    private int currentPosition;
    boolean isAlreadyExpand = false;
    private ArrayList<String> selectedItems = new ArrayList<>();
    private boolean hasChanged;
    ProgressDialog progressDialog;
    boolean isFirstClick = true;
    View bottomSheet;
     AppCompatActivity activity;

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
        } else return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, this.rootView);

        activity = (AppCompatActivity) super.getActivity();
        //this.toolbar.setNavigationIcon(R.drawable.logo);
        //this.toolbar.getLogo().set;
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        final ImageView imgView = (ImageView) toolbar.findViewById(R.id.logo_image);
        //imgView.setImageDrawable(getResources().getDrawable(R.drawable.logo2));
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.logo));

        this.toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);

        this.adapter = new PageAdapter(this, activity.getSupportFragmentManager());
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

        //Load animation
        /*makeOutAnimation = AnimationUtils.loadAnimation(this.getActivity(),
                R.anim.slide_down);

        makeInAnimation = AnimationUtils.loadAnimation(this.getActivity(),
                R.anim.slide_up);*/


        /*makeInAnimation = AnimationUtils.makeInAnimation(this.getActivity(), false);
        makeOutAnimation = AnimationUtils.makeOutAnimation(this.getActivity(), true);

        makeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                fab.setVisibility(View.VISIBLE);
            }
        });
        makeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });*/

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
                Log.d("state", String.valueOf(newState));
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
                    changeTags();
                } else {
                    fab.setImageResource(R.drawable.ic_action_cancel);
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    isAlreadyExpand = true;
                    viewShadow.setVisibility(View.VISIBLE);
                }


            }
        });

        viewShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });

        EventManager.loaderTags(new EventManager.OnResponseEventListener() {
            @Override
            public void onSuccess(Object object) {
                TagsListModel dataTemp = (TagsListModel) object;

                setTags(dataTemp);
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
        if(tab.getSelectedTabPosition()==EVENT_TAB){
            if (TooltipsManager.canShowTooltipAt(TooltipsManager.TOOLTIP_EVENT_LIST)) {
                TooltipsManager.initTooltipWithPoint(getActivity(), new Point(TooltipsManager.getCenterPoint(getActivity())[0],
                        TooltipsManager.getCenterPoint(getActivity())[1]), getActivity().getString(R.string.tooltip_event_list), Utils.myPixel(getActivity(), 380), Tooltip.Gravity.BOTTOM);
                TooltipsManager.setAlreadyShowTooltips(TooltipsManager.ALREADY_TOOLTIP_EVENT_LIST, true);
            }

            if(SocialManager.countData>0){
                if (TooltipsManager.canShowTooltipAt(TooltipsManager.TOOLTIP_SOCIAL_TAB)) {
                    TooltipsManager.initTooltipWithPoint(getActivity(), new Point(TooltipsManager.getCenterPoint(getActivity())[0],
                            TooltipsManager.getCenterPoint(getActivity())[1] / 3), getActivity().getString(R.string.tooltip_social_tab), Utils.myPixel(getActivity(), 380), Tooltip.Gravity.BOTTOM);
                    TooltipsManager.setAlreadyShowTooltips(TooltipsManager.ALREADY_TOOLTIP_SOCIAL_TAB, true);
                }
            }
        }

        //END OF TOOLTIP PART===============

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void changeTags() {
        if (selectedItems.size() > 0) {
            MemberSettingModel memberSettingModel = AccountManager.loadMemberSetting();
            //MemberSettingModel memberSettingModel = new MemberSettingModel();
            //MemberSettingResultModel memberSettingModel = AccountManager.loadMemberSetting();

            final String experiences = TextUtils.join(",", selectedItems.toArray(new String[this.selectedItems.size()]));
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
            if(!exceptionModel.getMessage().contains("no card"))
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

    private void setTags(TagsListModel tagsListModel) {
        EventManager.saveTags(tagsListModel.getData().getTagslist());
        AccountManager.getUserTags(new AccountManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                MemberSettingResultModel memberSettingResultModel = (MemberSettingResultModel) object;
                ArrayList<String> result = memberSettingResultModel.getData().getMembersettings().getExperiences();

                for (String res : getTags()) {
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

        public ViewHolder(final FragmentActivity activity, View parent, String text) {
            ButterKnife.bind(this, parent);
            this.textView.setText(text);
            this.parent = parent;
            this.text = text;

            this.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity != null)
                        onTagClick(ViewHolder.this);
                }
            });
        }
    }

    private void onTagClick(ViewHolder holder) {

        boolean selected = holder.checkView.getVisibility() != View.VISIBLE;
        boolean doNothing = false;

        if (selected)
            this.selectedItems.add(holder.text);
        else {
            if (this.selectedItems.size() == 1) {
                doNothing = true;
                selected = false;
            } else {
                this.selectedItems.remove(holder.text);
            }
        }

        if (!doNothing) {
            //holder.checkView.setVisibility(selected ? View.VISIBLE : View.GONE);
            setSelected(holder, selected, holder.text);
        }
    }

    private void setSelected(ViewHolder holder, boolean selected, String text) {
        if (selected) {
            if (text.equalsIgnoreCase("Art & Culture")) {
                holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_red_f));
                holder.textView.setTextColor(getResources().getColor(R.color.pink));
                holder.checkView.setImageResource(R.drawable.ic_tick_pink);
                //holder.checkView.setImageResource(R.mipmap.ic_check);
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
                /*holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_blue));
                holder.textView.setTextColor(getResources().getColor(R.color.bluedark_tag));*/
            }

        } else {
            holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_grey_f));
            holder.textView.setTextColor(getResources().getColor(R.color.divider_pantone));
            //holder.checkView.setImageResource(R.drawable.ic_tick_grey);
            //holder.checkView.setImageResource(R.drawable.ic_tick_yellow);
        }


        holder.checkView.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
        hasChanged = true;
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

    public static void sendLocationInfo() {
        //PART of postLocation
        PostLocationModel postLocationModel = new PostLocationModel(AccessToken.getCurrentAccessToken().getUserId(), SocialManager.lat, SocialManager.lng);
        //PostLocationModel postLocationModel = new PostLocationModel(AccessToken.getCurrentAccessToken().getUserId(), "-6.2216706", "106.8401574");
        String responses = new Gson().toJson(postLocationModel);
        Utils.d("res", responses);

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

    @Override
    public void onGlobalLayout() {
        this.viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        this.onPageSelected(0);
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
        currentPosition = position;
        if (position == CHAT_TAB) {
            startFetchChat();
        } else {
            stopFetchChat();
        }

        if (position == CHAT_TAB) {
            showToolbar();
            fab.setVisibility(View.GONE);
            bottomSheet.setVisibility(View.GONE);
            SocialManager.isInSocial = false;
        } else if (position == SOCIAL_TAB) {
            showToolbar();
            fab.setVisibility(View.GONE);
            TooltipsManager.setCanShowTooltips(TooltipsManager.TOOLTIP_SOCIAL_TAB, false);
            SocialManager.isInSocial = true;
            SocialTabFragment sc = (SocialTabFragment)this.adapter.fragments[position];
            sc.checkTooltipsInSug();

            //sc.refreshCard();
            //Log.d("", "");
            bottomSheet.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
            SocialManager.isInSocial = false;
            bottomSheet.setVisibility(View.VISIBLE);
        }

        this.lastSelectedFragment = (TabFragment) this.adapter.fragments[position];
        this.lastSelectedFragment.onTabSelected();
    }

    private void showToolbar()
    {
        CoordinatorLayout coordinator = (CoordinatorLayout) getActivity().findViewById(R.id.cl_main);
        AppBarLayout appbar = (AppBarLayout) getActivity().findViewById(R.id.appBar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();

        int[] consumed = new int[2];
        //behavior.onNestedPreScroll(coordinator, appbar, null, 0, -1000, consumed);
        behavior.onNestedFling(coordinator, appbar, null, 0, -1000, true);
    }

    private void startFetchChat() {
        if (AccountManager.loadMemberSetting().getChat() == 0)
            ((ChatTabFragment) this.adapter.getItem(CHAT_TAB)).startRepeatingTask();
    }

    private void stopFetchChat() {
        ((ChatTabFragment) this.adapter.getItem(CHAT_TAB)).stopRepeatingTask();
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
                final String badgeCount = fragment.getTitle() /*"13"*/;

                if (badgeCount.equals("0")) {
                    lblBadge.setVisibility(View.GONE);
                } else {
                    lblBadge.setVisibility(View.VISIBLE);
                    lblBadge.setText(fragment.getTitle());
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private static class PageAdapter extends FragmentPagerAdapter {
        private final Fragment[] fragments;

        public PageAdapter(HomeMain homeMain, FragmentManager fm) {
            super(fm);
            this.fragments = new Fragment[]{
                    //new EventTabFragment()
                    new EventsFragment()
                    , new SocialTabFragment()
                    , new ChatTabFragment()

                    //,new MoreTabFragment()
            };
            ((TabFragment) this.fragments[0]).setHomeMain(homeMain);
            ((TabFragment) this.fragments[1]).setHomeMain(homeMain);
            ((TabFragment) this.fragments[2]).setHomeMain(homeMain);
            //((TabFragment)this.fragments[3]).setHomeMain(homeMain);
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments[position];
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
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom, null);
        tabOne.setText(adapter.getPageTitle(0));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getIcon(0), 0, 0);
        tab.getTabAt(EVENT_TAB).setCustomView(tabOne);

        View tabTwo = LayoutInflater.from(getActivity())
                .inflate(R.layout.tab_custom_with_badge, null);
        TextView tabTwoTitle = (TextView) tabTwo.findViewById(R.id.tab);
        tabTwoTitle.setText(adapter.getPageTitle(1));
        //tabTwoTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_chat_white_24dp, 0, 0);
        tabTwoTitle.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getIcon(1), 0, 0);
        TextView tabTwoBadge = (TextView) tabTwo.findViewById(R.id.tab_badge);
        tabTwoBadge.setText("99");
        tab.getTabAt(SOCIAL_TAB).setCustomView(tabTwo);

        View tabThree = LayoutInflater.from(getActivity())
                .inflate(R.layout.tab_custom_with_badge, null);
        TextView tabThreeTitle = (TextView) tabThree.findViewById(R.id.tab);
        tabThreeTitle.setText(adapter.getPageTitle(2));
        tabThreeTitle.setCompoundDrawablesWithIntrinsicBounds(0, adapter.getIcon(2), 0, 0);
        TextView tabThreeBadge = (TextView) tabThree.findViewById(R.id.tab_badge);
        tabThreeBadge.setText("99");

        tab.getTabAt(CHAT_TAB).setCustomView(tabThree);
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
            TooltipsManager.initTooltipWithPoint(getActivity(), new Point(TooltipsManager.getCenterPoint(getActivity())[0],
                    TooltipsManager.getCenterPoint(getActivity())[1] / 3), getActivity().getString(R.string.tooltip_social_tab), Utils.myPixel(getActivity(), 380), Tooltip.Gravity.BOTTOM);
            TooltipsManager.setAlreadyShowTooltips(TooltipsManager.ALREADY_TOOLTIP_SOCIAL_TAB, true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(fetchChatReceiver);
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
}
