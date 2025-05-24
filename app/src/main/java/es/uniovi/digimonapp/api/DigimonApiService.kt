package es.uniovi.digimonapp.api

import es.uniovi.digimonapp.model.DigimonDetails_RootData
import es.uniovi.digimonapp.model.RootData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Interfaz de servicio para acceder a la Digi-API usando Retrofit
interface DigimonApiService {

    // Obtiene una lista de Digimon con filtros opcionales y paginación
    @GET("api/v1/digimon")
    suspend fun getDigimons(
        @Query("page") page: Int? = null, // Número de página (opcional)
        @Query("pageSize") pageSize: Int? = null, // Tamaño de página (opcional)
        @Query("name") name: String? = null, // Filtrar por nombre (opcional)
        @Query("attribute") attribute: String? = null, // Filtrar por atributo (opcional)
        @Query("level") level: String? = null, // Filtrar por nivel (opcional)
        @Query("xAntibody") xAntibody: Boolean? = null // Filtrar por X-Antibody (opcional)
    ): RootData // Devuelve el objeto raíz con la lista de Digimon

    // Obtiene los detalles de un Digimon específico por su nombre
    @GET("api/v1/digimon/{name}")
    suspend fun getDigimonDetails(
        @Path("name") name: String // Nombre del Digimon a consultar
    ): DigimonDetails_RootData // Devuelve los detalles del Digimon
}