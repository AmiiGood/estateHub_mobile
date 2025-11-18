package com.oscar.estatehubcompose.analisis.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Woman
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.google.maps.android.compose.Circle
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

                cameraPosition.position = CameraPosition.fromLatLngZoom(nuevaUbicacion, 14f)
                analisisViewModel.getData(ubicacionDispositivo?.latitude ?: 21.121153, ubicacionDispositivo?.longitude ?: -101.682511)
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


            selectedPlace?.let { place ->
                Marker(
                    state = MarkerState(position = place),
                    title = busqueda,
                )

                Circle(
                    center = place,
                    radius = 1000.0,
                    fillColor = Color(0x4D4285F4),
                    strokeWidth = 2f
                )
            } ?: run {
                Circle(
                    center = LatLng(
                        ubicacionDispositivo?.latitude ?: 21.121153,
                        ubicacionDispositivo?.longitude ?: -101.682511
                    ),
                    radius = 1000.0,
                    fillColor = Color(0x4D4285F4),
                    strokeWidth = 2f
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
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF101828)
                ),

                trailingIcon = {

                    if (busqueda.isNotEmpty()){
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Limpiar busqueda",
                            Modifier.clickable{
                                busqueda = "";
                                predictions = emptyList();
                            })
                    }else {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Buscar",)

                    }

                },
                placeholder = { Text("Busca una ubicación") },
                singleLine = true
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
                                    fontFamily = Parkinsans,
                                    color = Color(0xFF101828)
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


    Column(
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
    ) {
        Row(

        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
                Text(
                    "${data?.codigoPostal}",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Parkinsans,
                        color = Color(0xFF101828)
                    )
                )

                Text(
                    "${data?.colonia}",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Parkinsans,
                        color = Color(0xFF101828)
                    )
                )


            }

            Column(
                modifier = Modifier.wrapContentWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "${data?.localidad}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Parkinsans,
                        color = Color(0xFF101828)
                    )
                )
                Text(
                    "${data?.estado}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Parkinsans,
                        color = Color(0xFF101828)
                    )
                )
            }

        }

        AnimatedVisibility(
            visible = expanded
        ) {
            PropiedadesExpanded(Modifier.animateContentSize().verticalScroll(rememberScrollState()), data);
        }
    }


}







