package com.example.vinyls_jetpack_application
import android.view.View
import android.view.ViewGroup
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
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf

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

    @Test
    fun verifyAlbumFieldsExistenceInFirstAlbum() {
        // Navegar al fragmento de álbumes
        onView(withId(R.id.selector_albums)).perform(click())
    
        Thread.sleep(2000)
    
        // Verificar que el RecyclerView esté visible
        onView(withId(R.id.recycler_view_albums)).check(matches(isDisplayed()))
    
        // Verificar la existencia de los campos en el primer álbum (posición 0)
        onView(withId(R.id.recycler_view_albums))
            .check(matches(atPosition(0, hasDescendant(withId(R.id.album_name)))))
        onView(withId(R.id.recycler_view_albums))
            .check(matches(atPosition(0, hasDescendant(withId(R.id.album_release_date)))))
        onView(withId(R.id.recycler_view_albums))
            .check(matches(atPosition(0, hasDescendant(withId(R.id.album_description)))))
    }

    @Test
    fun retornoPantallaPrincipalTest() {
        val linearLayout = onView(
            allOf(
                withId(R.id.selector_albums),
                withParent(
                    allOf(
                        withId(R.id.selectors_up),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val linearLayout2 = onView(
            allOf(
                withId(R.id.selector_albums),
                childAtPositionBackHome(
                    allOf(
                        withId(R.id.selectors_up),
                        childAtPositionBackHome(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        linearLayout2.perform(click())

        val linearLayout3 = onView(
            allOf(
                withId(R.id.selector_home),
                withParent(
                    allOf(
                        withId(R.id.selectors_down),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        linearLayout3.check(matches(isDisplayed()))

        val linearLayout4 = onView(
            allOf(
                withId(R.id.selector_home),
                childAtPositionBackHome(
                    allOf(
                        withId(R.id.selectors_down),
                        childAtPositionBackHome(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        linearLayout4.perform(click())
    }

    private fun childAtPositionBackHome(
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