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
class AlbumDetailTest {

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
    fun albumDetailToHome() {
        // Navegar a la sección de álbumes
        onView(withId(R.id.selector_albums))
            .check(matches(isDisplayed()))
            .perform(click())

        // Hacer clic en el botón "Ver" del primer álbum
        onView(withId(R.id.recycler_view_albums))
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
    fun albumDetailToAlbumCatalog() {
        // Navegar a la sección de álbumes
        onView(withId(R.id.selector_albums))
            .check(matches(isDisplayed()))
            .perform(click())

        // Hacer clic en el botón "Ver" del primer álbum
        onView(withId(R.id.recycler_view_albums))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.button_ver)))

        // Volver al catálogo de álbumes
        onView(withId(R.id.selector_albums))
            .check(matches(isDisplayed()))
            .perform(click())

        // Verificar que la portada del álbum está visible
        onView(withId(R.id.recycler_view_albums))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(hasDescendant(withId(R.id.album_cover))))
    }

    @Test
    fun albumCatalogToAlbumDetail() {
        // Navegar a la sección de álbumes
        onView(withId(R.id.selector_albums))
            .check(matches(isDisplayed()))
            .perform(click())

        // Hacer clic en el botón "Ver" del primer álbum
        onView(withId(R.id.recycler_view_albums))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.button_ver)))

        // Verificar detalles del álbum
        onView(withId(R.id.album_name))
            .check(matches(withText("Buscando América")))
            .check(matches(isDisplayed()))

        onView(withId(R.id.album_release_date))
            .check(matches(withText("1984-08-01T00:00:00.000Z")))
            .check(matches(isDisplayed()))

        onView(withId(R.id.album_cover))
            .check(matches(isDisplayed()))

        onView(withId(R.id.album_description))
            .check(matches(withText("Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983.")))
            .check(matches(isDisplayed()))

        // Verificar que el RecyclerView de tracks está visible
        onView(withId(R.id.recycler_view_tracks))
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
