package es.uniovi.digimonapp.data

import es.uniovi.digimonapp.api.DigimonApiService
import es.uniovi.digimonapp.api.RetrofitClient
import es.uniovi.digimonapp.model.Digimon
import es.uniovi.digimonapp.model.RootData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

object Repository {

    private val apiService = RetrofitClient.retrofitService

    suspend fun updateDigimonData(): Flow<ApiResult<List<Digimon>>> = flow {
        emit(ApiResult.Loading(null))
        try {
            val response: RootData = apiService.getDigimons()
            emit(ApiResult.Success(response.digimon))
        } catch (e: HttpException) {
            emit(ApiResult.Error(e.message ?: "Error desconocido"))
        } catch (e: Exception) {
            emit(ApiResult.Error(e.message ?: "Error desconocido"))
        }
    }
}