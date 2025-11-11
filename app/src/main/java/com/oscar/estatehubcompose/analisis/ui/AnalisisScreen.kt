package com.oscar.estatehubcompose.analisis.ui

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
import com.oscar.estatehubcompose.R
import com.oscar.estatehubcompose.ui.theme.Parkinsans

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


        Box(Modifier.align(Alignment.BottomCenter).padding(10.dp)){
            CardPropiedad();

            /*
            Button(onClick = {
                Log.i("OSCAR", "${ubicacionDispositivo}")
            }) {
                Text("Analizar")
            }
            */

        }

    }

}

@Preview(showBackground = true)
@Composable
fun CardPropiedad(){

    Row(Modifier.padding(2.dp)
        .shadow(8.dp, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .background(Color.White)
        .padding(16.dp)
        , verticalAlignment = Alignment.CenterVertically) {

        AsyncImage(model = "https://images.homify.com/v1440015283/p/photo/image/832767/JLF_6309.jpg",
            contentDescription = "imagenFoto",
            Modifier.clip(RoundedCornerShape(8.dp)).size(100.dp))

        Spacer(Modifier.padding(10.dp))
        Column (verticalArrangement = Arrangement.Bottom) {
            Text("Argentina 2020A",
                style = TextStyle(fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Parkinsans));

            Text("$2,000,000",
                style = TextStyle(fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Parkinsans));

            Button(onClick = { /*TODO*/ },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.secondary
                )) {
                Text("Ver detalles")
            }
        }
    }

}
