package cl.duoc.louarayamacrame.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/* revisar start
        Puntos 4, 12, 13, 14, 15, 16, 17 y 18: registrar las entidades y DAO nuevos
        de colecciones, asociaciones, avisos, preferencias, eventos e historial QR.
        Incrementar la versión y crear migraciones que conserven los usuarios,
        productos y solicitudes actuales al agregar o transformar campos.
        Puntos 1 y 8: ubicar la inicialización de datos de demostración en la capa
        de datos y definir almacenamiento local de sesión según su duración prevista.
        El estado de sesión no debe depender de variables remember de navegación.
 */
@Database(
    entities = [UsuarioEntity::class, ProductoEntity::class, FavoritoEntity::class,
        SolicitudEntity::class, CampanaEntity::class], version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun comercioDao(): ComercioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `productos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `categoria` TEXT NOT NULL, `material` TEXT NOT NULL, `dimensiones` TEXT NOT NULL, `precio` INTEGER NOT NULL, `disponible` INTEGER NOT NULL, `imagenUri` TEXT)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `favoritos` (`usuarioId` INTEGER NOT NULL, `productoId` INTEGER NOT NULL, PRIMARY KEY(`usuarioId`, `productoId`))")
                db.execSQL("CREATE TABLE IF NOT EXISTS `solicitudes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `usuarioId` INTEGER NOT NULL, `productoId` INTEGER, `evento` TEXT NOT NULL, `medidas` TEXT NOT NULL, `material` TEXT NOT NULL, `colores` TEXT NOT NULL, `fecha` TEXT NOT NULL, `observaciones` TEXT NOT NULL, `estado` TEXT NOT NULL, `creado` INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `campanas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `titulo` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `fecha` TEXT NOT NULL, `activa` INTEGER NOT NULL)")
            }
        }

        fun obtenerBaseDatos(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java,
                "lou_araya_macrame_db"
            ).addMigrations(MIGRACION_1_2).build().also { INSTANCE = it }
        }
    }
}
//revisar end
