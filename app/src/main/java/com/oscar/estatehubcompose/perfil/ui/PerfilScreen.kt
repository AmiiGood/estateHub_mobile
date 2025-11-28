package com.oscar.estatehubcompose.perfil.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.oscar.estatehubcompose.perfil.data.network.response.PropiedadesResponse
import com.oscar.estatehubcompose.ui.theme.Parkinsans


@Composable
fun PerfilScreen(modifier: Modifier,perfilViewModel: PerfilViewModel , navController: NavController ){

    val propiedadesData by perfilViewModel.propiedadesData.observeAsState(null);
    val usuarioData by perfilViewModel.perfilData.observeAsState();




    LaunchedEffect(Unit) {
        perfilViewModel.getUsuario();
        perfilViewModel.getPropiedades();
    }

    Column(modifier) {


        Text(
            "Mi perfil",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        );
        PerfilData(propiedadesData,
            usuarioData?.data?.nombre,
            usuarioData?.data?.telefono)
    }
}


@Composable
fun PerfilData(propiedadesResponse: PropiedadesResponse?,
               nombre: String?, telefono:String?){

    val letraUser = nombre?.split("");
    Log.i("OSCAR", letraUser.toString())


    Column(Modifier.padding(15.dp).verticalScroll(rememberScrollState())) {

        Spacer(Modifier.padding(15.dp))

        Column(Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(top = 35.dp, start = 20.dp, end = 20.dp, bottom = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),

                contentAlignment = Alignment.Center
                ){

                Text(
                    "${letraUser?.get(1) ?: ""}",
                    style = TextStyle(
                        fontSize = 70.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.secondary
                );
            }

            Spacer(Modifier.padding(10.dp))
            if (nombre != null) {
                Text(
                    nombre,
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            };
            if (telefono != null) {
                Text(
                    telefono,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            };
        }


        Spacer(Modifier.padding(10.dp))
        Text(
            "Mis propiedades",
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        );


        propiedadesResponse?.data?.let {
            it.forEach { prop ->
                Spacer(Modifier.padding(8.dp))
                val imagenUrl = prop.imagenes.firstOrNull()?.urlImagen ?: ""
                CardPropiedad(prop.titulo, prop.direccion, imagenUrl)
            }
        }



    }
}


@Composable
fun CardPropiedad(titulo: String, direccion: String, imagen: String){
    Column(Modifier
        .fillMaxWidth()
        .shadow(10.dp, RoundedCornerShape(10.dp))
        .background(Color.White)
    ) {
        Row (Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)),verticalAlignment = Alignment.CenterVertically){
            if (imagen.isNotEmpty()) {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .size(125.dp),
                    contentScale = ContentScale.Crop,
                    model = imagen,
                    contentDescription = "Primera imagen de propiedad"
                )
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .size(125.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
            Spacer(Modifier.padding(10.dp))
            Column(Modifier) {
                Text(
                    titulo,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    direccion,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Light
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}