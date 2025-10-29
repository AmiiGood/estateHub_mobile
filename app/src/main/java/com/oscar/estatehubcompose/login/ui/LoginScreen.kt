package com.oscar.estatehubcompose.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Column(Modifier.fillMaxSize().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text("Iniciar sesion");
        OutlinedTextField(value = "",
            onValueChange = {},
            shape = RoundedCornerShape(10.dp),
            placeholder = {
            Text("Correo electronico")
        })

        Spacer(Modifier.padding(5.dp));
        OutlinedTextField(value = "",
            onValueChange = {},
            shape = RoundedCornerShape(10.dp),
            placeholder = {
            Text("Contraseña")
        })


        Button(onClick = {},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.padding(5.dp)) {
            Text("Iniciar sesion");
        }
    }

}