package es.uniovi.digimonapp.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.uniovi.digimonapp.databinding.ItemDigimonBinding
import es.uniovi.digimonapp.model.Digimon
import es.uniovi.digimonapp.R

class DigimonDataHolder(private val binding: ItemDigimonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        digimon: Digimon,
        position: Int,
        onItemClick: (String) -> Unit,
        onFavoriteClick: (Digimon, Int) -> Unit
    ) {
        binding.digimonName.text = digimon.name
        Glide.with(binding.root.context)
            .load(digimon.image)
            .into(binding.digimonImage)

        // Icono según estado
        val icon = if (digimon.isFavorite)
            R.drawable.ic_favorite_filled
        else
            R.drawable.ic_favorite_border
        binding.favoriteIcon.setImageResource(icon)

        // Clic para navegar al detalle
        binding.root.setOnClickListener {
            onItemClick(digimon.name)
        }

        // Clic en el icono de favoritos
        binding.favoriteIcon.setOnClickListener {
            onFavoriteClick(digimon, position)
        }
    }
}
