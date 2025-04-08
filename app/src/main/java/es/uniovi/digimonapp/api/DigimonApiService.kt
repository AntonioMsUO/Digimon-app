package es.uniovi.digimonapp.api

import es.uniovi.digimonapp.model.RootData
import retrofit2.http.GET
import retrofit2.http.Query

interface DigimonApiService {
    @GET("api/v1/digimon")
    suspend fun getDigimons(
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("name") name: String? = null,
        @Query("attribute") attribute: String? = null,
        @Query("level") level: String? = null,
        @Query("xAntibody") xAntibody: Boolean? = null
    ): RootData

}