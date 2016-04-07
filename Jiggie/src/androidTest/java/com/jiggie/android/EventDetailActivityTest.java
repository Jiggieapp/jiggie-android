package com.jiggie.android;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.jiggie.android.activity.event.EventDetailActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.Common;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Wandy on 4/5/2016.
 */
@RunWith(AndroidJUnit4.class)
public class EventDetailActivityTest {

    // third parameter is set to false which means the activity is not started automatically
    @Rule
    public ActivityTestRule<EventDetailActivity> rule =
            new ActivityTestRule(EventDetailActivity.class, true, false);
    /*public IntentsTestRule<EventDetailActivity> rule =
            new IntentsTestRule(EventDetailActivity.class);*/

    @Before
    public void setUp()
    {
        Utils.d("helloworld", "before");
        // Lazily start the Activity from the ActivityTestRule this time to inject the start Intent
        Intent i = new Intent();
        //startIntent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, NOTE.getId());
        i.putExtra(Common.FIELD_EVENT_ID, "56ed2b34d76c1c03008988fb");
        i.putExtra(Common.FIELD_EVENT_NAME, "Ticket Series 4");
        i.putExtra(Common.FIELD_EVENT_VENUE_NAME, "Aksara Kemang");
        ArrayList<String> tag = new ArrayList<>();
        tag.add("Art & Culture");
        i.putExtra(Common.FIELD_EVENT_TAGS, tag);
        i.putExtra(Common.FIELD_EVENT_DAY, "2016-04-05T23:00:00.000Z");
        i.putExtra(Common.FIELD_EVENT_DAY_END, "2016-04-06T17:00:00.000Z");
        ArrayList<String> photos = new ArrayList<>();
        photos.add("https://s3-us-west-2.amazonaws.com/cdnpartyhost/1451278193214_original.png");
        photos.add("https://s3-us-west-2.amazonaws.com/cdnpartyhost/1451278212696_original.png");
        i.putExtra(Common.FIELD_EVENT_PICS, photos);
        i.putExtra(Common.FIELD_EVENT_DESCRIPTION, "Ticket Series 4");
        rule.launchActivity(i);

        registerIdlingResource();
    }


    @Test
    public void wait_then_close() throws UiObjectNotFoundException{
        Utils.d("helloworld", "on wait then close");
        onView(withId(R.id.txtDescription))
                .check(matches(withText("Ticket Series 4")));
    }

    /**
     * Convenience method to register an IdlingResources with Espresso. IdlingResource resource is
     * a great way to tell Espresso when your app is in an idle state. This helps Espresso to
     * synchronize your test actions, which makes tests significantly more reliable.
     */
    private void registerIdlingResource() {
        Espresso.registerIdlingResources(
                rule.getActivity().getCountingIdlingResource());
    }
}
