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

//largetest berfungsi untuk menandai pengujian harus dijalankan sebagai bagian dari pengujian besar
@LargeTest
// pengujian menggunakan androidjunit4
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

//    Deklarasi rule penguji dengan menggunakan java virtual machine
    @Rule
    @JvmField
//  menggunakan main_activity untuk diuji
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

//    function penguji untuk menuju gallery
    @Test
    fun go_to_gallery() {
//    id dengan fab_galery dijadikan objek untuk diklik
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

//    bila text_view dengan id toolbar muncul dengan text "Gallery", maka uji dinyatakan benar
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

//    function penguji untuk menuju activity contact
    @Test
    fun go_to_contact() {
    //    id dengan fab_contact dijadikan objek untuk diklik
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

//    bila view dengan id et_search muncul, maka test dinyatakan benar
        val editText = onView(
            withId(R.id.et_search)
        )
        editText.check(matches(isDisplayed()))
    }

    //    function penguji untuk menuju activity timer
    @Test
    fun go_to_timer() {
        //    id dengan fab_timer dijadikan objek untuk diklik
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

        //    bila switch material dengan id sm_alarm dan sm_scheduler berada di activity tersebut, maka ujian dinyatakan benar
        val switch_ = onView(
            withId(R.id.sm_alarm)
        )
        switch_.check(matches(isDisplayed()))

        val switch_2 = onView(
            withId(R.id.sm_scheduler)
        )
        switch_2.check(matches(isDisplayed()))
    }

    //    function penguji untuk menuju activity media
    @Test
    fun go_to_media() {
        //    id dengan fab_gallery dijadikan objek untuk diklik
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

//        dilanjutkan dengan menekan fab dengan id fab yang menjadi obejk, bila ketika di klik akan memunculkan nav_host_fragment
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

        //    bila recyclerview yang muncul dengan id rv_main, maka ujian dinyatakan benar
        val recyclerview = onView(withId(R.id.rv_main))
        recyclerview.check(matches(isDisplayed()))
    }

//    function berfungsi untuk mencari view di parent berdasarkan posisi parent
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
