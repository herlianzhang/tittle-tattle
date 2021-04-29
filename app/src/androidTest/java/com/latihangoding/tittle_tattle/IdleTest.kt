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

    //    Deklarasi rule penguji dengan menggunakan java virtual machine
    @get:Rule
    //  menggunakan main_activity untuk diuji
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

//    sebelum memulai pengujian melakukan setup untuk meregister idling resource
    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingresource)
    }


//    setelah selesai melakukan pengujian, maka unregeister idling resource
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingresource)
    }

//    register idling resouce berfungsi agar uji akan menunggu hingga resource selesai, kemudian akan melanjutkan pengujian

// melakukan test pada home activity, ketika menemukan text "Cat Facts", maka pengujian dinyatakan benar
    @Test
    fun home_succesful_fetchingdata() {
        onView(withText("Cat Facts")).check(matches(isDisplayed()))
    }

//    melakukan test pada home activity, melakukan scroll kebawah hingga menemukan view holder dengan title "Dribbble"
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
//    bila text_view dengan id tv_title muncul dengan hasil "Dribbble", maka pengujian dinyatakan benar
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
//    melakukan test pada home activity namun sleep selama 1 detik kemudian
//    melakukan scroll kebawah hingga menemukan view holder dengan title "LoginRadius"
        Thread.sleep(1000)
        onView(withId(R.id.rv_main)).perform(
            RecyclerViewActions.scrollToHolder(
                withTitle<HomeAdapter.ViewHolder>(
                    "LoginRadius", R.id.tv_title
                )
            )
        )

        //    bila text_view dengan id tv_title muncul dengan hasil "LoginRadius", maka pengujian dinyatakan benar
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
        //    melakukan test pada home activity namun sleep selama 1 detik kemudian
//    melakukan scroll kebawah hingga menemukan view holder dengan title "PhishStats"
        Thread.sleep(1000)
        onView(withId(R.id.rv_main)).perform(
            RecyclerViewActions.scrollToHolder(
                withTitle<HomeAdapter.ViewHolder>(
                    "PhishStats", R.id.tv_title
                )
            )
        )

        //    bila text_view dengan id tv_title muncul dengan hasil "PhishStats", maka pengujian dinyatakan benar
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
//        melakukan test, dengan menekan fab dengan id fab_contact
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

//        setelah mengklik fab, maka akan masuk ke view baru, kemudian pengujian akan mengisi text view dengan "kevin tio"
//        kemudian mematikan soft keyboard (keyboard virtual pada handphone)
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

//        melakukan sleep selama satu detik, kemudian melakukan pencocokan item dengan fullname "Kevin Tiothanry"
        Thread.sleep(1000)
        val textView = onView(
            Matchers.allOf(
                withId(R.id.tv_fullname), withText("Kevin Tiothanry"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Kevin Tiothanry")))

//        melakukan pencocokan item dengan nomor telepon "+6282370601243",
        val textView2 = onView(
            Matchers.allOf(
                withId(R.id.tv_phoneNumber), withText("+6282370601243"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("+6282370601243")))

//        bila semua pengecekan benar, maka pengujian dinyatakan sesuai / benar
    }

    @Test
    fun checkContactFelixLiman() {
        //        melakukan test, dengan menekan fab dengan id fab_contact
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

        //        setelah mengklik fab, maka akan masuk ke view baru, kemudian pengujian akan mengisi text view dengan "Felix Liman"
//        kemudian mematikan soft keyboard (keyboard virtual pada handphone)
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

        //        melakukan sleep selama satu detik, kemudian melakukan pencocokan item dengan fullname "Felix Liman"
        Thread.sleep(1000)
        val textView = onView(
            Matchers.allOf(
                withId(R.id.tv_fullname), withText("Felix Liman"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Felix Liman")))

        //        melakukan pencocokan item dengan nomor telepon "+6285261746811",
        val textView3 = onView(
            Matchers.allOf(
                withId(R.id.tv_phoneNumber), withText("+6285261746811"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("+6285261746811")))

        //        bila semua pengecekan benar, maka pengujian dinyatakan sesuai / benar
    }

    @Test
    fun scrollToContactModem() {
//        melakukan test, dengan menekan fab dengan id fab_contact
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

//        sleep selama satu detik, melakukan scroll pada recycler view hingga menemukan item dengan full name "Modem"
        Thread.sleep(1000)
        onView(withId(R.id.rv_main)).perform(
            RecyclerViewActions.scrollToHolder(
                withTitle<ContactAdapter.ViewHolder>(
                    "Modem", R.id.tv_fullname
                )
            )
        )

//        melakukan pencocokan item dengan fullname "Modem", bila ditemukan, maka pengujian dinyatakan benar
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
        //        melakukan test, dengan menekan fab dengan id fab_contact
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

        //        setelah mengklik fab, maka akan masuk ke view baru, kemudian pengujian akan mengisi text view dengan "Kristian Antoni"
        //        kemudian mematikan soft keyboard (keyboard virtual pada handphone)
        Thread.sleep(1000)
        onView(withId(R.id.rv_main)).perform(
            RecyclerViewActions.scrollToHolder(
                withTitle<ContactAdapter.ViewHolder>(
                    "Kristian Antoni", R.id.tv_fullname
                )
            )
        )

        //        melakukan sleep selama satu detik, kemudian melakukan pencocokan item dengan fullname "Kristian Antoni"
//        bila ditemukan, maka pengujian dinyatakan benar
        val textView = onView(
            Matchers.allOf(
                withId(R.id.tv_fullname), withText("Kristian Antoni"),
                withParent(withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
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

//    function berfungsi untuk mencari item yang sesuai dengan text yang diinput
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