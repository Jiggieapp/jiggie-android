package com.jiggie.android.activity.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.api.API;
import com.jiggie.android.component.FlowLayout;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.BaseActivity;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.FilterManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class FilterActivity extends ToolbarActivity implements ViewTreeObserver.OnGlobalLayoutListener{

    @Bind(R.id.flowLayout) FlowLayout flowLayout;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.viewFailed) View failedView;

    private Set<String> selectedItems;
    private final static String TAG = FilterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        super.bindView();

        super.setToolbarTitle(getResources().getString(R.string.filter_title), true);

        selectedItems = new HashSet<>();
        this.flowLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.failedView.setVisibility(View.GONE);

        EventBus.getDefault().register(this);
        this.loadData();
    }


    @Override
    public void onGlobalLayout() {
        this.flowLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

    }

    private void loadData()
    {
        FilterManager.getUserTagList();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(FilterMessageEvent filterMessageEvent)
    {
        Utils.d(TAG, filterMessageEvent.getMessageEvent());
    }

    public void onEvent(ArrayList<String> result)
    {
        this.progressBar.setVisibility(View.GONE);
        this.failedView.setVisibility(View.GONE);

        for (String res : result) {
            final View view = getLayoutInflater().inflate(R.layout.item_setup_tag, flowLayout, false);
            final ViewHolder holder = new ViewHolder(FilterActivity.this, view, res);

            holder.textView.setText(holder.text);
            holder.textView.setTextColor(getResources().getColor(android.R.color.white));
            holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_blue));
            selectedItems.add(holder.text);
            flowLayout.addView(view);

            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    holder.container.setMinimumWidth(holder.container.getMeasuredWidth());
                }
            });
        }
    }

    static class ViewHolder {
        @Bind(R.id.textView)
        TextView textView;
        @Bind(R.id.checkView) View checkView;
        @Bind(R.id.container) View container;

        View parent;
        String text;

        public ViewHolder(final FilterActivity activity, View parent, String text) {
            ButterKnife.bind(this, parent);
            this.textView.setText(text);
            this.parent = parent;
            this.text = text;

            this.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity != null)
                        activity.onTagClick(ViewHolder.this);
                }
            });
        }
    }

    public class FilterMessageEvent
    {
        private String messageEvent;

        public String getMessageEvent() {
            return messageEvent;
        }

        public void setMessageEvent(String messageEvent) {
            this.messageEvent = messageEvent;
        }
    }

    private void onTagClick(ViewHolder holder) {
        final boolean selected = holder.checkView.getVisibility() != View.VISIBLE;

        if (selected)
        {
            this.selectedItems.add(holder.text);
            //holder.container.setBackgroundColor(getResources().getColor(R.color.setup_blue));
            holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_blue));
            holder.textView.setTextColor(getResources().getColor(android.R.color.white));
        }
        else
        {
            this.selectedItems.remove(holder.text);
            holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_grey));
            holder.textView.setTextColor(getResources().getColor(R.color.textDarkGray));
        }
        holder.checkView.setVisibility(selected ? View.VISIBLE : View.GONE);
    }
}
