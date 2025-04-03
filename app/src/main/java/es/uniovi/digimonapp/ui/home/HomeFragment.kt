package es.uniovi.digimonapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.uniovi.digimonapp.databinding.FragmentHomeBinding
import es.uniovi.digimonapp.domain.HomeViewModel
import android.util.Log


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DigimonListRecyclerViewAdapter()
        binding.digimonList.layoutManager = LinearLayoutManager(context)
        binding.digimonList.adapter = adapter

        viewModel.digimons.observe(viewLifecycleOwner, { digimons ->
            digimons?.let {
                adapter.submitList(it)
                Log.d("HomeFragment", "Datos en el Adapter: $it")
            }
        })

        viewModel.fetchDigimons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}