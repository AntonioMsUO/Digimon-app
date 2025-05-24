package es.uniovi.digimonapp.data

import es.uniovi.digimonapp.api.DigimonApiService
import es.uniovi.digimonapp.api.RetrofitClient
import es.uniovi.digimonapp.model.Digimon
import es.uniovi.digimonapp.model.DigimonDetails_RootData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow





// Repositorio para acceder a la Digi-API y obtener datos de Digimon
object Repository {

    // Servicio de la API configurado con Retrofit
    private val apiService = RetrofitClient.retrofitService

    // Obtiene una lista de Digimon según los filtros proporcionados
    suspend fun getDigimons(
        page: Int? = null,
        pageSize: Int? = null,
        name: String? = null,
        attribute: String? = null,
        level: String? = null,
        xAntibody: Boolean? = null
    ): Flow<ApiResult<List<Digimon>>> = flow {
        emit(ApiResult.Loading(null)) // Estado de carga
        try {
            val response = apiService.getDigimons(page, pageSize, name, attribute, level, xAntibody)
            emit(ApiResult.Success(response.digimon)) // Éxito con la lista de Digimon
        } catch (e: Exception) {
            emit(ApiResult.Error(e.localizedMessage ?: "Error desconocido")) // Error en la petición
        }
    }

    // Obtiene los detalles de un Digimon por su nombre
    suspend fun getDigimonDetails(name: String): Flow<ApiResult<DigimonDetails_RootData>> = flow {
        emit(ApiResult.Loading(null)) // Estado de carga
        try {
            val response = apiService.getDigimonDetails(name)
            emit(ApiResult.Success(response)) // Éxito con los detalles del Digimon
        } catch (e: Exception) {
            emit(ApiResult.Error(e.localizedMessage ?: "Error desconocido")) // Error en la petición
        }
    }
}