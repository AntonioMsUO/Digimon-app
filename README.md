# Digimon Dex - Aplicación de Android  

## Descripción  
Digimon Dex es una aplicación basada en la API [Digi-API](https://digi-api.com/) que permite explorar información detallada sobre los Digimon de la franquicia, incluyendo sus características, evoluciones y ubicación representativa en un mapa.  

## Funcionalidades  

### Pantalla de Inicio  
- **Mensaje de bienvenida** al abrir la aplicación.  
- **Navegación rápida** a las principales secciones: Listado de Digimon, Favoritos, Mapa y Configuración.  

### Listado de Digimon  
- Muestra un **listado completo** de los 1460 Digimon disponibles en Digi-API.  
- Cada Digimon se muestra con su **imagen, nombre, nivel y atributos principales**.  
- **Búsqueda** por nombre, nivel, atributo y familia.  
- **Filtros** para encontrar Digimon según características específicas.  

### Detalle de Digimon  
- Información detallada de cada Digimon:  
  - Imágenes oficiales.  
  - Nivel, atributos y familias.  
  - Posibles evoluciones y pre-evoluciones.  
- **Mapa de evolución** interactivo para visualizar las posibles ramificaciones evolutivas.  

### Mapa Interactivo  
- Muestra una ubicación representativa de cada Digimon en un **mapa interactivo**.  
- Cada marcador ofrece una breve descripción del Digimon y acceso a su ficha detallada.  

### Favoritos  
- Permite **marcar Digimon como favoritos** para acceder rápidamente a ellos.  
- Gestión de favoritos: añadir y eliminar Digimon de la lista.  

### Configuración  
- **Selección de idioma** (Español/Inglés).  
- **Modo claro/oscuro**.  
- Configuración de preferencias del usuario.  

### Almacenamiento y Conectividad  
- Uso de **Room** para almacenamiento local, permitiendo acceso offline.  
- **Sincronización automática** cuando hay conexión a internet.  

## Requisitos Técnicos  
- Compatible con **API 21+** (mínimo Nexus 7 y teléfonos Android antiguos).  
- Arquitectura **MVVM** con separación en paquetes.  
- Uso de **Navigation Component** y **fragmentos**.  
- Código en **Kotlin**, legible y comentado.  
- Uso de imágenes con licencia adecuada.  
- Implementación de **pruebas de UI**.  
