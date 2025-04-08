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

    suspend fun getDigimons(
        page: Int? = null,
        pageSize: Int? = null,
        name: String? = null,
        attribute: String? = null,
        level: String? = null,
        xAntibody: Boolean? = null
    ): Flow<ApiResult<List<Digimon>>> = flow {
        emit(ApiResult.Loading(null))
        try {
            val response = apiService.getDigimons(page, pageSize, name, attribute, level, xAntibody)
            emit(ApiResult.Success(response.digimon ?: emptyList()))
        } catch (e: Exception) {
            emit(ApiResult.Error(e.localizedMessage ?: "Error desconocido"))
        }
    }


}