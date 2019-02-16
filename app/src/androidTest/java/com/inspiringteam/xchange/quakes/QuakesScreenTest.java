package com.inspiringteam.xchange.quakes;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.inspiringteam.xchange.R;
import com.inspiringteam.xchange.ui.quakes.QuakesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuakesScreenTest {
    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     *
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<QuakesActivity> mNewsActivityTestRule =
            new ActivityTestRule<>(QuakesActivity.class);

    // More complex tests should be added as app's complexity rises
    @Test
    public void displayItemsInList(){
        // check if the ListView is visible
        onView(withId(R.id.quakes_list)).check(matches(isDisplayed()));
    }
}

