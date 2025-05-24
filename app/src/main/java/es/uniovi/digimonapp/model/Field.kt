package es.uniovi.digimonapp.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Field(
    val `field`: String,
    val id: Int,
    val image: String
) : Parcelable