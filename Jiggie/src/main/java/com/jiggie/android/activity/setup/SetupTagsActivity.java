package com.jiggie.android.activity.setup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.BitmapUtility;
import com.jiggie.android.component.FlowLayout;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.BaseActivity;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.android.volley.VolleyError;
import com.jiggie.android.manager.EventManager;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.TagsListModel;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by rangg on 12/01/2016.
 */
public class SetupTagsActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    public static final String PREF_SETUP_COMPLETED = "setup-completed";
    public static final String PARAM_EXPERIENCES = "param-experiences";

    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.flowLayout) FlowLayout flowLayout;
    @Bind(R.id.textFailed) TextView textFailed;
    @Bind(R.id.viewFailed) View failedView;
    @Bind(R.id.btnNext) Button btnNext;
    @Bind(R.id.root) View root;

    private Set<String> selectedItems;
    private static final String TAG = SetupTagsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_setup_tags);
        super.bindView();

        EventBus.getDefault().register(this);

        final Bitmap background = BitmapUtility.getBitmapResource(R.mipmap.signup1);
        final Bitmap blurBackground = BitmapUtility.blur(background);
        this.root.setBackground(new BitmapDrawable(super.getResources(), blurBackground));

        this.textFailed.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        this.flowLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.failedView.setVisibility(View.GONE);
        this.btnNext.setVisibility(View.GONE);
        this.selectedItems = new HashSet<>();
    }

    @Override
    public void onGlobalLayout() {
        this.flowLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        this.loadData();
    }

    @OnClick(R.id.btnRetry)
    void loadData() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.failedView.setVisibility(View.GONE);
        this.btnNext.setVisibility(View.GONE);

        EventManager.loaderTagsList();
    }

    public void onEvent(TagsListModel message){
        if (isActive()) {
            final LayoutInflater inflater = getLayoutInflater();
            final int length = message.getData().getTagslist().size();
            selectedItems.clear();

            for (int i = 0; i < length; i++) {
                final View view = inflater.inflate(R.layout.item_setup_tag, flowLayout, false);
                final ViewHolder holder = new ViewHolder(SetupTagsActivity.this, view, message.getData().getTagslist().get(i));

                holder.textView.setText(holder.text);
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

            EventManager.saveTagsList(message);
            progressBar.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
        }
    }

    public void onEvent(ExceptionModel message){
        if(message.getFrom().equals(Utils.FROM_SETUP_TAGS)){
            if (isActive()) {
                Toast.makeText(SetupTagsActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                failedView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void onTagClick(ViewHolder holder) {
        boolean selected = holder.checkView.getVisibility() != View.VISIBLE;
        boolean doNothing = false;

        if (selected)
            this.selectedItems.add(holder.text);
        else {
            if(this.selectedItems.size()==1){
                doNothing = true;
                selected = false;
            }else{
                this.selectedItems.remove(holder.text);
            }
        }

        if(!doNothing){
            holder.checkView.setVisibility(selected ? View.VISIBLE : View.GONE);
        }

        Log.d("tags", this.selectedItems.toString());

    }

    @Override
    protected int getThemeResource() { return R.style.AppTheme_Setup; }

    @OnClick(R.id.btnNext)
    @SuppressWarnings("unused")
    void btnNextOnClick() {
        final String[] tags = this.selectedItems.toArray(new String[this.selectedItems.size()]);
                App.getInstance().trackMixPanelEvent("Walkthrough Tags");

        super.startActivity(new Intent(this, SetupNotificationActivity.class)
                .putExtra(PARAM_EXPERIENCES, tags));
    }

    static class ViewHolder {
        @Bind(R.id.textView) TextView textView;
        @Bind(R.id.checkView) View checkView;
        @Bind(R.id.container) View container;

        View parent;
        String text;

        public ViewHolder(final SetupTagsActivity activity, View parent, String text) {
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

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
