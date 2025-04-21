package es.uniovi.digimonapp.domain

import android.app.Application
import androidx.lifecycle.*
import es.uniovi.digimonapp.data.local.AppDatabase
import es.uniovi.digimonapp.data.local.FavoritesRepository
import es.uniovi.digimonapp.model.local.FavoriteDigimon
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavoritesRepository
    val favorites: LiveData<List<FavoriteDigimon>>

    init {
        val dao = AppDatabase.getDatabase(application).favoriteDao()
        repository = FavoritesRepository(dao)
        favorites = repository.favorites
    }

    fun addFavorite(digimon: FavoriteDigimon) = viewModelScope.launch {
        repository.addFavorite(digimon)
    }

    fun removeFavorite(digimon: FavoriteDigimon) = viewModelScope.launch {
        repository.removeFavorite(digimon)
    }

    suspend fun isFavorite(name: String): Boolean {
        return repository.isFavorite(name)
    }
}
