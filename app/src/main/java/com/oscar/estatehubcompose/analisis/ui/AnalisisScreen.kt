package com.oscar.estatehubcompose.analisis.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType


@Composable
fun AnalisisScreen(modifier: Modifier){

    Mapa(modifier);

}


@Composable
fun Mapa(modifier: Modifier){

    val mapaType by remember{ mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }

    GoogleMap(modifier = Modifier.fillMaxSize(), properties = mapaType) {  }
}