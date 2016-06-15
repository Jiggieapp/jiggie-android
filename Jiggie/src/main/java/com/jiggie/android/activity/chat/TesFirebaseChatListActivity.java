package com.jiggie.android.activity.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;
import com.jiggie.android.model.RoomModel;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by LTE on 6/14/2016.
 */
public class TesFirebaseChatListActivity extends ToolbarActivity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_chatlist);
        ButterKnife.bind(this);
        super.bindView();
        super.setToolbarTitle("Firebase chat list", true);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query roomQuery = getQuery(mDatabase);
        roomQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //RoomModel roomModel = dataSnapshot.getValue(RoomModel.class);
                //String sd = String.valueOf(new Gson().toJson(roomModel));
                String sd = String.valueOf(dataSnapshot.getValue());

                Log.d("w", sd);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("e", "yes");
            }
        });

    }



    public Query getQuery(DatabaseReference databaseReference){
        return databaseReference.child("roommember").orderByChild("111222333");
    }

}
