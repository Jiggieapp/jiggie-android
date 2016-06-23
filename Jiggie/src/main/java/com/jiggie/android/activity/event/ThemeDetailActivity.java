package com.jiggie.android.activity.event;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.EventTabListAdapter;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.EventModel;
import com.jiggie.android.model.ThemeResultModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThemeDetailActivity extends ToolbarActivity implements ThemeView
        , EventTabListAdapter.ViewSelectedListener, AppBarLayout.OnOffsetChangedListener{

    private final static String TAG = ThemeDetailActivity.class.getSimpleName();

    @Bind(R.id.img_theme)
    ImageView imgTheme;

    @Bind(R.id.recycler_events)
    RecyclerView recyclerEvents;

    /*@Bind(R.id.lbl_theme_description)
    TextView lblThemeDescription;

    @Bind(R.id.lbl_title_name)
    TextView lblTitleName;*/

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.event_name)
    TextView eventName;

    @Bind(R.id.appBar)
    AppBarLayout appBar;

    private ThemePresenterImplementation themePresenter;
    private EventTabListAdapter adapter;
    String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_detail);
        super.bindView();

        themePresenter = new ThemePresenterImplementation(this);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void initView()
    {
        final Bundle bundle = getIntent().getExtras();
        final String image = bundle.getStringArrayList(Common.FIELD_EVENT_PICS).get(0);
        final String eventId = bundle.getString(Common.FIELD_EVENT_ID);
        title = bundle.getString(Common.FIELD_EVENT_NAME);
        final String description = bundle.getString(Common.FIELD_EVENT_DESCRIPTION);
        super.setToolbarTitle(Utils.BLANK, false);
        appBar.addOnOffsetChangedListener(this);

        Glide.with(this).load(image).into(imgTheme);
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setEnabled(true);
        themePresenter.loadThemesEvent(eventId);

        this.adapter = new EventTabListAdapter(this, this, true);
        //for header
        EventModel.Data.Events header = new EventModel.Data.Events();
        header.setTitle(title);
        header.setDescription(description);
        adapter.add(header);
        //end of for header
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerEvents.setLayoutManager(layoutManager);
        this.recyclerEvents.setAdapter(adapter);



    }

    @Override
    public void onFinishLoadTheme(ThemeResultModel result) {
        /*Utils.d(TAG, "result " + result.data.themes.image + " "
                + result.data.themes.desc + " "
                + result.data.events.get(0).getTitle());*/



        for(int i=0;i<result.data.events.size();i++)
        {
            /*Utils.d(TAG, "size " + adapter.getItemCount() + " "
                    + result.data.events.get(i).getTitle());*/
            result.data.events.get(i).isEvent = true;

        }
        this.adapter.addAll(result.data.events);
        this.adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
        swipeRefresh.setEnabled(false);
    }

    @Override
    public void onFailLoadTheme() {
        swipeRefresh.setRefreshing(false);
    }


    @Override
    public void onViewSelected(EventModel.Data.Events event) {
        Intent i = new Intent(this, EventDetailActivity.class);
        i.putExtra(Common.FIELD_EVENT_ID, event.get_id());
        i.putExtra(Common.FIELD_EVENT_NAME, event.getTitle());
        i.putExtra(Common.FIELD_EVENT_VENUE_NAME, event.getVenue_name());
        i.putExtra(Common.FIELD_EVENT_TAGS, event.getTags());
        i.putExtra(Common.FIELD_EVENT_DAY, event.getStart_datetime());
        i.putExtra(Common.FIELD_EVENT_DAY_END, event.getEnd_datetime());
        i.putExtra(Common.FIELD_EVENT_PICS, event.getPhotos());
        i.putExtra(Common.FIELD_EVENT_DESCRIPTION, event.getDescription());
        i.putExtra(Common.FIELD_EVENT_LIKE, event.getLikes());
        i.putExtra(Common.FIELD_EVENT_LOWEST_PRICE, event.getLowest_price());
        i.putExtra(Common.FIELD_FULLFILMENT_TYPE, event.getFullfillment_type());
        i.putExtra(Common.FIELD_EVENT_TIMEZONE, event.getTz());
        i.putExtra("from", TAG);
        super.startActivity(i);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //this.swipeRefresh.setEnabled(verticalOffset == 0);

        /*if (collapsingToolbarLayout.getHeight() + verticalOffset
                < 2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
            super.setToolbarTitle(Utils.BLANK, true);
            eventName.setText(title);
        } else {
            super.setToolbarTitle(Utils.BLANK, true);
            eventName.setText(title);
            //eventName.setText(Utils.BLANK);
        }*/

        eventName.setText(title);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.back_button)
    void backOnClick() {
        finish();
    }
    /*@Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }

        if (verticalOffset == 0) {
            refreshLayout.setEnabled(true);
        } else {
            refreshLayout.setEnabled(false);
        }
    }*/
}
