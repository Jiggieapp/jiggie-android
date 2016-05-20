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

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.InviteFriendsAdapter;
import com.jiggie.android.manager.AccountManager;
import com.jiggie.android.manager.InviteManager;
import com.jiggie.android.model.ContactPhoneModel;
import com.jiggie.android.model.InviteCodeResultModel;
import com.jiggie.android.model.PostContactModel;
import com.jiggie.android.model.PostInviteAllModel;
import com.jiggie.android.model.PostInviteModel;
import com.jiggie.android.model.ReferEventMixpanelModel;
import com.jiggie.android.model.ResponseContactModel;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by LTE on 5/12/2016.
 */
public class InviteFriendsActivity extends ToolbarActivity implements SwipeRefreshLayout.OnRefreshListener, InviteFriendsAdapter.InviteSelectedListener, AppBarLayout.OnOffsetChangedListener {

    @Bind(R.id.appBar)
    AppBarLayout appBar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    InviteFriendsAdapter adapter;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    //ArrayList<ContactPhoneModel> dataContact = new ArrayList<ContactPhoneModel>();
    boolean isLoading = false;
    ArrayList<PostContactModel.Contact> contactToPost = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_invite_friends);
        super.bindView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Invite Friend by Phone");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);

        relInviteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getInviteCodeResultModel() != null) {
                    App.getInstance().trackMixPanelReferral(Utils.REFERRAL_PHONE_ALL
                            , getReferEventMixPanelModel(
                                    inviteCodeResultModel.getData().getInvite_code().getCode(),
                                    inviteCodeResultModel.getData().getInvite_code().getInvite_url()));
                }


                ArrayList<PostInviteAllModel.Contact> contacts = new ArrayList<PostInviteAllModel.Contact>();
                for (int i = 0; i < InviteManager.dataRest.size(); i++) {
                    contacts.add(new PostInviteAllModel.Contact(InviteManager.dataRest.get(i).getName(), InviteManager.dataRest.get(i).getPhone(), InviteManager.dataRest.get(i).getEmail(), InviteManager.dataRest.get(i).getUniq_id()));
                }
                PostInviteAllModel postInviteAllModel = new PostInviteAllModel(AccessToken.getCurrentAccessToken().getUserId(), contacts);
                InviteManager.loaderInviteAll(postInviteAllModel, new InviteManager.OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {

                    }

                    @Override
                    public void onFailure(int responseCode, String message) {

                    }
                });
                for (int i = 0; i < InviteManager.arrBtnInvite.size(); i++) {
                    adapter.setInviteEnable(InviteManager.arrBtnInvite.get(i), false);
                }
            }
        });
        appBar.addOnOffsetChangedListener(this);

        swipeRefresh.setEnabled(true);
        isLoading = true;
        swipeRefresh.setRefreshing(true);
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

    private void startingData() {
        if (InviteManager.dataRest.size() == 0) {
            swipeRefresh.setEnabled(true);
            onRefresh();
        } else {
            try {
                /*txtInviteDesc.setText(InviteManager.total_credit);
                txtInviteDesc.setVisibility(View.VISIBLE);*/
            } catch (Exception e) {
                Log.d("total credit problem", e.toString());
            }

            setAdapters(InviteManager.dataRest, InviteManager.dataContact);
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
        swipeRefresh.setRefreshing(true);
        getContactPhoneInvite();
        //swipeRefresh.setEnabled(false);
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
        if (InviteManager.dataRest.size() == 0) {
            swipeRefresh.setEnabled(true);
        } else {
            swipeRefresh.setEnabled(false);
        }

    }

    /*private ReferEventMixpanelModel getReferEventMixPanelModel(final String code, final String url) {
        if (reverEventMixpanelModel == null) {
            return new ReferEventMixpanelModel(code, url);
        }
        return reverEventMixpanelModel;
    }*/


    private InviteCodeResultModel getInviteCodeResultModel() {
        final String inv = AccountManager.getInviteCodeFromPreference();
        if (inv.isEmpty())
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
                Utils.d(TAG, "contact size " + contact.getPhone().size());
                for(int i=0;i<contact.getPhone().size();i++){
                    if(i != 0){
                        phoneNumber += ";"+contact.getPhone().get(i);
                    }else{
                        phoneNumber = contact.getPhone().get(i);
                    }
                }
                openSMS(phoneNumber);
                //openSMS(contact.getPhone().get(0));
            }
        } else {
            PostInviteModel postInviteModel = new PostInviteModel(AccessToken.getCurrentAccessToken().getUserId(), new PostInviteModel.Contact(contact.getName(), contact.getEmail(), contact.getUniq_id()));
            InviteManager.loaderInvite(postInviteModel, new InviteManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    //success invite
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    //failed invite
                }
            });
        }
    }

    private void showProgressDialog()
    {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.wait) );
    }

    private void dismissProgressDialog()
    {
        if(progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }
    private void openSMS(String telp) {
        Utils.d(TAG, "telp " + telp);
        Uri uri = Uri.parse("smsto:" + telp);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", InviteManager.msg_share);
        startActivity(it);
    }

    private void getContactPhoneInvite() {
        showProgressDialog();
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params) {
                doGetContact();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //dismissProgressDialog();
            }
        }.execute();
    }

    private void doGetContact()
    {
        ContentResolver cr = getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = Utils.BLANK;
                /*String phoneNumber = Utils.BLANK;
                String email = Utils.BLANK;*/

                ArrayList<String> phoneNumber = new ArrayList<>();
                ArrayList<String> email = new ArrayList<>();
                String photoThumbnail = Utils.BLANK;

                try {
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                        /*Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                                null);*/
                        while (pCur.moveToNext()) {
                            //String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //alContacts.add(contactNumber);
                            name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            //email = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.MIMETYPE));
                            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                                Cursor Cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                                while (Cur.moveToNext()) {
                                    String mPhone = Cur.getString(Cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    phoneNumber.add(mPhone);
                                }
                                Cur.close();
                            }

                            photoThumbnail = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                            if (photoThumbnail == null) {
                                photoThumbnail = Utils.BLANK;
                            }

                            Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
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
                    }
                } catch (Exception e) {
                    dismissProgressDialog();
                }
            } while (cursor.moveToNext());
        }

        String responses = new Gson().toJson(InviteManager.dataContact);

        for (int i = 0; i < InviteManager.dataContact.size(); i++) {
            /*ArrayList<String> arrEmail = new ArrayList<>();
            arrEmail.add(InviteManager.dataContact.get(i).getEmail());
            ArrayList<String> arrPhone = new ArrayList<>();
            arrPhone.add(InviteManager.dataContact.get(i).getPhoneNumber());*/
            contactToPost.add(new PostContactModel.Contact(InviteManager.dataContact.get(i).getId()
                    , InviteManager.dataContact.get(i).getName(), InviteManager.dataContact.get(i).getEmail(),
                    InviteManager.dataContact.get(i).getPhoneNumber()));
        }

        String ds = new Gson().toJson(contactToPost);

        if (contactToPost.size() > 0) {
            final PostContactModel postContactModel = new PostContactModel(AccessToken.getCurrentAccessToken().getUserId()
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
                            /*txtInviteDesc.setText(InviteManager.total_credit);
                            txtInviteDesc.setVisibility(View.VISIBLE);*/
                        } catch (Exception e) {
                            Log.d("total credit problem", e.toString());
                            dismissProgressDialog();
                        }

                        InviteManager.msg_share = responseContactModel.getData().getMsg_share();
                        //==============


                        /*for (int i = 0; i < responseContactModel.getData().getContact().size(); i++) {
                            ResponseContactModel.Data.Contact c = responseContactModel.getData().getContact().get(i);
                            if (c.getEmail().size() == 1 && c.getEmail().get(0).equals("")) //fak kosong binatang
                            {
                                //contact.getEmail().clear();
                                responseContactModel.getData().getContact().get(i).getEmail().clear();
                            }
                        }*/
                        setAdapters(responseContactModel.getData().getContact(), InviteManager.dataContact);
                    } else {
                        isLoading = false;
                        swipeRefresh.setRefreshing(false);
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    isLoading = false;
                    swipeRefresh.setRefreshing(false);
                    dismissProgressDialog();
                }
            });
        }
    }

    private void setAdapters(ArrayList<ResponseContactModel.Data.Contact> data, ArrayList<ContactPhoneModel> dataContact) {
        InviteManager.dataRest = data;
        recyclerView.setAdapter(adapter = new InviteFriendsAdapter(this, dataContact, data, this));
        isLoading = false;
        swipeRefresh.setRefreshing(false);
        swipeRefresh.setEnabled(false);
        dismissProgressDialog();
    }
}
