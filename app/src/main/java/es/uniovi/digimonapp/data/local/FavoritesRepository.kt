package es.uniovi.digimonapp.data.local

import android.content.Context
import androidx.lifecycle.LiveData
import es.uniovi.digimonapp.model.local.FavoriteDigimon

class FavoritesRepository private constructor(context: Context) {

    private val favoriteDao = AppDatabase.getDatabase(context).favoriteDao()


    val favorites: LiveData<List<FavoriteDigimon>> = favoriteDao.getAllFavorites()

    suspend fun addFavorite(favorite: FavoriteDigimon) {
        favoriteDao.addFavorite(favorite)
    }

    suspend fun removeFavorite(favorite: FavoriteDigimon) {
        favoriteDao.removeFavorite(favorite)
    }

    suspend fun isFavorite(name: String): Boolean {
        return favoriteDao.isFavorite(name)
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoritesRepository? = null

        fun getInstance(context: Context): FavoritesRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = FavoritesRepository(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
