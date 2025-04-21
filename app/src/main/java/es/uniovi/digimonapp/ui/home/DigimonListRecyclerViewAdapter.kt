package es.uniovi.digimonapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import es.uniovi.digimonapp.model.Digimon
import es.uniovi.digimonapp.databinding.ItemDigimonBinding

class DigimonListRecyclerViewAdapter(
    // Acepta un lambda que toma el nombre del Digimon
    private val onFavoriteClick: (Digimon) -> Unit // Acepta un lambda que toma el objeto Digimon

) : ListAdapter<Digimon, DigimonDataHolder>(DigimonDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigimonDataHolder {
        val binding = ItemDigimonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigimonDataHolder(binding)
    }

    override fun onBindViewHolder(holder: DigimonDataHolder, position: Int) {
        val digimon = getItem(position) // Usar getItem para obtener el digimon en la posición
        holder.bind(digimon, onItemClick, onFavoriteClick) // ← pasa el nuevo callback
        holder.itemView.setOnClickListener {
            onItemClick(digimon.name) // Usar el nombre del Digimon para la navegación
        }
    }
}
object DigimonDiffCallback : DiffUtil.ItemCallback<Digimon>() {
    override fun areItemsTheSame(oldItem: Digimon, newItem: Digimon): Boolean {
        return oldItem.id == newItem.id // Puedes seguir usando el ID para comparar
    }

    override fun areContentsTheSame(oldItem: Digimon, newItem: Digimon): Boolean {
        return oldItem == newItem
    }
}
