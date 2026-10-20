package cl.duoc.louarayamacrame.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [
        Index(value = ["correo"], unique = true),
        Index(value = ["alias"], unique = true)
    ]
)
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
