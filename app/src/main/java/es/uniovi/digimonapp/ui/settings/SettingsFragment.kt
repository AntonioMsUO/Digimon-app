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

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(
            MaterialColors.getColor(view, android.R.attr.colorBackground, Color.WHITE)
        )
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "theme_preference" -> {
                when (sharedPreferences?.getString(key, "auto")) {
                    "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                activity?.recreate()
            }

            "language_preference" -> {
                val lang = sharedPreferences?.getString(key, "es")
                saveLanguageToAppPrefs(lang)
                activity?.recreate()
            }
        }
    }

    private fun saveLanguageToAppPrefs(languageCode: String?) {
        languageCode ?: return
        val appPrefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        appPrefs.edit() { putString("app_language", languageCode) }
    }


}
