package com.jiggie.android.activity.invite;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.component.adapter.InviteFriendsAdapter;
import com.jiggie.android.manager.InviteManager;
import com.jiggie.android.model.ContactPhoneModel;
import com.jiggie.android.model.PostContactModel;
import com.jiggie.android.model.PostInviteModel;
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
                /*ArrayList<PostInviteAllModel.Contact> contacts = new ArrayList<PostInviteAllModel.Contact>();
                for (int i = 0; i < dataRest.size(); i++) {
                    contacts.add(new PostInviteAllModel.Contact(dataRest.get(i).getName(), dataRest.get(i).getPhone(), dataRest.get(i).getEmail(), dataRest.get(i).getUniq_id()));
                }
                PostInviteAllModel postInviteAllModel = new PostInviteAllModel(AccessToken.getCurrentAccessToken().getUserId(), contacts);
                InviteManager.loaderInviteAll(postInviteAllModel, new InviteManager.OnResponseListener() {
                    @Override
                    public void onSuccess(Object object) {

                    }

                    @Override
                    public void onFailure(int responseCode, String message) {

                    }
                });*/
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

    @Override
    protected void onStart() {
        super.onStart();
        if (InviteManager.dataRest.size() == 0) {
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
        }
    }

    @Override
    public void onRefresh() {
        isLoading = true;
        swipeRefresh.setRefreshing(true);
        getContactPhoneInvite();
        //swipeRefresh.setEnabled(false);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        /*if (verticalOffset==0) {
            swipeRefresh.setEnabled(true);
        } else {
            swipeRefresh.setEnabled(false);
        }*/
        if (InviteManager.dataRest.size() == 0) {
            swipeRefresh.setEnabled(true);
        }else{
            swipeRefresh.setEnabled(false);
        }

    }

    @Override
    public void onInviteSelected(ResponseContactModel.Data.Contact contact) {
        if (contact.getEmail().get(0).equals(Utils.BLANK)) {
            if (contact.getPhone().get(0).equals(Utils.BLANK)) {
                //do nothing
            } else {
                openSMS(contact.getPhone().get(0));
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

    private void openSMS(String telp) {
        Uri uri = Uri.parse("smsto:" + telp);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", InviteManager.msg_share);
        startActivity(it);
    }

    private void getContactPhoneInvite() {
        ContentResolver cr = getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = Utils.BLANK;
                String phoneNumber = Utils.BLANK;
                String email = Utils.BLANK;
                String photoThumbnail = Utils.BLANK;

                try {
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        /*Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                                ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY+" ASC");*/
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                                null);
                        while (pCur.moveToNext()) {
                            //String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //alContacts.add(contactNumber);
                            name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            //email = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.MIMETYPE));
                            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            photoThumbnail = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                            if (photoThumbnail == null) {
                                photoThumbnail = Utils.BLANK;
                            }

                            Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                            while (emailCur.moveToNext()) {
                                email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                Log.e("Email", name + " " + email);
                            }
                            emailCur.close();


                            InviteManager.dataContact.add(new ContactPhoneModel(id, name, phoneNumber, email, photoThumbnail));

                            break;
                        }
                        pCur.close();
                    }

                } catch (Exception e) {

                }

            } while (cursor.moveToNext());
        }

        for (int i = 0; i < InviteManager.dataContact.size(); i++) {
            ArrayList<String> arrEmail = new ArrayList<>();
            arrEmail.add(InviteManager.dataContact.get(i).getEmail());
            ArrayList<String> arrPhone = new ArrayList<>();
            arrPhone.add(InviteManager.dataContact.get(i).getPhoneNumber());
            contactToPost.add(new PostContactModel.Contact(InviteManager.dataContact.get(i).getId(), InviteManager.dataContact.get(i).getName(), arrEmail, arrPhone));
        }

        if (contactToPost.size() > 0) {
            final PostContactModel postContactModel = new PostContactModel(AccessToken.getCurrentAccessToken().getUserId(), Utils.TYPE_ANDROID, contactToPost);
            InviteManager.loaderPostContact(postContactModel, new InviteManager.OnResponseListener() {
                @Override
                public void onSuccess(Object object) {
                    ResponseContactModel responseContactModel = (ResponseContactModel) object;

                    String sd = String.valueOf(new Gson().toJson(responseContactModel));

                    if (responseContactModel != null) {

                        //==============
                        try {
                            InviteManager.total_credit = "For " + String.valueOf(responseContactModel.getData().getTot_credit()) + " Credits";
                            txtInviteDesc.setText(InviteManager.total_credit);
                            txtInviteDesc.setVisibility(View.VISIBLE);
                        }catch (Exception e){
                            Log.d("total credit problem", e.toString());
                        }

                        InviteManager.msg_share = responseContactModel.getData().getMsg_share();
                        //==============

                        setAdapters(responseContactModel.getData().getContact(), InviteManager.dataContact);
                    } else {
                        isLoading = false;
                        swipeRefresh.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(int responseCode, String message) {
                    isLoading = false;
                    swipeRefresh.setRefreshing(false);
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
    }
}
