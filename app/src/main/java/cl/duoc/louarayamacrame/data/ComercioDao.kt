package cl.duoc.louarayamacrame.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ComercioDao {
    @Query("SELECT * FROM productos ORDER BY id DESC")
    fun productos(): Flow<List<ProductoEntity>>
    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun producto(id: Int): ProductoEntity?
    @Insert
    suspend fun crearProducto(item: ProductoEntity): Long
    @Update
    suspend fun editarProducto(item: ProductoEntity)
    @Query("DELETE FROM favoritos WHERE productoId = :productoId")
    suspend fun quitarFavoritosDelProducto(productoId: Int)
    @Query("DELETE FROM productos WHERE id = :productoId")
    suspend fun eliminarProducto(productoId: Int)

    @Transaction
    suspend fun eliminarProductoCompleto(productoId: Int) {
        quitarFavoritosDelProducto(productoId)
        eliminarProducto(productoId)
    }
    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contarProductos(): Int
    @Query("SELECT * FROM favoritos WHERE usuarioId = :usuarioId")
    fun favoritos(usuarioId: Int): Flow<List<FavoritoEntity>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun agregarFavorito(item: FavoritoEntity)
    @Query("DELETE FROM favoritos WHERE usuarioId = :usuarioId AND productoId = :productoId")
    suspend fun quitarFavorito(usuarioId: Int, productoId: Int)
    @Insert
    suspend fun crearSolicitud(item: SolicitudEntity): Long
    @Query("SELECT * FROM solicitudes WHERE usuarioId = :usuarioId ORDER BY creado DESC")
    fun misSolicitudes(usuarioId: Int): Flow<List<SolicitudEntity>>
    @Query("SELECT * FROM solicitudes ORDER BY creado DESC")
    fun todasSolicitudes(): Flow<List<SolicitudEntity>>
    @Query("UPDATE solicitudes SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(id: Int, estado: String)
    @Query("SELECT * FROM campanas ORDER BY id DESC")
    fun campanas(): Flow<List<CampanaEntity>>
    @Insert
    suspend fun crearCampana(item: CampanaEntity)
    @Update
    suspend fun editarCampana(item: CampanaEntity)
}
