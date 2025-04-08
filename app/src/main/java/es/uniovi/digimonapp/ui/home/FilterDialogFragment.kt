package es.uniovi.digimonapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import es.uniovi.digimonapp.R
import es.uniovi.digimonapp.databinding.FragmentFilterDialogBinding

class FilterDialogFragment : DialogFragment() {

    interface FilterDialogListener {
        fun onFilterApplied(name: String?, attribute: String?, level: String?, xAntibody: Boolean?)
    }

    private var listener: FilterDialogListener? = null
    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!

    fun setFilterDialogListener(listener: FilterDialogListener) {
        this.listener = listener
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.dialog_width),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val attributeDisplay = listOf(getString(R.string.all_option)) + resources.getStringArray(R.array.attribute_display).toList()
        val levelDisplay = listOf(getString(R.string.all_option)) + resources.getStringArray(R.array.level_display).toList()

        binding.attributeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, attributeDisplay)
        binding.levelSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, levelDisplay)

        binding.applyButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().takeIf { it.isNotBlank() }
            val attributeIndex = binding.attributeSpinner.selectedItemPosition - 1 // quitamos "Todos"
            val levelIndex = binding.levelSpinner.selectedItemPosition - 1

            val attribute = if (attributeIndex >= 0) resources.getStringArray(R.array.attribute_keys)[attributeIndex] else null
            val level = if (levelIndex >= 0) resources.getStringArray(R.array.level_keys)[levelIndex] else null
            val xAntibody = binding.xAntibodySwitch.isChecked

            listener?.onFilterApplied(name, attribute, level, xAntibody)
            dismiss()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}