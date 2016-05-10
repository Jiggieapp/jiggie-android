package com.jiggie.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiggie.android.R;
import com.jiggie.android.component.HomeMain;
import com.jiggie.android.component.TabFragment;

import butterknife.ButterKnife;

/**
 * Created by Wandy on 5/10/2016.
 */
public class FriendsFragment extends Fragment implements TabFragment{
    private HomeMain homeMain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        ButterKnife.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onTabSelected() {

    }

    @Override
    public void setHomeMain(HomeMain homeMain) {
        this.homeMain = homeMain;
    }

    @Override
    public int getIcon() {
        return 0;
    }
}
