package cl.duoc.louarayamacrame.model

/* revisar start
        Puntos 7, 8 y 22: comprobar las referencias a este modelo y a ProductoEntity.
        Si se conserva como modelo de dominio, definir el mapeo y utilizar sus medidas
        estructuradas; si no tiene uso, retirarlo para evitar dos definiciones divergentes.
 */
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
//revisar end
