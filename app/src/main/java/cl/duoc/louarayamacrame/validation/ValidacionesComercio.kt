package cl.duoc.louarayamacrame.validation

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

object ValidacionesComercio {
    private val formato =
        DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)

    fun fechaValida(fecha: String): Boolean = try {
        !LocalDate.parse(fecha, formato).isBefore(LocalDate.now())
    } catch (_: Exception) {
        false
    }

    fun cotizacionValida(
        evento: String,
        medidas: String,
        material: String,
        colores: String,
        fecha: String
    ): Boolean =
        listOf(evento, medidas, material, colores).all { it.isNotBlank() } && fechaValida(fecha)

    fun productoValido(
        nombre: String,
        descripcion: String,
        material: String,
        dimensiones: String,
        precio: Int
    ): Boolean =
        listOf(nombre, descripcion, material, dimensiones).all { it.isNotBlank() } && precio > 0
}
