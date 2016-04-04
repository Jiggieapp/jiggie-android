package com.jiggie.android;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;

import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.fragment.SignInFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

;

/**
 * Created by Wandy on 3/31/2016.
 */

@RunWith(AndroidJUnit4.class)
//@LargeTest
public class HelloWorldEspressoTest
        /*extends android.test.ActivityInstrumentationTestCase2<MainActivity> */{

    UiDevice mDevice;
    SignInFragment signInFragment;
    MainActivity mainActivity;

    @Rule
    //public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);
    public ActivityTestRule<MainActivity> splashActivityActivityTestRule
            = new ActivityTestRule(MainActivity.class);

    /*public ActivityTestRule<MainActivity> fbActivityTestRule
            = new ActivityTestRule(facebook.class);*/


    /*public HelloWorldEspressoTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }*/

    /*public HelloWorldEspressoTest()
    {
        super(MainActivity.class);
    }*/

    /*@Before
    public void setUp() throws Exception {
        super.setUp();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mainActivity = (MainActivity) getActivity();
        signInFragment = mainActivity.fragment;

        getInstrumentation().waitForIdleSync();
    }*/

    //@Override
    @Before
    public void setUp() throws Exception {
        //super.setUp();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mainActivity = splashActivityActivityTestRule.getActivity();
        /*getActivity().addFragment(signInFragment, SignInFragment.class.getSimpleName());
        getInstrumentation().waitForIdleSync();*/

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

        UiObject btnAuthorized = mDevice.findObject(new UiSelector()
                .instance(1)
                .className(android.widget.Button.class));
        //btnAuthorized.clickAndWaitForNewWindow(10000);
        btnAuthorized.click();

        /*Instrumentation.ActivityResult activityResult = getActivityResult();
        //intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(activityResult);
        intending(toPackage("com.jiggie.android.MainActivity"))
                .respondWith(activityResult);*/
        SystemClock.sleep(15000);
        //signInFragment.btnSignInOnClick();
        //signInFragment = splashActivityActivityTestRule.getActivity().fragment;
        //signInFragment.callbackManager.onActivityResult(64206, Activity.RESULT_OK, null);
        //signInFragment.doLogin();
        //signInFragment.doRandom();
        //mainActivity.onActivityResult(64206, Activity.RESULT_OK, null);
    }

    private Instrumentation.ActivityResult getActivityResult()
    {
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, null);
    }
}
