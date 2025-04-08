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

class DigimonDetailViewModel : ViewModel() {

    private val _digimonDetails = MutableLiveData<DigimonDetails_RootData?>()
    val digimonDetails: LiveData<DigimonDetails_RootData?> get() = _digimonDetails

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
                .collect() // Recoger el flujo de resultados
        }
    }
}
