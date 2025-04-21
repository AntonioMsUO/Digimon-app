package es.uniovi.digimonapp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteDigimon(
    @PrimaryKey val name: String,
    val imageUrl: String
)
