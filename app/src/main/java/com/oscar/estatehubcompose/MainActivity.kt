package com.oscar.estatehubcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oscar.estatehubcompose.login.ui.LoginScreen
import com.oscar.estatehubcompose.login.ui.LoginViewModel
import com.oscar.estatehubcompose.register.ui.RegisterScreen
import com.oscar.estatehubcompose.register.ui.RegisterViewModel
import com.oscar.estatehubcompose.ui.theme.EstateHubComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EstateHubComposeTheme {
                var showRegister by remember { mutableStateOf(true) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        if (showRegister) {
                            // Mostrar pantalla de registro
                            RegisterScreen(
                                viewModel = registerViewModel,
                                onNavigateToLogin = { showRegister = false }
                            )
                        } else {
                            // Mostrar pantalla de login (sin cambios)
                            LoginScreen()

                            // Botón temporal para testing
                            Button(
                                onClick = { showRegister = true },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Ver pantalla de Registro")
                            }
                        }
                    }
                }
            }
        }
    }
}