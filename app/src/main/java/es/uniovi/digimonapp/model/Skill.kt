package es.uniovi.digimonapp.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Skill(
    val description: String,
    val id: Int,
    val skill: String,
    val translation: String
) : Parcelable