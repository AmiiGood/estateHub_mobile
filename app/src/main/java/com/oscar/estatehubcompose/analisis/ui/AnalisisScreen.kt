package com.oscar.estatehubcompose.analisis.ui

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("MissingPermission")
@Composable
fun AnalisisScreen(modifier: Modifier){
    val context = LocalContext.current;
    var ubicacionDispositivo by remember { mutableStateOf<Location?>(null) };
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context);
    };
    var coordenadaInicial = LatLng(21.0190,-101.2574 );
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(coordenadaInicial, 25f)
    }


    LaunchedEffect(Unit) {

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                ubicacionDispositivo = it;
                Log.d("Ubicacion", "Ubicación obtenida: ${it.latitude}, ${it.longitude}")
                val nuevaUbicacion = LatLng(it.latitude, it.longitude)
                cameraPosition.position = CameraPosition.fromLatLngZoom(nuevaUbicacion, 17f)
            }
        }.addOnFailureListener { exception ->
            Log.e("Ubicacion", "Error obteniendo ubicación: ${exception.message}")
        }



        coordenadaInicial = LatLng(ubicacionDispositivo?.latitude ?: -101.2574,
            ubicacionDispositivo?.longitude ?: 21.0190)
    }



    Mapa(modifier, cameraPosition, ubicacionDispositivo);

}


@Composable
fun Mapa(modifier: Modifier, cameraPosition: CameraPositionState, ubicacionDispositivo: Location?){

    val mapaType by remember{ mutableStateOf(MapProperties(mapType = MapType.TERRAIN, isMyLocationEnabled = true)) }


    Box(modifier.fillMaxSize()){
        GoogleMap(modifier = Modifier.fillMaxSize(),
            properties = mapaType,
            cameraPositionState =  cameraPosition,
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)) {
        }

        Row(Modifier.padding(10.dp)) {
            Text("Lat: ${ubicacionDispositivo?.latitude}");
            Text("Long: ${ubicacionDispositivo?.longitude} ");
        }


        Box(Modifier.align(Alignment.BottomCenter)){
            Button(onClick = {
                Log.i("OSCAR", "${ubicacionDispositivo}")
            }) {
                Text("Analizar")
            }
        }

    }

}