package cl.duoc.louarayamacrame.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarUsuario(
        usuario: UsuarioEntity
    ): Long

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(
        correo: String
    ): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE alias = :alias LIMIT 1")
    suspend fun buscarPorAlias(
        alias: String
    ): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(
        id: Int
    ): UsuarioEntity?

    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    suspend fun obtenerUsuarios(): List<UsuarioEntity>

    @Update
    suspend fun actualizarUsuario(usuario: UsuarioEntity): Int
}
