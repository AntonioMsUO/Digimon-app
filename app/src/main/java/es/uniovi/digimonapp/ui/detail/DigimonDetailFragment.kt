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
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import es.uniovi.digimonapp.model.Field
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
    ): View {
        _binding = FragmentDigimonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.digimonDetails.observe(viewLifecycleOwner) { digimon ->
            try {
                digimon?.let {
                    binding.digimonName.text = it.name
                    binding.digimonLevel.text = it.levels.firstOrNull()?.level ?: getString(R.string.unknown)
                    binding.digimonAttribute.text = it.attributes.firstOrNull()?.attribute ?: getString(R.string.unknown)
                    binding.digimonType.text = it.types.firstOrNull()?.type ?: getString(R.string.unknown)
                    binding.digimonDescription.text = it.descriptions.firstOrNull { desc -> desc.language == "en_us" }?.description ?: getString(R.string.description_not_available)

                    Glide.with(this)
                        .load(it.images.firstOrNull()?.href ?: R.drawable.placeholder_image)
                        .into(binding.digimonImage)

                    binding.digimonSkillsDescriptionContainer.removeAllViews()
                    binding.digimonPriorEvolutionsContainer.removeAllViews()
                    binding.digimonNextEvolutionsContainer.removeAllViews()
                    binding.digimonFieldsContainer.removeAllViews()

                    addSkills(it.skills)
                    addEvolutions(it.priorEvolutions, it.nextEvolutions)
                    addFields(it.fields)
                } ?: Log.e("DigimonDetailFragment", "El Digimon es nulo")
            } catch (e: Exception) {
                Log.e("DigimonDetailFragment", "Error al procesar los detalles del Digimon", e)
            }
        }

        binding.showMapButton.setOnClickListener {
            viewModel.digimonDetails.value?.let { digimon ->
                val bundle = Bundle().apply {
                    putParcelable("digimon", digimon)
                }
                findNavController().navigate(R.id.action_digimonDetailFragment_to_mapFragment, bundle)
            }
        }



        digimonName?.let {
            viewModel.fetchDigimonDetails(it)
        } ?: Log.e("DigimonDetailFragment", "El nombre del Digimon es nulo")
    }


    private fun addSkills(skills: List<Skill>) {
        for (skill in skills) {
            val skillView = LayoutInflater.from(context).inflate(R.layout.item_skill, binding.digimonSkillsDescriptionContainer, false)
            val skillName = skillView.findViewById<TextView>(R.id.skill_name)
            val skillDescription = skillView.findViewById<TextView>(R.id.skill_description)

            skillName.text = skill.skill
            skillDescription.text = skill.description

            binding.digimonSkillsDescriptionContainer.addView(skillView)
        }
    }

    private fun addEvolutions(priorEvolutions: List<PriorEvolution>, nextEvolutions: List<NextEvolution>) {
        // Evoluciones previas
        for (evolution in priorEvolutions) {
            val evolutionView = LayoutInflater.from(context).inflate(R.layout.item_evolution, binding.digimonPriorEvolutionsContainer, false)
            val evolutionName = evolutionView.findViewById<TextView>(R.id.evolution_name)
            val evolutionImage = evolutionView.findViewById<ImageView>(R.id.evolution_image)
            val evolutionCondition = evolutionView.findViewById<TextView>(R.id.evolution_condition)

            evolutionName.text = evolution.digimon
            Glide.with(this).load(evolution.image).into(evolutionImage)
            evolutionCondition.text = evolution.condition.ifEmpty { "Sin condición" }

            // Configurar el OnClickListener para navegar al detalle del Digimon
            evolutionView.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("digimonName", evolution.digimon)
                }
                findNavController().navigate(R.id.action_digimonDetailFragment_self, bundle)
            }

            binding.digimonPriorEvolutionsContainer.addView(evolutionView)
        }

        // Evoluciones siguientes
        for (evolution in nextEvolutions) {
            val evolutionView = LayoutInflater.from(context).inflate(R.layout.item_evolution, binding.digimonNextEvolutionsContainer, false)
            val evolutionName = evolutionView.findViewById<TextView>(R.id.evolution_name)
            val evolutionImage = evolutionView.findViewById<ImageView>(R.id.evolution_image)
            val evolutionCondition = evolutionView.findViewById<TextView>(R.id.evolution_condition)

            evolutionName.text = evolution.digimon
            Glide.with(this).load(evolution.image).into(evolutionImage)
            evolutionCondition.text = evolution.condition.ifEmpty { "Sin condición" }

            // Configurar el OnClickListener para navegar al detalle del Digimon
            evolutionView.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("digimonName", evolution.digimon)
                }
                findNavController().navigate(R.id.action_digimonDetailFragment_self, bundle)
            }

            binding.digimonNextEvolutionsContainer.addView(evolutionView)
        }
    }

    private fun addFields(fields: List<Field>) {
        for (field in fields) {
            val fieldView = LayoutInflater.from(context).inflate(R.layout.item_field, binding.digimonFieldsContainer, false)
            val fieldName = fieldView.findViewById<TextView>(R.id.field_name)
            val fieldImage = fieldView.findViewById<ImageView>(R.id.field_image)

            fieldName.text = field.field
            Glide.with(this).load(field.image).into(fieldImage)

            binding.digimonFieldsContainer.addView(fieldView)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DIGIMON_NAME = "digimonName"

    }
}
