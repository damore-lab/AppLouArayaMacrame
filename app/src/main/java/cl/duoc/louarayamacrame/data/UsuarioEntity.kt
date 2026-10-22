package cl.duoc.louarayamacrame.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/* revisar start
        Punto 15: conectar avatarUri con selección y edición de avatar. Guardar
        preferencias de catálogo y avisos por usuario, en campos o entidad relacionada,
        con su migración. Los permisos del dispositivo se consultan al sistema;
        no guardar una copia que pueda quedar desactualizada como estado definitivo.
 */
@Entity(
    tableName = "usuarios",
    indices = [
        Index(value = ["correo"], unique = true),
        Index(value = ["alias"], unique = true)
    ]
)
//revisar end
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val alias: String,
    val correo: String,

    val contrasenaHash: String,
    val contrasenaSalt: String,

    val rol: String = "CLIENTE",
    val avatarUri: String? = null
)
