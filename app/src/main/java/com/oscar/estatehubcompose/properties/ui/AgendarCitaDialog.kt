package com.oscar.estatehubcompose.properties.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.oscar.estatehubcompose.citas.data.network.response.HorarioDisponible
import com.oscar.estatehubcompose.ui.theme.Parkinsans
import com.oscar.estatehubcompose.ui.theme.PrimaryPersonalized
import com.oscar.estatehubcompose.ui.theme.SeventhPersonalized
import com.oscar.estatehubcompose.ui.theme.ThirdPersonalized
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.isNotEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaDialog(
    propertyId: Int,
    propertyTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    isLoading: Boolean = false,
    horariosDisponibles: List<HorarioDisponible> = emptyList(),
    onFechaSelected: (String) -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val dateFormatISO = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = !isLoading,
            dismissOnClickOutside = !isLoading,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ThirdPersonalized)
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Agendar visita",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryPersonalized
                                )
                            )
                            Text(
                                text = propertyTitle,
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.Normal,
                                    color = SeventhPersonalized
                                ),
                                maxLines = 1
                            )
                        }

                        if (!isLoading) {
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = PrimaryPersonalized
                                )
                            }
                        }
                    }
                }

                // Contenido
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Info card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF9FAFB)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF3B82F6),
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = "Horario de atención: 8:00 AM - 8:00 PM",
                                    style = TextStyle(
                                        fontSize = 13.sp,
                                        fontFamily = Parkinsans,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryPersonalized,
                                        lineHeight = 18.sp
                                    )
                                )
                                Text(
                                    text = "Las citas deben agendarse con al menos 1 día de anticipación",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = Parkinsans,
                                        fontWeight = FontWeight.Normal,
                                        color = SeventhPersonalized,
                                        lineHeight = 16.sp
                                    )
                                )
                            }
                        }
                    }

                    // Date selector
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Fecha de la visita",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = Parkinsans,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryPersonalized
                            )
                        )

                        OutlinedButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = PrimaryPersonalized
                            ),
                            enabled = !isLoading
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedDate?.let {
                                        dateFormat.format(Date(it))
                                    } ?: "Seleccionar fecha",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = Parkinsans,
                                        fontWeight = if (selectedDate != null)
                                            FontWeight.Medium else FontWeight.Normal,
                                        color = if (selectedDate != null)
                                            PrimaryPersonalized else SeventhPersonalized
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Calendario",
                                    modifier = Modifier.size(20.dp),
                                    tint = PrimaryPersonalized
                                )
                            }
                        }
                    }

                    // Mostrar horarios disponibles si hay una fecha seleccionada
                    if (selectedDate != null && horariosDisponibles.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Horarios disponibles (${horariosDisponibles.size})",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimaryPersonalized
                                )
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 200.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(horariosDisponibles) { horario ->
                                    val isSelected = selectedTime?.let {
                                        horario.horaLocal == String.format("%02d:%02d", it.first, it.second)
                                    } ?: false

                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(44.dp)
                                            .clickable {
                                                val parts = horario.horaLocal.split(":")
                                                selectedTime = Pair(
                                                    parts[0].toInt(),
                                                    parts[1].toInt()
                                                )
                                            },
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) PrimaryPersonalized else Color.White,
                                        border = BorderStroke(
                                            1.dp,
                                            if (isSelected) PrimaryPersonalized else ThirdPersonalized
                                        )
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = horario.horaLocal,
                                                style = TextStyle(
                                                    fontSize = 13.sp,
                                                    fontFamily = Parkinsans,
                                                    fontWeight = FontWeight.Medium,
                                                    color = if (isSelected) Color.White else PrimaryPersonalized
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else if (selectedDate != null && horariosDisponibles.isEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "No hay horarios disponibles para esta fecha",
                                    style = TextStyle(
                                        fontSize = 13.sp,
                                        fontFamily = Parkinsans,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF92400E)
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryPersonalized
                            ),
                            enabled = !isLoading
                        ) {
                            Text(
                                "Cancelar",
                                style = TextStyle(
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp
                                )
                            )
                        }

                        Button(
                            onClick = {
                                if (selectedDate != null && selectedTime != null) {
                                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                                    calendar.timeInMillis = selectedDate!!
                                    calendar.set(Calendar.HOUR_OF_DAY, selectedTime!!.first)
                                    calendar.set(Calendar.MINUTE, selectedTime!!.second)
                                    calendar.set(Calendar.SECOND, 0)

                                    val isoDate = isoFormat.format(calendar.time)
                                    onConfirm(isoDate)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryPersonalized,
                                contentColor = Color.White,
                                disabledContainerColor = PrimaryPersonalized.copy(alpha = 0.5f),
                                disabledContentColor = Color.White.copy(alpha = 0.7f)
                            ),
                            enabled = selectedDate != null && selectedTime != null && !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Confirmar",
                                    style = TextStyle(
                                        fontFamily = Parkinsans,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate = datePickerState.selectedDateMillis
                        selectedDate?.let { millis ->
                            val fecha = dateFormatISO.format(Date(millis))
                            Log.d("AgendarCitaDialog", "Fecha seleccionada para horarios: $fecha")
                            onFechaSelected(fecha)
                        }
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = PrimaryPersonalized
                    )
                ) {
                    Text(
                        "Aceptar",
                        style = TextStyle(
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = SeventhPersonalized
                    )
                ) {
                    Text(
                        "Cancelar",
                        style = TextStyle(
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                titleContentColor = PrimaryPersonalized,
                headlineContentColor = PrimaryPersonalized,
                weekdayContentColor = SeventhPersonalized,
                subheadContentColor = PrimaryPersonalized,
                yearContentColor = PrimaryPersonalized,
                currentYearContentColor = PrimaryPersonalized,
                selectedYearContentColor = Color.White,
                selectedYearContainerColor = PrimaryPersonalized,
                dayContentColor = PrimaryPersonalized,
                selectedDayContentColor = Color.White,
                selectedDayContainerColor = PrimaryPersonalized,
                todayContentColor = PrimaryPersonalized,
                todayDateBorderColor = PrimaryPersonalized
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    titleContentColor = PrimaryPersonalized,
                    headlineContentColor = PrimaryPersonalized,
                    weekdayContentColor = SeventhPersonalized,
                    subheadContentColor = PrimaryPersonalized,
                    yearContentColor = PrimaryPersonalized,
                    currentYearContentColor = PrimaryPersonalized,
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = PrimaryPersonalized,
                    dayContentColor = PrimaryPersonalized,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = PrimaryPersonalized,
                    todayContentColor = PrimaryPersonalized,
                    todayDateBorderColor = PrimaryPersonalized,
                    disabledDayContentColor = SeventhPersonalized.copy(alpha = 0.3f)
                )
            )
        }
    }
}