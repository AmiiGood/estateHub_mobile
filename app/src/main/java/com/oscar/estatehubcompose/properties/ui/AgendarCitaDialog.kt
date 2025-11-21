package com.oscar.estatehubcompose.properties.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.oscar.estatehubcompose.ui.theme.Parkinsans
import com.oscar.estatehubcompose.ui.theme.PrimaryPersonalized
import com.oscar.estatehubcompose.ui.theme.SeventhPersonalized
import com.oscar.estatehubcompose.ui.theme.ThirdPersonalized
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaDialog(
    propertyId: Int,
    propertyTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    isLoading: Boolean = false
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

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
                // Header
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

                // Content
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
                            Text(
                                text = "Selecciona la fecha y hora para tu visita. Te contactaremos pronto.",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontFamily = Parkinsans,
                                    fontWeight = FontWeight.Normal,
                                    color = SeventhPersonalized,
                                    lineHeight = 18.sp
                                )
                            )
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

                    // Time selector
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Hora de la visita",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = Parkinsans,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryPersonalized
                            )
                        )

                        OutlinedButton(
                            onClick = { showTimePicker = true },
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
                                    text = selectedTime?.let {
                                        String.format("%02d:%02d", it.first, it.second)
                                    } ?: "Seleccionar hora",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = Parkinsans,
                                        fontWeight = if (selectedTime != null)
                                            FontWeight.Medium else FontWeight.Normal,
                                        color = if (selectedTime != null)
                                            PrimaryPersonalized else SeventhPersonalized
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Hora",
                                    modifier = Modifier.size(20.dp),
                                    tint = PrimaryPersonalized
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
                                    val calendar = Calendar.getInstance()
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

    // DatePicker Dialog - Material3 Compose
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
                            fontWeight = FontWeight.Medium
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
                disabledDayContentColor = SeventhPersonalized.copy(alpha = 0.3f),
                selectedDayContentColor = Color.White,
                selectedDayContainerColor = PrimaryPersonalized,
                todayContentColor = PrimaryPersonalized,
                todayDateBorderColor = PrimaryPersonalized,
                dayInSelectionRangeContentColor = PrimaryPersonalized,
                dayInSelectionRangeContainerColor = ThirdPersonalized
            )
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = "Seleccionar fecha",
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                        style = TextStyle(
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = PrimaryPersonalized
                        )
                    )
                },
                headline = {
                    datePickerState.selectedDateMillis?.let {
                        Text(
                            text = dateFormat.format(Date(it)),
                            modifier = Modifier.padding(start = 24.dp, bottom = 12.dp),
                            style = TextStyle(
                                fontFamily = Parkinsans,
                                fontWeight = FontWeight.Medium,
                                fontSize = 24.sp,
                                color = PrimaryPersonalized
                            )
                        )
                    }
                },
                showModeToggle = true,
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
                    disabledDayContentColor = SeventhPersonalized.copy(alpha = 0.3f),
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = PrimaryPersonalized,
                    todayContentColor = PrimaryPersonalized,
                    todayDateBorderColor = PrimaryPersonalized,
                    dayInSelectionRangeContentColor = PrimaryPersonalized,
                    dayInSelectionRangeContainerColor = ThirdPersonalized
                )
            )
        }
    }

    // TimePicker Dialog - Material3 Compose
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedTime?.first ?: 10,
            initialMinute = selectedTime?.second ?: 0,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            containerColor = Color.White,
            title = {
                Text(
                    text = "Seleccionar hora",
                    style = TextStyle(
                        fontFamily = Parkinsans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = PrimaryPersonalized
                    )
                )
            },
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = ThirdPersonalized,
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = PrimaryPersonalized,
                        selectorColor = PrimaryPersonalized,
                        containerColor = Color.White,
                        periodSelectorBorderColor = PrimaryPersonalized,
                        periodSelectorSelectedContainerColor = PrimaryPersonalized,
                        periodSelectorUnselectedContainerColor = Color.Transparent,
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContentColor = PrimaryPersonalized,
                        timeSelectorSelectedContainerColor = PrimaryPersonalized,
                        timeSelectorUnselectedContainerColor = ThirdPersonalized,
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContentColor = PrimaryPersonalized
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedTime = Pair(timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
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
                    onClick = { showTimePicker = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = SeventhPersonalized
                    )
                ) {
                    Text(
                        "Cancelar",
                        style = TextStyle(
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        )
    }
}