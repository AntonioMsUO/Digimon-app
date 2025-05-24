package es.uniovi.digimonapp.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Description(
    val description: String,
    val language: String,
    val origin: String
) : Parcelable