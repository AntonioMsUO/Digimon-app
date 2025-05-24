package es.uniovi.digimonapp.domain

import android.app.Application
import androidx.lifecycle.*
import es.uniovi.digimonapp.data.local.FavoritesRepository
import es.uniovi.digimonapp.model.local.FavoriteDigimon
import kotlinx.coroutines.launch

// ViewModel encargado de gestionar la lista de Digimon favoritos y exponerla a la UI
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    // Repositorio de favoritos que maneja la lógica de acceso a datos locales
    private val repository: FavoritesRepository = FavoritesRepository.getInstance(application)

    // LiveData con la lista de Digimon favoritos, reactivo a cambios en la base de datos
    val favorites: LiveData<List<FavoriteDigimon>> = repository.favorites

    // Añade un Digimon a la lista de favoritos
    fun addFavorite(digimon: FavoriteDigimon) = viewModelScope.launch {
        repository.addFavorite(digimon)
    }

    // Elimina un Digimon de la lista de favoritos
    fun removeFavorite(digimon: FavoriteDigimon) = viewModelScope.launch {
        repository.removeFavorite(digimon)
    }

    // Comprueba si un Digimon está marcado como favorito por su nombre
    suspend fun isFavorite(name: String): Boolean {
        return repository.isFavorite(name)
    }
}