package es.uniovi.digimonapp.data

import es.uniovi.digimonapp.state.AppState

// Clase sellada que representa el resultado de una operación de API
sealed class ApiResult <out T> (
    val status: AppState, // Estado de la operación (SUCCESS, ERROR, LOADING)
    val data: T?,         // Datos devueltos por la API (si los hay)
    val message: String?  // Mensaje de error o información adicional
) {

    // Resultado exitoso con los datos obtenidos
    data class Success<out R>(val _data: R): ApiResult<R>(
        status = AppState.SUCCESS,
        data = _data,
        message = null
    )

    // Resultado de error con un mensaje descriptivo
    data class Error(val exception: String): ApiResult<Nothing>(
        status = AppState.ERROR,
        data = null,
        message = exception
    )

    // Estado de carga, opcionalmente con datos parciales
    data class Loading<out R>(val _data: R?): ApiResult<R>(
        status = AppState.LOADING,
        data = _data,
        message = "Loading..."
    )

}