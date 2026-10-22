package cl.duoc.louarayamacrame.validation

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/* revisar start
        Puntos 6, 7 y 20: ampliar casos de dimensiones y unidades inválidas, precio
        fuera de rango, error por campo y períodos de campañas invertidos o inválidos.
        Añadir en archivos propios pruebas de ViewModels con fallos de guardado,
        doble envío, permisos por rol y transiciones de estado; no limitarse a validar textos.
 */
class ValidacionesComercioTest {
    @Test
    fun productoRequierePrecioPositivo() {
        assertFalse(
            ValidacionesComercio.productoValido(
                "Tapiz",
                "Decoración",
                "Algodón",
                "90 cm",
                0
            )
        )
        assertTrue(
            ValidacionesComercio.productoValido(
                "Tapiz",
                "Decoración",
                "Algodón",
                "90 cm",
                10000
            )
        )
    }

    @Test
    fun solicitudRequiereCampos() {
        val fecha = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))
        assertFalse(ValidacionesComercio.cotizacionValida("", "90 cm", "Algodón", "Beige", fecha))
        assertTrue(
            ValidacionesComercio.cotizacionValida(
                "Boda",
                "90 cm",
                "Algodón",
                "Beige",
                fecha
            )
        )
    }

    @Test
    fun fechasInvalidasSonRechazadas() {
        assertFalse(ValidacionesComercio.fechaValida("31/02/2028"))
        assertFalse(ValidacionesComercio.fechaValida("mañana"))
        assertFalse(ValidacionesComercio.fechaValida("01/01/2020"))
    }

    @Test
    fun productoRechazaCamposVacios() {
        assertFalse(
            ValidacionesComercio.productoValido(
                "", "Decoración", "Algodón", "90 cm", 10000
            )
        )

        assertFalse(
            ValidacionesComercio.productoValido(
                "Tapiz", "", "Algodón", "90 cm", 10000
            )
        )

        assertFalse(
            ValidacionesComercio.productoValido(
                "Tapiz", "Decoración", "", "90 cm", 10000
            )
        )

        assertFalse(
            ValidacionesComercio.productoValido(
                "Tapiz", "Decoración", "Algodón", "", 10000
            )
        )
    }

    @Test
    fun productoRechazaPrecioNegativo() {
        assertFalse(
            ValidacionesComercio.productoValido(
                "Tapiz", "Decoración", "Algodón", "90 cm", -5000
            )
        )
    }

    @Test
    fun cotizacionRechazaCamposVacios() {
        val fecha = LocalDate.now().plusDays(5)
            .format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))

        assertFalse(
            ValidacionesComercio.cotizacionValida(
                "Boda", "", "Algodón", "Beige", fecha
            )
        )

        assertFalse(
            ValidacionesComercio.cotizacionValida(
                "Boda", "90 cm", "", "Beige", fecha
            )
        )

        assertFalse(
            ValidacionesComercio.cotizacionValida(
                "Boda", "90 cm", "Algodón", "", fecha
            )
        )
    }

    @Test
    fun cotizacionRechazaFechaInvalida() {
        assertFalse(
            ValidacionesComercio.cotizacionValida(
                "Boda", "90 cm", "Algodón", "Beige", "31/02/2028"
            )
        )
    }
}
//revisar end
