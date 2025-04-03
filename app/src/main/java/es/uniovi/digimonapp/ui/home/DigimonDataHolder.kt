package es.uniovi.digimonapp.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.uniovi.digimonapp.databinding.ItemDigimonBinding
import es.uniovi.digimonapp.model.Digimon
import android.util.Log


class DigimonDataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemBinding = ItemDigimonBinding.bind(itemView)

    fun bind(digimon: Digimon) {
        itemBinding.digimonName.text = digimon.name
        Glide.with(itemView.context)
            .load(digimon.image)
            .into(itemBinding.digimonImage)
        Log.d("DigimonAdapter", "Vinculando datos: ${digimon.name}")
    }
}