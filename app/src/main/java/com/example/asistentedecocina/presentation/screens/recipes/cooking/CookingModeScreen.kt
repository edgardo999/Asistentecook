package com.example.asistentedecocina.presentation.screens.recipes.cooking

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.asistentedecocina.data.local.entity.RecipeEntity
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingModeScreen(
    recipe: RecipeEntity,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(0.seconds) }
    val totalSteps = recipe.instructions.size

    LaunchedEffect(isTimerRunning, remainingTime) {
        if (isTimerRunning && remainingTime > 0.seconds) {
            kotlinx.coroutines.delay(1000)
            remainingTime -= 1.seconds
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, "Salir del modo cocina")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Progreso
            LinearProgressIndicator(
                progress = (currentStep + 1).toFloat() / totalSteps,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Paso actual
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Número de paso
                    Text(
                        text = "Paso ${currentStep + 1} de $totalSteps",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Instrucción
                    Text(
                        text = recipe.instructions[currentStep],
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Timer (si está activo)
                    if (remainingTime > 0.seconds) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = formatTime(remainingTime.inWholeSeconds),
                                style = MaterialTheme.typography.displayMedium,
                                color = if (isTimerRunning) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                            
                            IconButton(
                                onClick = { isTimerRunning = !isTimerRunning }
                            ) {
                                Icon(
                                    imageVector = if (isTimerRunning) {
                                        Icons.Default.Pause
                                    } else {
                                        Icons.Default.PlayArrow
                                    },
                                    contentDescription = if (isTimerRunning) "Pausar" else "Continuar",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Controles
            Surface(
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    // Botón anterior
                    OutlinedIconButton(
                        onClick = { if (currentStep > 0) currentStep-- },
                        enabled = currentStep > 0
                    ) {
                        Icon(Icons.Default.ArrowBack, "Paso anterior")
                    }

                    // Botón de timer
                    FilledTonalIconButton(
                        onClick = {
                            remainingTime = 5.minutes
                            isTimerRunning = true
                        }
                    ) {
                        Icon(Icons.Default.Timer, "Agregar temporizador")
                    }

                    // Botón siguiente
                    FilledIconButton(
                        onClick = { 
                            if (currentStep < totalSteps - 1) {
                                currentStep++
                                remainingTime = 0.seconds
                                isTimerRunning = false
                            }
                        },
                        enabled = currentStep < totalSteps - 1
                    ) {
                        Icon(Icons.Default.ArrowForward, "Siguiente paso")
                    }
                }
            }
        }
    }
}

private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
} 