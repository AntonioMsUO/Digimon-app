package es.uniovi.digimonapp.domain

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

class HomeViewModel : ViewModel() {

    private val _digimons = MutableLiveData<MutableList<Digimon>>()
    val digimons: LiveData<MutableList<Digimon>> get() = _digimons

    private val _appUIStateObservable = MutableLiveData<AppUIState>()
    val appUIStateObservable: LiveData<AppUIState> get() = _appUIStateObservable

    private var currentPage = 0
    private val pageSize = 20

    private var lastFilters: Filters? = null

    data class Filters(
        val name: String?,
        val attribute: String?,
        val level: String?,
        val xAntibody: Boolean?
    )

    init {
        loadDigimons()
    }

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

    fun loadNextPage() {
        currentPage++
        loadDigimons(currentPage, lastFilters, append = true)
    }

    fun applyFilters(name: String?, attribute: String?, level: String?, xAntibody: Boolean?) {
        currentPage = 0

        // Limpiar los filtros si son "todos" o vacíos
        val cleanedName = name?.takeIf { it.isNotBlank() }
        val cleanedAttribute = attribute?.takeIf { it.isNotBlank() && it.lowercase() != "all" }
        val cleanedLevel = level?.takeIf { it.isNotBlank() && it.lowercase() != "all" }

        loadDigimons(
            page = currentPage,
            filters = Filters(cleanedName, cleanedAttribute, cleanedLevel, xAntibody)
        )
    }

}
