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

    private val _digimons = MutableLiveData<List<Digimon>>()
    val digimons: LiveData<List<Digimon>> get() = _digimons

    private val _appUIStateObservable = MutableLiveData<AppUIState>()
    val appUIStateObservable: LiveData<AppUIState> get() = _appUIStateObservable

    init {
        fetchDigimons()
    }

    fun fetchDigimons() {
        viewModelScope.launch {
            Repository.updateDigimonData()
                .map { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _digimons.value = result.data
                            Log.d("HomeViewModel", "Datos recibidos: ${result.data}")
                            AppUIState.Success(result.data)
                        }
                        is ApiResult.Error -> {
                            Log.e("HomeViewModel", "Error: ${result.message}")
                            AppUIState.Error(result.message)
                        }
                        is ApiResult.Loading -> {
                            Log.d("HomeViewModel", "Cargando datos...")
                            AppUIState.Loading(true)
                        }
                    }
                }
                .collect { state ->
                    _appUIStateObservable.value = state
                }
        }
    }
}