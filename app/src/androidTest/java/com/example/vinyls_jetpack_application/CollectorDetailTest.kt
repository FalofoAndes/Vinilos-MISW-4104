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
import com.example.vinilos_app.network.EspressoIdlingResource
import com.example.vinilos_app.ui.MainActivity
import org.junit.*
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CollectorDetailTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        // Registrar Espresso Idling Resource
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource())
    }

    @After
    fun tearDown() {
        // Desregistrar Espresso Idling Resource
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource())
    }

    @Test
    fun collectorsCatalogToFirstCollectorDetailTest() {
        // Navegar al catálogo de coleccionistas
        onView(withId(R.id.selector_collectors))
            .perform(click())



        // Verificar que el RecyclerView de coleccionistas está visible
        onView(withId(R.id.recycler_view_collectors))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(1))) // Verificar que hay al menos un elemento en la lista

        // Interactuar con el primer elemento del RecyclerView
        onView(withId(R.id.recycler_view_collectors))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Verificar que los detalles del coleccionista están visibles
        onView(withId(R.id.collector_name))
            .check(matches(isDisplayed()))
            .check(matches(withText("Manolo Bellon")))

        onView(withId(R.id.collector_email))
            .check(matches(isDisplayed()))
            .check(matches(withText("manollo@caracol.com.co")))

        onView(withId(R.id.collector_telephone))
            .check(matches(isDisplayed()))
            .check(matches(withText("3502457896")))

        // Regresar al Home
        onView(withId(R.id.selector_home))
            .perform(click())



        // Verificar que hemos regresado al Home
        onView(withId(R.id.central_image))
            .check(matches(isDisplayed()))
    }

    @Test
    fun collectorsCatalogToLastCollectorDetailTest() {
        // Navegar al catálogo de coleccionistas
        onView(withId(R.id.selector_collectors))
            .perform(click())

        // Verificar que el RecyclerView de coleccionistas está visible
        onView(withId(R.id.recycler_view_collectors))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(1))) // Verificar que hay al menos un elemento en la lista

        // Obtener el último elemento del RecyclerView
        val lastPosition = getLastItemPosition()

        // Interactuar con el último elemento
        onView(withId(R.id.recycler_view_collectors))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    lastPosition, click()
                )
            )

        // Verificar que los detalles del coleccionista están visibles
        onView(withId(R.id.collector_name))
            .check(matches(isDisplayed()))

        onView(withId(R.id.collector_email))
            .check(matches(isDisplayed()))

        onView(withId(R.id.collector_telephone))
            .check(matches(isDisplayed()))

        // Regresar al catálogo de coleccionistas
        onView(withId(R.id.selector_collectors))
            .perform(click())

        // Verificar que hemos regresado al catálogo
        onView(withId(R.id.recycler_view_collectors))
            .check(matches(isDisplayed()))
    }

    /**
     * Función auxiliar para obtener la última posición del RecyclerView.
     */
    private fun getLastItemPosition(): Int {
        var itemCount = 0
        mActivityScenarioRule.scenario.onActivity { activity ->
            val recyclerView = activity.findViewById<RecyclerView>(R.id.recycler_view_collectors)
            itemCount = recyclerView?.adapter?.itemCount ?: 0
        }
        return itemCount - 1 // Devuelve la última posición
    }
}
