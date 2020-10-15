package com.aveworks.eagle

import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import com.aveworks.eagle.fragments.HomeFragmentDirections
import com.aveworks.eagle.fragments.TransactionListFragmentDirections
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class TransactionListFragmentTest {
    private val hiltRule = HiltAndroidRule(this)
    private val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(activityTestRule)

    @Before
    fun jumpToTransactionListFragment() {
        activityTestRule.scenario.onActivity {
            runOnUiThread {
                val directions = HomeFragmentDirections.actionHomeFragmentToTransactionListFragment("404")
                findNavController(it, R.id.nav_host).navigate(directions)
            }
        }
    }

    @Test
    fun test_recyclerViewVisible(){
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))

        // TODO add more UI tests
    }
}

