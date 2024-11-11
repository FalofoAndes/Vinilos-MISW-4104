package com.example.vinyls_jetpack_application
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.example.vinilos_app.network.EspressoIdlingResource
import com.example.vinilos_app.ui.MainActivity
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before

@LargeTest
@RunWith(AndroidJUnit4::class)
class CollectorsCatalogTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource())
    }

    @Test
    fun homeToCollectorsCatalogTest() {
        onView(withId(R.id.selector_collectors))
            .perform(click())

        onView(withId(R.id.recycler_view_collectors))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(1)))  // Verifica que al menos hay un ítem cargado

        onView(withText("Manolo Bellon"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun collectorsCatalogToHomeTest() {
        // Navegar al catálogo de coleccionistas
        onView(withId(R.id.selector_collectors))
            .perform(click())

        // Verificar que el RecyclerView de coleccionistas está visible
        onView(withId(R.id.recycler_view_collectors))
            .check(matches(isDisplayed()))

        // Interactuar con un elemento específico del RecyclerView
        onView(withId(R.id.recycler_view_collectors))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Verificar que el nombre del primer coleccionista es "Manolo Bellon"
        onView(allOf(withId(R.id.collector_name), withText("Manolo Bellon")))
            .check(matches(isDisplayed()))

        // Navegar de vuelta a la pantalla principal
        onView(withId(R.id.selector_home))
            .perform(click())

        // Verificar que se ha vuelto a la pantalla principal
        onView(withId(R.id.central_image))
            .check(matches(isDisplayed()))

        onView(withId(R.id.acerca_de_app_text))
            .check(matches(withText("Acerca de la app")))
    }
}