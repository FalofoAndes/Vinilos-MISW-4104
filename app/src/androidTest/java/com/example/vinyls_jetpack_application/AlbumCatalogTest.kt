package com.example.vinyls_jetpack_application


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.vinyls_jetpack_application.ui.MainActivity // Cambia según tu actividad principal
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.*

@RunWith(AndroidJUnit4::class)
class AlbumCatalogTest {

    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        // Aquí puedes inicializar datos de prueba si es necesario
    }


}
