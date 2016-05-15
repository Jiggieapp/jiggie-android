package com.jiggie.android.activity.invite;

import com.jiggie.android.model.InviteCodeResultModel;

/**
 * Created by Wandy on 5/13/2016.
 */
public interface InviteCodeView {
    void onFinishGetInviteCode(InviteCodeResultModel inviteCodeResultModel);
    void onFailedToGetInviteCode(String message);
}
