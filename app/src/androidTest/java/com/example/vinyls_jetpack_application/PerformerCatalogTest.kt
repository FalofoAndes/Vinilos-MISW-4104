package com.example.vinyls_jetpack_application

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.contrib.RecyclerViewActions
import com.example.vinilos_app.models.Performer
import com.example.vinilos_app.ui.MainActivity
import com.example.vinilos_app.ui.adapter.PerformerAdapter
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matcher

@RunWith(AndroidJUnit4::class)
class PerformerCatalogTest {

    private lateinit var adapter: PerformerAdapter

    @Before
    fun setup() {
        // Crea un adaptador de prueba sin necesidad de un RecyclerView
        adapter = PerformerAdapter(emptyList()) {}
    }

    @Test
    fun performersSortedByname(){
        // Crea una lista de performers desordenados
        val performers = listOf(
            Performer(102, "Carlos Santana", "image_url", "Description", "1947-07-20", "1947-07-20"),
            Performer(103, "Rubén Blades", "image_url", "Description", "1948-07-16", "1948-07-16" ),
            Performer(104, "Juan Luis Guerra", "image_url", "Description", "1957-06-07", "1957-06-07")
        )

        // Actualiza el adaptador con la lista desordenada
        adapter.updatePerformers(performers)

        // Obtiene la lista ordenada y verifica el orden
        val sortedPerformers = adapter.getPerformers()
        assertEquals("Carlos Santana", sortedPerformers[0].name)
        assertEquals("Juan Luis Guerra", sortedPerformers[1].name)
        assertEquals("Rubén Blades", sortedPerformers[2].name)
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testDisplayArtistsListAlphabetically() {
        // Navegar a la sección de artistas
        onView(withId(R.id.selector_artists)).perform(ViewActions.click())

        // Verificar que el RecyclerView de artistas esté visible
        onView(withId(R.id.recycler_view_performers)).check(matches(isDisplayed()))

        // Obtener los nombres de los artistas del RecyclerView
        val artistNames = mutableListOf<String>()
        onView(withId(R.id.recycler_view_performers))
            .perform(object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return withId(R.id.recycler_view_performers) // Sin tipo genérico
                }

                override fun perform(uiController: UiController?, view: View?) {
                    // Obtener los nombres de los artistas de cada item en el RecyclerView
                    val recyclerView = view as RecyclerView
                    for (i in 0 until recyclerView.childCount) {
                        val itemView = recyclerView.getChildAt(i)
                        val textView = itemView.findViewById<TextView>(R.id.text_performer_name)
                        artistNames.add(textView.text.toString())
                    }
                }

                override fun getDescription(): String {
                    return "Obtener nombres de los artistas del RecyclerView"
                }
            })

        // Verificar que la lista de nombres está ordenada alfabéticamente
        val sortedArtistNames = artistNames.sorted()
        assertEquals("La lista de artistas no está ordenada alfabéticamente", sortedArtistNames, artistNames)
    }


    @Test
    fun testScrollToLastArtistAndCheckDetails() {
        // Navegar a la sección de artistas
        onView(withId(R.id.selector_artists)).perform(ViewActions.click())
        Thread.sleep(2000)

        // Desplazarse automáticamente al elemento que contiene el nombre "Shakira Isabel Mebarak Ripoll"
        onView(withId(R.id.recycler_view_performers))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(allOf(withId(R.id.text_performer_name), withText("Shakira Isabel Mebarak Ripoll")))
                )
            )

        // Verificar que el nombre del artista está visible
        onView(withId(R.id.recycler_view_performers))
            .check(matches(hasDescendant(allOf(withId(R.id.text_performer_name), withText("Shakira Isabel Mebarak Ripoll")))))

        // Verificar que el resto de la información (descripción y botón) también están visibles
        onView(withId(R.id.recycler_view_performers))
            .check(matches(hasDescendant(withId(R.id.text_performer_description))))
        onView(withId(R.id.recycler_view_performers))
            .check(matches(hasDescendant(withId(R.id.button_ver))))
    }


}
