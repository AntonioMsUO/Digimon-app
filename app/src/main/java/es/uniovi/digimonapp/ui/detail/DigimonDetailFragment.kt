package es.uniovi.digimonapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import es.uniovi.digimonapp.R
import es.uniovi.digimonapp.databinding.FragmentDigimonDetailBinding
import es.uniovi.digimonapp.domain.DigimonDetailViewModel
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import es.uniovi.digimonapp.model.NextEvolution
import es.uniovi.digimonapp.model.PriorEvolution
import es.uniovi.digimonapp.model.Skill

class DigimonDetailFragment : Fragment() {

    private var _binding: FragmentDigimonDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DigimonDetailViewModel by viewModels()

    // El nombre del Digimon se pasará a través del Bundle
    private var digimonName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            digimonName = it.getString(ARG_DIGIMON_NAME) // Obtener el nombre del Digimon
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDigimonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa el LiveData del ViewModel para obtener los detalles del Digimon
        viewModel.digimonDetails.observe(viewLifecycleOwner) { digimon ->
            digimon?.let {
                binding.digimonName.text = it.name
                binding.digimonLevel.text = it.levels.firstOrNull()?.level ?: "Desconocido"
                binding.digimonAttribute.text = it.attributes.firstOrNull()?.attribute ?: "Desconocido"
                binding.digimonType.text = it.types.firstOrNull()?.type ?: "Desconocido"
                binding.digimonDescription.text = it.descriptions.firstOrNull { desc -> desc.language == "en_us" }?.description ?: "Descripción no disponible"
                Glide.with(this).load(it.images.first().href).into(binding.digimonImage)

                addSkills(it.skills)
                addEvolutions(it.priorEvolutions, it.nextEvolutions)
            }
        }

        // Carga los detalles del Digimon usando su nombre
        digimonName?.let { viewModel.fetchDigimonDetails(it) }
    }

    private fun addSkills(skills: List<Skill>) {
        for (skill in skills) {
            val skillView = TextView(context).apply {
                text = skill.skill
            }
            binding.digimonSkillsContainer.addView(skillView)
        }
    }

    private fun addEvolutions(priorEvolutions: List<PriorEvolution>, nextEvolutions: List<NextEvolution>) {
        // Agregar evoluciones previas
        for (evolution in priorEvolutions) {
            val evolutionView = TextView(context).apply {
                text = evolution.digimon
            }
            binding.digimonPriorEvolutionsContainer.addView(evolutionView)
        }
        // Agregar evoluciones siguientes
        for (evolution in nextEvolutions) {
            val evolutionView = TextView(context).apply {
                text = evolution.digimon
            }
            binding.digimonNextEvolutionsContainer.addView(evolutionView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DIGIMON_NAME = "digimonName"

        @JvmStatic
        fun newInstance(digimonName: String) =
            DigimonDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DIGIMON_NAME, digimonName)
                }
            }
    }
}
