package com.jiggie.android;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.component.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Wandy on 4/6/2016.
 */
@RunWith(AndroidJUnit4.class)
public class SocialTabFragmentTest {
    UiDevice mDevice;
    MainActivity mainActivity;

    @Rule
    public IntentsTestRule<MainActivity> splashActivityActivityTestRule
            = new IntentsTestRule(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        //super.setUp();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mainActivity = splashActivityActivityTestRule.getActivity();
        /*getActivity().addFragment(signInFragment, SignInFragment.class.getSimpleName());
        getInstrumentation().waitForIdleSync();*/

    }

    @Test
    public void onSocialTabFragmentClick() throws UiObjectNotFoundException
    {
        Utils.d("helloworldespressotest", "onsocialtabfragmentclick");
        //onView(withId(R.id.tab)).perform()
        UiObject socialTb = mDevice.findObject(new UiSelector()
                .instance(1)
                .className(android.support.v7.app.ActionBar.Tab.class));
        socialTb.click();
        SystemClock.sleep(10000);
        //android.support.v7.app.ActionBar$Tab
    }
}
