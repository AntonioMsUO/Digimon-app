# Digimon Dex - Aplicación de Android  

## Descripción  
Digimon Dex es una aplicación basada en la API [Digi-API](https://digi-api.com/) que permite explorar información detallada sobre los Digimon de la franquicia, incluyendo sus características, evoluciones y ubicación representativa en un mapa.  

## Arquitectura
La arquitectura de la app sigue el patrón MVVM (Model-View-ViewModel) y está organizada en capas bien diferenciadas para separar la lógica de negocio, la gestión de datos y la interfaz de usuario. 
A continuación se describe cada parte y su interacción:

**1. Capa de Modelo (Model)**
- Incluye las clases de datos (`Digimon`, `DigimonDetails_RootData`, `Attribute`, `Level`, `Type`, etc.) que representan la información que se obtendrá de la API, así como las entidades de la base de datos local (`FavoriteDigimon`).
- Los modelos son simples data classes, algunos implementan `Parcelable` para facilitar el paso de datos entre fragmentos para el mapeo de ubicaciones.

**2. Capa de Datos (Data)**
- **Acceso a API:**  
  - `DigimonApiService` define las llamadas a la Digi-API usando Retrofit.
  - `RetrofitClient` configura Retrofit y Moshi para la serialización JSON.
  - `Repository` centraliza el acceso a la API y expone métodos suspendidos que devuelven flujos (`Flow`) de resultados envueltos en `ApiResult` (éxito, error, cargando).
- **Persistencia local:**  
  - Se usa Room para almacenar favoritos.  
  - `AppDatabase` define la base de datos y el DAO (`FavoriteDigimonDao`) para operaciones CRUD sobre favoritos.
  - `FavoritesRepository` expone métodos para añadir, eliminar y consultar favoritos, y mantiene un `LiveData` reactivo con la lista de favoritos.

**3. Capa de Dominio (ViewModel)**
- **`HomeViewModel`:**  
  - Gestiona la lista principal de Digimon, la paginación, los filtros y la sincronización con los favoritos.
  - Expone `LiveData` para la lista de Digimon, el estado de la UI y los favoritos.
  - Permite alternar el estado de favorito y aplicar filtros.
- **`DigimonDetailViewModel`:**  
  - Gestiona la obtención y exposición de los detalles de un Digimon concreto.
  - Expone un `LiveData` con los detalles.
- **`FavoritesViewModel`:**  
  - Gestiona la lista de favoritos y expone métodos para añadir/eliminar favoritos y consultar si un Digimon es favorito.

**4. Capa de Interfaz de Usuario (UI)**
- **Fragments:**  
  - `HomeFragment`: Muestra la lista principal, permite buscar, filtrar y marcar favoritos.
  - `DigimonDetailFragment`: Muestra los detalles de un Digimon, sus evoluciones, habilidades y permite navegar al mapa.
  - `FavoritesFragment`: Muestra la lista de favoritos y permite eliminarlos.
  - `MapFragment`: Muestra un mapa con marcadores según las familias del Digimon.
  - `SettingsFragment`: Permite cambiar tema e idioma.
- **Adapters y ViewHolders:**  
  - Adaptadores para listas (`DigimonListRecyclerViewAdapter`, `FavoritesListAdapter`) y sus respectivos ViewHolders para mostrar los datos.
- **Diálogos:**  
  - `FilterDialogFragment` permite aplicar filtros avanzados a la lista de Digimon.

**5. Navegación y Configuración**
- Se usa Navigation Component para la navegación entre fragmentos.
- `MainActivity` gestiona la barra de navegación inferior y aplica el idioma guardado.
- `DigimonApp` inicializa el tema y el idioma según las preferencias del usuario.

**6. Utilidades**
- `LocaleHelper` gestiona el cambio de idioma de la app.

**Resumen de flujo de datos:**
- La UI observa los datos expuestos por los ViewModel mediante `LiveData`.
- Los ViewModel obtienen los datos del `Repository` (API o base de datos local).
- Los cambios en favoritos se reflejan automáticamente en la UI gracias a la reactividad de Room y LiveData.
- La navegación y la configuración de la app están centralizadas y desacopladas de la lógica de negocio.

## Mapa de relaciones entre clases




