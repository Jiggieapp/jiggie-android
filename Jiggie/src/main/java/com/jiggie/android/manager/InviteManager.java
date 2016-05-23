package com.jiggie.android.manager;

import android.widget.Button;

import com.jiggie.android.App;
import com.jiggie.android.api.InviteInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.model.ContactPhoneModel;
import com.jiggie.android.model.PostContactModel;
import com.jiggie.android.model.PostInviteAllModel;
import com.jiggie.android.model.PostInviteModel;
import com.jiggie.android.model.ReferEventMixpanelModel;
import com.jiggie.android.model.ResponseContactModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LTE on 5/13/2016.
 */
public class InviteManager extends BaseManager {

    private static InviteInterface inviteInterface;
    public static ArrayList<Button> arrBtnInvite = new ArrayList<>();
    public static ArrayList<Boolean> arrBtnInvite2 = new ArrayList<>();
    public static ArrayList<ContactPhoneModel> dataContact = new ArrayList<ContactPhoneModel>();
    public static ArrayList<ResponseContactModel.Data.Contact> dataRest = new ArrayList<ResponseContactModel.Data.Contact>();
    public static String msg_share = Utils.BLANK;
    public static String total_credit = Utils.BLANK;
    public static final String latestTimeInvite = "time_invite";
    public static final String repeatInvite = "repeat_invite";
    public static final String INVITE_DAY = "day";
    public static final String INVITE_WEEK = "week";
    public static final String INVITE_MONTH = "month";

    private static ReferEventMixpanelModel referEventMixpanelModel;

    public static void initInviteService(){
        inviteInterface = getRetrofit().create(InviteInterface.class);
    }

    private static InviteInterface getInstance(){
        if(AccountManager.getAccessTokenFromPreferences().isEmpty())
            inviteInterface = null;
        if(inviteInterface == null)
            initInviteService();

        return inviteInterface;
    }

    private static void postContact(PostContactModel postContactModel, Callback callback) throws IOException {
        getInstance().postContact(Utils.URL_POST_CONTACT, postContactModel).enqueue(callback);
    }

    private static void inviteFriend(PostInviteModel postInviteModel, Callback callback) throws IOException {
        getInstance().postInvite(Utils.URL_INVITE, postInviteModel).enqueue(callback);
    }

    private static void inviteFriendAll(PostInviteAllModel postInviteAllModel, Callback callback) throws IOException {
        getInstance().postInviteAll(Utils.URL_INVITE_ALL, postInviteAllModel).enqueue(callback);
    }

    private static void getInviteCode(String fb_id, Callback callback) throws IOException {
        getInstance().getInviteCode(fb_id).enqueue(callback);
    }

    public static void loaderPostContact(final PostContactModel postContactModel, final OnResponseListener onResponseListener){
        try {
            postContact(postContactModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        onResponseListener.onSuccess(response.body());
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {
                    loaderPostContact(postContactModel, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderInvite(final PostInviteModel postInviteModel, final OnResponseListener onResponseListener){
        try {
            inviteFriend(postInviteModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        onResponseListener.onSuccess(response.body());
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {
                    loaderInvite(postInviteModel, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderInviteAll(final PostInviteAllModel postInviteAllModel, final OnResponseListener onResponseListener){
        try {
            inviteFriendAll(postInviteAllModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        onResponseListener.onSuccess(response.body());
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {
                    loaderInviteAll(postInviteAllModel, onResponseListener);
                }
            });
        }catch (IOException e){
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderInviteCode(final String fb_id, final OnResponseListener onResponseListener) {
        try {
            getInviteCode(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        onResponseListener.onSuccess(response.body());
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("Failure", t.toString());
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {
                    loaderInviteCode(fb_id, onResponseListener);
                }
            });
        } catch (IOException e) {
            Utils.d("Exception", e.toString());
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public interface OnResponseListener {
        public void onSuccess(Object object);
        public void onFailure(int responseCode, String message);
    }

    public static boolean validateTimeInvite(long longTimeInMilis){
        boolean gotoInviteFriends = false;
        long longTimeInMilisSaved = App.getSharedPreferences().getLong(latestTimeInvite, 0);
        if(longTimeInMilisSaved==0){
            App.getSharedPreferences().edit().putLong(latestTimeInvite, longTimeInMilis).putString(repeatInvite, INVITE_DAY).commit();
            gotoInviteFriends = false;
        }else{
            Calendar calSaved = Calendar.getInstance();
            calSaved.setTimeInMillis(longTimeInMilisSaved);

            Calendar calNow = Calendar.getInstance();
            calNow.setTimeInMillis(longTimeInMilis);

            int daySaved = calSaved.get(Calendar.DAY_OF_MONTH);
            int dayNow = calNow.get(Calendar.DAY_OF_MONTH);
            int dayGap = dayNow - daySaved;

            int monthSaved = calSaved.get(Calendar.MONTH);
            int monthNow = calNow.get(Calendar.MONTH);
            int monthGap = monthNow - monthSaved;

            if(monthGap>0){

                if(monthGap==1){
                    int dayMonthGap = (calSaved.getActualMaximum(Calendar.DAY_OF_MONTH) - daySaved) + dayNow;
                    if(dayMonthGap>=calNow.getActualMaximum(Calendar.DAY_OF_MONTH)){
                        App.getSharedPreferences().edit().putLong(latestTimeInvite, longTimeInMilis).putString(repeatInvite, INVITE_MONTH).commit();
                        gotoInviteFriends = true;
                    }else{

                        if(App.getSharedPreferences().getString(repeatInvite, Utils.BLANK).equals(INVITE_WEEK)){
                            App.getSharedPreferences().edit().putLong(latestTimeInvite, longTimeInMilis).putString(repeatInvite, INVITE_MONTH).commit();
                            gotoInviteFriends = true;
                        }else{
                            //do nothing
                            gotoInviteFriends = false;
                        }
                    }

                }else{
                    App.getSharedPreferences().edit().putLong(latestTimeInvite, longTimeInMilis).putString(repeatInvite, INVITE_MONTH).commit();
                    gotoInviteFriends = true;
                }

            }else{
                if(dayGap>=7){
                    if(App.getSharedPreferences().getString(repeatInvite, Utils.BLANK).equals(INVITE_WEEK)){
                        App.getSharedPreferences().edit().putLong(latestTimeInvite, longTimeInMilis).putString(repeatInvite, INVITE_MONTH).commit();
                        gotoInviteFriends = true;
                    }else{
                        //do nothing
                        gotoInviteFriends = false;
                    }

                }else{

                    int addedHour = 0;
                    if(dayGap!=0){
                        addedHour = dayGap*24;
                    }

                    int hourSaved = addedHour + calSaved.get(Calendar.HOUR);
                    int hourNow = calNow.get(Calendar.HOUR);
                    int hourGap = hourSaved - hourNow;

                    if(hourGap>=24){
                        if(App.getSharedPreferences().getString(repeatInvite, Utils.BLANK).equals(INVITE_DAY)){
                            App.getSharedPreferences().edit().putLong(latestTimeInvite, longTimeInMilis).putString(repeatInvite, INVITE_WEEK).commit();
                            gotoInviteFriends = true;
                        }
                    }else{
                        //do nothing
                        gotoInviteFriends = false;
                    }

                }
            }

        }

        return gotoInviteFriends;
    }

    public static void clearTimeInvite(){
        App.getSharedPreferences().edit().putLong(latestTimeInvite, 0).commit();
    }

}
