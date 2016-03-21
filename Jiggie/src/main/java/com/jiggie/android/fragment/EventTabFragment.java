package com.jiggie.android.fragment;

import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.adapter.EventTabListAdapter;
import com.jiggie.android.component.adapter.EventTagAdapter;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.ExceptionModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/*import com.etiennelawlor.quickreturn.library.enums.QuickReturnViewType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnRecyclerViewOnScrollListener;
import com.etiennelawlor.quickreturn.library.utils.QuickReturnUtils;*/

/**
 * Created by rangg on 21/10/2015.
 */
public class EventTabFragment extends Fragment
        implements TabFragment/*, SwipeRefreshLayout.OnRefreshListener*/, EventTabListAdapter.ViewSelectedListener {
    /*@Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;*/
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.contentView2)
    FrameLayout contentView2;
    /*@Bind(R.id.img_wk)
    ImageView imgWk;
    @Bind(R.id.txt_wk_action)
    TextView txtWkAction;
    @Bind(R.id.txt_wk_title)
    TextView txtWkTitle;
    @Bind(R.id.txt_wk_desc)
    TextView txtWkDesc;*/

    TextView txtWkDesc;
    @Bind(R.id.layout_walkthrough)
    RelativeLayout layoutWalkthrough;
    /*@Bind(R.id.tab)
    TableLayout timeTab;*/
    /*@Bind(R.id.txtFilter)
    TextView txtFilter;
    @Bind(R.id.footerContainer)
    LinearLayout footerContainer;*/
    /*@Nullable @Bind(R.id.main_content)
    CoordinatorLayout coordinatorLayout;*/

    //private ArrayList<Event> events;

    private EventTabListAdapter adapter;
    private EventTagAdapter tagAdapter;
    private HomeMain homeMain;
    private String searchText;
    private boolean isLoading;
    private View rootView;
    private String title;
    private boolean mIsHiding;
    private boolean mIsShowing;

    private ArrayList<EventModel.Data.Events> events = new ArrayList<EventModel.Data.Events>();

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final String TAG = EventTabFragment.class.getSimpleName();
    private View failedView;
    private Dialog dialogWalkthrough;

    public EventTabFragment() {

    }

    @Override
    public void onTabSelected() {
        //App.getInstance().trackMixPanelEvent("View Events");
        /*if ((this.adapter != null) && (this.adapter.getItemCount() == 0)) {
            this.onRefresh();
        }

        if (App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_EVENT, false)) {
                showWalkthroughDialog();
            }
        */
    }

    @Override
    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public int getIcon() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = this.rootView = inflater.inflate(R.layout.fragment_tab_event, container, false);
        ButterKnife.bind(this, view);
        //EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //ButterKnife.bind(this, this.rootView);
        //EventBus.getDefault().register(this);
        //EventManager.initEventService();
        //EventBus.getDefault().register(this);

        this.recyclerView.setAdapter(this.adapter = new EventTabListAdapter(this, this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(super.getContext());
        this.recyclerView.setLayoutManager(layoutManager);
        //this.refreshLayout.setOnRefreshListener(this);

        this.events = new ArrayList<>();
        super.setHasOptionsMenu(true);

        //wandy 18-02-2016
        /*if (App.getSharedPreferences().getBoolean(Utils.SET_WALKTHROUGH_EVENT, false)) {
            layoutWalkthrough.setVisibility(View.VISIBLE);
            imgWk.setImageResource(R.drawable.wk_img_event);
            txtWkAction.setVisibility(View.GONE);
            txtWkTitle.setText(R.string.wk_event_title);
            txtWkDesc.setText(R.string.wk_event_desc);
        }*/
        this.onTabSelected();
    }


    /**
     * Hide the quick return view.
     * <p/>
     * Animates hiding the view, with the view sliding down and out of the screen.
     * After the view has disappeared, its visibility will change to GONE.
     *
     * @param view The quick return view
     */
    private void hide(final View view) {
        mIsHiding = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(view.getHeight())
                .setInterpolator(INTERPOLATOR)
                .setDuration(200);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Prevent drawing the View after it is gone
                mIsHiding = false;
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a hide should show the view
                mIsHiding = false;
                if (!mIsShowing) {
                    show(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }

    /**
     * Show the quick return view.
     * <p/>
     * Animates showing the view, with the view sliding up from the bottom of the screen.
     * After the view has reappeared, its visibility will change to VISIBLE.
     *
     * @param view The quick return view
     */
    private void show(final View view) {
        mIsShowing = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(0)
                .setInterpolator(INTERPOLATOR)
                .setDuration(200);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mIsShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a show should hide the view
                mIsShowing = false;
                if (!mIsHiding) {
                    hide(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animator.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*inflater.inflate(R.menu.menu_event, menu);
        final MenuItem searchMenu = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        final Handler handler = new Handler();*/
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchText = ((TextUtils.isEmpty(query)) || (query.trim().length() == 0)) ? null : query.trim();
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //filter(true);
                    }
                }, getResources().getInteger(R.integer.event_search_delay));
                return true;
            }
        });*/
        /*MenuItemCompat.setOnActionExpandListener(searchMenu, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchText = null;
                //filter(true);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
        this.searchText = null;*/

        super.onCreateOptionsMenu(menu, inflater);
    }

    /*@Override
    public void onRefresh() {
        if (super.getContext() == null) {
            // fragment has been destroyed.
            return;
        } else if (this.isLoading) {
            // refresh is ongoing
            return;
        }
        this.isLoading = true;
        this.refreshLayout.setRefreshing(true);
        final AccessToken token = AccessToken.getCurrentAccessToken();

        EventManager.loaderEvent(token.getUserId());
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.layout_walkthrough)
    void walkthroughOnClick() {
        Utils.SHOW_WALKTHROUGH_EVENT = false;
        layoutWalkthrough.setVisibility(View.GONE);
        App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, false).commit();
    }

    //Added by Aga
    public void onEvent(ArrayList<EventModel.Data.Events> message) {
        int size = message.size();
        if (searchText == null) {
            events = message;
            filter(true);
        }
        //refreshLayout.setRefreshing(false);
    }

    public void onEvent(EventModel message) {
        events.clear();
        events = message.getData().getEvents();

        int size = message.getData().getEvents().size();
        adapter.clear();

        if (searchText == null)
            adapter.addAll(events);

        //isLoading = false;
        //refreshLayout.setRefreshing(false);
        filter(true);
    }

    public void onEvent(ExceptionModel message) {
        if (message.getFrom().equals(Utils.FROM_EVENT)) {
            isLoading = false;
            if (getContext() != null) {
                Toast.makeText(getContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
                //refreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onViewSelected(EventModel.Data.Events event) {
        Intent i = new Intent(super.getActivity(), EventDetailActivity.class);
        i.putExtra(Common.FIELD_EVENT_ID, event.get_id());
        i.putExtra(Common.FIELD_EVENT_NAME, event.getTitle());
        super.startActivity(i);
    }

    private void filter(boolean notify) {
        /*if ((!this.refreshLayout.isRefreshing()) && (this.searchText != null)) {
            this.adapter.clear();
            final int size = this.events.size();
            //final String searchText = this.searchText.toLowerCase();
            Utils.d(TAG, "masuk " + size);

            for (int i = 0; i < size; i++) {
                final EventModel.Data.Events event = this.events.get(i);
                if (event.getTitle().toLowerCase().contains(searchText))
                    this.adapter.add(event);
                else if (event.getVenue_name().toLowerCase().contains(searchText))
                    this.adapter.add(event);
                else {
                    final String[] tags = new String[event.getTags().size()];
                    event.getTags().toArray(tags);
                    final int tagCount = tags.length;

                    for (int t = 0; t < tagCount; t++) {
                        final String tag = tags[t];
                        /if (tag.toLowerCase().contains(searchText)) {
                            this.adapter.add(event);
                            break;
                        }
                    }
                }
            }*/

            /*if (notify)
                adapter.notifyDataSetChanged();
        } else if (!this.refreshLayout.isRefreshing()) {
            this.adapter.clear();
            this.adapter.addAll(this.events);
            if (notify) {
                int sizes = events.size();
                this.adapter.notifyDataSetChanged();
            }
        }*/

        /*final int size = this.events.size();
        this.adapter.clear();*/
        this.adapter.clear();
        this.adapter.addAll(this.events);
        this.adapter.notifyDataSetChanged();

        //Added by Aga 11-2-2016
        if (adapter.getItemCount() > 0) {
            getFailedView().setVisibility(View.GONE);
        } else {
            getFailedView().setVisibility(View.VISIBLE);
        }
        //------
    }
    //--------------------------------

    private View getFailedView() {
        if (this.failedView == null) {

            //Added by Aga
            contentView2.setVisibility(View.VISIBLE);
            //-------------

            this.failedView = LayoutInflater.from(super.getContext()).inflate(R.layout.view_failed, this.contentView2, false);
            this.failedView.findViewById(R.id.btnRetry).setVisibility(View.GONE);
            TextView txt = (TextView) this.failedView.findViewById(R.id.textFailed);
            txt.setText(getResources().getString(R.string.title_no_event));
            this.contentView2.addView(this.failedView);
        }
        return this.failedView;
    }

    //--------------------------------
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
        //EventBus.getDefault().unregister(this);
    }

    private static EventTabFragment instance;
    /*
    private static void newInstance(String title) {
        instance = new EventTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        instance.setArguments(bundle);
    }*/

    public static EventTabFragment newInstance(int position) {
        //EventTabFragment fragment = new EventTabFragment();
        instance = new EventTabFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        instance.setArguments(args);
        return instance;
    }

    public static EventTabFragment getInstance(int position) {
        if (instance == null)
            instance = newInstance(position);
        return instance;
    }

    @Override
    public String getTitle() {
        final int position = getArguments().getInt("position");
        switch (position) {
            case 0:
                //return this.getActivity().getResources().getString(R.string.today);
                return "Today";
            case 1:
                //return this.getActivity().getResources().getString(R.string.tomorrow);
                return "Tomorrow";
            case 2:
                //return this.getActivity().getResources().getString(R.string.upcoming);
                return "Upcoming";
            default:
                return "";
        }
    }

    private void showWalkthroughDialog() {
        dialogWalkthrough = new Dialog(getActivity());
        dialogWalkthrough.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWalkthrough.setContentView(R.layout.walkthrough_screen);
        dialogWalkthrough.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogWalkthrough.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        RelativeLayout layout = (RelativeLayout) dialogWalkthrough.findViewById(R.id.layout_walkthrough);
        ImageView imgWk = (ImageView) dialogWalkthrough.findViewById(R.id.img_wk);
        TextView txtWkAction = (TextView) dialogWalkthrough.findViewById(R.id.txt_wk_action);
        TextView txtWkTitle = (TextView) dialogWalkthrough.findViewById(R.id.txt_wk_title);
        TextView txtWkDesc = (TextView) dialogWalkthrough.findViewById(R.id.txt_wk_desc);
        imgWk.setImageResource(R.drawable.wk_event);
        txtWkAction.setVisibility(View.GONE);
        txtWkTitle.setText(R.string.wk_event_title);
        txtWkDesc.setText(R.string.wk_event_desc);

        dialogWalkthrough.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Utils.SHOW_WALKTHROUGH_EVENT = false;
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, false).commit();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.SHOW_WALKTHROUGH_EVENT = false;
                App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, false).commit();
                dialogWalkthrough.dismiss();
            }
        });

        dialogWalkthrough.show();
    }
}
