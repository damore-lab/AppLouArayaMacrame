package cl.duoc.louarayamacrame.validation

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

/* revisar start
        Puntos 6, 7 y 8: centralizar reglas y resultados de error por campo para
        productos, cotizaciones y campañas. Validar medidas positivas con unidades,
        precio representable y mayor que cero, fechas y orden de inicio y término.
        Compartir las reglas con los ViewModels para evitar validaciones distintas
        entre lo que muestra el formulario y lo que se permite guardar.
 */
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
//revisar end
