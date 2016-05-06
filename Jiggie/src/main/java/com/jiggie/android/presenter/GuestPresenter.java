package com.jiggie.android.presenter;

import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.CommerceManager;
import com.jiggie.android.model.GuestInfo;
import com.jiggie.android.model.PostSummaryModel;

/**
 * Created by Wandy on 4/20/2016.
 */
public class GuestPresenter {

    private final static String TAG = GuestPresenter.class.getSimpleName();
    public void saveGuest(PostSummaryModel.Guest_detail guestDetail)
    {
        final String guestDetails = String.valueOf(new Gson().toJson(guestDetail));
        App.getInstance().getSharedPreferences().edit()
                .putString(Utils.GUEST_DETAilS, guestDetails).apply();
    }

    public void saveGuest(GuestInfo guestInfo)
    {
        final String temp = String.valueOf(
                new Gson().toJson(guestInfo.data.guest_detail));
        App.getInstance().getSharedPreferences()
            .edit().putString(Utils.GUEST_DETAilS, temp).apply();
        Utils.d(TAG, "guest " + App.getSharedPreferences()
                .getString(Utils.GUEST_DETAilS, "superb"));
    }

    public PostSummaryModel.Guest_detail loadGuest()
    {
        final String gDetail = App.getInstance()
                .getSharedPreferences()
                .getString(Utils.GUEST_DETAilS, null);
        if(gDetail == null)
            return null;

        PostSummaryModel.Guest_detail guest_detail
                = new Gson().fromJson(gDetail, PostSummaryModel.Guest_detail.class);
        //Utils.d(TAG, "guest_detail" + guest_detail.dial_code + " " + guest_detail.phone);
        return guest_detail;
    }

    public void loadGuestInfo(final OnFinishGetGuestInfo onFinishGetGuestInfo)
    {
        CommerceManager.loaderGuest(new CommerceManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                GuestInfo guestInfo
                        = (GuestInfo) object;
                onFinishGetGuestInfo.onFinish(guestInfo);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                onFinishGetGuestInfo.onFailed();
            }
        });
    }

    OnFinishGetGuestInfo onFinishGetGuestInfo;
    public interface OnFinishGetGuestInfo
    {
        void onFinish(GuestInfo guestInfo);
        void onFailed();
    }
}
