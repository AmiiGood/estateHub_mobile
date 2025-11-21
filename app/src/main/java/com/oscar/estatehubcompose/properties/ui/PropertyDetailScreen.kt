package com.oscar.estatehubcompose.properties.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.oscar.estatehubcompose.properties.data.network.response.PropiedadDetail
import com.oscar.estatehubcompose.ui.theme.*
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun PropertyDetailScreen(
    propertyId: Int,
    propertyDetailViewModel: PropertyDetailViewModel,
    navController: NavController
) {
    val propertyDetail by propertyDetailViewModel.propertyDetail.observeAsState()
    val isLoading by propertyDetailViewModel.isLoading.observeAsState(false)
    val error by propertyDetailViewModel.error.observeAsState()

    LaunchedEffect(propertyId) {
        propertyDetailViewModel.loadPropertyDetail(propertyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Propiedad") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = PrimaryPersonalized
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = PrimaryPersonalized
                )
            )
        },
        bottomBar = {
            propertyDetail?.let { propiedad ->
                BottomBar(propiedad = propiedad)
            }
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryPersonalized)
                }
            }
            error != null -> {
                ErrorView(
                    error = error ?: "Error desconocido",
                    onRetry = { propertyDetailViewModel.loadPropertyDetail(propertyId) },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            propertyDetail != null -> {
                PropertyDetailContent(
                    propiedad = propertyDetail!!,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PropertyDetailContent(
    propiedad: PropiedadDetail,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()
    val imageCount = propiedad.imagenes.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        userScrollEnabled = true
    ) {
        // Carrusel de imágenes
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                if (imageCount > 0) {
                    HorizontalPager(
                        count = imageCount,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = propiedad.imagenes[page].urlImagen,
                            contentDescription = propiedad.titulo,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Indicadores
                    if (imageCount > 1) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            repeat(imageCount) { index ->
                                Surface(
                                    modifier = Modifier.size(8.dp),
                                    shape = CircleShape,
                                    color = if (index == pagerState.currentPage)
                                        Color.White
                                    else
                                        Color.White.copy(alpha = 0.5f)
                                ) {}
                            }
                        }
                    }

                    // Contador de imágenes
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = PrimaryPersonalized.copy(alpha = 0.85f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${pagerState.currentPage + 1}/$imageCount",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Información principal
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Título y precio
                Text(
                    text = propiedad.titulo,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPersonalized
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = formatPrice(propiedad.precioVenta ?: propiedad.precioRenta ?: 0.0),
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPersonalized
                        )
                    )
                    if (propiedad.precioRenta != null && propiedad.precioRenta > 0) {
                        Text(
                            text = "/ mes",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = Parkinsans,
                                fontWeight = FontWeight.Normal,
                                color = SeventhPersonalized
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Badges de características especiales
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        if (propiedad.estatus == "disponible") {
                            StatusBadge(
                                text = "Disponible",
                                icon = Icons.Default.CheckCircle,
                                backgroundColor = Color(0xFF10B981),
                                textColor = Color.White
                            )
                        }
                    }
                    item {
                        if (propiedad.credito) {
                            StatusBadge(
                                text = "Acepta crédito",
                                icon = Icons.Default.AccountBalance,
                                backgroundColor = Color(0xFF3B82F6),
                                textColor = Color.White
                            )
                        }
                    }
                    item {
                        if (propiedad.alberca) {
                            StatusBadge(
                                text = "Con alberca",
                                icon = Icons.Default.Pool,
                                backgroundColor = Color(0xFF06B6D4),
                                textColor = Color.White
                            )
                        }
                    }
                    item {
                        StatusBadge(
                            text = propiedad.tipoPropiedad.capitalize(Locale.getDefault()),
                            icon = Icons.Default.Home,
                            backgroundColor = ThirdPersonalized,
                            textColor = PrimaryPersonalized
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ubicación
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = SeventhPersonalized,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "${propiedad.direccion}, ${propiedad.colonia}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Normal,
                            color = SeventhPersonalized
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${propiedad.ciudad}, ${propiedad.estado} - CP ${propiedad.codigoPostal}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Normal,
                        color = SeventhPersonalized
                    ),
                    modifier = Modifier.padding(start = 28.dp)
                )
            }
        }

        // Características principales
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(12.dp),
                color = ThirdPersonalized
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FeatureItem(
                        icon = Icons.Default.Hotel,
                        label = "Habitaciones",
                        value = propiedad.numHabitaciones.toString()
                    )
                    FeatureItem(
                        icon = Icons.Default.Bathtub,
                        label = "Baños",
                        value = propiedad.numBanios.toString()
                    )
                    FeatureItem(
                        icon = Icons.Default.SquareFoot,
                        label = "m²",
                        value = propiedad.metrosCuadrados.toInt().toString()
                    )
                    FeatureItem(
                        icon = Icons.Default.DirectionsCar,
                        label = "Estacionamiento",
                        value = propiedad.numEstacionamiento.toString()
                    )
                }
            }
        }

        // Descripción
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Descripción",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPersonalized
                        )
                    )

                    Text(
                        text = "ID: #${propiedad.idPropiedad}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Medium,
                            color = SeventhPersonalized
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = propiedad.descripcion,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Normal,
                        color = SeventhPersonalized,
                        lineHeight = 22.sp
                    )
                )
            }
        }

        // Amenidades
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Amenidades",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPersonalized
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                val amenidades = buildList {
                    if (propiedad.residencial) add("Residencial" to Icons.Default.Apartment)
                    if (propiedad.jardin) add("Jardín" to Icons.Default.Grass)
                    if (propiedad.alberca) add("Alberca" to Icons.Default.Pool)
                    if (propiedad.sotano) add("Sótano" to Icons.Default.Stairs)
                    if (propiedad.terraza) add("Terraza" to Icons.Default.Balcony)
                    if (propiedad.cuartoServicio) add("Cuarto de servicio" to Icons.Default.Room)
                    if (propiedad.muebles) add("Amueblado" to Icons.Default.Chair)
                    if (propiedad.credito) add("Acepta crédito" to Icons.Default.CreditCard)
                    add("${propiedad.plantas} ${if (propiedad.plantas > 1) "plantas" else "planta"}" to Icons.Default.Layers)
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    amenidades.chunked(2).forEach { rowAmenidades ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowAmenidades.forEach { (amenidad, icon) ->
                                AmenityChip(
                                    label = amenidad,
                                    icon = icon,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Espaciador si solo hay un elemento en la fila
                            if (rowAmenidades.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // Mapa de ubicación
        item {
            PropertyMapSection(propiedad = propiedad)
        }

        // Espaciado final
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun FeatureItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = PrimaryPersonalized,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Bold,
                color = PrimaryPersonalized
            )
        )
        Text(
            text = label,
            style = TextStyle(
                fontSize = 11.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Normal,
                color = SeventhPersonalized
            )
        )
    }
}

@Composable
fun AmenityChip(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = ThirdPersonalized
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryPersonalized,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryPersonalized
                )
            )
        }
    }
}

