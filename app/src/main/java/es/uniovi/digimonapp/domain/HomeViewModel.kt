package es.uniovi.digimonapp.domain

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uniovi.digimonapp.data.Repository
import es.uniovi.digimonapp.data.ApiResult
import es.uniovi.digimonapp.model.Digimon
import es.uniovi.digimonapp.ui.AppUIState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import android.util.Log
import es.uniovi.digimonapp.data.local.FavoritesRepository
import es.uniovi.digimonapp.model.local.FavoriteDigimon

// ViewModel encargado de gestionar la lista de Digimon, su paginación, filtrado y favoritos en la pantalla principal
class HomeViewModel(private val favoriteRepository: FavoritesRepository) : ViewModel() {

    // LiveData con la lista de Digimon mostrados en la pantalla principal
    private val _digimons = MutableLiveData<MutableList<Digimon>>()
    val digimons: LiveData<MutableList<Digimon>> get() = _digimons

    // LiveData con la lista de favoritos, reactivo a cambios en la base de datos local
    val favorites = favoriteRepository.favorites

    // LiveData para el estado de la UI (cargando, éxito, error)
    private val _appUIStateObservable = MutableLiveData<AppUIState>()
    val appUIStateObservable: LiveData<AppUIState> get() = _appUIStateObservable

    // Posición de scroll para restaurar el estado de la lista
    private var scrollPosition: Int = 0

    // Variables para la paginación y filtros actuales
    private var currentPage = 0
    private val pageSize = 20
    private var lastFilters: Filters? = null

    // Clase interna para agrupar los filtros aplicados
    data class Filters(
        val name: String?,
        val attribute: String?,
        val level: String?,
        val xAntibody: Boolean?
    )

    init {
        loadDigimons()
    }

    // Carga la lista de Digimon desde la API, aplicando paginación y filtros si es necesario
    fun loadDigimons(
        page: Int = 0,
        filters: Filters? = null,
        append: Boolean = false
    ) {
        viewModelScope.launch {
            lastFilters = filters
            Repository.getDigimons(
                page = page,
                pageSize = pageSize,
                name = filters?.name,
                attribute = filters?.attribute,
                level = filters?.level,
                xAntibody = filters?.xAntibody
            )
                .map { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            val newList = result.data ?: emptyList()
                            _digimons.value = if (append) {
                                (_digimons.value ?: mutableListOf()).apply { addAll(newList) }
                            } else {
                                newList.toMutableList()
                            }
                            AppUIState.Success(result.data)
                        }
                        is ApiResult.Error -> AppUIState.Error(result.message)
                        is ApiResult.Loading -> AppUIState.Loading(true)
                    }
                }
                .collect { state ->
                    _appUIStateObservable.value = state
                }
        }
    }

    // Alterna el estado de favorito de un Digimon y actualiza la base de datos local
    @SuppressLint("ImplicitSamInstance")
    fun toggleFavorite(digimon: Digimon) {
        viewModelScope.launch {
            val isFav = favoriteRepository.isFavorite(digimon.name)
            if (isFav) {
                favoriteRepository.removeFavorite(FavoriteDigimon(digimon.name, digimon.image))
                digimon.isFavorite = false
            } else {
                favoriteRepository.addFavorite(FavoriteDigimon(digimon.name, digimon.image))
                digimon.isFavorite = true
            }
            // Actualiza la lista de Digimon en el LiveData
            _digimons.value = _digimons.value?.map {
                if (it.name == digimon.name) digimon else it
            }?.toMutableList()
        }
    }

    // Carga la siguiente página de Digimon (paginación infinita)
    fun loadNextPage() {
        currentPage++
        loadDigimons(currentPage, lastFilters, append = true)
    }

    // Aplica filtros a la búsqueda de Digimon y reinicia la paginación
    fun applyFilters(name: String?, attribute: String?, level: String?, xAntibody: Boolean?) {
        currentPage = 0
        // Limpia los filtros si son "todos" o vacíos
        val cleanedName = name?.takeIf { it.isNotBlank() }
        val cleanedAttribute = attribute?.takeIf { it.isNotBlank() && it.lowercase() != "all" }
        val cleanedLevel = level?.takeIf { it.isNotBlank() && it.lowercase() != "all" }
        loadDigimons(
            page = currentPage,
            filters = Filters(cleanedName, cleanedAttribute, cleanedLevel, xAntibody)
        )
    }

    // Actualiza el estado de favorito de los Digimon en la lista según los nombres recibidos
    fun updateFavoriteStatus(favoriteNames: List<String>) {
        _digimons.value = _digimons.value?.map { digimon ->
            digimon.copy(isFavorite = favoriteNames.contains(digimon.name))
        }?.toMutableList()
    }

    // Guarda y recupera la posición de scroll de la lista
    fun saveScrollPosition(position: Int) {
        scrollPosition = position
    }

    fun getScrollPosition(): Int {
        return scrollPosition
    }
}