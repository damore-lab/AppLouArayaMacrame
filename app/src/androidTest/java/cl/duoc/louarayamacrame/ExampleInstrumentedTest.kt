package cl.duoc.louarayamacrame

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/* revisar start
        Punto 20: esta prueba solo comprueba el paquete. Crear pruebas instrumentadas
        propias para aislamiento entre usuarios, migraciones y persistencia en Room,
        historial tras desactivar productos y restauración de sesión, selección y formularios.
        Punto 21: complementar con comprobaciones en dispositivo de cámara, ubicación
        y QR, incluyendo permisos denegados, cancelación y recursos no disponibles.
        Registrar por separado qué se automatizó y qué se verificó manualmente.
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext =
            InstrumentationRegistry.getInstrumentation().targetContext

        assertEquals(
            "cl.duoc.louarayamacrame",
            appContext.packageName
        )
    }
}
//revisar end
