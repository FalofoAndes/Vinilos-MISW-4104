package com.example.vinyls_jetpack_application

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
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
class PerformerDetailTest {

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
    fun performerDetailToHome() {
        // Navegar a la sección de artistas
        onView(withId(R.id.selector_artists))
            .check(matches(isDisplayed()))
            .perform(click())

        // Hacer clic en el botón "Ver" del primer artista
        onView(withId(R.id.recycler_view_performers))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.button_ver)))

        // Navegar de vuelta al Home
        onView(withId(R.id.selector_home))
            .check(matches(isDisplayed()))
            .perform(click())

        // Verificar que estamos en la pantalla principal
        onView(withId(R.id.central_image))
            .check(matches(isDisplayed()))

        onView(withId(R.id.acerca_de_app_text))
            .check(matches(withText("Acerca de la app")))
    }

    @Test
    fun performerDetailToAlbumCatalog() {
        // Navegar a la sección de artistas
        onView(withId(R.id.selector_artists))
            .check(matches(isDisplayed()))
            .perform(click())

        // Hacer clic en el botón "Ver" del primer artista
        onView(withId(R.id.recycler_view_performers))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.button_ver)))

        // Volver al catálogo de álbumes
        onView(withId(R.id.selector_artists))
            .check(matches(isDisplayed()))
            .perform(click())

        // Verificar que la portada del artista está visible
        onView(withId(R.id.recycler_view_performers))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(hasDescendant(withId(R.id.image_performer))))
    }

    @Test
    fun performerCatalogToPerformerDetail() {
        // Navegar a la sección de artistas
        onView(withId(R.id.selector_artists))
            .check(matches(isDisplayed()))
            .perform(click())

        // Hacer clic en el botón "Ver" del primer artista
        onView(withId(R.id.recycler_view_performers))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.button_ver)))

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
