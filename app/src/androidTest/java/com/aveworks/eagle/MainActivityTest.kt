package com.aveworks.eagle

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class MainActivityTest {
    private val hiltRule = HiltAndroidRule(this)
    private val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(activityTestRule)

    @Test
    fun testHomeFragment(){
        onView(withId(R.id.logo)).check(matches(isDisplayed()))
        onView(withId(R.id.button)).check(matches(isDisplayed()))


        // Check continue button for disable/enable state
        onView(withId(R.id.button)).check(matches(not(isEnabled())))

        onView(withId(R.id.xpub)).perform(ViewActions.typeText("xpub"))
        onView(withId(R.id.button)).check(matches(isEnabled()))

        onView(withId(R.id.button)).perform()
    }
}