@Composable
fun BottomBar(propiedad: PropiedadDetail) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { /* TODO: Llamar */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PrimaryPersonalized
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Llamar",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Llamar",
                    style = TextStyle(
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                )
            }

            Button(
                onClick = { /* TODO: Agendar cita */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPersonalized
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Agendar",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Agendar cita",
                    style = TextStyle(
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@Composable
fun ErrorView(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = SeventhPersonalized
            )

            Text(
                text = "Algo salió mal",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryPersonalized
                )
            )

            Text(
                text = error,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Normal,
                    color = SeventhPersonalized
                )
            )

            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPersonalized
                )
            ) {
                Text(
                    "Reintentar",
                    style = TextStyle(
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    return formatter.format(price)
}

@Composable
fun PropertyMapSection(propiedad: PropiedadDetail) {
    var showFullScreenMap by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    val propertyLocation = LatLng(
        propiedad.latitud.toDoubleOrNull() ?: 0.0,
        propiedad.longitud.toDoubleOrNull() ?: 0.0
    )

    // URL de Google Static Maps API para imagen estática
    val staticMapUrl = remember(propertyLocation) {
        buildString {
            append("https://maps.googleapis.com/maps/api/staticmap?")
            append("center=${propertyLocation.latitude},${propertyLocation.longitude}")
            append("&zoom=15")
            append("&size=600x300")
            append("&markers=color:red%7C${propertyLocation.latitude},${propertyLocation.longitude}")
            append("&key=${com.oscar.estatehubcompose.BuildConfig.API_KEY}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Ubicación",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Bold,
                color = PrimaryPersonalized
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tarjeta con la dirección
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = ThirdPersonalized
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = PrimaryPersonalized,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Ubicación",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = propiedad.direccion,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryPersonalized
                        )
                    )
                    Text(
                        text = "${propiedad.colonia}, ${propiedad.ciudad}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Normal,
                            color = SeventhPersonalized
                        )
                    )
                }

                IconButton(
                    onClick = {
                        // Abrir en Google Maps
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            android.net.Uri.parse("https://maps.google.com/?q=${propertyLocation.latitude},${propertyLocation.longitude}")
                        )
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Abrir en mapas",
                        tint = PrimaryPersonalized
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Imagen estática del mapa - Mucho más ligera
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable { showFullScreenMap = true },
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 4.dp
        ) {
            Box {
                // Usar AsyncImage de Coil para la imagen estática del mapa
                AsyncImage(
                    model = staticMapUrl,
                    contentDescription = "Mapa de ${propiedad.direccion}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay sutil para indicar que es clickeable
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.05f))
                )

                // Botón de expandir
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    shape = CircleShape,
                    color = PrimaryPersonalized,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { showFullScreenMap = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Expandir mapa",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Texto indicativo
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Toca el mapa para verlo en pantalla completa",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Normal,
                color = SeventhPersonalized
            )
        )
    }

    // Diálogo de mapa en pantalla completa
    if (showFullScreenMap) {
        FullScreenMapDialog(
            propiedad = propiedad,
            onDismiss = { showFullScreenMap = false }
        )
    }
}

