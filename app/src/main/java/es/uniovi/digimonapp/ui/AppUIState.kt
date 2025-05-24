package es.uniovi.digimonapp.ui

import es.uniovi.digimonapp.model.Digimon
import es.uniovi.digimonapp.state.AppState

// Define los posibles estados de la UI para la app
sealed class AppUIState(val state: AppState) {
    // Estado de éxito, contiene una lista de Digimon (puede ser nula)
    data class Success(val datos: List<Digimon>?) : AppUIState(AppState.SUCCESS)
    // Estado de error, contiene un mensaje opcional
    data class Error(val message: String?) : AppUIState(AppState.ERROR)
    // Estado de carga, indica si está cargando (por defecto true)
    data class Loading(val loading: Boolean = true) : AppUIState(AppState.LOADING)
}