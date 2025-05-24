package es.uniovi.digimonapp.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.color.MaterialColors
import es.uniovi.digimonapp.R
import androidx.core.content.edit

// Fragmento de preferencias para la pantalla de ajustes de la app
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    // Infla las preferencias desde el archivo XML
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    // Cambia el color de fondo según el tema actual
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(
            MaterialColors.getColor(view, android.R.attr.colorBackground, Color.WHITE)
        )
    }

    // Registra el listener para cambios en las preferencias al reanudar el fragmento
    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    // Elimina el listener al pausar el fragmento para evitar fugas de memoria
    override fun onPause() {
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    // Responde a los cambios en las preferencias
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            // Cambia el tema de la app según la preferencia seleccionada
            "theme_preference" -> {
                when (sharedPreferences?.getString(key, "auto")) {
                    "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                activity?.recreate()
            }
            // Cambia el idioma de la app y lo guarda en preferencias
            "language_preference" -> {
                val lang = sharedPreferences?.getString(key, "es")
                saveLanguageToAppPrefs(lang)
                activity?.recreate()
            }
        }
    }

    // Guarda el idioma seleccionado en las preferencias de la app
    private fun saveLanguageToAppPrefs(languageCode: String?) {
        languageCode ?: return
        val appPrefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        appPrefs.edit() { putString("app_language", languageCode) }
    }
}