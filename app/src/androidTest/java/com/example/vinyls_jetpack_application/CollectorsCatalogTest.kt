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
class CollectorsCatalogTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun homeToCollectorsCatalogTest() {
        val linearLayout = onView(
            allOf(
                withId(R.id.selector_collectors),
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
                withId(R.id.selector_collectors),
                childAtPosition(
                    allOf(
                        withId(R.id.selectors_up),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        linearLayout2.perform(click())

        val recyclerView = onView(
            allOf(
                withId(R.id.recycler_view_collectors),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val linearLayout3 = onView(
            allOf(
                withParent(withParent(withId(R.id.recycler_view_collectors))),
                isDisplayed()
            )
        )
        linearLayout3.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.collector_name), withText("Manolo Bellon"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.collector_telephone), withText("3502457896"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(isDisplayed()))

        val textView3 = onView(
            allOf(
                withId(R.id.collector_email), withText("manollo@caracol.com.co"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(isDisplayed()))
    }

    @Test
    fun collectorsCatalogToHomeTest() {
        val linearLayout = onView(
            allOf(
                withId(R.id.selector_collectors),
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
                withId(R.id.selector_collectors),
                childAtPosition(
                    allOf(
                        withId(R.id.selectors_up),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        linearLayout2.perform(click())

        val recyclerView = onView(
            allOf(
                withId(R.id.recycler_view_collectors),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val linearLayout3 = onView(
            allOf(
                withParent(withParent(withId(R.id.recycler_view_collectors))),
                isDisplayed()
            )
        )
        linearLayout3.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.collector_name), withText("Manolo Bellon"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.collector_telephone), withText("3502457896"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(isDisplayed()))

        val textView3 = onView(
            allOf(
                withId(R.id.collector_email), withText("manollo@caracol.com.co"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(isDisplayed()))

        val linearLayout4 = onView(
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
        linearLayout4.check(matches(isDisplayed()))

        val linearLayout5 = onView(
            allOf(
                withId(R.id.selector_home),
                childAtPosition(
                    allOf(
                        withId(R.id.selectors_down),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        linearLayout5.perform(click())

        val imageView = onView(
            allOf(
                withId(R.id.central_image),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView4 = onView(
            allOf(
                withId(R.id.acerca_de_app_text), withText("Acerca de la app"),
                withParent(withParent(withId(R.id.nav_host_fragment))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Acerca de la app")))
    }

    private fun childAtPosition(
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
}