package com.example.vinyls_jetpack_application

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.Test
import java.io.File

class RipperTest {

    @Test
    fun ripperTest() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Obtener el contexto de la aplicación y crear un directorio para capturas
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val screenshotDir = File(context.filesDir, "test_screenshots")
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs()
        }

        // Abre la aplicación desde el launcher
        device.pressHome()
        val appIcon = device.findObject(UiSelector().description("Vinyls_Jetpack_Application"))
        if (appIcon.exists()) {
            appIcon.clickAndWaitForNewWindow()
        } else {
            println("No se encontró el icono de la aplicación. Finalizando prueba.")
            return
        }

        // Encuentra y recorre elementos interactivos
        val elementCount = 20 // Define un número más alto para exploración amplia
        for (i in 0 until elementCount) {
            try {
                val element = device.findObject(UiSelector().instance(i))
                if (element.exists() && element.isEnabled) {
                    println("Interactuando con el elemento: ${element.className}")

                    // Realiza la acción dependiendo del tipo de elemento
                    when {
                        element.className.contains("Button", ignoreCase = true) -> {
                            element.clickAndWaitForNewWindow()
                        }
                        element.className.contains("EditText", ignoreCase = true) -> {
                            element.text = "Texto de prueba"
                        }
                        else -> {
                            element.click()
                        }
                    }

                    // Captura de pantalla después de la interacción
                    val screenshot = File(screenshotDir, "ripper_test_step_$i.png")
                    device.takeScreenshot(screenshot)
                } else {
                    println("Elemento no interactivo o inexistente en la posición: $i")
                }
            } catch (e: Exception) {
                println("Error interactuando con el elemento: ${e.message}")
            }
        }

        // Captura final de la pantalla al terminar la exploración
        val finalScreenshot = File(screenshotDir, "ripper_test_final.png")
        device.takeScreenshot(finalScreenshot)

        println("Prueba de ripper testing completada. Las capturas de pantalla están en: ${screenshotDir.absolutePath}")
    }
}