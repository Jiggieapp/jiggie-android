package com.jiggie.android;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;
import android.widget.Button;

import com.jiggie.android.activity.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

;

/**
 * Created by Wandy on 3/31/2016.
 */

@RunWith(AndroidJUnit4.class)
//@LargeTest
public class HelloWorldEspressoTest {

    UiDevice mDevice;

    @Rule
    //public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);
    public ActivityTestRule<MainActivity> splashActivityActivityTestRule
            = new ActivityTestRule(MainActivity.class);

    /*public ActivityTestRule<MainActivity> fbActivityTestRule
            = new ActivityTestRule(facebook.class);*/

    @Before
    public void setUp() throws Exception {
        //super.setUp();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void onSplashCreated() throws UiObjectNotFoundException {
        //onView(withText("Hello world!")).check(matches(isDisplayed()));
        onView(withId(R.id.btnSignIn))
                .perform(click());

        // mDevice.wait(10000L);
        UiObject userName = mDevice.findObject(new UiSelector()
                .instance(0)
                .className(android.widget.EditText.class));
        UiObject password = mDevice.findObject(new UiSelector()
                .instance(1)
                .className(android.widget.EditText.class));
        UiObject button = mDevice.findObject(new UiSelector()
                .instance(0)
                .className(Button.class));
        userName.setText("wanceq_2804@hotmail.com");
        password.setText("untukm43Nfb");
        button.clickAndWaitForNewWindow();
    }

    private Instrumentation.ActivityResult onActivityResult()
    {

    }
}
