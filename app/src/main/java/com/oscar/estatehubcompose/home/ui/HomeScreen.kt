package com.oscar.estatehubcompose.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Home(modifier: Modifier, homeViewModel: HomeViewModel){

    val permisos = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )



    LaunchedEffect(Unit) {

        if(!permisos.allPermissionsGranted){
            permisos.launchMultiplePermissionRequest();
        }
    }



    Column(modifier) {
        Text("EN RUTA HOME")
    }
}