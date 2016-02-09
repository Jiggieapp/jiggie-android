package com.jiggie.android.activity.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.activity.setup.SetupTagsActivity;
import com.jiggie.android.api.API;
import com.jiggie.android.component.FlowLayout;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.BaseActivity;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.FilterManager;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.SuccessModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class FilterActivity extends ToolbarActivity implements ViewTreeObserver.OnGlobalLayoutListener{

    @Bind(R.id.flowLayout) FlowLayout flowLayout;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.viewFailed) View failedView;
    @Bind(R.id.btnApply) Button btnApply;

   // private Set<String> selectedItems;
    private ArrayList<String> selectedItems;
    private final static String TAG = FilterActivity.class.getSimpleName();
    private boolean hasChanged;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        super.bindView();

        super.setToolbarTitle(getResources().getString(R.string.filter_title), true);

        selectedItems = new ArrayList<>();
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
        AccountManager.getUserTagList();
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

        SettingModel settingModel = AccountManager.loadSetting();
        /*for(String already : settingModel.getData().getExperiences())
        {
            Utils.d(TAG, "already " + already);
        }*/

        //fetch all experiences from server

        Set<String> tags = App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .getStringSet(Utils.TAGS_LIST, null);

        for (String res : tags /*settingModel.getData().getExperiences() *//*result*/) {
            final View view = getLayoutInflater().inflate(R.layout.item_setup_tag, flowLayout, false);
            final ViewHolder holder = new ViewHolder(FilterActivity.this, view, res);

            holder.textView.setText(holder.text);
            //holder.textView.setTextColor(getResources().getColor(android.R.color.white));
            //holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_blue));
            //selectedItems.add(holder.text);
            flowLayout.addView(view);
            if(result.contains(res))
            //if(settingModel.getData().getExperiences().contains(res))
            {
                //setSelected(holder.container, holder.textView, true);
                selectedItems.add(res);
                holder.checkView.setVisibility(View.GONE);
            }
            else
            {
                //setSelected(holder.container, holder.textView, false);
                holder.checkView.setVisibility(View.VISIBLE);
            }
            //onTagClick(holder);
            setSelected(holder, holder.checkView.getVisibility() != View.VISIBLE);
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

        } else {
            this.selectedItems.remove(holder.text);
        }
        setSelected(holder, selected);
    }

    private void setSelected(ViewHolder holder, boolean selected)
    {
        if(selected)
        {
            holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_blue));
            holder.textView.setTextColor(getResources().getColor(android.R.color.white));
        }
        else
        {
            holder.container.setBackground(getResources().getDrawable(R.drawable.btn_tag_grey));
            holder.textView.setTextColor(getResources().getColor(R.color.textDarkGray));
        }
        holder.checkView.setVisibility(selected ? View.VISIBLE : View.GONE);
        hasChanged = true;
    }

    @OnClick(R.id.btnApply)
    @SuppressWarnings("unused")
    void onClickBtnApply()
    {
        for(String item : selectedItems)
            Utils.d(TAG, "item " + item);

        if(selectedItems.size() > 0)
        {
            MemberSettingModel memberSettingModel = AccountManager.loadMemberSetting();
            final String experiences = TextUtils.join(",", selectedItems.toArray(new String[this.selectedItems.size()]));
            Utils.d(TAG, "experiences " + experiences);
            //memberSettingModel.setExperiences(selectedItems.toArray(new String[this.selectedItems.size()]).toString());
            memberSettingModel.setExperiences(experiences);
            showProgressDialog();
            AccountManager.loaderMemberSetting(memberSettingModel);
        }
        else
        {
            showConfirmationDialog();
        }
    }

    public void onEvent(SuccessModel message){
        /*dialog = App.showProgressDialog(this);

        if (isActive()) {
            dialog.dismiss();
            finish();
        }*/
        // Start new activity from app context instead of current activity. This prevent crash when activity has been destroyed.
        //final App app = App.getInstance();
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showConfirmationDialog()
    {
        final AlertDialog builder = new AlertDialog.Builder(FilterActivity.this)
                //.setTitle(getAct)
                .setMessage(getResources().getString(R.string.choose_one_experience))
                .setPositiveButton(FilterActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
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

    private void showProgressDialog()
    {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.please_wait), false);
        progressDialog.show();
    }
}
