package es.uniovi.digimonapp.ui

import es.uniovi.digimonapp.model.Digimon
import es.uniovi.digimonapp.state.AppState

sealed class AppUIState (val state: AppState) {
    data class Success(val datos: List<Digimon>?): AppUIState(AppState.SUCCESS)
    data class Error (val message:String?): AppUIState(AppState.ERROR)
    data class Loading(val loading:Boolean = true): AppUIState(AppState.LOADING)
}