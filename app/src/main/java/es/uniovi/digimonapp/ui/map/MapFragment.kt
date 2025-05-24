package es.uniovi.digimonapp.ui.map

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import es.uniovi.digimonapp.R
import es.uniovi.digimonapp.model.DigimonDetails_RootData

class MapFragment : Fragment() {

    private val TAG = "MapFragment"

    private lateinit var mapImageView: ImageView
    private lateinit var markerContainer: FrameLayout

    private val familyToLocations = mapOf(
        "Nature Spirits" to listOf("Gear Savanna", "Night Canyon", "Primary Village"),
        "Deep Savers" to listOf("Dragon's Eye Lake", "Ice Sanctuary"),
        "Nightmare Soldiers" to listOf("Fluorescent Cave", "Signpost Forest"),
        "Wind Guardians" to listOf("Beetle Forest", "Toy Town"),
        "Metal Empire" to listOf("Factorial Town"),
        "Unknown" to listOf("Binary Castle"),
        "Dark Area" to listOf("Mt. Infinity"),
        "Virus Busters" to listOf("Ice Sanctuary", "File City"),
        "Dragon's Roar" to listOf("Boncano Volcano", "Drill Tunnel"),
        "Jungle Troopers" to listOf("Ancient Dino Valley")
    )

    private val locationCoordinates = mapOf(
        "Primary Village" to Pair(0.5f, 0.682f),
        "File City" to Pair(0.5f, 0.587f),
        "Beetle Forest" to Pair(0.5f, 0.449f),
        "Dragon's Eye Lake" to Pair(0.455f, 0.224f),
        "Drill Tunnel" to Pair(0.387f, 0.141f),
        "Signpost Forest" to Pair(0.324f, 0.069f),
        "Factorial Town" to Pair(0.297f, 0.245f),
        "Fluorescent Cave" to Pair(0.238f, 0.069f),
        "Gear Savanna" to Pair(0.134f, 0.163f),
        "Night Canyon" to Pair(0.043f, 0.313f),
        "Toy Town" to Pair(0.112f, 0.419f),
        "Ice Sanctuary" to Pair(0.236f, 0.503f),
        "Ancient Dino Valley" to Pair(0.375f, 0.658f),
        "Boncano Volcano" to Pair(0.300f, 0.579f),
        "Binary Castle" to Pair(0.360f, 0.464f),
        "Mt. Infinity" to Pair(0.295f, 0.444f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        mapImageView = root.findViewById(R.id.map_image)
        markerContainer = root.findViewById(R.id.marker_container)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val digimon = arguments?.getParcelable<DigimonDetails_RootData>("digimon") ?: run {
            Log.e(TAG, "No se recibió el Digimon en los argumentos")
            return
        }

        Log.d(TAG, "Digimon recibido: ${digimon.name}")
        val bottomNavigationView = activity?.findViewById<View>(R.id.bottom_nav_view)
        bottomNavigationView?.visibility = View.GONE

        mapImageView.setImageResource(R.drawable.map)

        val shownLocations = mutableSetOf<String>()

        digimon.fields.forEach { field ->
            val family = field.field
            val locations = familyToLocations[family]
            if (locations != null) {
                Log.d(TAG, "Familia '$family' -> Ubicaciones: $locations")
                shownLocations.addAll(locations)
            } else {
                Log.w(TAG, "Familia '$family' no encontrada en el mapa")
            }
        }

        mapImageView.post {
            val width = mapImageView.width
            val height = mapImageView.height
            Log.d(TAG, "Tamaño del mapa: width=$width, height=$height")

            shownLocations.forEach { location ->
                val coords = locationCoordinates[location]
                if (coords == null) {
                    Log.w(TAG, "Ubicación '$location' no tiene coordenadas definidas")
                    return@forEach
                }

                val x = coords.first * width
                val y = coords.second * height

                Log.d(TAG, "Mostrando marcador en '$location': x=$x, y=$y")

                if (!isAdded) return@post // o return@observe, o return@launch, etc.

                val marker = LayoutInflater.from(requireContext()).inflate(R.layout.map_marker, markerContainer, false)
                marker.rotation = 45f // Gira 45 grados a la izquierda
                marker.x = x
                marker.y = y
                markerContainer.addView(marker)
                Log.d(TAG, "Marcador añadido para '$location'")

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

}
