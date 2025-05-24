package es.uniovi.digimonapp.util

import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocaleHelper {

    fun applySavedLanguage(context: Context): Context {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val langCode = prefs.getString("app_language", Locale.getDefault().language) ?: "es"
        return setLocale(context, langCode)
    }

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}
