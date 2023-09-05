package com.example.homeshare;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ThreadLocalRandom;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BasicFunctionalityTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testLogin() {
        //logging into the account
        onView(withId(R.id.AuthEmail)).perform(typeText("james@usc.edu"));
        onView(withId(R.id.AuthPassword)).perform(typeText("jamie123"));
        onView(withId(R.id.AuthPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //checking whether we are in the correct account
        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.EditProfileName)).check(matches(withText("james madison")));

        //logging out of the account for the next test
        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
    }
    @Test
    public void testCreateAccount() {
        // Context of the app under test.
        int randomNum = ThreadLocalRandom.current().nextInt(0, 10000 + 1);
        onView(withId(R.id.AuthSignup)).perform(click());
        onView(withId(R.id.AuthResetEmail)).perform(typeText("John" + randomNum + " Adams"));
        onView(withId(R.id.AuthCreateInstagram)).perform(typeText("john"));
        onView(withId(R.id.AuthCreateInstagram)).perform(closeSoftKeyboard());
        onView(withId(R.id.EditProfileMale)).perform(click());
        onView(withId(R.id.ListingCreateSubmit)).perform(click());
        onView(withId(R.id.AuthCreateEmail)).perform(typeText("john" + randomNum + "@usc.edu"));
        onView(withId(R.id.AuthEmail)).perform(typeText("johnny123"));
        onView(withId(R.id.AuthCreatePasswordConfirm)).perform(typeText("johnny123"));
        onView(withId(R.id.AuthCreatePasswordConfirm)).perform(closeSoftKeyboard());
        onView(withId(R.id.AuthSubmit2)).perform(click());
        onView(withId(R.id.EditProfileAlcoholicOld)).perform(click());
        onView(withId(R.id.EditProfileNightOwl)).perform(click());
        onView(withId(R.id.AuthCreateBio)).perform(typeText("I came from a small town called Boston"));
        onView(withId(R.id.AuthCreateBio)).perform(closeSoftKeyboard());
        onView(withId(R.id.AuthSubmit3)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.EditProfileName)).check(matches(withText("John" + randomNum + " Adams")));
        onView(withId(R.id.AuthLogout)).perform(click());
    }
    @Test
    public void testEditAccount() {
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

        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.EditProfileSubmit)).perform(click());
        onView(withId(R.id.ListingDescription)).perform(clearText());
        onView(withId(R.id.ListingDescription)).perform(typeText("I am a senior computer science student from the Bay Area v2"));
        onView(withId(R.id.ListingDescription)).perform(closeSoftKeyboard());
        onView(withId(R.id.EditProfileSubmit)).perform(click());
        onView(withId(R.id.Biography)).check(matches(withText("I am a senior computer science student from the Bay Area v2")));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.ProfileTab)).perform(click());
        onView(withId(R.id.AuthLogout)).perform(click());
    }
    @Test
    public void testLogout() {
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
        //checking whether we are in the correct account
        onView(withId(R.id.ProfileTab)).perform(click());

        //logging out of the account for the next test
        onView(withId(R.id.AuthLogout)).perform(click());
        assertTrue(Auth.mAuth.getCurrentUser() == null);
    }
}