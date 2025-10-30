package com.oscar.estatehubcompose.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oscar.estatehubcompose.ui.theme.Parkinsans
import com.oscar.estatehubcompose.ui.theme.PrimaryPersonalized
import com.oscar.estatehubcompose.ui.theme.SecondaryPersonalized

@Composable
fun LoginScreen(){

    Column {
        PreviewLoginScreen();
    }

}

@Composable
@Preview(showBackground = true)
fun PreviewLoginScreen(){
    Column(Modifier.fillMaxSize().padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()){

            Text("Iniciar sesion",
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
        OutlinedTextField(value = "",
            onValueChange = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(53.dp),
            placeholder = {
            Text("Correo electronico")});

        Spacer(Modifier.padding(5.dp));
        OutlinedTextField(value = "",
            onValueChange = {},
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(53.dp),
            placeholder = {
            Text("Contraseña")
        })

        Spacer(Modifier.padding(5.dp));

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Text("olvidaste tu contraseña?", style = TextStyle(fontSize = 13.sp,
                fontFamily = Parkinsans,
                fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.primary);
        }
        Spacer(Modifier.padding(10.dp));

        Button(onClick = {},
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
            Text("Registrarse")
        }



    }

}