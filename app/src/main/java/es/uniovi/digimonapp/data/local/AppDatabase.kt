package es.uniovi.digimonapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.uniovi.digimonapp.model.local.FavoriteDigimon

// Anotación que define la base de datos Room y sus entidades
@Database(entities = [FavoriteDigimon::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // Método abstracto para acceder al DAO de favoritos
    abstract fun favoriteDao(): FavoriteDigimonDao

    companion object {
        // Instancia única de la base de datos (patrón Singleton)
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Obtiene la instancia de la base de datos, creándola si es necesario
        fun getDatabase(context: Context): AppDatabase {
            // Si la instancia ya existe, la devuelve; si no, la crea de forma segura
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "digimon_database" // Nombre del archivo de la base de datos
                ).build().also { INSTANCE = it }
            }
        }
    }
}