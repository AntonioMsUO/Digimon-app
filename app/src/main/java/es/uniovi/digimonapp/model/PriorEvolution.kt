package es.uniovi.digimonapp.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PriorEvolution(
    val condition: String,
    val digimon: String,
    val id: Int,
    val image: String,
    val url: String
) : Parcelable