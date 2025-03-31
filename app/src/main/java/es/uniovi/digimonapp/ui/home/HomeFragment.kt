package es.uniovi.digimonapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import es.uniovi.digimonapp.databinding.FragmentHomeBinding
import es.uniovi.digimonapp.ui.home.placeholder.PlaceholderContent

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar el RecyclerView
        binding.digimonList.layoutManager = LinearLayoutManager(context)
        binding.digimonList.adapter = DigimonListRecyclerViewAdapter(PlaceholderContent.ITEMS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}