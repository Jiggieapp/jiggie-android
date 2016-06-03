package com.jiggie.android.activity.invite;

import com.jiggie.android.App;
import com.jiggie.android.component.Utils;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.model.InviteCodeResultModel;

/**
 * Created by Wandy on 5/13/2016.
 */
public class InviteCodePresenterImplementation implements InviteCodePresenter{

    InviteCodeView inviteCodeView;
    private final static String TAG = InviteCodePresenterImplementation.class.getSimpleName();
    InviteCodePresenterImplementation(InviteCodeView inviteCodeView)
    {
        this.inviteCodeView = inviteCodeView;
    }

    @Override
    public void getInviteCode() {
        AccountManager.getInviteCode(new OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                inviteCodeView.onFinishGetInviteCode((InviteCodeResultModel) object);
            }

            @Override
            public void onFailure(int responseCode, String message) {
                inviteCodeView.onFailedToGetInviteCode(message);
            }
        });
    }
}
