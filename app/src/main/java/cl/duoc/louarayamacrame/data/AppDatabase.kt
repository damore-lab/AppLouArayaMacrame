package cl.duoc.louarayamacrame.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UsuarioEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerBaseDatos(
            context: Context
        ): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lou_araya_macrame_db"
                ).build()

                INSTANCE = instancia

                instancia
            }
        }
    }
}
