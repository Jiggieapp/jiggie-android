package com.jiggie.android.manager;

import android.content.Context;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.activity.profile.ProfileDetailModel;
import com.jiggie.android.api.AccountInterface;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.callback.CustomCallback;
import com.jiggie.android.listener.OnResponseListener;
import com.jiggie.android.model.AboutModel;
import com.jiggie.android.model.AccessTokenModel;
import com.jiggie.android.model.ExceptionModel;
import com.jiggie.android.model.LoginModel;
import com.jiggie.android.model.LoginResultModel;
import com.jiggie.android.model.MemberInfoModel;
import com.jiggie.android.model.MemberSettingModel;
import com.jiggie.android.model.MemberSettingResultModel;
import com.jiggie.android.model.SettingModel;
import com.jiggie.android.model.Success2Model;
import com.jiggie.android.model.SuccessTokenModel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LTE on 2/1/2016.
 */
public class AccountManager extends BaseManager {
    private static final String TAG = AccountManager.class.getSimpleName();
    static AccountInterface accountInterface;

    public static boolean anySettingChange = false;
    public static boolean isInSettingPage = false;

    public static void initAccountService() {
        accountInterface = getRetrofit().create(AccountInterface.class);
    }

    private static AccountInterface getInstance() {
        Utils.d(TAG, "token " + AccountManager.getAccessTokenFromPreferences() + "n ");
        if(AccountManager.getAccessTokenFromPreferences().isEmpty())
            accountInterface = null;
        if (accountInterface == null) {
            Utils.d(TAG, "token null fak" );
            accountInterface = getRetrofit().create(AccountInterface.class);
        }
        return accountInterface;
    }

