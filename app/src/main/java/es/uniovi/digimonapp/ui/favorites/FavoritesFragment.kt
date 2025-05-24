package es.uniovi.digimonapp.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import es.uniovi.digimonapp.R
import es.uniovi.digimonapp.databinding.FragmentFavoritesBinding
import es.uniovi.digimonapp.domain.FavoritesViewModel
import es.uniovi.digimonapp.model.local.FavoriteDigimon
import kotlinx.coroutines.launch

// Fragmento encargado de mostrar y gestionar la lista de Digimon favoritos
class FavoritesFragment : Fragment() {

    // ViewBinding para acceder a las vistas del layout
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    // ViewModel para acceder y modificar los favoritos
    private val viewModel: FavoritesViewModel by viewModels()
    // Adaptador para la lista de favoritos
    private lateinit var adapter: FavoritesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla el layout usando ViewBinding
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpia la toolbar y el binding para evitar fugas de memoria
        (requireActivity() as AppCompatActivity).setSupportActionBar(null)
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Configura la toolbar personalizada
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        // Inicializa el adaptador con callbacks para navegación y eliminación
        val adapter = FavoritesListAdapter(
            onItemClick = { digimonName ->
                // Navega al detalle del Digimon seleccionado
                val bundle = Bundle().apply {
                    putString("digimonName", digimonName)
                }
                findNavController().navigate(R.id.action_favoritesFragment_to_detailFragment, bundle)
            },
            onFavoriteClick = { digimon ->
                // Elimina el Digimon de favoritos
                viewModel.removeFavorite(digimon)
            }
        )

        // Configura el RecyclerView
        val layoutManager = LinearLayoutManager(context)
        binding.favoritesList.layoutManager = layoutManager
        binding.favoritesList.adapter = adapter

        // Observa los favoritos y actualiza la lista cuando cambian
        viewModel.favorites.observe(viewLifecycleOwner) { favs ->
            adapter.submitList(favs.toList())
            Log.d("FavoritesFragment", "Favoritos observados: $favs")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Infla el menú con la opción de búsqueda
        inflater.inflate(R.menu.home_menu, menu)

        val searchItem = menu.findItem(R.id.search_filter)
        val searchView = searchItem.actionView as SearchView

        // Configura el listener para filtrar la lista según la búsqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterFavorites(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    // Filtra la lista de favoritos según el texto de búsqueda
    private fun filterFavorites(query: String?) {
        viewModel.favorites.observe(viewLifecycleOwner) { allFavs ->
            val filtered = if (!query.isNullOrBlank()) {
                allFavs.filter { it.name.contains(query, ignoreCase = true) }
            } else {
                allFavs
            }
            adapter.submitList(filtered)
        }
    }
}