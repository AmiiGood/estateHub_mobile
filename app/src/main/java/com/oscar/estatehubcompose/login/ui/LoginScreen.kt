package com.oscar.estatehubcompose.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oscar.estatehubcompose.ui.theme.PrimaryPersonalized
import com.oscar.estatehubcompose.ui.theme.SecondaryPersonalized

@Composable
fun LoginScreen(){

    Column {
        PreviewLoginScreen();
    }

}

@Composable
@Preview
fun PreviewLoginScreen(){
    Column(Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()){

            Text("Iniciar sesion", style = TextStyle(fontSize = 40.sp));
            Text("Accede a tu cuenta para continuar", style = TextStyle(fontSize = 16.sp));

        }
        OutlinedTextField(value = "",
            onValueChange = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
            Text("Correo electronico")
        })

        Spacer(Modifier.padding(5.dp));
        OutlinedTextField(value = "",
            onValueChange = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
            Text("Contraseña")
        })

        Text("Olvidaste tu contraseña?")

        Button(onClick = {},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.padding(5.dp).fillMaxWidth()) {
            Text("Iniciar sesion");


        }

        Button(onClick = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(5.dp).fillMaxWidth()) {
            Text("Registrarse")
        }



    }

}