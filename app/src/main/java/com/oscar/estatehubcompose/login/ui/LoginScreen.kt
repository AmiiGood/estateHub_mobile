package com.oscar.estatehubcompose.login.ui

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.estatehubcompose.ui.theme.Parkinsans
import com.oscar.estatehubcompose.ui.theme.PrimaryPersonalized
import com.oscar.estatehubcompose.ui.theme.SecondaryPersonalized

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController){

    LoginScreenContainer(Modifier, loginViewModel, navController);

}

@Composable
fun LoginScreenContainer(modifier: Modifier, loginViewModel: LoginViewModel,navController: NavController ){

    var passwordViewState by rememberSaveable { mutableStateOf(false)};
    val correo by loginViewModel.correo.observeAsState(initial = "");
    val password by loginViewModel.contrasenia.observeAsState(initial = "");
    val isLogged by loginViewModel.isLogged.observeAsState(null);

    val context = LocalContext.current;


    LaunchedEffect(isLogged) {

        isLogged?.let { logged ->
            if (logged) {
                navController.navigate("home");
            } else if(!logged){
                Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    Column(Modifier.fillMaxSize().padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()){

            Text("Iniciar sesión",
                style = TextStyle(fontSize = 40.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary);
            Text("Accede a tu cuenta para",
                style = TextStyle(fontSize = 16.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.primary);
            Text("continuar",
                style = TextStyle(fontSize = 16.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.primary);

        }
        Spacer(Modifier.padding(5.dp));
        OutlinedTextField(value = correo,
            onValueChange = {
                loginViewModel.setCorreo(it)
            },
            textStyle = TextStyle(fontSize = 13.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Medium),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(53.dp),

            placeholder = {
            if (correo == "") Text("Correo electronico",style = TextStyle(fontSize = 13.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Medium),)
            else Text(correo,style = TextStyle(fontSize = 13.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Medium),)}
        );

        Spacer(Modifier.padding(5.dp));
        OutlinedTextField(value = password,
            onValueChange = {
                loginViewModel.setContrasenia(it)
            },
            textStyle = TextStyle(fontSize = 13.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Medium),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(53.dp),
            visualTransformation = if (passwordViewState) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                IconButton(onClick = {
                    passwordViewState = !passwordViewState
                },
                ) {
                    Icon(imageVector = if(passwordViewState) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Ver contraseña", tint = MaterialTheme.colorScheme.primary);
                }
            },
            placeholder = {
                Row(Modifier.fillMaxWidth()) {
                    Text("Contraseña",style = TextStyle(fontSize = 13.sp,
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Medium),)
                }

        })

        Spacer(Modifier.padding(5.dp));

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Text("olvidaste tu contraseña?", style = TextStyle(fontSize = 13.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.primary);
        }
        Spacer(Modifier.padding(10.dp));

        Button(onClick = {
            loginViewModel.onLogin(correo, password);
        },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.padding(5.dp).fillMaxWidth().height(53.dp).shadow(8.dp, RoundedCornerShape(10.dp))) {
            Text("Iniciar sesion");


        }
        Spacer(Modifier.padding(10.dp));
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

        Spacer(Modifier.padding(10.dp));
        OutlinedButton(onClick = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(5.dp).fillMaxWidth().height(53.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(imageVector = Icons.Filled.AppRegistration,
                    contentDescription = "Registrarse", tint = MaterialTheme.colorScheme.primary);
                Text("Registrarse")
            }

        }



    }

}