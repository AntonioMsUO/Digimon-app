package es.uniovi.digimonapp.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uniovi.digimonapp.data.Repository
import es.uniovi.digimonapp.data.ApiResult
import es.uniovi.digimonapp.model.DigimonDetails_RootData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import android.util.Log
import es.uniovi.digimonapp.ui.AppUIState

// ViewModel encargado de gestionar y exponer los detalles de un Digimon a la UI
class DigimonDetailViewModel : ViewModel() {

    // LiveData que contiene los detalles del Digimon
    private val _digimonDetails = MutableLiveData<DigimonDetails_RootData?>()
    val digimonDetails: LiveData<DigimonDetails_RootData?> get() = _digimonDetails

    // Método para obtener los detalles de un Digimon por su nombre
    fun fetchDigimonDetails(digimonName: String) {
        viewModelScope.launch {
            Repository.getDigimonDetails(digimonName)
                .map { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _digimonDetails.value = result.data
                        }
                        is ApiResult.Error -> AppUIState.Error(result.message)
                        is ApiResult.Loading -> AppUIState.Loading(true)
                    }
                }
                .collect() // Recoge el flujo de resultados
        }
    }
}