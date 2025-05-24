package es.uniovi.digimonapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.uniovi.digimonapp.R
import es.uniovi.digimonapp.data.local.FavoritesRepository
import es.uniovi.digimonapp.databinding.FragmentHomeBinding
import es.uniovi.digimonapp.domain.HomeViewModel
import es.uniovi.digimonapp.domain.HomeViewModelFactory

// Fragmento principal que muestra la lista de Digimon y permite buscar, filtrar y marcar favoritos
class HomeFragment : Fragment(), FilterDialogFragment.FilterDialogListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: DigimonListRecyclerViewAdapter

    // Infla la vista y configura el ViewModel y el Adapter si ya hay datos cargados
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val appContext = requireContext().applicationContext
        val repository = FavoritesRepository.getInstance(appContext)
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        // Si ya hay datos, configura el adapter y restaura la posición del scroll
        if (!viewModel.digimons.value.isNullOrEmpty()) {
            adapter = DigimonListRecyclerViewAdapter(
                onItemClick = { digimonName ->
                    val bundle = Bundle().apply {
                        putString("digimonName", digimonName)
                    }
                    findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
                },
                onFavoriteClick = { digimon, position ->
                    viewModel.toggleFavorite(digimon)
                    adapter.notifyItemChanged(position)
                }
            )

            val layoutManager = LinearLayoutManager(context)
            binding.digimonList.layoutManager = layoutManager
            binding.digimonList.adapter = adapter

            viewModel.digimons.value?.toList()?.let { adapter.submitList(it) }
            val savedPosition = viewModel.getScrollPosition()
            binding.digimonList.scrollToPosition(savedPosition)
        }

        return binding.root
    }

    // Guarda la posición del scroll al pausar el fragmento
    override fun onPause() {
        super.onPause()
        val layoutManager = binding.digimonList.layoutManager as LinearLayoutManager
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        viewModel.saveScrollPosition(firstVisiblePosition)
    }

    // Restaura la posición del scroll al reanudar el fragmento
    override fun onResume() {
        super.onResume()
        val layoutManager = binding.digimonList.layoutManager as LinearLayoutManager
        val savedPosition = viewModel.getScrollPosition()
        layoutManager.scrollToPosition(savedPosition)
    }

    // Configura la Toolbar, el Adapter, los observadores y la paginación
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura la Toolbar como ActionBar
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        setHasOptionsMenu(true)

        adapter = DigimonListRecyclerViewAdapter(
            onItemClick = { digimonName ->
                val bundle = Bundle().apply {
                    putString("digimonName", digimonName)
                }
                findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
            },
            onFavoriteClick = { digimon, position ->
                viewModel.toggleFavorite(digimon)
                adapter.notifyItemChanged(position)
            }
        )

        val layoutManager = LinearLayoutManager(context)
        binding.digimonList.layoutManager = layoutManager
        binding.digimonList.adapter = adapter

        // Observa los cambios en la lista de Digimon y actualiza el Adapter
        viewModel.digimons.observe(viewLifecycleOwner) { digimons ->
            digimons?.let {
                adapter.submitList(it.toList())
                Log.d("HomeFragment", "Datos en el Adapter: $it")
            }
        }

        // Observa los favoritos y actualiza el estado de favoritos en la lista
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            viewModel.updateFavoriteStatus(favorites.map { it.name })
        }

        // Listener para paginación al llegar al final de la lista
        binding.digimonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                    viewModel.loadNextPage()
                }
            }
        })

        // Carga los datos si la lista está vacía
        if (viewModel.digimons.value.isNullOrEmpty()) {
            viewModel.loadDigimons()
        }
    }

    // Infla el menú y configura la búsqueda por nombre
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        val searchItem = menu.findItem(R.id.search_filter)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.applyFilters(query, null, null, null)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    // Gestiona la selección de opciones del menú (filtros avanzados)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("HomeFragment", "Elemento del menú pulsado: ${item.itemId}")
        return when (item.itemId) {
            R.id.action_filter -> {
                Log.d("Menu filter", "Entra en el select")
                val filterDialog = FilterDialogFragment()
                filterDialog.setFilterDialogListener(this)
                filterDialog.show(parentFragmentManager, "filterDialog")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Recibe los filtros aplicados desde el diálogo y los pasa al ViewModel
    override fun onFilterApplied(name: String?, attribute: String?, level: String?, xAntibody: Boolean?) {
        viewModel.applyFilters(name, attribute, level, xAntibody)
    }

    // Libera el binding y quita la Toolbar para evitar interferencias
    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).setSupportActionBar(null)
        _binding = null
    }
}