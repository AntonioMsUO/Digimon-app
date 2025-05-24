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

// Fragmento encargado de mostrar el detalle de un Digimon seleccionado
class DigimonDetailFragment : Fragment() {

    // ViewBinding para acceder a las vistas del layout de forma segura
    private var _binding: FragmentDigimonDetailBinding? = null
    private val binding get() = _binding!!

    // ViewModel asociado al fragmento para gestionar los datos del Digimon
    private val viewModel: DigimonDetailViewModel by viewModels()

    // Nombre del Digimon recibido por argumentos (Bundle)
    private var digimonName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recupera el nombre del Digimon de los argumentos del fragmento
        arguments?.let {
            digimonName = it.getString(ARG_DIGIMON_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla el layout usando ViewBinding
        _binding = FragmentDigimonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa los detalles del Digimon y actualiza la UI cuando cambian
        viewModel.digimonDetails.observe(viewLifecycleOwner) { digimon ->
            try {
                digimon?.let {
                    // Muestra los datos principales del Digimon
                    binding.digimonName.text = it.name
                    binding.digimonLevel.text = it.levels.firstOrNull()?.level ?: getString(R.string.unknown)
                    binding.digimonAttribute.text = it.attributes.firstOrNull()?.attribute ?: getString(R.string.unknown)
                    binding.digimonType.text = it.types.firstOrNull()?.type ?: getString(R.string.unknown)
                    binding.digimonDescription.text = it.descriptions.firstOrNull { desc -> desc.language == "en_us" }?.description ?: getString(R.string.description_not_available)

                    // Carga la imagen del Digimon (o un placeholder si no hay imagen)
                    Glide.with(this)
                        .load(it.images.firstOrNull()?.href ?: R.drawable.placeholder_image)
                        .into(binding.digimonImage)

                    // Limpia los contenedores antes de añadir los nuevos elementos
                    binding.digimonSkillsDescriptionContainer.removeAllViews()
                    binding.digimonPriorEvolutionsContainer.removeAllViews()
                    binding.digimonNextEvolutionsContainer.removeAllViews()
                    binding.digimonFieldsContainer.removeAllViews()

                    // Añade las habilidades, evoluciones y campos a la UI
                    addSkills(it.skills)
                    addEvolutions(it.priorEvolutions, it.nextEvolutions)
                    addFields(it.fields)
                } ?: Log.e("DigimonDetailFragment", "El Digimon es nulo")
            } catch (e: Exception) {
                Log.e("DigimonDetailFragment", "Error al procesar los detalles del Digimon", e)
            }
        }

        // Botón para mostrar el mapa con la localización del Digimon
        binding.showMapButton.setOnClickListener {
            viewModel.digimonDetails.value?.let { digimon ->
                val bundle = Bundle().apply {
                    putParcelable("digimon", digimon)
                }
                findNavController().navigate(R.id.action_digimonDetailFragment_to_mapFragment, bundle)
            }
        }

        // Solicita los detalles del Digimon al ViewModel si el nombre es válido
        digimonName?.let {
            viewModel.fetchDigimonDetails(it)
        } ?: Log.e("DigimonDetailFragment", "El nombre del Digimon es nulo")
    }

    // Añade las habilidades del Digimon al contenedor correspondiente
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

    // Añade las evoluciones previas y siguientes del Digimon a la UI
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

            // Permite navegar al detalle de la evolución seleccionada
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

            // Permite navegar al detalle de la evolución seleccionada
            evolutionView.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("digimonName", evolution.digimon)
                }
                findNavController().navigate(R.id.action_digimonDetailFragment_self, bundle)
            }

            binding.digimonNextEvolutionsContainer.addView(evolutionView)
        }
    }

    // Añade los campos (fields) del Digimon a la UI
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

    // Libera el binding para evitar fugas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Clave para pasar el nombre del Digimon por argumentos
        private const val ARG_DIGIMON_NAME = "digimonName"
    }
}