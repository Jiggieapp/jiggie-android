package com.jiggie.android;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.test.ActivityInstrumentationTestCase2;

import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.fragment.SignInFragment;

import org.junit.Rule;

/**
 * Created by Wandy on 4/4/2016.
 */
public class SignInFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private SignInFragment signInFragment;

    public SignInFragmentTest() {
        super(MainActivity.class);
    }

    /*@Override protected void setUp() throws Exception {
        super.setUp();
        Utils.d("SignInFragmentTest", "masuk di isini");
        signInFragment = new SignInFragment();
        getActivity().addFragment(signInFragment, SignInFragment.class.getSimpleName());
        getInstrumentation().waitForIdleSync();
    }*/

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        signInFragment = new SignInFragment();
        getActivity().addFragment(signInFragment, SignInFragment.class.getSimpleName());
        getInstrumentation().waitForIdleSync();

    }

    /*public void test_Should_Set_Title_TextView_Text() {
        TextView titleTextView = (TextView) booksFragment.getView().findById(R.id.title);
        assertEqual("some title here", titleTextView.getText().toString());
    }*/

}
