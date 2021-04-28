package com.latihangoding.tittle_tattle

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.latihangoding.tittle_tattle.ui.contact.ContactAdapter
import com.latihangoding.tittle_tattle.ui.home.HomeAdapter
import com.latihangoding.tittle_tattle.utils.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class IdleTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingresource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingresource)
    }

    @Test
    fun home_succesful_fetchingdata() {
        onView(withText("Cat Facts")).check(matches(isDisplayed()))
    }

    @Test
    fun homeScrollToDribbble() {
        Thread.sleep(1000)
        onView(withId(R.id.rv_main)).perform(
            RecyclerViewActions.scrollToHolder(
                withTitle<HomeAdapter.ViewHolder>(
                    "Dribbble", R.id.tv_title
                )
            )
        )
        val textView = onView(
            Matchers.allOf(
                withId(R.id.tv_title), withText("Dribbble"),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
    }

    @Test
    fun homeScrollToLoginRadius() {
        Thread.sleep(1000)
        onView(withId(R.id.rv_main)).perform(
            RecyclerViewActions.scrollToHolder(
                withTitle<HomeAdapter.ViewHolder>(
                    "LoginRadius", R.id.tv_title
                )
            )
        )
        val textView = onView(
            Matchers.allOf(
                withId(R.id.tv_title), withText("LoginRadius"),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
    }

    @Test
    fun homeScrollToPhishStats() {
        Thread.sleep(1000)
        onView(withId(R.id.rv_main)).perform(
            RecyclerViewActions.scrollToHolder(
                withTitle<HomeAdapter.ViewHolder>(
                    "PhishStats", R.id.tv_title
                )
            )
        )
        val textView = onView(
            Matchers.allOf(
                withId(R.id.tv_title), withText("PhishStats"),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
    }

    @Test
    fun checkContactKevinTio() {
        val floatingActionButton = onView(
            Matchers.allOf(
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
        floatingActionButton.perform(ViewActions.click())

        val textInputEditText = onView(
            Matchers.allOf(
                withId(R.id.et_search),
                isDisplayed()
            )
        )
        textInputEditText.perform(
            ViewActions.replaceText("kevin tio"),
            ViewActions.closeSoftKeyboard()
        )
        Thread.sleep(1000)
        val textView = onView(
            Matchers.allOf(
                withId(R.id.tv_fullname), withText("Kevin Tiothanry"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Kevin Tiothanry")))

        val textView2 = onView(
            Matchers.allOf(
                withId(R.id.tv_phoneNumber), withText("+6282370601243"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("+6282370601243")))
    }

    @Test
    fun checkContactFelixLiman() {
        val floatingActionButton = onView(
            Matchers.allOf(
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
        floatingActionButton.perform(ViewActions.click())

        val textInputEditText = onView(
            Matchers.allOf(
                withId(R.id.et_search),
                isDisplayed()
            )
        )
        textInputEditText.perform(
            ViewActions.replaceText("Felix Liman"),
            ViewActions.closeSoftKeyboard()
        )
        Thread.sleep(1000)
        val textView = onView(
            Matchers.allOf(
                withId(R.id.tv_fullname), withText("Felix Liman"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Felix Liman")))

        val textView3 = onView(
            Matchers.allOf(
                withId(R.id.tv_phoneNumber), withText("+6285261746811"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("+6285261746811")))
    }

    @Test
    fun scrollToContactModem() {
        val floatingActionButton = onView(
            Matchers.allOf(
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
        floatingActionButton.perform(ViewActions.click())
        Thread.sleep(1000)
        onView(withId(R.id.rv_main)).perform(
            RecyclerViewActions.scrollToHolder(
                withTitle<ContactAdapter.ViewHolder>(
                    "Modem", R.id.tv_fullname
                )
            )
        )
        val textView = onView(
            Matchers.allOf(
                withId(R.id.tv_fullname), withText("Modem"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
    }

    @Test
    fun scrollToContactKristianAntoni() {
        val floatingActionButton = onView(
            Matchers.allOf(
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
        floatingActionButton.perform(ViewActions.click())
        Thread.sleep(1000)
        onView(withId(R.id.rv_main)).perform(
            RecyclerViewActions.scrollToHolder(
                withTitle<ContactAdapter.ViewHolder>(
                    "Kristian Antoni", R.id.tv_fullname
                )
            )
        )
        val textView = onView(
            Matchers.allOf(
                withId(R.id.tv_fullname), withText("Kristian Antoni"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
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

    private inline fun <reified T : RecyclerView.ViewHolder> withTitle(
        title: String,
        id: Int
    ): Matcher<RecyclerView.ViewHolder?> {
        return object : BoundedMatcher<RecyclerView.ViewHolder?, T>(
            T::class.java
        ) {
            override fun matchesSafely(item: T): Boolean {
                return item.itemView.findViewById<TextView>(id).text.toString() == title
            }

            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }
        }
    }
}