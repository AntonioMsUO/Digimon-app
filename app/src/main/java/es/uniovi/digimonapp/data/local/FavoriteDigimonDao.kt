package es.uniovi.digimonapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import es.uniovi.digimonapp.model.local.FavoriteDigimon

@Dao
interface FavoriteDigimonDao {

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): LiveData<List<FavoriteDigimon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(digimon: FavoriteDigimon)

    @Delete
    suspend fun removeFavorite(digimon: FavoriteDigimon)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE name = :name)")
    suspend fun isFavorite(name: String): Boolean
}
