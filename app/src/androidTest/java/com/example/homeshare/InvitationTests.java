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
public class InvitationTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testSendInvite() {
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(4000);
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

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());


        onView(withId(R.id.AuthEmail)).perform(typeText("lovelock@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("123,dan"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        onView(withText(address)).perform(closeSoftKeyboard());
        onView(withText(address)).perform(scrollTo(),click());

        onView(withId(R.id.ListingDescription)).perform(typeText("I would love to join you apartment"));
        onView(withId(R.id.ListingDescription)).perform(closeSoftKeyboard());
        onView(withId(R.id.InvitationSubmit)).perform(scrollTo(), click());

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean flag = false;
        Map<String,Listing> listings = Database.readListingCache();
        for (Map.Entry<String,Listing> entry : listings.entrySet()){
            if(entry.getValue().address.equals(address)){
                for(int i = 0;i<entry.getValue().invitations.size();i++){
                    if(entry.getValue().invitations.get(i).sender.name.equals("Daniel Lovelock"))
                        flag = true;
                }
            }
        }
        assertTrue(flag);

    }
    @Test
    public void testAcceptInvite() {
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(4000);
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

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());


        onView(withId(R.id.AuthEmail)).perform(typeText("lovelock@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("123,dan"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        onView(withText(address)).perform(closeSoftKeyboard());
        onView(withText(address)).perform(scrollTo(),click());

        onView(withId(R.id.ListingDescription)).perform(typeText("I would love to join you apartment"));
        onView(withId(R.id.ListingDescription)).perform(closeSoftKeyboard());
        onView(withId(R.id.InvitationSubmit)).perform(scrollTo(), click());

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.InboxTab)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withTagValue(is((Object) "Accept" + "lovelock@usc.edu"+ address))).perform(scrollTo(),click());

        //ListingManager.inbox.get("lovelock@usc.edu").accept(null);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean flag = false;
        Map<String,Listing> listings = Database.readListingCache();
        for (Map.Entry<String,Listing> entry : listings.entrySet()){
            if(entry.getValue().address.equals(address)){
                for(int i = 0;i<entry.getValue().roommates.size();i++){
                    if(entry.getValue().roommates.get(i).name.equals("Daniel Lovelock"))
                        flag = true;
                }
            }
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.deleteListing)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
        assertTrue(flag);
    }
    @Test
    public void testDeclineInvite() {
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(4000);
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

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());


        onView(withId(R.id.AuthEmail)).perform(typeText("lovelock@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("123,dan"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        onView(withText(address)).perform(closeSoftKeyboard());
        onView(withText(address)).perform(scrollTo(),click());

        onView(withId(R.id.ListingDescription)).perform(typeText("I would love to join you apartment"));
        onView(withId(R.id.ListingDescription)).perform(closeSoftKeyboard());
        onView(withId(R.id.InvitationSubmit)).perform(scrollTo(), click());

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.InboxTab)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withTagValue(is((Object) "Decline" + "lovelock@usc.edu"+ address))).perform(scrollTo(),click());

        boolean flag = false;
        Map<String,Listing> listings = Database.readListingCache();
        for (Map.Entry<String,Listing> entry : listings.entrySet()){
            if(entry.getValue().address.equals(address)){
                for(int i = 0;i<entry.getValue().roommates.size();i++){
                    if(entry.getValue().roommates.get(i).name.equals("Daniel Lovelock"))
                        flag = true;
                }
            }
        }
        System.out.println(flag);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.backToMainButton)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
        assertFalse(flag);
    }
}