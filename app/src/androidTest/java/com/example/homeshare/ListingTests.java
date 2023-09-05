package com.example.homeshare;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListingTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testCreateListing() {
        // Context of the app under test.
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String address = "3705, Smallwood ct, Pleasanton, CA";
        int maxRoommates = 8;
        Date expiration = new Date("12/31/2022");
        String description = "amazing crib great roommates will be a fun time";


        onView(withId(R.id.CreateListingButton)).perform(click());
        onView(withId(R.id.ListingAddress)).perform(typeText(address));
        onView(withId(R.id.ListingRoommates)).perform(typeText("8"));
        onView(withId(R.id.ListingExpiration)).perform(typeText("12/31/2022"));
        onView(withId(R.id.ListingExpiration)).perform(closeSoftKeyboard());

        onView(withId(R.id.ListingDescription)).perform(typeText(description));
        onView(withId(R.id.ListingDescription)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Listing> allListings = Database.readListingCache();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean flag = false;
        for (Map.Entry<String,Listing> entry : allListings.entrySet()) {
            if (entry.getValue().description.equals(description) && entry.getValue().address.equals(address))
                flag = true;
        }

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
        assertTrue(flag);
    }
    @Test
    public void testDeleteListing() {
        // Context of the app under test.
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, 10000 + 1);
        String address = randomNum + ", Smallwood ct, Pleasanton, CA";
        int maxRoommates = 8;
        Date expiration = new Date("12/31/2022");
        String description = "amazing crib great roommates will be a fun time";


        onView(withId(R.id.CreateListingButton)).perform(click());
        onView(withId(R.id.ListingAddress)).perform(typeText(address));
        onView(withId(R.id.ListingRoommates)).perform(typeText("8"));
        onView(withId(R.id.ListingExpiration)).perform(typeText("12/31/2022"));
        onView(withId(R.id.ListingExpiration)).perform(closeSoftKeyboard());

        onView(withId(R.id.ListingDescription)).perform(typeText(description));
        onView(withId(R.id.ListingDescription)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText(address)).perform(closeSoftKeyboard());
        onView(withText(address)).perform(scrollTo(),click());
        onView(withId(R.id.deleteListing)).perform(click());
        //check whether that one still exists in the database
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Listing> allListings = Database.readListingCache();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean flag = false;
        for (Map.Entry<String,Listing> entry : allListings.entrySet()) {
            if (entry.getValue().description.equals(description) && entry.getValue().address.equals(address))
                flag = true;
        }



        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
        assertFalse(flag);
    }
}