package cl.duoc.louarayamacrame

import cl.duoc.louarayamacrame.validation.Validaciones
import org.junit.Assert.*
import org.junit.Test

class ValidacionesTest {

    @Test
    fun nombreVacioEsInvalido() {
        assertFalse(Validaciones.nombreValido(""))
    }

    @Test
    fun nombreCorrectoEsValido() {
        assertTrue(Validaciones.nombreValido("Juan Pérez"))
    }

    @Test
    fun correoSinArrobaEsInvalido() {
        assertFalse(Validaciones.correoValido("usuarioincorrecto"))
    }

    @Test
    fun correoCorrectoEsValido() {
        assertTrue(Validaciones.correoValido("prueba@gmail.com"))
    }

    @Test
    fun correoVacioEsInvalido() {
        assertFalse(Validaciones.correoValido(""))
    }

    @Test
    fun contrasenaLoginVaciaEsInvalida() {
        assertFalse(Validaciones.contrasenaLoginValida(""))
    }

    @Test
    fun contrasenaLoginIngresadaEsValida() {
        assertTrue(
            Validaciones.contrasenaLoginValida("123")
        )
    }

    @Test
    fun contrasenaRegistroCortaEsInvalida() {
        assertFalse(
            Validaciones.contrasenaRegistroValida("1234567")
        )
    }

    @Test
    fun contrasenaRegistroCorrectaEsValida() {
        assertTrue(
            Validaciones.contrasenaRegistroValida("Prueba1234")
        )
    }

    @Test
    fun contrasenasDiferentesSonInvalidas() {
        assertFalse(
            Validaciones.contrasenasCoinciden(
                "Prueba1234",
                "Prueba5678"
            )
        )
    }

    @Test
    fun contrasenasIgualesSonValidas() {
        assertTrue(
            Validaciones.contrasenasCoinciden(
                "Prueba1234",
                "Prueba1234"
            )
        )
    }

    @Test
    fun confirmacionVaciaEsInvalida() {
        assertFalse(
            Validaciones.contrasenasCoinciden(
                "Prueba1234",
                ""
            )
        )
    }
}
