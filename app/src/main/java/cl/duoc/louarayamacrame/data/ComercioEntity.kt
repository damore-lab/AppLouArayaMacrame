package cl.duoc.louarayamacrame.data

import androidx.room.*

/* revisar start
        Puntos 4 y 7: separar producto activo de disponibilidad o confección a pedido;
        definir medidas positivas y sus unidades sin perder los datos existentes.
 */
@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String, val descripcion: String, val categoria: String,
    val material: String, val dimensiones: String, val precio: Int,
    val disponible: Boolean = true, val imagenUri: String? = null
)

//revisar end

@Entity(tableName = "favoritos", primaryKeys = ["usuarioId", "productoId"])
data class FavoritoEntity(val usuarioId: Int, val productoId: Int)

/* revisar start
        Puntos 4 y 18: conservar una copia de los datos relevantes del producto,
        total acordado y estados definidos. Distinguir fecha de creación de fecha
        requerida y mantener cada solicitud asociada a su cliente local.
 */
@Entity(tableName = "solicitudes")
data class SolicitudEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int, val productoId: Int?, val evento: String,
    val medidas: String, val material: String, val colores: String,
    val fecha: String, val observaciones: String,
    val estado: String = "Pendiente", val creado: Long = System.currentTimeMillis()
)

//revisar end

/* revisar start
        Puntos 12 y 13: sustituir vigencia libre por inicio y término estructurados
        y relacionar campañas con productos o colecciones. Crear entidades locales
        de colección y sus asociaciones, preferentemente en archivos propios.
        Puntos 14, 16 y 17: agregar entidades para avisos por usuario con estado leído
        y destino, eventos con fechas y coordenadas, e historial de escaneos por usuario.
 */
@Entity(tableName = "campanas")
data class CampanaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String, val descripcion: String, val fecha: String, val activa: Boolean = true
)
//revisar end
