package com.example.homeshare;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.util.EnumSet.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class QueryTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testFilterBySmoker() {
        // login
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.SmokerChip)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //grab all of the visible listings
        Vector<Listing> displayedListings = MainActivity.instance.listingManager.getCurrentlyDisplayedListings();
        for(Listing l : displayedListings){
            if(!l.postedBy.preferences.smoker) fail();
        }
//                onData(withId(R.layout.view_listing_filter)).check(matches()));
        //check if each ones postedBy is a smoker


        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
        assertTrue(true);
    }
    @Test
    public void testFilterByAlcohol() {
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.AlcoholChip)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //grab all of the visible listings
        Vector<Listing> displayedListings = MainActivity.instance.listingManager.getCurrentlyDisplayedListings();
        for(Listing l : displayedListings){
            if(!l.postedBy.preferences.alcoholic) fail();
        }
//                onData(withId(R.layout.view_listing_filter)).check(matches()));
        //check if each ones postedBy is a smoker


        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
        assertTrue(true);
    }
    @Test
    public void testFilterByNightOwl() {
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.NightOwlChip)).perform(scrollTo(), click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //grab all of the visible listings
        Vector<Listing> displayedListings = MainActivity.instance.listingManager.getCurrentlyDisplayedListings();
        for(Listing l : displayedListings){
            if(!l.postedBy.preferences.nightOwl) fail();
        }


        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
        assertTrue(true);
    }
    @Test
    public void testFilterBySocial() {
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.SocialChip)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //grab all of the visible listings
        Vector<Listing> displayedListings = MainActivity.instance.listingManager.getCurrentlyDisplayedListings();
        for(Listing l : displayedListings){
            if(!l.postedBy.preferences.social) fail();
        }

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
        assertTrue(true);
    }
    @Test
    public void testFilterBySmokerAndAlcohol() {
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.SmokerChip)).perform(click());
        onView(withId(R.id.AlcoholChip)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //grab all of the visible listings
        Vector<Listing> displayedListings = MainActivity.instance.listingManager.getCurrentlyDisplayedListings();
        for(Listing l : displayedListings){
            if(!l.postedBy.preferences.smoker || !l.postedBy.preferences.alcoholic) fail();
        }

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
        assertTrue(true);
    }
    @Test
    public void testDeselectFilter(){
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.SmokerChip)).perform(click());
        onView(withId(R.id.SmokerChip)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //grab all of the visible listings
        Vector<Listing> displayedListings = MainActivity.instance.listingManager.getCurrentlyDisplayedListings();
        Map<String,Listing> listingCache = Database.readListingCache();
        if(listingCache.size() != displayedListings.size()) fail();

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
        assertTrue(true);
    }
}