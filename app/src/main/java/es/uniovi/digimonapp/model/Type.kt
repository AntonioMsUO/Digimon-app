package es.uniovi.digimonapp.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Type(
    val id: Int,
    val type: String
) : Parcelable