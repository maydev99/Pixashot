package com.bombadu.pixashot.end2end

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.bombadu.pixashot.R
import com.bombadu.pixashot.ui.MainActivity2
import com.bombadu.pixashot.ui.adapters.ImageAdapter
import com.bombadu.pixashot.ui.adapters.SavedImagesAdapter
import com.bombadu.pixashot.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class End2EndTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity2::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun end2EndTest1() {
        //Navigate to ImageSearch Fragment
        onView(withId(R.id.searchFragment)).perform(click())

        //Enter Search
        onView(withId(R.id.image_search_edit_text)).perform(typeText("dog"))

        //Wait for images to appear from network
        Thread.sleep(3000) //Can make this better***

        //Choose 1st item in list (position 0)
        onView(withId(R.id.recycler_view))
            .perform(actionOnItemAtPosition<ImageAdapter.ImageHolder>(0, click()))

        //Enter Comments is ImageDetail Activity
        onView(withId(R.id.detail_comments_edit_text)).perform(typeText("Cool Dog")).perform(
            pressImeActionButton())

        //Enter Post by in Image Detail Activity
        onView(withId(R.id.detail_name_edit_text)).perform(typeText("Me")).perform(
            pressImeActionButton())

        //Navigate back to Saved Images Fragment
        onView(withId(R.id.savedFragment)).perform(click())

        //Click on Saved Items Recycler View to Edit Item
        onView(withId(R.id.saved_recycler_view))
            .perform(actionOnItemAtPosition<SavedImagesAdapter.SavedImageHolder>(0, click()))

        //Change Name to Michael
        onView(withId(R.id.detail_name_edit_text)).perform(clearText())
        onView(withId(R.id.detail_name_edit_text)).perform(typeText("Michael")).perform(
            pressImeActionButton()
        )

        //Navigate back to Saved Images Fragment
        onView(withId(R.id.savedFragment)).perform(click())

        //Swipe To Delete Item from SavedImage fragment
        onView(withId(R.id.saved_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SavedImagesAdapter.SavedImageHolder>(0, GeneralSwipeAction(
                Swipe.SLOW, GeneralLocation.BOTTOM_RIGHT, GeneralLocation.BOTTOM_LEFT, Press.FINGER
            ))
        )

        //Check About Dialog in Options Menu
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(withText("About")).perform(click())

        Thread.sleep(2000)
        pressBack()

    }
}