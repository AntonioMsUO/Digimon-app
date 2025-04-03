package es.uniovi.digimonapp.api

import es.uniovi.digimonapp.model.RootData
import retrofit2.http.GET

interface DigimonApiService {
    @GET("api/v1/digimon")
    suspend fun getDigimons(): RootData
}