@Composable
fun StatusBadge(
    text: String,
    icon: ImageVector,
    backgroundColor: Color,
    textColor: Color
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenMapDialog(
    propiedad: PropiedadDetail,
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val propertyLocation = LatLng(
        propiedad.latitud.toDoubleOrNull() ?: 0.0,
        propiedad.longitud.toDoubleOrNull() ?: 0.0
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(propertyLocation, 15f)
    }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapType = MapType.NORMAL,
                    isMyLocationEnabled = false
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = false,
                    mapToolbarEnabled = true,
                    zoomGesturesEnabled = true,
                    scrollGesturesEnabled = true,
                    tiltGesturesEnabled = true
                )
            ) {
                Marker(
                    state = MarkerState(position = propertyLocation),
                    title = propiedad.titulo,
                    snippet = propiedad.direccion,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )

                Circle(
                    center = propertyLocation,
                    radius = 500.0,
                    fillColor = PrimaryPersonalized.copy(alpha = 0.1f),
                    strokeColor = PrimaryPersonalized.copy(alpha = 0.3f),
                    strokeWidth = 2f
                )
            }

            // Barra superior con botones
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = PrimaryPersonalized
                        )
                    }

                    Text(
                        text = "Ubicación",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPersonalized
                        )
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Botón de compartir
                        IconButton(
                            onClick = {
                                val shareText = """
                                    ${propiedad.titulo}
                                    ${propiedad.direccion}
                                    ${propiedad.colonia}, ${propiedad.ciudad}
                                    
                                    Ver ubicación: https://maps.google.com/?q=${propertyLocation.latitude},${propertyLocation.longitude}
                                """.trimIndent()

                                val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                    putExtra(android.content.Intent.EXTRA_SUBJECT, "Ubicación de ${propiedad.titulo}")
                                }
                                context.startActivity(
                                    android.content.Intent.createChooser(shareIntent, "Compartir ubicación")
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir ubicación",
                                tint = PrimaryPersonalized
                            )
                        }

                        // Botón de abrir en Google Maps
                        IconButton(
                            onClick = {
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    android.net.Uri.parse("https://maps.google.com/?q=${propertyLocation.latitude},${propertyLocation.longitude}")
                                )
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "Abrir en Google Maps",
                                tint = PrimaryPersonalized
                            )
                        }
                    }
                }
            }

            // Información en la parte inferior
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = propiedad.direccion,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryPersonalized
                        )
                    )
                    Text(
                        text = "${propiedad.colonia}, ${propiedad.ciudad}, ${propiedad.estado}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Normal,
                            color = SeventhPersonalized
                        )
                    )
                }
            }
        }
    }
}