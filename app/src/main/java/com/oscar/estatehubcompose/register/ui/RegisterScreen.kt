package com.oscar.estatehubcompose.register.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oscar.estatehubcompose.ui.theme.Parkinsans

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    val context = LocalContext.current

    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val nombre by viewModel.nombre.observeAsState("")
    val apellidoPaterno by viewModel.apellidoPaterno.observeAsState("")
    val apellidoMaterno by viewModel.apellidoMaterno.observeAsState("")
    val telefono by viewModel.telefono.observeAsState("")
    val isLoading by viewModel.isLoading.observeAsState(false)
    val registerSuccess by viewModel.registerSuccess.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState("")

    val emailError by viewModel.emailError.observeAsState("")
    val passwordError by viewModel.passwordError.observeAsState("")
    val nombreError by viewModel.nombreError.observeAsState("")
    val apellidoPaternoError by viewModel.apellidoPaternoError.observeAsState("")
    val apellidoMaternoError by viewModel.apellidoMaternoError.observeAsState("")
    val telefonoError by viewModel.telefonoError.observeAsState("")

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            Toast.makeText(
                context,
                "Usuario registrado exitosamente",
                Toast.LENGTH_LONG
            ).show()
            onRegisterSuccess()
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    RegisterScreenContent(
        email = email,
        onEmailChange = { viewModel.setEmail(it) },
        password = password,
        onPasswordChange = { viewModel.setPassword(it) },
        nombre = nombre,
        onNombreChange = { viewModel.setNombre(it) },
        apellidoPaterno = apellidoPaterno,
        onApellidoPaternoChange = { viewModel.setApellidoPaterno(it) },
        apellidoMaterno = apellidoMaterno,
        onApellidoMaternoChange = { viewModel.setApellidoMaterno(it) },
        telefono = telefono,
        onTelefonoChange = { viewModel.setTelefono(it) },
        isLoading = isLoading,
        registerSuccess = registerSuccess,
        errorMessage = errorMessage,
        emailError = emailError,
        passwordError = passwordError,
        nombreError = nombreError,
        apellidoPaternoError = apellidoPaternoError,
        apellidoMaternoError = apellidoMaternoError,
        telefonoError = telefonoError,
        onRegisterClick = {
            viewModel.onRegister(
                email = email,
                password = password,
                nombre = nombre,
                apellidoPaterno = apellidoPaterno,
                apellidoMaterno = apellidoMaterno,
                telefono = telefono
            )
        },
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun RegisterScreenContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    nombre: String,
    onNombreChange: (String) -> Unit,
    apellidoPaterno: String,
    onApellidoPaternoChange: (String) -> Unit,
    apellidoMaterno: String,
    onApellidoMaternoChange: (String) -> Unit,
    telefono: String,
    onTelefonoChange: (String) -> Unit,
    isLoading: Boolean = false,
    registerSuccess: Boolean = false,
    errorMessage: String = "",
    emailError: String = "",
    passwordError: String = "",
    nombreError: String = "",
    apellidoPaternoError: String = "",
    apellidoMaternoError: String = "",
    telefonoError: String = "",
    onRegisterClick: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Crear cuenta",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Completa los datos para",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "registrarte",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.padding(10.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp),
                placeholder = { Text("Correo electrónico") },
                isError = emailError.isNotEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            if (emailError.isNotEmpty()) {
                Text(
                    text = emailError,
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Spacer(Modifier.padding(5.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp),
                placeholder = { Text("Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                isError = passwordError.isNotEmpty()
            )
            if (passwordError.isNotEmpty()) {
                Text(
                    text = passwordError,
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Spacer(Modifier.padding(5.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = nombre,
                onValueChange = onNombreChange,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp),
                placeholder = { Text("Nombre") },
                isError = nombreError.isNotEmpty()
            )
            if (nombreError.isNotEmpty()) {
                Text(
                    text = nombreError,
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Spacer(Modifier.padding(5.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = apellidoPaterno,
                onValueChange = onApellidoPaternoChange,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp),
                placeholder = { Text("Apellido paterno") },
                isError = apellidoPaternoError.isNotEmpty()
            )
            if (apellidoPaternoError.isNotEmpty()) {
                Text(
                    text = apellidoPaternoError,
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Spacer(Modifier.padding(5.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = apellidoMaterno,
                onValueChange = onApellidoMaternoChange,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp),
                placeholder = { Text("Apellido materno") },
                isError = apellidoMaternoError.isNotEmpty()
            )
            if (apellidoMaternoError.isNotEmpty()) {
                Text(
                    text = apellidoMaternoError,
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Spacer(Modifier.padding(5.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = telefono,
                onValueChange = onTelefonoChange,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp),
                placeholder = { Text("Teléfono (10 dígitos)") },
                isError = telefonoError.isNotEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            if (telefonoError.isNotEmpty()) {
                Text(
                    text = telefonoError,
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Spacer(Modifier.padding(10.dp))

        Button(
            onClick = onRegisterClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(53.dp)
                .shadow(8.dp, RoundedCornerShape(10.dp)),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.height(24.dp)
                )
            } else {
                Text("Registrarse")
            }
        }

        Spacer(Modifier.padding(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "or",
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.padding(10.dp))

        OutlinedButton(
            onClick = onNavigateToLogin,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(53.dp)
        ) {
            Text("Ya tengo cuenta - Iniciar sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreenContent(
        email = "",
        onEmailChange = {},
        password = "",
        onPasswordChange = {},
        nombre = "",
        onNombreChange = {},
        apellidoPaterno = "",
        onApellidoPaternoChange = {},
        apellidoMaterno = "",
        onApellidoMaternoChange = {},
        telefono = "",
        onTelefonoChange = {},
        emailError = "",
        passwordError = "",
        nombreError = "",
        apellidoPaternoError = "",
        apellidoMaternoError = "",
        telefonoError = ""
    )
}