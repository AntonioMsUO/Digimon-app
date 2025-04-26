package es.uniovi.digimonapp.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.uniovi.digimonapp.databinding.ItemFavoritesBinding
import es.uniovi.digimonapp.model.local.FavoriteDigimon
import es.uniovi.digimonapp.R

class FavoritesListAdapter(
    private val onItemClick: (String) -> Unit,
    private val onFavoriteClick: (FavoriteDigimon) -> Unit
) : ListAdapter<FavoriteDigimon, FavoritesListAdapter.FavoriteViewHolder>(FavoriteDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val digimon = getItem(position)
        holder.bind(digimon, onItemClick, onFavoriteClick)
    }

    class FavoriteViewHolder(private val binding: ItemFavoritesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            digimon: FavoriteDigimon,
            onItemClick: (String) -> Unit,
            onFavoriteClick: (FavoriteDigimon) -> Unit
        ) {
            binding.digimonName.text = digimon.name

            Glide.with(binding.root.context)
                .load(digimon.imageUrl)
                .into(binding.digimonImage)

            // Mostrar el icono de favorito relleno
            binding.favoriteIcon.setImageResource(R.drawable.ic_favorite_filled)

            // Pulsar sobre el icono para quitar de favoritos
            binding.favoriteIcon.setOnClickListener {
                onFavoriteClick(digimon)
            }

            // Pulsar en la tarjeta para ir al detalle
            itemView.setOnClickListener { onItemClick(digimon.name) }
        }
    }
}

object FavoriteDiffCallback : DiffUtil.ItemCallback<FavoriteDigimon>() {
    override fun areItemsTheSame(oldItem: FavoriteDigimon, newItem: FavoriteDigimon): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: FavoriteDigimon, newItem: FavoriteDigimon): Boolean {
        return oldItem == newItem
    }
}
