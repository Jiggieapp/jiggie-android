package com.jiggie.android.activity.ecommerce;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.google.gson.Gson;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.PaymentMethodAdapter;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.CCModel;
import com.jiggie.android.model.CCScreenModel;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.PostDeleteCCModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 2/25/2016.
 */
public class PaymentMethodActivity extends ToolbarActivity implements PaymentMethodAdapter.ViewSelectedListener, ViewTreeObserver.OnGlobalLayoutListener, SwipeRefreshLayout.OnRefreshListener, PaymentMethodAdapter.LongClickListener{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private int section2Start = 0;
    private PaymentMethodAdapter adapter;
    private boolean isLoading;
    private String totalPrice;
    private Dialog dialogLongClick;

    String fb_id = "321321";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_payment_method);
        super.bindView();
        super.setToolbarTitle(getResources().getString(R.string.select_payment), true);

        Intent a = getIntent();
        totalPrice = a.getStringExtra(Common.FIELD_PRICE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        swipeRefresh.setOnRefreshListener(this);
        this.isLoading = false;

    }

    @Override
    public void onGlobalLayout() {
        this.recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        refreshData();
    }

    @Override
    public void onRefresh() {
        if (this.isLoading) {
            // refresh is ongoing
            return;
        }
        this.isLoading = true;
        loadData(fb_id);
    }

    private void loadData(String fb_id){
        CommerceManager.loaderCCList(fb_id, new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                swipeRefresh.setRefreshing(false);
                CCModel ccModel = (CCModel) object;
                ArrayList<CCModel.Data.Creditcard_information> ccInformation = ccModel.getData().getCreditcard_informations();

                for (int i = 0; i < ccInformation.size(); i++) {
                    CommerceManager.arrCCScreen.add(new CCScreenModel(ccInformation.get(i), null, Utils.BLANK));
                }

                section2Start = CommerceManager.arrCCScreen.size() + 1;
                setAdapters(section2Start, CommerceManager.arrCCScreen);
            }

            @Override
            public void onFailure(int responseCode, String message) {

            }
        });
    }

    private void setAdapters(int section2Start, ArrayList<CCScreenModel> dataCredit){
        adapter = new PaymentMethodAdapter(PaymentMethodActivity.this, this, this, section2Start, dataCredit);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewSelected(int position, CCScreenModel dataCredit) {
        if(position==(section2Start-1)){
            //add new CC
            Intent i = new Intent(PaymentMethodActivity.this, AddCreditCardActivity.class);
            i.putExtra(Common.FIELD_PRICE, totalPrice);
            startActivityForResult(i, 0);
        }else if(position == (section2Start + 1)){
            //other bank
            setResult(RESULT_OK, new Intent().putExtra(Common.FIELD_PAYMENT_TYPE, Utils.TYPE_VA));
            finish();
        }else if(position == section2Start){
            //mandiri
            setResult(RESULT_OK, new Intent().putExtra(Common.FIELD_PAYMENT_TYPE, Utils.TYPE_BP));
            finish();
        }else{
            //exist CC
            setResult(RESULT_OK, new Intent().putExtra(Common.FIELD_PAYMENT_TYPE, Utils.TYPE_CC).putExtra(dataCredit.getClass().getName(), dataCredit));
            finish();
        }
    }

    @Override
    public void onLongClick(int position, CCScreenModel dataCredit) {
        if(position==(section2Start-1)||position == (section2Start + 1)||position == section2Start){
            //do nothing
        }else{
            //exist CC
            showLongClickDialog(position, dataCredit.getCreditcardInformation());
        }
    }

    private void showLongClickDialog(final int position, final CCModel.Data.Creditcard_information dataCredit){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Credit Card").setMessage("Are you sure to delete this Credit card?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogLongClick.dismiss();
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!dataCredit.getSaved_token_id().equals(Utils.BLANK)){
                    PostDeleteCCModel postDeleteCCModel = new PostDeleteCCModel(fb_id, dataCredit.getMasked_card());
                    CommerceManager.loaderDeleteCC(postDeleteCCModel, new CommerceManager.OnResponseListener() {
                        @Override
                        public void onSuccess(Object object) {
                            CommerceManager.arrCCScreen.remove(position);
                            section2Start = CommerceManager.arrCCScreen.size() + 1;
                            setAdapters(section2Start, CommerceManager.arrCCScreen);
                        }

                        @Override
                        public void onFailure(int responseCode, String message) {
                            Log.d("delete","failed");
                        }
                    });
                }else{
                    CommerceManager.arrCCScreen.remove(position);
                    section2Start = CommerceManager.arrCCScreen.size() + 1;
                    setAdapters(section2Start, CommerceManager.arrCCScreen);
                }
            }
        });
        dialogLongClick = builder.create();
        dialogLongClick.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //refreshData();
            section2Start = CommerceManager.arrCCScreen.size() + 1;
            setAdapters(section2Start, CommerceManager.arrCCScreen);
        }
    }

    private void refreshData(){
        if(CommerceManager.arrCCScreen.size()==0){
            isLoading = false;
            swipeRefresh.setRefreshing(true);
            this.onRefresh();
        }else{
            section2Start = CommerceManager.arrCCScreen.size() + 1;
            setAdapters(section2Start, CommerceManager.arrCCScreen);
        }
    }
}
