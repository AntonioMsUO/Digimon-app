package es.uniovi.digimonapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import es.uniovi.digimonapp.model.Digimon
import es.uniovi.digimonapp.databinding.ItemDigimonBinding

class DigimonListRecyclerViewAdapter : ListAdapter<Digimon, DigimonDataHolder>(DigimonDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigimonDataHolder {
        val binding = ItemDigimonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigimonDataHolder(binding.root)
    }

    override fun onBindViewHolder(holder: DigimonDataHolder, position: Int) {
        val digimon = getItem(position)
        holder.bind(digimon)
    }
}

object DigimonDiffCallback : DiffUtil.ItemCallback<Digimon>() {
    override fun areItemsTheSame(oldItem: Digimon, newItem: Digimon): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Digimon, newItem: Digimon): Boolean {
        return oldItem == newItem
    }
}