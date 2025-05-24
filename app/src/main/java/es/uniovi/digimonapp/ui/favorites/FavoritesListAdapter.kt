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

// Adaptador para mostrar la lista de Digimon favoritos en un RecyclerView
class FavoritesListAdapter(
    // Callback al pulsar un elemento (navegar al detalle)
    private val onItemClick: (String) -> Unit,
    // Callback al pulsar el icono de favorito (eliminar de favoritos)
    private val onFavoriteClick: (FavoriteDigimon) -> Unit
) : ListAdapter<FavoriteDigimon, FavoritesListAdapter.FavoriteViewHolder>(FavoriteDiffCallback) {

    // Crea un nuevo ViewHolder para un elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    // Asocia los datos del Digimon favorito al ViewHolder
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val digimon = getItem(position)
        holder.bind(digimon, onItemClick, onFavoriteClick)
    }

    // ViewHolder que representa cada elemento de la lista de favoritos
    class FavoriteViewHolder(private val binding: ItemFavoritesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            digimon: FavoriteDigimon,
            onItemClick: (String) -> Unit,
            onFavoriteClick: (FavoriteDigimon) -> Unit
        ) {
            // Muestra el nombre del Digimon
            binding.digimonName.text = digimon.name

            // Carga la imagen del Digimon usando Glide
            Glide.with(binding.root.context)
                .load(digimon.imageUrl)
                .into(binding.digimonImage)

            // Muestra el icono de favorito relleno
            binding.favoriteIcon.setImageResource(R.drawable.ic_favorite_filled)

            // Al pulsar el icono, elimina de favoritos
            binding.favoriteIcon.setOnClickListener {
                onFavoriteClick(digimon)
            }

            // Al pulsar la tarjeta, navega al detalle del Digimon
            itemView.setOnClickListener { onItemClick(digimon.name) }
        }
    }
}

// Callback para optimizar las actualizaciones del RecyclerView
object FavoriteDiffCallback : DiffUtil.ItemCallback<FavoriteDigimon>() {
    // Compara si dos elementos son el mismo por nombre
    override fun areItemsTheSame(oldItem: FavoriteDigimon, newItem: FavoriteDigimon): Boolean {
        return oldItem.name == newItem.name
    }

    // Compara si el contenido de dos elementos es igual
    override fun areContentsTheSame(oldItem: FavoriteDigimon, newItem: FavoriteDigimon): Boolean {
        return oldItem == newItem
    }
}