package com.latihangoding.tittle_tattle


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun go_to_gallery() {
        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab_gallery),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val textView = onView(
            allOf(
                withText("Gallery"),
                withParent(
                    allOf(
                        withId(R.id.toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Gallery")))
    }

    @Test
    fun go_to_contact() {
        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab_contact),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val editText = onView(
            withId(R.id.et_search)
        )
        editText.check(matches(isDisplayed()))
    }

    @Test
    fun go_to_timer() {
        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab_timer),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val switch_ = onView(
            withId(R.id.sm_alarm)
        )
        switch_.check(matches(isDisplayed()))

        val switch_2 = onView(
            withId(R.id.sm_scheduler)
        )
        switch_2.check(matches(isDisplayed()))
    }

    @Test
    fun go_to_media() {
        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab_gallery),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val floatingActionButton2 = onView(
            allOf(
                withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(click())

        val recyclerview = onView(withId(R.id.rv_main))
        recyclerview.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
