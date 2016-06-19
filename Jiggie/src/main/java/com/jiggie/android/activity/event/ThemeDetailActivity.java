package com.jiggie.android.activity.event;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
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

public class ThemeDetailActivity extends ToolbarActivity implements ThemeView, EventTabListAdapter.ViewSelectedListener{

    private final static String TAG = ThemeDetailActivity.class.getSimpleName();

    @Bind(R.id.img_theme)
    ImageView imgTheme;

    @Bind(R.id.recycler_events)
    RecyclerView recyclerEvents;

    @Bind(R.id.lbl_theme_description)
    TextView lblThemeDescription;

    @Bind(R.id.lbl_title_name)
    TextView lblTitleName;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private ThemePresenterImplementation themePresenter;
    private EventTabListAdapter adapter;


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
        final String title = bundle.getString(Common.FIELD_EVENT_NAME);
        final String description = bundle.getString(Common.FIELD_EVENT_DESCRIPTION);
        lblTitleName.setText(title);
        lblThemeDescription.setText(description);

        super.setToolbarTitle(title, true);
        Glide.with(this).load(image).into(imgTheme);
        swipeRefresh.setRefreshing(true);
        themePresenter.loadThemesEvent(eventId);
    }

    @Override
    public void onFinishLoadTheme(ThemeResultModel result) {
        /*Utils.d(TAG, "result " + result.data.themes.image + " "
                + result.data.themes.desc + " "
                + result.data.events.get(0).getTitle());*/
        swipeRefresh.setEnabled(false);
        swipeRefresh.setRefreshing(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerEvents.setLayoutManager(layoutManager);

        this.adapter = new EventTabListAdapter(this, this);
        for(int i=0;i<result.data.events.size();i++)
        {
            result.data.events.get(i).isEvent = true;

        }
        this.adapter.addAll(result.data.events);
        this.recyclerEvents.setAdapter(adapter);

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
