package es.uniovi.digimonapp.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// URL base de la Digi-API
private const val BASE_URL = "https://digi-api.com/"

// Configuración de Moshi para parsear JSON a objetos Kotlin
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()) // Soporte para clases de datos de Kotlin
    .build()

// Configuración de Retrofit con Moshi como convertidor y la URL base
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi)) // Conversor Moshi
    .baseUrl(BASE_URL) // URL base de la API
    .build()

// Objeto singleton que expone el servicio de la API
object RetrofitClient {
    // Lazy Instance del servicio DigimonApiService
    val retrofitService: DigimonApiService by lazy {
        retrofit.create(DigimonApiService::class.java)
    }
}