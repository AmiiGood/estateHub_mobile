# EstateHub Mobile

**Aplicación móvil de análisis y gestión inmobiliaria con IA integrada**

## Descripción

EstateHub es una aplicación móvil completa para el mercado inmobiliario mexicano que combina análisis de propiedades con inteligencia artificial. Permite a los usuarios explorar propiedades, analizar ubicaciones con datos demográficos y económicos, y agendar visitas, todo en una interfaz moderna construida con Jetpack Compose.

## Características

### Exploración de Propiedades
- **Catálogo completo** con búsqueda y filtros avanzados
- **Detalles enriquecidos** con galería de imágenes, mapas interactivos y amenidades
- **Filtros inteligentes** por precio, ubicación, tipo, habitaciones y más
- **Sistema de categorías** (En venta, En renta)

### Análisis Geográfico con IA
- **Análisis demográfico** de colonias y códigos postales
- **Información económica** con datos del INEGI
- **Puntos de interés** cercanos (comercios, servicios, educación)
- **Estimaciones de precios** generadas por Gemini AI
- **Análisis de plusvalía** y rentabilidad proyectada
- **Recomendaciones de negocios** basadas en la ubicación

### Sistema de Citas
- **Agendamiento inteligente** con selección de fecha y horario
- **Horarios disponibles** en tiempo real
- **Notificaciones** de confirmación

### Gestión de Usuario
- **Registro e inicio de sesión** seguros
- **Perfil personalizado**

## Tecnologías

### Frontend
- **Jetpack Compose** - UI moderna y declarativa
- **Material Design 3** - Componentes y estilos actualizados
- **Coil** - Carga eficiente de imágenes
- **Navigation Compose** - Navegación entre pantallas
- **Accompanist** - Permisos, Pager y utilidades

### Backend & APIs
- **Retrofit** - Cliente HTTP para APIs REST
- **Gson** - Serialización/deserialización JSON
- **Google Maps SDK** - Mapas y ubicación
- **Google Places API** - Autocompletado de direcciones
- **Gemini AI API** - Análisis inteligente con IA

### Arquitectura & Patrones
- **Clean Architecture** - Separación en capas (UI, Domain, Data)
- **MVVM** - ViewModels con LiveData/StateFlow
- **Hilt** - Inyección de dependencias
- **Repository Pattern** - Abstracción de fuentes de datos
- **Use Cases** - Lógica de negocio encapsulada

### Almacenamiento & Estado
- **DataStore** - Preferencias y tokens seguros
- **Room** - Base de datos local
- **Kotlin Coroutines** - Operaciones asíncronas
- **Flow** - Manejo reactivo de datos

## Estructura del Proyecto
```
app/src/main/java/com/oscar/estatehubcompose/
├── analisis/              # Módulo de análisis geográfico con IA
│   ├── data/             
│   │   ├── network/      # Clientes y servicios de API
│   │   └── AnalisisRepository.kt
│   ├── domain/           # Casos de uso
│   └── ui/               # Pantallas y ViewModels
├── citas/                # Sistema de agendamiento
│   ├── data/
│   ├── domain/
│   └── ui/
├── properties/           # Gestión de propiedades
│   ├── data/
│   ├── domain/
│   └── ui/
├── login/                # Autenticación
├── register/             # Registro de usuarios
├── perfil/               # Perfil de usuario
├── core/                 # Configuración central
│   └── DI/               # Módulos de inyección de dependencias
├── helpers/              # Utilidades compartidas
└── ui/theme/             # Tema y estilos
```

## Instalación

### Prerrequisitos
- Android Studio Hedgehog | 2023.1.1 o superior
- JDK 11 o superior
- Android SDK (API 30+)
- Cuenta de Google Cloud (para APIs de Maps y Places)
- API Key de Gemini AI

### Configuración

1. **Clonar el repositorio**
```bash
git clone https://github.com/AmiiGood/estateHub_mobile.git
cd estatehub_mobile
```

2. **Configurar las API Keys**

