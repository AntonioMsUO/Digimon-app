package es.uniovi.digimonapp.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.uniovi.digimonapp.data.local.FavoritesRepository

// Factoría para crear instancias de HomeViewModel con su repositorio de favoritos
class HomeViewModelFactory(
    private val repository: FavoritesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}