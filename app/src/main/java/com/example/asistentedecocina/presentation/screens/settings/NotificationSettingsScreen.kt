package com.example.asistentedecocina.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.asistentedecocina.core.notification.NotificationPreferencesRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    preferencesRepository: NotificationPreferencesRepository,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }
    
    val dailyTipsEnabled by preferencesRepository.dailyTipsEnabled.collectAsState(initial = true)
    val recipeRemindersEnabled by preferencesRepository.recipeRemindersEnabled.collectAsState(initial = true)
    val timerNotificationsEnabled by preferencesRepository.timerNotificationsEnabled.collectAsState(initial = true)
    val notificationTime by preferencesRepository.notificationTime.collectAsState(initial = "09:00")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración de Notificaciones") }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Consejos diarios
            NotificationSettingItem(
                title = "Consejos diarios",
                description = "Recibe un consejo culinario cada día",
                icon = Icons.Default.Lightbulb,
                enabled = dailyTipsEnabled,
                onEnabledChange = { enabled ->
                    preferencesRepository.setDailyTipsEnabled(enabled)
                }
            )

            // Recordatorios de recetas
            NotificationSettingItem(
                title = "Recordatorios de recetas",
                description = "Recibe recordatorios de tus recetas favoritas",
                icon = Icons.Default.Restaurant,
                enabled = recipeRemindersEnabled,
                onEnabledChange = { enabled ->
                    preferencesRepository.setRecipeRemindersEnabled(enabled)
                }
            )

            // Notificaciones de temporizador
            NotificationSettingItem(
                title = "Notificaciones de temporizador",
                description = "Recibe notificaciones cuando termine un temporizador",
                icon = Icons.Default.Timer,
                enabled = timerNotificationsEnabled,
                onEnabledChange = { enabled ->
                    preferencesRepository.setTimerNotificationsEnabled(enabled)
                }
            )

            // Hora de notificación
            if (dailyTipsEnabled || recipeRemindersEnabled) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showTimePicker = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Hora de notificación",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Las notificaciones se enviarán a las $notificationTime",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Cambiar hora"
                        )
                    }
                }
            }
        }

        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                onTimeSelected = { hour, minute ->
                    val time = String.format("%02d:%02d", hour, minute)
                    preferencesRepository.setNotificationTime(time)
                    showTimePicker = false
                }
            )
        }
    }
}

@Composable
private fun NotificationSettingItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Switch(
                checked = enabled,
                onCheckedChange = onEnabledChange
            )
        }
    }
}

@Composable
private fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {
    var selectedHour by remember { mutableStateOf(9) }
    var selectedMinute by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Seleccionar hora") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Selector de hora
                    NumberPicker(
                        value = selectedHour,
                        onValueChange = { selectedHour = it },
                        range = 0..23
                    )
                    Text(":")
                    // Selector de minutos
                    NumberPicker(
                        value = selectedMinute,
                        onValueChange = { selectedMinute = it },
                        range = 0..59
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(selectedHour, selectedMinute)
                    onDismissRequest()
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                val newValue = if (value >= range.last) range.first else value + 1
                onValueChange(newValue)
            }
        ) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Aumentar")
        }
        
        Text(
            text = String.format("%02d", value),
            style = MaterialTheme.typography.headlineMedium
        )
        
        IconButton(
            onClick = {
                val newValue = if (value <= range.first) range.last else value - 1
                onValueChange(newValue)
            }
        ) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Disminuir")
        }
    }
} 