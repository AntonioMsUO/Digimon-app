package es.uniovi.digimonapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import es.uniovi.digimonapp.util.LocaleHelper

// Clase de aplicación principal que inicializa el tema y el idioma según las
// preferencias del usuario
class DigimonApp : Application() {

    // Se llama al crear la aplicación; aplica el tema guardado en preferencias
    override fun onCreate() {
        super.onCreate()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        when (prefs.getString("theme_preference", "auto")) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    // Se llama antes de crear el contexto base; aplica el idioma guardado
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.applySavedLanguage(base))
    }
}