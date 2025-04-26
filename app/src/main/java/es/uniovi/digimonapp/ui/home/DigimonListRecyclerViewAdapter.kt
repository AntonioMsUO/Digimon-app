package es.uniovi.digimonapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.uniovi.digimonapp.databinding.ItemDigimonBinding
import es.uniovi.digimonapp.model.Digimon

class DigimonListRecyclerViewAdapter(
    private val onItemClick: (String) -> Unit,
    private val onFavoriteClick: (Digimon, Int) -> Unit
) : RecyclerView.Adapter<DigimonDataHolder>() {

    private var digimonList: List<Digimon> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigimonDataHolder {
        val binding = ItemDigimonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigimonDataHolder(binding)
    }

    override fun onBindViewHolder(holder: DigimonDataHolder, position: Int) {
        holder.bind(digimonList[position], position, onItemClick, onFavoriteClick)
    }

    override fun getItemCount(): Int = digimonList.size

    fun submitList(list: List<Digimon>) {
        digimonList = list
        notifyDataSetChanged()
    }
}
