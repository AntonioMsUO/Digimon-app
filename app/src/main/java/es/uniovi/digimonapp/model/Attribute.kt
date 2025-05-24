package es.uniovi.digimonapp.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Attribute(
    val attribute: String,
    val id: Int
) : Parcelable