package com.jiggie.android;

/**
 * Created by Wandy on 4/6/2016.
 */
import com.jiggie.android.activity.event.EventDetailActivity;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses({HelloWorldEspressoTest.class
        , EventDetailActivity.class
        , SocialTabFragmentTest.class})
public class UnitTestSuite {}
