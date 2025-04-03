package es.uniovi.digimonapp.model

import com.squareup.moshi.Json

data class RootData(
    @Json(name = "content")
    val digimon: List<Digimon>,
    val pageable: Pageable
)