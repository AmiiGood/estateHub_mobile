package com.oscar.estatehubcompose.analisis.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oscar.estatehubcompose.analisis.data.network.response.Propiedad
import com.oscar.estatehubcompose.ui.theme.Parkinsans
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PropiedadCardFloating(
    propiedad: Propiedad?,
    onNavigateToDetail: (Int) -> Unit,
    onClose: () -> Unit
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    var expanded by rememberSaveable { mutableStateOf(false) }

    if (propiedad == null) return

    Column(
        Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { expanded = !expanded }
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessHigh
                )
            )
            .padding(14.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                // Tipo de propiedad
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when(propiedad.tipoPropiedad) {
                            "Casa" -> Icons.Filled.House
                            "Departamento" -> Icons.Filled.Apartment
                            "Local Comercial", "Local comercial" -> Icons.Filled.AddBusiness
                            else -> Icons.Filled.LocationCity
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        propiedad.tipoPropiedad,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Parkinsans,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(Modifier.width(8.dp))

                    // Badge de estatus
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when(propiedad.estatus) {
                                    "En venta" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                                    "En renta" -> Color(0xFF2196F3).copy(alpha = 0.2f)
                                    else -> Color.Gray.copy(alpha = 0.2f)
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = propiedad.estatus,
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Parkinsans,
                                color = when(propiedad.estatus) {
                                    "En venta" -> Color(0xFF2E7D32)
                                    "En renta" -> Color(0xFF1565C0)
                                    else -> Color.Gray
                                }
                            )
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                // Precio
                Text(
                    text = if (propiedad.estatus == "En venta") {
                        numberFormat.format(propiedad.precioVenta)
                    } else {
                        "${numberFormat.format(propiedad.precioRenta)}/mes"
                    },
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = Parkinsans,
                        color = Color(0xFF101828)
                    )
                )
            }

            // Boton cerrar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5))
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cerrar",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Ubicacion
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "${propiedad.colonia}, ${propiedad.ciudad}",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Parkinsans,
                    color = Color.Gray
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(Modifier.padding(top = 12.dp)) {
                Text(
                    "Características:",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Parkinsans,
                        color = Color(0xFF101828)
                    )
                )

                Spacer(Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CaracteristicaItem(
                            icon = Icons.Filled.Bed,
                            label = "Habitaciones",
                            value = "${propiedad.numHabitaciones}",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        CaracteristicaItem(
                            icon = Icons.Filled.Shower,
                            label = "Baños",
                            value = "${propiedad.numBanios}",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CaracteristicaItem(
                            icon = Icons.Filled.SquareFoot,
                            label = "Área",
                            value = "${propiedad.metrosCuadrados}m²",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        if (propiedad.numEstacionamiento > 0) {
                            CaracteristicaItem(
                                icon = Icons.Filled.DirectionsCar,
                                label = "Estacionamiento",
                                value = "${propiedad.numEstacionamiento}",
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }

                if (propiedad.jardin || propiedad.alberca || propiedad.terraza ||
                    propiedad.residencial || propiedad.sotano) {

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Amenidades:",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Parkinsans,
                            color = Color(0xFF101828)
                        )
                    )

                    Spacer(Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (propiedad.residencial) {
                            AmenidadChip("Residencial", Icons.Filled.Apartment)
                        }
                        if (propiedad.jardin) {
                            AmenidadChip("Jardín", Icons.Filled.Grass)
                        }
                        if (propiedad.alberca) {
                            AmenidadChip("Alberca", Icons.Filled.Pool)
                        }
                        if (propiedad.terraza) {
                            AmenidadChip("Terraza", Icons.Filled.Deck)
                        }
                        if (propiedad.sotano) {
                            AmenidadChip("Sótano", Icons.Filled.Stairs)
                        }
                    }
                }

                // Dirección completa
                Spacer(Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = propiedad.direccion,
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = Parkinsans,
                            color = Color(0xFF101828)
                        )
                    )
                }

                // Botón Ver detalles
                Spacer(Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onNavigateToDetail(propiedad.idPropiedad) },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Ver detalles completos",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = Parkinsans,
                                color = Color.White
                            )
                        )
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CaracteristicaItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(6.dp))
        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Parkinsans,
                    color = Color.Gray
                )
            )
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Parkinsans,
                    color = Color(0xFF101828)
                )
            )
        }
    }
}

@Composable
fun AmenidadChip(
    texto: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = texto,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Parkinsans,
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}