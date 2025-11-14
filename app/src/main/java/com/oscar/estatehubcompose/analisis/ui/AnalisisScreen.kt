package com.oscar.estatehubcompose.analisis.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.oscar.estatehubcompose.analisis.Models.GeocodificadorInfo
import com.oscar.estatehubcompose.ui.theme.Parkinsans

@SuppressLint("MissingPermission")
@Composable
fun AnalisisScreen(modifier: Modifier, analisisViewModel: AnalisisViewModel) {
    val context = LocalContext.current
    var ubicacionDispositivo by remember { mutableStateOf<Location?>(null) }


    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val coordenadaInicial = LatLng(21.0190, -101.2574)
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(coordenadaInicial, 25f)
    }

    LaunchedEffect(Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                ubicacionDispositivo = it
                Log.d("Ubicacion", "Ubicación obtenida: ${it.latitude}, ${it.longitude}")
                val nuevaUbicacion = LatLng(it.latitude, it.longitude)
                cameraPosition.position = CameraPosition.fromLatLngZoom(nuevaUbicacion, 17f)
            }
        }.addOnFailureListener { exception ->
            Log.e("Ubicacion", "Error obteniendo ubicación: ${exception.message}")
        }
    }

    Mapa(modifier, cameraPosition, ubicacionDispositivo, context, analisisViewModel)
}


@Composable
fun Mapa(
    modifier: Modifier,
    cameraPosition: CameraPositionState,
    ubicacionDispositivo: Location?,
    context: Context,
    analisisViewModel: AnalisisViewModel
) {


    //Se declara un tipo de mapa
    val mapaType by remember {
        mutableStateOf(MapProperties(mapType = MapType.TERRAIN, isMyLocationEnabled = true))
    }
    //Se declara una variable donde se guardara la busqueda
    var busqueda by rememberSaveable { mutableStateOf("") }
    //Se declara una lista donde iran las predicciones de dicha busqueda
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    //Se declara la latitud y longitud del resultado de la busqueda
    var selectedPlace by remember { mutableStateOf<LatLng?>(null) }
    //Se declara el cliente de places
    val placesClient = remember { Places.createClient(context) }
    val data by analisisViewModel.data.observeAsState();


    LaunchedEffect(Unit) {
        analisisViewModel.getData(ubicacionDispositivo?.latitude ?: 21.121153, ubicacionDispositivo?.longitude ?: -101.682511)
    }


    Box(modifier.fillMaxSize()) {
        // Mapa
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapaType,
            cameraPositionState = cameraPosition,
            uiSettings = MapUiSettings(myLocationButtonEnabled = true),
            contentPadding = PaddingValues(
                top = 80.dp,
                bottom = 150.dp
            )
        ) {
            selectedPlace?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = busqueda,

                )
            }
        }

        // Columna superior con buscador y sugerencias
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // TextField de búsqueda
            OutlinedTextField(
                value = busqueda,
                //Cada que el tecto cambie se ejecutara buscarLugares que arroja una lista de posibles lugares
                onValueChange = { query ->
                    busqueda = query
                    buscarLugares(placesClient, query) { lugares ->
                        predictions = lugares
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Medium
                ),
                trailingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Buscar")
                },
                placeholder = { Text("Busca una ubicación") }
            )

            // Esta es la ui de dicha lista que se ejecuta en el input
            // Si el input no esta vacio se muestra lo siguiente
            if (predictions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .padding(top = 4.dp)
                        .shadow(4.dp, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                ) {
                    items(predictions) { prediction ->
                        Column {
                            Text(
                                text = prediction.getFullText(null).toString(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    //Funcion de click para cada elemento de la lista
                                    .clickable {
                                        //Se ejecuta obtenerDetalles lugar
                                        obtenerDetallesLugar(
                                            placesClient,
                                            prediction.placeId
                                        ) { latLng ->
                                            selectedPlace = latLng
                                            cameraPosition.position =
                                                CameraPosition.fromLatLngZoom(latLng, 15f)
                                            busqueda = prediction.getPrimaryText(null).toString()
                                            predictions = emptyList();

                                            analisisViewModel.getData(latLng.latitude, latLng.longitude);

                                        }
                                    }
                                    .padding(12.dp),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = Parkinsans
                                )
                            )
                            if (prediction != predictions.last()) {
                                Divider(color = Color.LightGray, thickness = 0.5.dp)
                            }
                        }
                    }
                }
            }
        }

        // Card de propiedad en la parte inferior
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        ) {
            CardPropiedad(data)
        }
    }
}

@Composable
fun CardPropiedad(data: GeocodificadorInfo?) {

    var expanded by rememberSaveable { mutableStateOf(false) }


    Row(
        Modifier
            .padding(2.dp)
            .clickable{
                expanded = !expanded
            }
            .shadow(8.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Column(verticalArrangement = Arrangement.Bottom) {
            Text(
                "${data?.codigoPostal}",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Parkinsans
                )
            )

            Text(
                "Jardines de jerez",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Parkinsans
                )
            )


                AnimatedVisibility(visible = expanded
                ) {
                    PropiedadesExpanded(Modifier.animateContentSize());
                }


        }
    }
}


@Composable
fun PropiedadesExpanded(modifier:Modifier){
    Column() {
        Text("Texto expanded")
        Text("Texto expanded")
        Text("Texto expanded")
        Text("Texto expanded")
    }
};



fun buscarLugares(
    placesClient: PlacesClient,
    query: String,
    onResult: (List<AutocompletePrediction>) -> Unit
) {
    // Si el query está vacío, limpiar las predicciones
    if (query.isEmpty()) {
        onResult(emptyList())
        return
    }

    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            onResult(response.autocompletePredictions)
        }
        .addOnFailureListener { exception ->
            Log.e("Places", "Error buscando lugares", exception)
            onResult(emptyList())
        }
}

fun obtenerDetallesLugar(
    placesClient: PlacesClient,
    placeId: String,
    onResult: (LatLng) -> Unit
) {
    val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.NAME)
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response ->
            response.place.latLng?.let { onResult(it) }
        }
        .addOnFailureListener { exception ->
            Log.e("Places", "Error obteniendo detalles", exception)
        }
}