@Composable
fun PropiedadesExpanded(modifier:Modifier, data: GeocodificadorInfo?){

    var expanded by rememberSaveable { mutableStateOf(false) }
    var expanded2 by rememberSaveable { mutableStateOf(false) }

    Column {
        Column(modifier.clickable{
            expanded = !expanded
        }) {
            Column (Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))){
                Spacer(Modifier.padding(10.dp))
                Row(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
                    verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(10.dp)){
                        Icon(imageVector = Icons.Filled.Info,
                            contentDescription = "Informacion geografica",
                            tint = MaterialTheme.colorScheme.secondary)
                    }

                    Spacer(Modifier.padding(10.dp))
                    Text("Informacion Geografica");
                }
                AnimatedVisibility(visible = expanded) {
                    Column(Modifier.padding(5.dp)) {
                        Text("Informacion general:",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = Parkinsans,
                                color = Color(0xFF101828)
                            ));
                        Atributo(Modifier, Icons.Filled.CreditCard, Color(0XFF8AA624), "Nivel socieconomico:","${data?.nse}")
                        Atributo(Modifier, Icons.Filled.Groups, Color(0xFFDBE4C9), "Poblacion total:", "${data?.ponlacionTotal}")
                        Atributo(Modifier, Icons.Filled.Apartment, Color(0xFFFEEE91),"Personas con trabajo:","${data?.empleados}");
                        Spacer(Modifier.padding(10.dp))
                        Text("Educacion: ",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = Parkinsans,
                                color = Color(0xFF101828)

                            ));
                        Atributo(Modifier, Icons.Filled.School, Color.Green, "Poblacion analfabeta:","${data?.pob_analfabeta}")
                        Atributo(Modifier, Icons.Filled.School, Color.Green, "Poblacion con secundaria:","${data?.pob_secundaria}")
                        Atributo(Modifier, Icons.Filled.School, Color.Green, "Poblacion sin estudios:","${data?.pob_noEducacion}")

                        Spacer(Modifier.padding(10.dp))
                        Text("Viviendas:",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = Parkinsans,
                                color = Color(0xFF101828)
                            ));
                        Atributo(Modifier, Icons.Filled.Apartment, Color(0xFFE67E22),"Viviendas habitadas:","${data?.viviendas_habitadas}");
                        Atributo(Modifier, Icons.Filled.Apartment, Color(0xFFE67E22),"Viviendas con automovil:","${data?.viviendas_automovil}");

                        Spacer(Modifier.padding(10.dp))
                        Text("Informacion de la poblacion:",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = Parkinsans,
                                color = Color(0xFF101828)
                            ));


                        Atributo(Modifier, Icons.Filled.Man, Color(0xFFB87C4C),"Hombres:","${data?.hombres}");
                        Atributo(Modifier, Icons.Filled.Woman, Color(0xFFB87C4C),"Mujeres:","${data?.mujeres}");
                        Atributo(Modifier, Icons.Filled.Groups, Color(0xFFB87C4C),"15 a 64:","${data?.quince_seisCuatro}");
                        Atributo(Modifier, Icons.Filled.Man, Color(0xFFB87C4C),"18 a 24:","${data?.diezOcho_veninteCuatro}");
                        Atributo(Modifier, Icons.Filled.Man, Color(0xFFB87C4C),"+60:","${data?.mas_sesenta}");
                        Atributo(Modifier, Icons.Filled.Man, Color(0xFFB87C4C),"+65:","${data?.mas_seisCinco}");




                        Spacer(Modifier.padding(10.dp))
                        Text("Negocios en la zona:",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = Parkinsans,
                                color = Color(0xFF101828)
                            ));
                        Atributo(Modifier, Icons.Filled.HealthAndSafety, Color(0xFFFE6244),"Salud:","${data?.hospitales_farmacias}");
                        Atributo(Modifier, Icons.Filled.Fastfood, Color(0xFFFF8040),"Restaurantes:","${data?.restaurantes}");
                        Atributo(Modifier, Icons.Filled.School, Color(0xFF0046FF),"Educacion:","${data?.educacion}");
                        Atributo(Modifier, Icons.Filled.AccountBalance, Color(0xFF84994F),"Financieros:","${data?.financiero}");
                        Atributo(Modifier, Icons.Filled.Nature, Color(0xFF347433),"Parques:","${data?.parques}");
                        Atributo(Modifier, Icons.Filled.TheaterComedy, Color(0xFF347433),"Entretenimiento:","${data?.entretenimiento}");
                        Atributo(Modifier, Icons.Filled.Handshake, Color(0xFF7FDBDA),"Comercio:","${data?.negocios}");
                        Atributo(Modifier, Icons.Filled.LocalParking, Color.White,"Estacionamiento:","${data?.estacionamientos}");
                    }


                }
            }
        }

        Column(modifier.clickable{
            expanded2 = !expanded2
        }) {
            Spacer(Modifier.padding(10.dp))

            Row(Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(10.dp)){
                    Icon(imageVector = Icons.Filled.Analytics,
                        contentDescription = "Analizar",
                        tint = MaterialTheme.colorScheme.secondary)
                }

                Spacer(Modifier.padding(10.dp))
                Text("Enriquecer informacion");
            }
            AnimatedVisibility(visible = expanded2) {
                Text("INFORMACION PARA ANALIZAR") }


        }
    }


};

@Composable
fun Atributo(modifier: Modifier, icono: ImageVector, color: Color,texto:String, data: String){

    Spacer(Modifier.padding(5.dp))
    Column(modifier.fillMaxWidth()) {


        Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            Box(Modifier.clip(RoundedCornerShape(5.dp))
                .background(color)
                .padding(5.dp),
                contentAlignment = Alignment.Center){
                Icon(imageVector = icono, contentDescription = "NSE", Modifier.size(14.dp));
            }

            Spacer(Modifier.padding(5.dp))
            Text(texto,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Parkinsans,
                    color = Color(0xFF101828)
                ));
            Spacer(Modifier.padding(3.dp))
            Text("${data}",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Parkinsans,
                    color = Color(0xFF101828)
                ));
        }
    }
}



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
