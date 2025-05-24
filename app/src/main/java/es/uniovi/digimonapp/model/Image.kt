package es.uniovi.digimonapp.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Image(
    val href: String,
    val transparent: Boolean
) : Parcelable