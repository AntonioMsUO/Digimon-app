package es.uniovi.digimonapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import es.uniovi.digimonapp.model.local.FavoriteDigimon

// DAO (Data Access Object) para la entidad FavoriteDigimon
@Dao
interface FavoriteDigimonDao {

    // Obtiene todos los Digimon marcados como favoritos
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): LiveData<List<FavoriteDigimon>>

    // Añade un Digimon a favoritos o lo actualiza si ya existe
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(digimon: FavoriteDigimon)

    // Elimina un Digimon de la lista de favoritos
    @Delete
    suspend fun removeFavorite(digimon: FavoriteDigimon)

    // Comprueba si un Digimon está marcado como favorito por su nombre
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE name = :name)")
    suspend fun isFavorite(name: String): Boolean
}