Crear archivo `local.properties` en la raíz del proyecto:
```properties
BASE_URL=https://backend-api.com/
API_KEY=google_maps_api_key
GEMINI_KEY=gemini_api_key
```

3. **Sincronizar dependencias**
```bash
./gradlew build
```

4. **Ejecutar la aplicación**
- Conecta un dispositivo Android o inicia un emulador
- Click en "Run" en Android Studio o ejecuta:
```bash
./gradlew installDebug
```

## Arquitectura

### Clean Architecture con MVVM
```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (UI - Jetpack Compose + ViewModels)   │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│          Domain Layer                   │
│      (Use Cases + Entities)             │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│           Data Layer                    │
│  (Repositories + Data Sources)          │
│  • Remote (Retrofit)                    │
│  • Local (DataStore)                    │
└─────────────────────────────────────────┘
```

### Flujo de Datos
```
Usuario → UI (Compose) → ViewModel → UseCase → Repository → API/DB
                ↓           ↓          ↓          ↓
              State    LiveData    Result    Response
```

## Pantallas Principales

### 1. Login & Registro
- Autenticación segura con validación en tiempo real
- Gestión de sesión con tokens JWT
- Diseño limpio y accesible

### 2. Mercado de Propiedades
- Vista de tarjetas con carrusel de imágenes
- Filtros avanzados por múltiples criterios
- Pull-to-refresh para actualizar
- Búsqueda por ubicación con normalización de texto

### 3. Detalle de Propiedad
- Galería de imágenes con indicadores
- Información completa (precio, características, amenidades)
- Mapa interactivo con ubicación
- Botón de agendamiento de cita
- Compartir ubicación

### 4. Análisis Geográfico
- Mapa con círculo de 1km de radio
- Búsqueda de ubicaciones con autocompletado
- Información demográfica detallada (INEGI)
- Análisis con IA (precios, plusvalía, rentabilidad)
- Recomendaciones de negocios
- Interfaz expandible/colapsable

### 5. Perfil de Usuario
- Información personal
- Propiedades publicadas

## Diseño UI/UX

### Tema Personalizado
```kotlin
val PrimaryPersonalized = Color(0xFF101828)
val SecondaryPersonalized = Color(0xFFD0D5DD)
val ThirdPersonalized = Color(0xFFE4E7EC)
```

### Tipografía
- **Fuente principal:** Parkinsans (Google Fonts)
- Variantes: Light, Regular, Medium, SemiBold, Bold, ExtraBold

### Componentes Reutilizables
- `FilterDialog` - Diálogo de filtros avanzados
- `AgendarCitaDialog` - Modal de agendamiento
- `PropertyCard` - Tarjeta de propiedad con carrusel
- `ErrorView` - Vista de error con retry
- `StatusBadge` - Insignias de estado

## Seguridad

- **Tokens JWT** almacenados en DataStore cifrado
- **HTTPS** para todas las comunicaciones
- **API Keys** en `local.properties` (no versionadas)
- **Validación de entrada** en formularios
- **Manejo seguro de permisos** (ubicación, notificaciones)

## Dependencias Principales
```gradle
// Core
implementation("androidx.core:core-ktx:1.10.1")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

// Compose
implementation(platform("androidx.compose:compose-bom:2024.09.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")

// Dependency Injection
implementation("com.google.dagger:hilt-android:2.51.1")
kapt("com.google.dagger:hilt-android-compiler:2.51.1")

// Maps & Location
implementation("com.google.maps.android:maps-compose:4.4.1")
implementation("com.google.android.gms:play-services-location:21.0.1")
implementation("com.google.android.libraries.places:places:3.3.0")

// Image Loading
implementation("io.coil-kt:coil-compose:2.5.0")
```

## Contribuciones

Este es un proyecto académico desarrollado para Integradora de último cuatrimestre en la Universidad Tecnológica de León

## Licencia

Este proyecto es de uso académico y propiedad de **SweetCode**

## Autores

**Alexis, Oscar, Zuri, Viví**

---

**EstateHub** - Análisis Inmobiliario Inteligente