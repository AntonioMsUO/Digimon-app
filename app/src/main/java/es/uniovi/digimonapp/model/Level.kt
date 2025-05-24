package es.uniovi.digimonapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Level(
    val id: Int,
    val level: String
) : Parcelable