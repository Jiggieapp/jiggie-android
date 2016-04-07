package com.jiggie.android;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jiggie.android.activity.MainActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.fragment.SignInFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

;

/**
 * Created by Wandy on 3/31/2016.
 */

@RunWith(AndroidJUnit4.class)
//@LargeTest
public class HelloWorldEspressoTest
        /*extends android.test.ActivityInstrumentationTestCase2<MainActivity> */ {

    UiDevice mDevice;
    SignInFragment signInFragment;
    MainActivity mainActivity;

    /*@Rule
    public ActivityTestRule<MainActivity> splashActivityActivityTestRule
            = new ActivityTestRule(MainActivity.class);*/

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
    public void onSplashCreated() throws UiObjectNotFoundException {
        Utils.d("helloworld", "onsplashcreated");
        //onView(withText("Hello world!")).check(matches(isDisplayed()));
        if (!App.getInstance().isUserLoggedIn()) {
            onView(withId(R.id.btnSignIn))
                    .perform(click());
            SystemClock.sleep(10000);
            Utils.d("helloworld", App.getInstance().isUserLoggedIn() + "");
            if (!App.getInstance().isUserLoggedIn())
            {
                UiObject userName = mDevice.findObject(new UiSelector()
                        .instance(0)
                        .className(android.widget.EditText.class));
                UiObject password = mDevice.findObject(new UiSelector()
                        .instance(1)
                        .className(android.widget.EditText.class));
                UiObject button = mDevice.findObject(new UiSelector()
                        .instance(0)
                        .className(Button.class));
                //if(userName.exists() && password.exists())
                //{
                    userName.setText("wanceq_2804@hotmail.com");
                    password.setText("untukm43Nfb");
                    button.clickAndWaitForNewWindow();

                    UiObject btnAuthorized = mDevice.findObject(new UiSelector()
                            .instance(1)
                            .className(android.widget.Button.class));
                    if(btnAuthorized.exists())
                    {
                        btnAuthorized.click();
                    }
                    SystemClock.sleep(15000);
               }
            //}
        }
        else {
            SystemClock.sleep(10000);
            UiObject event = mDevice.findObject(new UiSelector()
                .instance(0)
                .className(LinearLayout.class));
            event.click();
            intended(toPackage("com.jiggie.android.debug"));
        }
    }


}
