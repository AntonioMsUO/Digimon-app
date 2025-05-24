package es.uniovi.digimonapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.uniovi.digimonapp.databinding.ItemDigimonBinding
import es.uniovi.digimonapp.model.Digimon

// Adaptador para mostrar la lista principal de Digimon en un RecyclerView
class DigimonListRecyclerViewAdapter(
    // Callback al pulsar un elemento (navegar al detalle)
    private val onItemClick: (String) -> Unit,
    // Callback al pulsar el icono de favorito (marcar/desmarcar favorito)
    private val onFavoriteClick: (Digimon, Int) -> Unit
) : RecyclerView.Adapter<DigimonDataHolder>() {

    // Lista de Digimon a mostrar
    private var digimonList: List<Digimon> = listOf()

    // Crea un nuevo ViewHolder para un elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigimonDataHolder {
        val binding = ItemDigimonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigimonDataHolder(binding)
    }

    // Asocia los datos del Digimon al ViewHolder
    override fun onBindViewHolder(holder: DigimonDataHolder, position: Int) {
        holder.bind(digimonList[position], position, onItemClick, onFavoriteClick)
    }

    // Devuelve el número de elementos en la lista
    override fun getItemCount(): Int = digimonList.size

    // Actualiza la lista de Digimon y refresca la vista
    fun submitList(list: List<Digimon>) {
        digimonList = list
        notifyDataSetChanged()
    }
}