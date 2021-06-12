package com.inspiringteam.xchange.quakes

import android.app.Activity
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import com.inspiringteam.xchange.R
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule

import com.inspiringteam.xchange.ui.quakes.QuakesActivity
import org.junit.Rule

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId


@RunWith(AndroidJUnit4::class)
@LargeTest
class QuakesScreenTest {
    @Rule
    @JvmField
    val activityRule = activityTestRule<QuakesActivity>()

    // More complex tests should be added as app's complexity rises
    @Test
    fun displayItemsInList() {
        // check if the ListView is visible
        onView(withId(R.id.quakesListView))
            .check(matches(isDisplayed()))
    }
}

inline fun <reified T : Activity> activityTestRule(initialTouchMode: Boolean = false, launchActivity: Boolean = true) =
    ActivityTestRule(T::class.java, initialTouchMode, launchActivity)
