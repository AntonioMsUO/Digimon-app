package es.uniovi.digimonapp.model

import com.squareup.moshi.Json

data class DigimonDetails_RootData(
    val id: Int,
    val name: String,
    @Json(name = "xAntibody")
    val xAntibody: Boolean,
    val images: List<Image>,
    val levels: List<Level>,
    val types: List<Type>,
    val attributes: List<Attribute>,
    val fields: List<Field>,
    @Json(name = "releaseDate")
    val releaseDate: String,
    val descriptions: List<Description>,
    val skills: List<Skill>,
    @Json(name = "priorEvolutions")
    val priorEvolutions: List<PriorEvolution>,
    @Json(name = "nextEvolutions")
    val nextEvolutions: List<NextEvolution>
)