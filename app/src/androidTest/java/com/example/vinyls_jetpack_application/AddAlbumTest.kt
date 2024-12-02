package com.example.vinyls_jetpack_application

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vinilos_app.network.EspressoIdlingResource
import com.example.vinilos_app.ui.MainActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddAlbumTest {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun addAlbumCancelButton() {
        // Navegar a la sección de álbumes
        onView(withId(R.id.selector_albums))
            .check(matches(isDisplayed()))
            .perform(click())

        // Navegar de vuelta al Home
        onView(withId(R.id.fab_add_album)).perform(click())

        // Type the album name
        onView(withId(R.id.albumName)).check(matches(isDisplayed())).perform(typeText("New Album"))

        // Confirm the addition
        onView(withId(R.id.cancelAlbum)).perform(click())

        // Check if the new album is displayed in the list
        onView(withId(R.id.selector_albums))
            .check(matches(isDisplayed()))

    }
    @Test
    fun addAlbumFullForm() {
        // Navegar a la sección de álbumes
        onView(withId(R.id.selector_albums))
            .check(matches(isDisplayed()))
            .perform(click())

        // Navegar de vuelta al Home
        onView(withId(R.id.fab_add_album)).perform(click())

        // Type the album name
        onView(withId(R.id.albumName)).check(matches(isDisplayed())).perform(typeText("New Album"))
        onView(withId(R.id.albumCover)).perform(typeText("http://example.com/cover.jpg"))
        onView(withId(R.id.albumReleaseDate)).perform(typeText("2024-11-01"))
        onView(withId(R.id.albumDescription)).perform(typeText("This is a new album"))

        // Confirm the addition
        onView(withId(R.id.saveAlbum)).perform(click())

        onView(withId(R.id.selector_albums))
            .check(matches(isDisplayed()))

    }

    // Matcher personalizado para clics en hijos específicos de un item del RecyclerView
    private fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isDisplayed()
            override fun getDescription(): String = "Click on a child view with specified id."
            override fun perform(uiController: UiController?, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }
}
