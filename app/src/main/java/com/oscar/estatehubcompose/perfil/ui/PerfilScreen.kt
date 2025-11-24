package com.oscar.estatehubcompose.perfil.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.oscar.estatehubcompose.perfil.data.network.response.PropiedadesResponse
import com.oscar.estatehubcompose.ui.theme.Parkinsans


@Composable
fun PerfilScreen(modifier: Modifier,perfilViewModel: PerfilViewModel , navController: NavController ){

    val propiedadesData by perfilViewModel.propiedadesData.observeAsState(null);

    LaunchedEffect(Unit) {
        perfilViewModel.getPropiedades();
    }

    Column(modifier) {
        PerfilData(propiedadesData)
    }
}


@Composable
fun PerfilData(propiedadesResponse: PropiedadesResponse?){



    Column(Modifier.padding(15.dp)) {

        Text(
            "Mi perfil",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        );
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
                    "O",
                    style = TextStyle(
                        fontSize = 70.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.secondary
                );
            }

            Spacer(Modifier.padding(10.dp))
            Text(
                "Oscar",
                style = TextStyle(
                    fontSize = 25.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.primary
            );
           Text(
                "4775872265",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            );
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


        propiedadesResponse?.data?.firstOrNull()?.imagenes?.firstOrNull()?.let { imagen ->
            AsyncImage(
                modifier = Modifier.size(100.dp),
                model = imagen.urlImagen,
                contentDescription = "Primera imagen de propiedad"
            )
        }



    }
}