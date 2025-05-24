package es.uniovi.digimonapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import es.uniovi.digimonapp.databinding.ActivityMainBinding

// Actividad principal que gestiona la navegación y la configuración visual de la app
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Aplica el idioma guardado antes de crear el contexto base
    override fun attachBaseContext(newBase: Context) {
        val context = es.uniovi.digimonapp.util.LocaleHelper.applySavedLanguage(newBase)
        super.attachBaseContext(context)
    }

    // Configura la ventana, la navegación y la visibilidad de la barra inferior
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Asocia el BottomNavigationView con el NavController
        binding.bottomNavView.setupWithNavController(navController)

        // Controla la visibilidad del BottomNavigationView según el destino
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailFragment,
                R.id.mapFragment -> binding.bottomNavView.visibility = View.GONE
                else -> binding.bottomNavView.visibility = View.VISIBLE
            }
        }
    }
}