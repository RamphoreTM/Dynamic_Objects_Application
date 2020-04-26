package com.example.dynamic_instantiation_test;

import android.widget.TextView;

import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.app.PendingIntent.getActivity;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.rule.ActivityTestRule.*;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {

    }


    @Test
    public void ButtonPush(){
        String[] TestString = {"Ford Fiesta","Ford Figo","Ford T-Cross","Ford Ranger","Ford S20 Plus"};
        int rando = new Random().nextInt(TestString.length);
        onView(withId(R.id.llListView)).check(matches(not(hasDescendant(withText(TestString[rando])))));
        for (int i=0; i<5;i++) {
            onView(withId(R.id.edtCarName)).perform(typeText(TestString[i]));
            closeSoftKeyboard();
            onView(withText("Add")).perform(click());
        }
        onView(withText(TestString[rando])).check(matches(isDisplayed()));
    }

    @Test
    public void WordClick(){
        String[] TestString = {"Ford Fiesta","Ford Figo","Ford T-Cross","Ford Ranger","Ford S20 Plus"};

        //add words
        for (int i=0; i<5;i++) {
            onView(withId(R.id.edtCarName)).perform(typeText(TestString[i]));
            closeSoftKeyboard();
            onView(withText("Add")).perform(click());
        }


        //delete and check
        for (int i = TestString.length -1; i >= 0; i--){
            onView(withText(TestString[i])).check(matches(isDisplayed()));
            onView(withText(TestString[i])).perform(click());
            //onView(withText(TestString[i] + " has been removed")).inRoot(withDecorView(not(getActivity(MainActivity.this).getWindow().getDecorView()))).check(matches(isDisplayed()));
            onView(withId(R.id.llListView)).check(matches(not(hasDescendant(withText(TestString[i])))));
        }
    }
}