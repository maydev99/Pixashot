package com.bombadu.pixashot.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.bombadu.pixashot.R
import com.bombadu.pixashot.ui.adapters.ImageAdapter
import com.bombadu.pixashot.ui.adapters.SavedImagesAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivity2Test {

    @Before
    fun setup() {
        val activityScenario = ActivityScenario.launch((MainActivity2::class.java))
    }


    //Check if Views are Displayed
    @Test
    fun isActivityInView() {
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()))
    }

    @Test
    fun isBottomNavInView() {
        onView((withId(R.id.bottom_nav))).check(matches(isDisplayed()))
    }

    @Test
    fun isSaveImagesFragmentInView() {
        onView(withId(R.id.saved_images_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun isSearchFragmentInView() {
        onView(withId(R.id.searchFragment)).check(matches(isDisplayed()))
    }

    //Navigation Testing

    @Test
    fun navigateToSearchFragment() {
        onView(withId(R.id.searchFragment)).perform(click())
        onView(withId(R.id.image_search_edit_text)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToSearchFragmentThenBackToSavedFragment() {
        onView(withId(R.id.searchFragment)).perform(click())
        onView(withId(R.id.image_search_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.savedFragment)).perform(click())
        onView(withId(R.id.saved_recycler_view)).check(matches(isDisplayed()))
    }



    @Test
    fun recyclerviewItemClick() {
        onView(withId(R.id.saved_recycler_view))
            .check(matches(isDisplayed()))
        onView(withId(R.id.saved_recycler_view))
            .perform(actionOnItemAtPosition<SavedImagesAdapter.SavedImageHolder>(0, click()))
    }

    //Test Options Menu
    @Test
    fun testOptionsMenu(){
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(withText("About"))
            .perform(click())

        pressBack()

    }

    //Testing Search Fragment
    @Test
    fun testSearchEntry() {
        onView(withId(R.id.searchFragment)).perform(click())
        onView(withId(R.id.image_search_edit_text)).perform(typeText("hat"))

        Thread.sleep(3000)

        onView(withId(R.id.recycler_view))
            .perform(actionOnItemAtPosition<ImageAdapter.ImageHolder>(0, click()))
    }

}