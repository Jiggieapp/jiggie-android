package com.jiggie.android.activity.invite;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.social.SocialView;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.InviteFriendsAdapter;
import com.jiggie.android.component.adapter.InviteFriendsNewAdapter;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.InviteManager;
import com.jiggie.android.model.ContactPhoneModel;
import com.jiggie.android.model.InviteCodeModel;
import com.jiggie.android.model.InviteCodeResultModel;
import com.jiggie.android.model.PostContactModel;
import com.jiggie.android.model.PostInviteAllModel;
import com.jiggie.android.model.PostInviteModel;
import com.jiggie.android.model.ReferEventMixpanelModel;
import com.jiggie.android.model.ResponseContactModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

import butterknife.Bind;

/**
 * Created by LTE on 5/12/2016.
 */
public class InviteFriendsActivity extends ToolbarActivity implements SwipeRefreshLayout.OnRefreshListener, InviteFriendsAdapter.InviteSelectedListener, AppBarLayout.OnOffsetChangedListener
        , InviteFriendsNewAdapter.InviteSelectedListener, InviteCodeView {

    @Bind(R.id.appBar)
    AppBarLayout appBar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    /*@Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;*/
    InviteFriendsAdapter adapter;
    InviteFriendsNewAdapter adapterNew;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    //ArrayList<ContactPhoneModel> dataContact = new ArrayList<ContactPhoneModel>();
    boolean isLoading = false;
    //ArrayList<PostContactModel.Contact> contactToPost = new ArrayList<>();
    ArrayList<ContactPhoneModel> contactToPost = new ArrayList<>();
    //ArrayList<ResponseContactModel.Data.Contact> dataRest = new ArrayList<ResponseContactModel.Data.Contact>();
    @Bind(R.id.rel_invite_all)
    RelativeLayout relInviteAll;
    //String text;
    @Bind(R.id.txt_invite_desc)
    TextView txtInviteDesc;
    final int PERMISSION_REQUEST_CONTACT = 18;
    ProgressDialog progressDialog;
    private ReferEventMixpanelModel reverEventMixpanelModel;
    private InviteCodeResultModel inviteCodeResultModel;
    private final String TAG = InviteFriendsActivity.class.getSimpleName();
    InviteCodePresenterImplementation inviteCodePresenterImplementation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_invite_friends);
        super.bindView();

        inviteCodePresenterImplementation = new InviteCodePresenterImplementation(this);

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Invite Friends");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);

        relInviteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                if (getInviteCodeResultModel() != null) {
                    App.getInstance().trackMixPanelReferral(Utils.REFERRAL_PHONE_ALL
                            , getReferEventMixPanelModel(
                                    inviteCodeResultModel.getData().getInvite_code().getCode(),
                                    inviteCodeResultModel.getData().getInvite_code().getInvite_url()));
                }


                ArrayList<PostInviteAllModel.Contact> contacts = new ArrayList<PostInviteAllModel.Contact>();
                for (int i = 0; i < InviteManager.dataContact.size() /*2*/; i++) {
                    PostInviteAllModel.Contact postContactModel;
                    if(InviteManager.dataContact.get(i).getPhone().get(0).contains("@")
                            && InviteManager.dataContact.get(i).getPhone().get(0).contains("."))
                    {
                        postContactModel = new PostInviteAllModel.Contact
                                (InviteManager.dataContact.get(i).getName()
                                        , new ArrayList<String>()
                                        , InviteManager.dataContact.get(i).getPhone()
                                        , InviteManager.dataContact.get(i).getId());
                    }
                    else
                    {
                        postContactModel = new PostInviteAllModel.Contact
                                (InviteManager.dataContact.get(i).getName()
                                        , InviteManager.dataContact.get(i).getPhone()
                                        , new ArrayList<String>()
                                        , InviteManager.dataContact.get(i).getId());
                    }
                    contacts.add(postContactModel);
                }
                PostInviteAllModel postInviteAllModel
                        = new PostInviteAllModel(AccessToken.getCurrentAccessToken().getUserId(), contacts);
                InviteManager.loaderInviteAll(postInviteAllModel, new InviteManager.OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {
                        InviteManager.arrBtnInvite2 = new ArrayList<Boolean>(Collections.nCopies(InviteManager.dataContact.size(), false));
                        adapterNew.notifyDataSetChanged();
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(int responseCode, String message) {
                        dismissProgressDialog();
                    }
                });

                //dismissProgressDialog();
                /*for (int i = 0; i < InviteManager.arrBtnInvite.size(); i++) {
                    //adapterNew.setInviteEnable(InviteManager.arrBtnInvite.get(i), false);

                }*/



            }
        });
        appBar.addOnOffsetChangedListener(this);

        //swipeRefresh.setEnabled(true);
        isLoading = true;
        //swipeRefresh.setRefreshing(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                //swipeRefresh.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(InviteFriendsActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(InviteFriendsActivity.this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InviteFriendsActivity.this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage(getResources().getString(R.string.confirm_contact));//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(InviteFriendsActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                startingData();
            }
        } else {
            startingData();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if (InviteManager.dataRest.size() == 0) {
            swipeRefresh.setEnabled(true);
            onRefresh();
        }else{
            try {
                txtInviteDesc.setText(InviteManager.total_credit);
                txtInviteDesc.setVisibility(View.VISIBLE);
            }catch (Exception e){
                Log.d("total credit problem", e.toString());
            }

            setAdapters(InviteManager.dataRest, InviteManager.dataContact);
        }*/
        askForContactPermission();
    }

    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

    private void startingData() {
        if (InviteManager.dataContact.size() == 0
                || getInviteCodeResultModel() == null
                || getInviteCodeResultModel().getData().getInvite_code().getRewards_inviter() == null
                || getInviteCodeResultModel().getData().getInvite_code().getRewards_inviter().isEmpty()) {
            //swipeRefresh.setEnabled(true);
            onRefresh();
        } else {
            recyclerView.setAdapter(adapterNew = new InviteFriendsNewAdapter
                    (InviteFriendsActivity.this, InviteManager.dataContact, InviteFriendsActivity.this));
            try {
                final int totalCredit = InviteManager.dataContact.size()
                        * Integer.parseInt(getInviteCodeResultModel().getData().getInvite_code().getRewards_inviter());
                txtInviteDesc.setText("+Rp. " + formatter.format(totalCredit));
                txtInviteDesc.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Log.d("total credit problem", e.toString());
            }

           // setAdapters(InviteManager.dataRest, InviteManager.dataContact);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startingData();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    //ToastMaster.showMessage(getActivity(),"No permission for contacts");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onRefresh() {
        isLoading = true;
        if(getInviteCodeResultModel() == null
                || getInviteCodeResultModel().getData().getInvite_code().getRewards_inviter() == null
                || getInviteCodeResultModel().getData().getInvite_code().getRewards_inviter().isEmpty())
        {
            showProgressDialog();
            inviteCodePresenterImplementation.getInviteCode();
        }
        else getContactPhoneInvite();
    }


    /*private void showProgressDialog() {
        progressDialog = App.showProgressDialog(InviteFriendsActivity.this);
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
*/
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        /*if (verticalOffset==0) {
            swipeRefresh.setEnabled(true);
        } else {
            swipeRefresh.setEnabled(false);
        }*/
        /*if (InviteManager.dataRest.size() == 0) {
            swipeRefresh.setEnabled(true);
        } else {
            swipeRefresh.setEnabled(false);
        }*/

    }

    /*private ReferEventMixpanelModel getReferEventMixPanelModel(final String code, final String url) {
        if (reverEventMixpanelModel == null) {
            return new ReferEventMixpanelModel(code, url);
        }
        return reverEventMixpanelModel;
    }*/


    private String getMsgShare() {
        return AccountManager.getMsgShareFromPreference();
    }

    private void setMsgShare(String token) {
        AccountManager.setMsgShareFromPreference(token);
    }

    private InviteCodeResultModel getInviteCodeResultModel() {
        final String inv = AccountManager.getInviteCodeFromPreference();
        if (inv.isEmpty() || inv == null)
            return null;
        else {
            inviteCodeResultModel
                    = new Gson().fromJson(inv, InviteCodeResultModel.class);
            return inviteCodeResultModel;
        }
    }

    private ReferEventMixpanelModel getReferEventMixPanelModel
            (final String code, final String url, final String name, final ArrayList<String> email, final ArrayList<String> phone) {
        if (reverEventMixpanelModel == null) {
            reverEventMixpanelModel = new ReferEventMixpanelModel(code, url, name, email, phone);
            return reverEventMixpanelModel;
        }
        return reverEventMixpanelModel;
    }

    private ReferEventMixpanelModel getReferEventMixPanelModel
            (final String code, final String url) {
        if (reverEventMixpanelModel == null) {
            reverEventMixpanelModel = new ReferEventMixpanelModel(code, url);
            return reverEventMixpanelModel;
        }
        return reverEventMixpanelModel;
    }


    @Override
    public void onInviteSelected(ResponseContactModel.Data.Contact contact) {
        if (getInviteCodeResultModel() != null) {
            App.getInstance().trackMixPanelReferral(Utils.REFERRAL_PHONE_SINGULAR
                    , getReferEventMixPanelModel(
                            inviteCodeResultModel.getData().getInvite_code().getCode(),
                            inviteCodeResultModel.getData().getInvite_code().getInvite_url()
                            , contact.getName(), contact.getEmail(), contact.getPhone()));
        }

        if (contact.getEmail().size() == 1 && contact.getEmail().get(0).isEmpty()) {
            if (contact.getPhone().size() == 0) {
                //do nothing
            } else {
                String phoneNumber = Utils.BLANK;
                for (int i = 0; i < contact.getPhone().size(); i++) {
                    if (i != 0) {
                        phoneNumber += ";" + contact.getPhone().get(i);
                    } else {
                        phoneNumber = contact.getPhone().get(i);
                    }
                }
                openSMS(phoneNumber);
                //openSMS(contact.getPhone().get(0));
            }
        } else {
            /*PostInviteModel postInviteModel = new PostInviteModel(AccessToken.getCurrentAccessToken().getUserId()
                    , new PostInviteModel.Contact(contact.getName(), contact.getEmail(), contact.getUniq_id()));
            InviteManager.loaderInvite(postInviteModel, new InviteManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    //success invite
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    //failed invite
                }
            });*/
        }
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.wait));
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void openSMS(String telp) {
        Uri uri = Uri.parse("smsto:" + telp);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", /*InviteManager.msg_share*/
                getMsgShare());
        startActivity(it);
    }


    private void getContactPhoneInvite() {
        showProgressDialog();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                doGetContact2();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //dismissProgressDialog();
                if (InviteManager.dataContact != null)
                {
                    recyclerView.setAdapter(adapterNew = new InviteFriendsNewAdapter
                            (InviteFriendsActivity.this, InviteManager.dataContact, InviteFriendsActivity.this));

                    final int totalCredit = InviteManager.dataContact.size()
                            * Integer.parseInt(getInviteCodeResultModel().getData().getInvite_code().getRewards_inviter());
                    txtInviteDesc.setText("+Rp. " + formatter.format(totalCredit));
                    txtInviteDesc.setVisibility(View.VISIBLE);
                    InviteManager.arrBtnInvite2 = new ArrayList<Boolean>(Collections.nCopies(InviteManager.dataContact.size(), true));
                    //Collections.fill(InviteManager.arrBtnInvite2, Boolean.TRUE);
                }
                isLoading = false;
                //swipeRefresh.setRefreshing(false);
                //swipeRefresh.setEnabled(false);
                dismissProgressDialog();
            }
        }.execute();
    }

    private void doGetContact2() {
        long startnow;
        long endnow;

        startnow = android.os.SystemClock.uptimeMillis();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        //ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        Cursor cursor = getContentResolver().query(uri, new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                        , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        , ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                        , ContactsContract.Contacts._ID
                        , ContactsContract.Data.PHOTO_THUMBNAIL_URI
                        }
                , selection
                , null
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        cursor.moveToFirst();
        ArrayList<String> phoneNumber;
        //ArrayList<ContactPhoneModel> emlRecs = new ArrayList<ContactPhoneModel>();
        HashSet<String> emlRecsHS = new HashSet<String>();

        while (cursor.isAfterLast() == false) {
            phoneNumber = new ArrayList<>();
            String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactNumber = contactNumber.replaceAll("[^0-9+]", "");
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //String mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
           // int contactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String photoThumbnail = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
            if (emlRecsHS.add(contactNumber.toLowerCase())) {
                //ArrayList<String> email = new ArrayList<>();
                phoneNumber.add(contactNumber);
                InviteManager.dataContact.add(new ContactPhoneModel
                        (phoneContactID + ""
                                , contactName
                                , phoneNumber
                                , new ArrayList<String>()
                                , ""));
                //emlRecs.add(contactPhoneModel);
            }
            cursor.moveToNext();
        }

        ArrayList<ContactPhoneModel> email = getNameEmailDetails();

        InviteManager.dataContact.addAll(email);
        Collections.sort(InviteManager.dataContact);

        endnow = android.os.SystemClock.uptimeMillis();
        Utils.d("END", "TimeForContacts " + (endnow - startnow) + " ms " + cursor.getCount());
        cursor.close();
        cursor = null;
    }

    private void sendToServer() {
        /*for (int i = 0; i < InviteManager.dataContact.size(); i++) {
            contactToPost.add(new PostContactModel.Contact(InviteManager.dataContact.get(i).getId()
                    , InviteManager.dataContact.get(i).getName(), InviteManager.dataContact.get(i).getEmail(),
                    InviteManager.dataContact.get(i).getPhoneNumber()));
        }*/

        //contactToPost.addAll(InviteManager.dataContact);

        if (InviteManager.dataContact.size() > 0) {
            final PostContactModel postContactModel = new PostContactModel(AccessToken.getCurrentAccessToken().getUserId()
                    , Utils.TYPE_ANDROID, InviteManager.dataContact);
            InviteManager.loaderPostContact(postContactModel, new InviteManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    ResponseContactModel responseContactModel = (ResponseContactModel) object;

                    //String sd = String.valueOf(new Gson().toJson(responseContactModel));
                    if (responseContactModel != null) {
                        //==============
                        try {
                            InviteManager.total_credit = "For " + String.valueOf(responseContactModel.getData().getTot_credit()) + " Credits";
                            txtInviteDesc.setText(InviteManager.total_credit);
                            txtInviteDesc.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Utils.d("total credit problem", e.toString());
                            dismissProgressDialog();
                        }

                        //InviteManager.msg_share = responseContactModel.getData().getMsg_share();
                        //Utils.d(TAG, responseContactModel.getData().getMsg_share());
                        setMsgShare(responseContactModel.getData().getMsg_share());
                        //==============
                        setAdapters(responseContactModel.getData().getContact(), InviteManager.dataContact);
                    } else {
                        isLoading = false;
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    isLoading = false;
                    dismissProgressDialog();
                }
            });
        }

        //endnow = android.os.SystemClock.uptimeMillis();
        //Utils.d(TAG, "TimeForContactsJannes " + (endnow - startnow) + " ms " + cursor.getCount());

    }

    /*private boolean isAlreadyExist(final String number) {
        if (number.isEmpty())
            return true;
        final int size = InviteManager.dataContact.size();
        if (size > 0 *//*&& InviteManager.dataContact.get(size - 1).getPhone().size() > 0*//*) {
            if (InviteManager.dataContact.get(size - 1).getPhone().get(0).equals(number)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean isEmailAlreadyExist(final String id) {
        if (id.isEmpty())
            return true;
        final int size = InviteManager.dataContact.size();
        if (size > 0) {
            if (InviteManager.dataContact.get(size - 1).getId().equals(id)) {
                return true;
            }
            return false;
        }
        return false;
    }*/


    /*ContentResolver cr = context.getContentResolver();
    String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
    String order = "CASE WHEN "
            + ContactsContract.Contacts.DISPLAY_NAME
            + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
            + ContactsContract.Contacts.DISPLAY_NAME
            + ", "
            + ContactsContract.CommonDataKinds.Email.DATA
            + " COLLATE NOCASE";
    String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
    Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);*/

    public ArrayList<ContactPhoneModel> getNameEmailDetails() {
        ArrayList<ContactPhoneModel> emlRecs = new ArrayList<ContactPhoneModel>();
        HashSet<String> emlRecsHS = new HashSet<String>();
        ContentResolver cr = this.getContentResolver();
        String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
        String order = "CASE WHEN "
                + ContactsContract.Contacts.DISPLAY_NAME
                + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                + ContactsContract.Contacts.DISPLAY_NAME
                + ", "
                + ContactsContract.CommonDataKinds.Email.DATA
                + " COLLATE NOCASE";
        String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
        if (cur.moveToFirst()) {
            do {
                // names comes in hand sometimes
                String id = cur.getString(4);
                String name = cur.getString(1);
                String emlAddr = cur.getString(3);

                // keep unique only
                if (emlRecsHS.add(emlAddr.toLowerCase())) {
                    ArrayList<String> email = new ArrayList<>();
                    email.add(emlAddr);
                    ContactPhoneModel contactPhoneModel = new ContactPhoneModel
                            (id
                            , name
                            , email
                            , new ArrayList<String>()
                            ,"");
                    emlRecs.add(contactPhoneModel);
                }
            } while (cur.moveToNext());
        }

        cur.close();
        return emlRecs;
    }

    private void doGetContact() {
        long startnow;
        long endnow;

        startnow = android.os.SystemClock.uptimeMillis();

        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        ContentResolver cr = getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , new String[]{ContactsContract.Contacts._ID}
                , selection, null, null);
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //ArrayList<String> email = getNameEmailDetails();
        ArrayList<String> email = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = Utils.BLANK;
                /*String phoneNumber = Utils.BLANK;
                String email = Utils.BLANK;*/

                ArrayList<String> phoneNumber = new ArrayList<>();
                String photoThumbnail = Utils.BLANK;

                try {
                    //if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(uri
                            , new String[]
                                    {ContactsContract.CommonDataKinds.Phone.NUMBER
                                            , ContactsContract.CommonDataKinds.Email.DATA
                                            , ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI}
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                    while (pCur.moveToNext()) {
                        //String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //alContacts.add(contactNumber);
                        name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        photoThumbnail = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                        if (photoThumbnail == null) {
                            photoThumbnail = Utils.BLANK;
                        }

                        Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI
                                , null
                                , ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?"
                                , new String[]{id}
                                , null);
                        while (emailCur.moveToNext()) {
                            String mEmail = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            email.add(mEmail);
                            //og.e("Email", name + " " + email);
                        }
                        emailCur.close();
                        InviteManager.dataContact.add(new ContactPhoneModel(id, name, phoneNumber, email, photoThumbnail));

                        break;
                    }
                    pCur.close();
                    //}
                } catch (Exception e) {
                    dismissProgressDialog();
                }
            } while (cursor.moveToNext());
        }

        endnow = android.os.SystemClock.uptimeMillis();
        Utils.d(TAG, "TimeForContacts " + (endnow - startnow) + " ms " + cursor.getCount());
        startnow = endnow;

        String responses = new Gson().toJson(InviteManager.dataContact);

        /*for (int i = 0; i < InviteManager.dataContact.size(); i++) {
            contactToPost.add(new PostContactModel.Contact(InviteManager.dataContact.get(i).getId()
                    , InviteManager.dataContact.get(i).getName(), InviteManager.dataContact.get(i).getEmail(),
                    InviteManager.dataContact.get(i).getPhoneNumber()));
        }*/


        if (contactToPost.size() > 0) {
            final PostContactModel postContactModel = new PostContactModel(AccessToken.getCurrentAccessToken().getUserId() /*"10204456507851351"*/
                    , Utils.TYPE_ANDROID, contactToPost);
            InviteManager.loaderPostContact(postContactModel, new InviteManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    ResponseContactModel responseContactModel = (ResponseContactModel) object;

                    //String sd = String.valueOf(new Gson().toJson(responseContactModel));
                    if (responseContactModel != null) {
                        //==============
                        try {
                            InviteManager.total_credit = "For " + String.valueOf(responseContactModel.getData().getTot_credit()) + " Credits";
                            txtInviteDesc.setText(InviteManager.total_credit);
                            txtInviteDesc.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Log.d("total credit problem", e.toString());
                            dismissProgressDialog();
                        }

                        //InviteManager.msg_share = responseContactModel.getData().getMsg_share();
                        setMsgShare(responseContactModel.getData().getMsg_share());
                        //==============

                        setAdapters(responseContactModel.getData().getContact(), InviteManager.dataContact);
                    } else {
                        isLoading = false;
                        //swipeRefresh.setRefreshing(false);
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    isLoading = false;
                    //swipeRefresh.setRefreshing(false);
                    dismissProgressDialog();
                }
            });
        }
        endnow = android.os.SystemClock.uptimeMillis();
        Utils.d(TAG, "TimeForContactsJannes " + (endnow - startnow) + " ms " + cursor.getCount());
    }

    private void setAdapters(ArrayList<ResponseContactModel.Data.Contact> data, ArrayList<ContactPhoneModel> dataContact) {
        InviteManager.dataRest = data;
        recyclerView.setAdapter(adapter = new InviteFriendsAdapter(this, dataContact, data, this));
        isLoading = false;
        //swipeRefresh.setRefreshing(false);
        //swipeRefresh.setEnabled(false);
        dismissProgressDialog();
    }


    @Override
    public void onInviteSelected(ContactPhoneModel contact, final int  position) {
        PostInviteModel.Contact contactToSend;
        if(contact.getPhone().get(0).contains("@") && contact.getPhone().get(0).contains(".")) //email
        {
            ArrayList<String> email = new ArrayList<>();
            email.addAll(contact.getPhone());
            contactToSend = new PostInviteModel.Contact(contact.getName(), email
                , contact.getId(), new ArrayList<String>());
        }
        else
        {
            ArrayList<String> phone = new ArrayList<>();
            phone.addAll(contact.getPhone());
            contactToSend = new PostInviteModel.Contact(contact.getName(), new ArrayList<String>()
                    , contact.getId(), phone);
        }

        showProgressDialog();
        PostInviteModel postInviteModel = new PostInviteModel(AccessToken.getCurrentAccessToken().getUserId()
                , contactToSend);
        InviteManager.loaderInvite(postInviteModel, new InviteManager.OnResponseListener() {
            @Override
            public void onSuccess(Object object) {
                //success invite
                //Utils.d(TAG, "success ");
                InviteManager.arrBtnInvite2.set(position, false);
                adapterNew.notifyDataSetChanged();
                Toast.makeText(InviteFriendsActivity.this, "Your invitation has been succesfully sent", Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }

            @Override
            public void onFailure(int responseCode, String message) {
                //failed invite
                InviteManager.arrBtnInvite2.set(position, true);
                adapterNew.notifyDataSetChanged();
                dismissProgressDialog();
            }
        });
        //}
    }

    @Override
    public void onFinishGetInviteCode(InviteCodeResultModel inviteCodeResultModel) {
        dismissProgressDialog();
        if(inviteCodeResultModel != null)
        {
            setInviteCodeResultModel(inviteCodeResultModel);
            getContactPhoneInvite();
            /*if(inviteCodeResultModel.getData() != null)
            {
                setInviteCodeResultModel(inviteCodeResultModel);
                getContactPhoneInvite();
            }
            else
            {
                InviteManager.dataContact = new ArrayList<>();
                inviteCodePresenterImplementation.getInviteCode();
            }*/
        }
        else
        {
            InviteManager.dataContact = new ArrayList<>();
            inviteCodePresenterImplementation.getInviteCode();
        }

    }

    @Override
    public void onFailedToGetInviteCode(String message) {
        dismissProgressDialog();
        finish();
    }

    private void setInviteCodeResultModel(InviteCodeResultModel inviteCodeResultModel) {
        AccountManager.setInviteCodeToPreferences(new Gson().toJson(inviteCodeResultModel).toString());
        //inviteCodeResultModel = getInviteCodeResultModel();
        //startingData();
    }
}
