package cl.duoc.louarayamacrame.data

import androidx.room.*

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String, val descripcion: String, val categoria: String,
    val material: String, val dimensiones: String, val precio: Int,
    val disponible: Boolean = true, val imagenUri: String? = null
)

@Entity(tableName = "favoritos", primaryKeys = ["usuarioId", "productoId"])
data class FavoritoEntity(val usuarioId: Int, val productoId: Int)

@Entity(tableName = "solicitudes")
data class SolicitudEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int, val productoId: Int?, val evento: String,
    val medidas: String, val material: String, val colores: String,
    val fecha: String, val observaciones: String,
    val estado: String = "Pendiente", val creado: Long = System.currentTimeMillis()
)

@Entity(tableName = "campanas")
data class CampanaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String, val descripcion: String, val fecha: String, val activa: Boolean = true
)
