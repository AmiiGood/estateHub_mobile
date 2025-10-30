package com.oscar.estatehubcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oscar.estatehubcompose.home.ui.Home
import com.oscar.estatehubcompose.login.ui.LoginScreen
import com.oscar.estatehubcompose.login.ui.LoginViewModel
import com.oscar.estatehubcompose.ui.theme.EstateHubComposeTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            //Declaramos la barra de navegacion
            val navController = rememberNavController();
            //Creamos un arreglo con los componentes donde no estara la barra de navegacion
            val noMenu = listOf<String>("login", "registro");
            //Accede a la informacion de la pantalla donde estas
            val currentBackStackEntry by navController.currentBackStackEntryAsState();
            //Obtiene el nombre de la ruta de la pantalla donde estas
            val rutactual = currentBackStackEntry?.destination?.route
            //Verifica que la ruta no este dentro de el arreglo noMenu
            val mostrarNavegacion= rutactual !in noMenu;
            EstateHubComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    if(mostrarNavegacion) {
                        Navbar(navController)
                    }
                }) { innerPadding ->

                    AppNavigation(Modifier.padding(innerPadding), navController)

                }
            }
        }
    }
    @Composable
    fun AppNavigation(modifier: Modifier, navHostController: NavHostController){

        NavHost(navHostController, "login", Modifier) {
            composable("login"){
                LoginScreen(loginViewModel = loginViewModel, navController = navHostController)
            }
            composable("home"){
                Home()
            }
        }

    }

    @Composable
    fun Navbar(navController: NavController){
        var selectedItem by rememberSaveable { mutableStateOf(0) }
        val items = listOf("home");
        val selectedIcons = listOf(Icons.Filled.Home)
        val unselectedIcons = listOf(Icons.Filled.Home)


        NavigationBar(modifier = Modifier,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.contentColorFor(NavigationBarDefaults.containerColor)) {

            items.forEachIndexed { index, item  ->
                NavigationBarItem(
                    icon = { Icon( if(selectedItem == index) selectedIcons[index] else unselectedIcons[index], contentDescription = item) },
                label = {Text(item)},
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(item)
                    })
            }

        }

    }
}





