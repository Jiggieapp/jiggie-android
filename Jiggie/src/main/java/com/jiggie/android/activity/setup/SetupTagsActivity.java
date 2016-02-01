package com.jiggie.android.activity.setup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.jiggie.android.component.activity.BaseActivity;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;
import com.android.volley.VolleyError;

import org.json.JSONArray;

import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_setup_tags);
        super.bindView();

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

        VolleyHandler.getInstance().createVolleyArrayRequest("user/tagslist", new VolleyRequestListener<String[], JSONArray>() {
            @Override
            public String[] onResponseAsync(JSONArray jsonArray) {
                final int length = jsonArray.length();
                final String[] values = new String[length];
                for (int i = 0; i < length; i++)
                    values[i] = jsonArray.optString(i);
                return values;
            }

            @Override
            public void onResponseCompleted(String[] values) {
                if (isActive()) {
                    final LayoutInflater inflater = getLayoutInflater();
                    final int length = values.length;
                    selectedItems.clear();

                    for (int i = 0; i < length; i++) {
                        final View view = inflater.inflate(R.layout.item_setup_tag, flowLayout, false);
                        final ViewHolder holder = new ViewHolder(SetupTagsActivity.this, view, values[i]);

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

                    progressBar.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isActive()) {
                    Toast.makeText(SetupTagsActivity.this, App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    failedView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void onTagClick(ViewHolder holder) {
        final boolean selected = holder.checkView.getVisibility() != View.VISIBLE;

        if (selected)
            this.selectedItems.add(holder.text);
        else
            this.selectedItems.remove(holder.text);

        holder.checkView.setVisibility(selected ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int getThemeResource() { return R.style.AppTheme_Setup; }

    @OnClick(R.id.btnNext)
    @SuppressWarnings("unused")
    void btnNextOnClick() {
        App.getInstance().trackMixPanelEvent("Walkthrough Tags");
        super.startActivity(new Intent(this, SetupNotificationActivity.class).putExtra(PARAM_EXPERIENCES, this.selectedItems.toArray(new String[this.selectedItems.size()])));
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
}
