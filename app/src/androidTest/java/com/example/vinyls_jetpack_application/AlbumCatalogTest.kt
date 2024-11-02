package com.example.vinyls_jetpack_application
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.example.vinilos_app.ui.MainActivity
import org.hamcrest.Matcher

@LargeTest
@RunWith(AndroidJUnit4::class)
class AlbumCatalogTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun albumInteractionScrollAndSelectAlbum() {
        // Navegar al fragmento de álbumes si es necesario
        onView(withId(R.id.selector_albums)).perform(click())

        Thread.sleep(2000)

        // Verificar que el RecyclerView esté visible
        onView(withId(R.id.recycler_view_albums)).check(matches(isDisplayed()))

        // Desplazarse automáticamente al elemento que contiene "ejemplo 2"
        onView(withId(R.id.recycler_view_albums))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(allOf(withId(R.id.album_name), withText("ejemplo 2")))
                )
            )

        // Realizar clic en el botón "Ver" dentro del mismo elemento que contiene "ejemplo 2"
        onView(withId(R.id.recycler_view_albums))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(allOf(withId(R.id.album_name), withText("ejemplo 2"))),
                    clickChildViewWithId(R.id.button_ver)
                )
            )
    }

    // ViewAction personalizado para hacer clic en un subelemento específico dentro de un ViewHolder
    private fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(View::class.java)
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v = view.findViewById<View>(id)
                v?.performClick()
            }
        }
    }

    // Función para verificar la visibilidad de un elemento en una posición específica
    private fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: return false // No hay elemento en esa posición
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }


}