    public static SuccessTokenModel getAccessToken2()
    {
        try {
            final String fb_token = AccessToken.getCurrentAccessToken().getToken();
            AccessTokenModel accessTokenModel = new AccessTokenModel();
            accessTokenModel.setToken(fb_token);
            /*return getBasicRetrofit()
                    .create(AccountInterface.class)
                    .getAccessToken(Utils.URL_GET_ACCESS_TOKEN,accessTokenModel)
                    .execute().body();*/
            Response<SuccessTokenModel> response = getRetrofit()
                    .create(AccountInterface.class)
                    .getAccessToken(Utils.URL_GET_ACCESS_TOKEN, accessTokenModel).execute();
            Utils.d(TAG, "before return");
            return response.body();
            /*return getRetrofit()
                    .create(AccountInterface.class)
                    .getAccessToken(Utils.URL_GET_ACCESS_TOKEN, accessTokenModel).execute().body();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*@Override
    public AccountInterface getService(Retrofit retrofit) {
        accountInterface = retrofit.create(AccountInterface.class);
    }*/

    private static void postLogin(LoginModel loginRequestModel, Callback callback) throws IOException {
        getInstance().postLogin(Utils.URL_LOGIN, loginRequestModel).enqueue(callback);
    }

    private static void postMemberSetting(MemberSettingModel memberSettingModel, Callback callback) throws IOException {
        getInstance().postMemberSetting(Utils.URL_MEMBER_SETTING, memberSettingModel).enqueue(callback);
    }

    private static void getMemberInfo(String fb_id, Callback callback) throws IOException {
        getInstance().getMemberInfo(fb_id).enqueue(callback);
    }

    private static void getSetting(String fb_id, Callback callback) throws IOException {
        getInstance().getSetting(fb_id).enqueue(callback);
    }

    private static void postEditAbout(AboutModel aboutModel, Callback callback) throws IOException {
        getInstance().postEditAbout(Utils.URL_EDIT_ABOUT, aboutModel).enqueue(callback);
    }

    private static void getCityList(Callback callback) throws IOException {
        getInstance().getCityList().enqueue(callback);
    }

    public static void loaderLogin(LoginModel loginRequestModel) {
        try {
            postLogin(loginRequestModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d(TAG, responses);*/

                    if (response.code() == Utils.CODE_SUCCESS) {
                        LoginResultModel dataLogin = (LoginResultModel) response.body();

                        //Added by Aga 29-2-2015-----
                        //boolean a = dataLogin.getData().getLogin().getShow_walkthrough_new().isEvent();
                        //boolean b = dataLogin.getData().getLogin().getShow_walkthrough_new().isChat();
                        // c = dataLogin.getData().getLogin().getShow_walkthrough_new().isSocial();
                        App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_EVENT, dataLogin.getData().getLogin().getShow_walkthrough_new().isEvent()).commit();
                        App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_SOCIAL, dataLogin.getData().getLogin().getShow_walkthrough_new().isSocial()).commit();
                        App.getSharedPreferences().edit().putBoolean(Utils.SET_WALKTHROUGH_CHAT, dataLogin.getData().getLogin().getShow_walkthrough_new().isChat()).commit();
                        //-------------

                        SettingModel dataTemp = setSettingModelFromLogin(dataLogin);
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SIGN_IN, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SIGN_IN, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_SIGN_IN, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMemberSetting(final MemberSettingModel memberSettingModel) {
        try {
            postMemberSetting(memberSettingModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    if (response.code() == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        dataTemp.setFrom(Utils.FROM_PROFILE_SETTING);
                        EventBus.getDefault().post(dataTemp);
                        AccountManager.saveMemberSetting(memberSettingModel);

                        /*MemberSettingResultModel memberSettingModel = (MemberSettingResultModel) response.body();
                        AccountManager.saveMemberSetting(memberSettingModel);*/

                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MEMBER_SETTING, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d(TAG, "failure");
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MEMBER_SETTING, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MEMBER_SETTING, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMemberSetting
            (final MemberSettingModel memberSettingModel, final com.jiggie.android.listener.OnResponseListener onResponseListener) {
        try {
            postMemberSetting(memberSettingModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    if (response.code() == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        dataTemp.setFrom(Utils.FROM_PROFILE_SETTING);
                        AccountManager.saveMemberSetting(memberSettingModel);

                        /*MemberSettingResultModel memberSettingModel = (MemberSettingResultModel) response.body();
                        AccountManager.saveMemberSetting(memberSettingModel);*/
                        onResponseListener.onSuccess(response.body());

                    } else {
                        //EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MEMBER_SETTING, Utils.RESPONSE_FAILED));
                        onResponseListener.onFailure(Utils.CODE_FAILED, Utils.RESPONSE_FAILED);
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.RESPONSE_FAILED);
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_MEMBER_SETTING, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMemberInfo(String fb_id) {
        try {
            getMemberInfo(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/
                    if (response.code() == Utils.CODE_SUCCESS) {
                        //Utils.d(TAG, "response " +  Utils.print(response));
                        MemberInfoModel dataTemp = (MemberInfoModel) response.body();
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_DETAIL, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_DETAIL, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_DETAIL, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderMemberInfo(String fb_id, final com.jiggie.android.listener.OnResponseListener onResponseListener) {
        try {
            getMemberInfo(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/
                    if (response.code() == Utils.CODE_SUCCESS) {
                        //Utils.d(TAG, "response " +  Utils.print(response));
                        MemberInfoModel dataTemp = (MemberInfoModel) response.body();
                        onResponseListener.onSuccess(dataTemp);
                    } else {
                        onResponseListener.onFailure(Utils.CODE_FAILED, Utils.RESPONSE_FAILED);
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.RESPONSE_FAILED);
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_DETAIL, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderSetting(String fb_id) {
        try {
            getSetting(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    if (response.code() == Utils.CODE_SUCCESS) {
                        MemberSettingResultModel data = (MemberSettingResultModel) response.body();
                        MemberSettingModel temp = new MemberSettingModel(data);
                        saveMemberSetting(temp);
                        /*App.getInstance().savePreference(Utils.MEMBER_SETTING_MODEL
                                , new Gson().toJson(response.body()));*/

                        SettingModel dataTemp = setSettingModelFromMemberSetting(data);
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void doUpload(File file, final com.jiggie.android.listener.OnResponseListener onResponseListener) {
        doUpload(file, new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response) {
                onResponseListener.onSuccess(response.body());
                Utils.d(TAG, "custom callback response " + Utils.print(response));
            }

            @Override
            public void onCustomCallbackFailure(String t) {
                onResponseListener.onFailure(Utils.CODE_FAILED, t);
                Utils.d(TAG, "custom callback response failure");
            }

            @Override
            public void onNeedToRestart() {
                Utils.d(TAG, "custom callback response need to restart");
            }
        });
    }

    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    private static void doUpload(File file, Callback callback) {
        final String mime = "image/" + getFileExt(file.getName());

        RequestBody filee = RequestBody.create(
                //MediaType.parse("multipart/form-data")
                MediaType.parse(mime)
                , file);


        MultipartBody.Part body =
                MultipartBody.Part.createFormData("filefield", file.getName(), filee);
        RequestBody fb_id = RequestBody.create(MediaType.parse("multipart/form-data")
                , AccessToken.getCurrentAccessToken().getUserId());
        Map<String, RequestBody> map = new HashMap<>();
        map.put("fb_id", fb_id);

        getInstance().upload4(body, fb_id).enqueue(callback);
    }

    public static void loaderSettingNew(String fb_id, final com.jiggie.android.listener.OnResponseListener onResponseListener) {
        try {
            getSetting(fb_id, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    if (response.code() == Utils.CODE_SUCCESS) {
                        MemberSettingResultModel data = (MemberSettingResultModel) response.body();
                        MemberSettingModel temp = new MemberSettingModel(data);
                        saveMemberSetting(temp);
                        /*App.getInstance().savePreference(Utils.MEMBER_SETTING_MODEL
                                , new Gson().toJson(response.body()));*/

                        SettingModel dataTemp = setSettingModelFromMemberSetting(data);
                        //EventBus.getDefault().post(dataTemp);
                        onResponseListener.onSuccess(data);
                    } else {
                        //EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.RESPONSE_FAILED));
                        onResponseListener.onFailure(Utils.CODE_FAILED, response.message());
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    //EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.MSG_EXCEPTION + t.toString()));
                    onResponseListener.onFailure(Utils.CODE_FAILED, t);
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_SETTING, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    public static void loaderEditAbout(AboutModel aboutModel) {
        try {
            postEditAbout(aboutModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    /*String responses = new Gson().toJson(response.body());
                    Utils.d("res", responses);*/
                    if (response.code() == Utils.CODE_SUCCESS) {
                        Success2Model dataTemp = (Success2Model) response.body();
                        EventBus.getDefault().post(dataTemp);
                    } else {
                        EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_EDIT, Utils.RESPONSE_FAILED));
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    Utils.d("failure", t.toString());
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_EDIT, Utils.MSG_EXCEPTION + t.toString()));
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            Utils.d("exception", e.toString());
            EventBus.getDefault().post(new ExceptionModel(Utils.FROM_PROFILE_EDIT, Utils.MSG_EXCEPTION + e.toString()));
        }
    }

    private static void saveTagsList() {

    }

    public static SettingModel loadSetting() {
        SettingModel settingModel = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING,
                Context.MODE_PRIVATE).getString(Utils.SETTING_MODEL, ""), SettingModel.class);
        return settingModel;
    }

    public static void saveLogin(LoginModel loginModel) {
        String model = new Gson().toJson(loginModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_LOGIN, Context.MODE_PRIVATE).edit()
                .putString(Utils.LOGIN_MODEL, model).apply();
    }

    public static LoginModel loadLogin() {
        LoginModel loginModel = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_LOGIN,
                Context.MODE_PRIVATE).getString(Utils.LOGIN_MODEL, ""), LoginModel.class);
        return loginModel;
    }

    public static MemberSettingModel loadMemberSetting() {
        MemberSettingModel memberSettingModel = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING,
                Context.MODE_PRIVATE).getString(Utils.MEMBER_SETTING_MODEL, ""), MemberSettingModel.class);
        if (memberSettingModel != null) {
            if (memberSettingModel.getFb_id() == null)
                memberSettingModel.setFb_id(AccessToken.getCurrentAccessToken().getUserId());
            return memberSettingModel;
        }
        return null;
    }

    /*public static MemberSettingResultModel loadMemberSetting()
    {
        MemberSettingResultModel memberSettingModel
                = new Gson().fromJson(App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING,
                Context.MODE_PRIVATE).getString(Utils.MEMBER_SETTING_MODEL, ""), MemberSettingResultModel.class);
        return memberSettingModel;
    }*/

    public static void saveMemberSetting(MemberSettingModel memberSettingModel) {
        /*if(memberSettingModel.getFb_id() == null)
            memberSettingModel.setFb_id(AccessToken.getCurrentAccessToken().getUserId());
        */
        String model = new Gson().toJson(memberSettingModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE).edit()
                .putString(Utils.MEMBER_SETTING_MODEL, model).apply();
    }

    public static void saveSetting(SettingModel settingModel) {
        String model = new Gson().toJson(settingModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE).edit()
                .putString(Utils.SETTING_MODEL, model).apply();
    }

    private static void getUserTagList(Callback callback) {
        getInstance().getUserTagList(AccessToken.getCurrentAccessToken()
                .getUserId()).enqueue(callback);
    }

    public static void getUserTagList() {
        getUserTagList(new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response) {
                MemberSettingResultModel memberSettingResultModel = (MemberSettingResultModel) response.body();
                EventBus.getDefault().post(memberSettingResultModel.getData().getMembersettings().getExperiences());
            }

            @Override
            public void onCustomCallbackFailure(String t) {
                Utils.d(TAG, "response fail" + t);
                ExceptionModel exceptionModel = new ExceptionModel(TAG, t);
                EventBus.getDefault().post(exceptionModel);
            }

            @Override
            public void onNeedToRestart() {

            }
        });
    }

    public static void getUserTags(final OnResponseListener onResponseListener) {
        try {
            getUserTagList(new CustomCallback() {
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
                    Utils.d(TAG, "response fail" + t);
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (Exception e) {
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }

    }

    public static void loaderMemberSetting2(final MemberSettingModel memberSettingModel, final OnResponseListener onResponseListener) {
        try {
            postMemberSetting(memberSettingModel, new CustomCallback() {
                @Override
                public void onCustomCallbackResponse(Response response) {
                    int responseCode = response.code();
                    if (responseCode == Utils.CODE_SUCCESS) {
                        AccountManager.saveMemberSetting(memberSettingModel);
                        onResponseListener.onSuccess(response.body());
                    } else {
                        onResponseListener.onFailure(responseCode, Utils.RESPONSE_FAILED);
                    }
                }

                @Override
                public void onCustomCallbackFailure(String t) {
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    public static void loaderCityList(final OnResponseListener onResponseListener) {
        try {
            getCityList(new CustomCallback() {
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
                    onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + t.toString());
                }

                @Override
                public void onNeedToRestart() {

                }
            });
        } catch (IOException e) {
            onResponseListener.onFailure(Utils.CODE_FAILED, Utils.MSG_EXCEPTION + e.toString());
        }
    }

    private static SettingModel setSettingModelFromLogin(LoginResultModel data) {
        boolean success = true;
        LoginResultModel.Data.Login login = data.getData().getLogin();

        SettingModel.Data.Notifications notifications = new SettingModel.Data.Notifications(login.getNotifications().isChat(), login.getNotifications().isFeed(),
                login.getNotifications().isLocation());
        //payment still empty
        SettingModel.Data.Payment payment = new SettingModel.Data.Payment();

        SettingModel.Data settingData = new SettingModel.Data(login.get_id(), login.getFb_id(), login.getGender(), notifications,
                login.getUpdated_at(), login.getAccount_type(), login.getExperiences(), login.getGender_interest(), payment, login.getPhone());

        SettingModel model = new SettingModel(success, settingData);
        model.setIs_new_user(login.is_new_user());
        model.setHelp_phone(login.getHelp_phone());
        model.setMatchme(login.isMatchme());
        model.setDevice_type(login.getDevice_type());
        //model.setShow_walkthrough(login.isShow_walkthrough());
        model.setShow_walkthrough(false);

        return model;
    }

    private static SettingModel setSettingModelFromMemberSetting(MemberSettingResultModel data) {
        boolean success = true;
        MemberSettingResultModel.Data.MemberSettings memberSettingResultModel = data.getData().getMembersettings();
        SettingModel.Data.Notifications notifications = new SettingModel.Data.Notifications(memberSettingResultModel.getNotifications().isChat(), memberSettingResultModel.getNotifications().isFeed(),
                memberSettingResultModel.getNotifications().isLocation());
        //payment still empty
        SettingModel.Data.Payment payment = new SettingModel.Data.Payment();

        SettingModel.Data settingData = new SettingModel.Data(memberSettingResultModel.get_id(), memberSettingResultModel.getFb_id(), memberSettingResultModel.getGender(), notifications,
                memberSettingResultModel.getUpdated_at(), memberSettingResultModel.getAccount_type(), memberSettingResultModel.getExperiences(), memberSettingResultModel.getGender_interest(), payment, memberSettingResultModel.getPhone());

        //SettingModel model = new SettingModel(success, settingData);
        SettingModel model = AccountManager.loadSetting();
        model.setSuccess(success);
        model.setData(settingData);
        Utils.d(TAG, "phoneNo " + memberSettingResultModel.getPhone());
        model.getData().setPhone(memberSettingResultModel.getPhone());
        AccountManager.saveSetting(model);
        return model;
    }

    public static void verifyPhoneNumber(final String phoneNumber, Callback callback) {
        getInstance().verifyPhoneNumber(AccessToken.getCurrentAccessToken().getUserId()
                , phoneNumber).enqueue(callback);
    }

    public static void verifyPhoneNumber(final String phoneNumber) {
        verifyPhoneNumber(phoneNumber, new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response) {
                /*String responses = new Gson().toJson(response.body());
                Utils.d(TAG, responses);*/

                Success2Model success2Model = (Success2Model) response.body();
                EventBus.getDefault().post(success2Model);
            }

            @Override
            public void onCustomCallbackFailure(String t) {

            }

            @Override
            public void onNeedToRestart() {

            }
        });
    }

    public static void verifyVerificationCode(final String verificationCode) {
        verifyVerificationCode(verificationCode, new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response) {
                /*final String res = new Gson().toJson(response.body());
                Utils.d(TAG, "response " + res);*/

                if (response.code() == Utils.CODE_SUCCESS) {
                    Success2Model success2Model = (Success2Model) response.body();
                    EventBus.getDefault().post(success2Model);
                } else {
                    EventBus.getDefault().post(new ExceptionModel(Utils.FROM_VERIFY_VERIFICATION_CODE, Utils.RESPONSE_FAILED));
                }
            }

            @Override
            public void onCustomCallbackFailure(String t) {

            }

            @Override
            public void onNeedToRestart() {

            }
        });
    }


    public static void getAccessToken
        //(final OnFinishGetAccessToken onFinishGetAccessToken)
    (final CommerceManager.OnResponseListener onResponseListener) {
        getAccessToken(new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response) {
                SuccessTokenModel successModel = (SuccessTokenModel) response.body();
                setAccessTokenToPreferences(successModel.data.token);
                onResponseListener.onSuccess(successModel);
                //onFinishGetAccessToken.onFinishGetAccessToken(successModel.getToken());
            }

            @Override
            public void onCustomCallbackFailure(String t) {
                //onResponseListener.onFailure(Utils.CODE_FAILED, t);
            }

            @Override
            public void onNeedToRestart() {

            }
        });
    }


    private static void getAccessToken(Callback callback) {
        final String fb_token = AccessToken.getCurrentAccessToken().getToken();
        AccessTokenModel accessTokenModel = new AccessTokenModel();
        accessTokenModel.setToken(fb_token);
        getInstance().getAccessToken(Utils.URL_GET_ACCESS_TOKEN,
                accessTokenModel).enqueue(callback);
    }



    private static void verifyVerificationCode(final String verificationCode, Callback callback) {
        getInstance().verifyVerificationCode(AccessToken.getCurrentAccessToken().getUserId()
                , verificationCode).enqueue(callback);
    }

    private static void saveMemberSetting(MemberSettingResultModel memberSettingModel) {
        String model = new Gson().toJson(memberSettingModel);
        App.getInstance().getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE).edit()
                .putString(Utils.MEMBER_SETTING_MODEL, model).apply();
    }

    public static String getAccessTokenFromPreferences() {
        final String accessToken = App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .getString(Utils.ACCESS_TOKEN, "");

        return accessToken;
    }

    public static void setAccessTokenToPreferences(String token) {
        App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .edit()
                .putString(Utils.ACCESS_TOKEN, token)
                .apply();
    }

    public static String getInviteCodeFromPreference() {
        final String accessToken = App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .getString(Utils.INVITE_CODE, "");
        return accessToken;
    }

    public static void setInviteCodeToPreferences(String token) {
        App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .edit()
                .putString(Utils.INVITE_CODE, token)
                .apply();
    }

    public static void onLogout()
    {
        App.getInstance()
                .getSharedPreferences(Utils.PREFERENCE_SETTING, Context.MODE_PRIVATE)
                .edit().clear().apply();
        App.getInstance().getSharedPreferences().edit().clear().apply();
    }

    public OnFinishGetAccessToken onFinishGetAccessToken;

    public static void doDelete(String url, final com.jiggie.android.listener.OnResponseListener onResponseListener) {
        doDelete(url, new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response) {
                Utils.d(TAG, "success delete");
                onResponseListener.onSuccess(response);
            }

            @Override
            public void onCustomCallbackFailure(String t) {
                Utils.d(TAG, "fail delete");
                onResponseListener.onFailure(Utils.CODE_FAILED, t);
            }

            @Override
            public void onNeedToRestart() {

            }
        });
    }

    private static void doDelete(final String url, Callback callback) {
        ProfileDetailModel profileDetailModel = new ProfileDetailModel(url
                , AccessToken.getCurrentAccessToken().getUserId());
        getInstance().deletePhoto(profileDetailModel).enqueue(callback);
    }


    public interface OnFinishGetAccessToken {
        public Retrofit onFinishGetAccessToken(String accessToken);
    }

    public interface OnResponseListener {
        void onSuccess(Object object);
        void onFailure(int responseCode, String message);
    }

    //wandy 13-05-2016
    public static void getInviteCode(final com.jiggie.android.listener.OnResponseListener onResponseListener)
    {
        getInviteCode(new CustomCallback() {
            @Override
            public void onCustomCallbackResponse(Response response) {
                onResponseListener.onSuccess(response.body());
            }

            @Override
            public void onCustomCallbackFailure(String t) {
                onResponseListener.onFailure(Utils.CODE_FAILED, t);
            }

            @Override
            public void onNeedToRestart() {
                getInviteCode(onResponseListener);
            }
        });
    }

    private static void getInviteCode(final Callback callback)
    {
        getInstance().getInviteCode(AccessToken.getCurrentAccessToken().getUserId()).enqueue(callback);
    }
}
