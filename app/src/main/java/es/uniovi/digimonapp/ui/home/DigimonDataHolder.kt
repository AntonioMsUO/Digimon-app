package es.uniovi.digimonapp.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.uniovi.digimonapp.databinding.ItemDigimonBinding
import es.uniovi.digimonapp.model.Digimon
import es.uniovi.digimonapp.R

// ViewHolder para mostrar la información de un Digimon en la lista principal
class DigimonDataHolder(private val binding: ItemDigimonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // Asocia los datos del Digimon y los listeners a la vista
    fun bind(
        digimon: Digimon,
        position: Int,
        onItemClick: (String) -> Unit,           // Callback para navegar al detalle
        onFavoriteClick: (Digimon, Int) -> Unit  // Callback para marcar/desmarcar favorito
    ) {
        // Muestra el nombre del Digimon
        binding.digimonName.text = digimon.name

        // Carga la imagen del Digimon usando Glide
        Glide.with(binding.root.context)
            .load(digimon.image)
            .into(binding.digimonImage)

        // Cambia el icono según si es favorito o no
        val icon = if (digimon.isFavorite)
            R.drawable.ic_favorite_filled
        else
            R.drawable.ic_favorite_border
        binding.favoriteIcon.setImageResource(icon)

        // Al pulsar la tarjeta, navega al detalle del Digimon
        binding.root.setOnClickListener {
            onItemClick(digimon.name)
        }

        // Al pulsar el icono de favorito, marca/desmarca como favorito
        binding.favoriteIcon.setOnClickListener {
            onFavoriteClick(digimon, position)
        }
    }
}