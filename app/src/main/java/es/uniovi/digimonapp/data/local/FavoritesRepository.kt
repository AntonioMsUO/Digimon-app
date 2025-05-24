package es.uniovi.digimonapp.data.local

import android.content.Context
import androidx.lifecycle.LiveData
import es.uniovi.digimonapp.model.local.FavoriteDigimon

// Repositorio para gestionar operaciones sobre Digimon favoritos en la base de datos local
class FavoritesRepository(context: Context) {

    // Acceso al DAO de favoritos a través de la base de datos
    val favoriteDao = AppDatabase.getDatabase(context).favoriteDao()

    // LiveData con la lista de todos los favoritos, se actualiza automáticamente
    val favorites: LiveData<List<FavoriteDigimon>> = favoriteDao.getAllFavorites()

    // Añade un Digimon a favoritos (o lo actualiza si ya existe)
    suspend fun addFavorite(favorite: FavoriteDigimon) {
        favoriteDao.addFavorite(favorite)
    }

    // Elimina un Digimon de la lista de favoritos
    suspend fun removeFavorite(favorite: FavoriteDigimon) {
        favoriteDao.removeFavorite(favorite)
    }

    // Comprueba si un Digimon está marcado como favorito por su nombre
    suspend fun isFavorite(name: String): Boolean {
        return favoriteDao.isFavorite(name)
    }

    companion object {
        // Instancia única del repositorio (patrón Singleton)
        @Volatile
        var INSTANCE: FavoritesRepository? = null

        // Obtiene la instancia del repositorio, creándola si es necesario
        fun getInstance(context: Context): FavoritesRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = FavoritesRepository(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}