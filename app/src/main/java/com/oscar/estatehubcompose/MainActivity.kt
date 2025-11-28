package com.oscar.estatehubcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.libraries.places.api.Places
import com.oscar.estatehubcompose.analisis.ui.AnalisisScreen
import com.oscar.estatehubcompose.properties.ui.Property
import com.oscar.estatehubcompose.properties.ui.PropertyViewModel
import com.oscar.estatehubcompose.analisis.ui.AnalisisViewModel
import com.oscar.estatehubcompose.login.ui.LoginScreen
import com.oscar.estatehubcompose.login.ui.LoginViewModel
import com.oscar.estatehubcompose.perfil.ui.PerfilScreen
import com.oscar.estatehubcompose.perfil.ui.PerfilViewModel
import com.oscar.estatehubcompose.properties.ui.PropertyDetailScreen
import com.oscar.estatehubcompose.properties.ui.PropertyDetailViewModel
import com.oscar.estatehubcompose.register.ui.RegisterScreen
import com.oscar.estatehubcompose.register.ui.RegisterViewModel
import com.oscar.estatehubcompose.ui.theme.EstateHubComposeTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels();
    private val propertyViewModel: PropertyViewModel by viewModels();
    private val registerViewModel: RegisterViewModel by viewModels();
    private val analisisViewModel: AnalisisViewModel by viewModels();
    private val perfilViewModel: PerfilViewModel by viewModels();
    val propertyDetailViewModel: PropertyDetailViewModel by viewModels()
    private val apiKey = BuildConfig.API_KEY;




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            //Se declara Places (api que contiene una serie de puntos de interes)
            if (!Places.isInitialized()) {
                Places.initialize(applicationContext, apiKey)
            }
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
            composable("registro"){
                RegisterScreen(registerViewModel = registerViewModel, navController = navHostController)
            }

            composable("mercado"){
                Property(modifier, propertyViewModel = propertyViewModel, navController = navHostController)
            }
            composable("analisis"){
                AnalisisScreen(modifier, analisisViewModel, onNavigateToPropertyDetail = { idPropiedad ->
                    navHostController.navigate("propertyDetail/$idPropiedad")
                })
            }
            composable(
                route = "propertyDetail/{propertyId}",
                arguments = listOf(navArgument("propertyId") { type = NavType.IntType })
            ) { backStackEntry ->
                val propertyId = backStackEntry.arguments?.getInt("propertyId") ?: 0
                PropertyDetailScreen(
                    propertyId = propertyId,
                    propertyDetailViewModel = propertyDetailViewModel,
                    navController = navHostController
                )
            }

            composable("perfil"){
                PerfilScreen(modifier, perfilViewModel,navHostController)
            }
        }
    }

    @Composable
    fun Navbar(navController: NavController){
        var selectedItem by rememberSaveable { mutableStateOf(0) }
        val items = listOf("mercado","analisis", "Perfil");
        val selectedIcons = listOf(Icons.Filled.AddHome,Icons.Filled.Map,  Icons.Filled.AccountCircle)
        val unselectedIcons = listOf(Icons.Filled.AddHome, Icons.Filled.Map, Icons.Filled.AccountCircle)

        NavigationBar(modifier = Modifier,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.contentColorFor(NavigationBarDefaults.containerColor)) {

            items.forEachIndexed { index, item  ->
                NavigationBarItem(
                    icon = {
                        Icon( if(selectedItem == index) selectedIcons[index] else unselectedIcons[index], contentDescription = item)
                    },
                    label = {Text(item)},
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(item)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary
                    ))
            }
        }
    }
}





