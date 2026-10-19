package cl.duoc.louarayamacrame.model

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val material: String,
    val anchoCm: Double,
    val largoCm: Double,
    val precio: Int,
    val disponible: Boolean
)