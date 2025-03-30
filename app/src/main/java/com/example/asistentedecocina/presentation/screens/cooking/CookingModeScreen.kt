package com.example.asistentedecocina.presentation.screens.cooking

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
import com.example.asistentedecocina.core.model.Recipe
import com.example.asistentedecocina.presentation.components.ChefAnimation
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookingModeScreen(
    recipe: Recipe,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStepIndex by remember { mutableStateOf(0) }
    var timerSeconds by remember { mutableStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var showConfirmExit by remember { mutableStateOf(false) }

    val currentStep = recipe.steps[currentStepIndex]
    val progress = (currentStepIndex + 1).toFloat() / recipe.steps.size

    LaunchedEffect(isTimerRunning, timerSeconds) {
        if (isTimerRunning && timerSeconds > 0) {
            delay(1.seconds)
            timerSeconds--
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe.name) },
                navigationIcon = {
                    IconButton(onClick = { showConfirmExit = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                },
                actions = {
                    Text(
                        text = "${currentStepIndex + 1}/${recipe.steps.size}",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Barra de progreso
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth()
                )

                // Chef animado
                Box(
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ChefAnimation(
                        isThinking = false,
                        isListening = false,
                        modifier = Modifier.fillMaxSize(0.8f)
                    )
                }

                // Instrucción actual
                Surface(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxWidth(),
                    tonalElevation = 2.dp,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentStep.instruction,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )

                        if (currentStep.duration > 0) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (!isTimerRunning && timerSeconds == 0) {
                                    OutlinedButton(
                                        onClick = {
                                            timerSeconds = currentStep.duration * 60
                                            isTimerRunning = true
                                        }
                                    ) {
                                        Icon(Icons.Default.Timer, contentDescription = null)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Iniciar temporizador (${currentStep.duration} min)")
                                    }
                                } else {
                                    Text(
                                        text = formatTime(timerSeconds),
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    IconButton(
                                        onClick = { isTimerRunning = !isTimerRunning }
                                    ) {
                                        Icon(
                                            if (isTimerRunning) Icons.Default.Pause 
                                            else Icons.Default.PlayArrow,
                                            contentDescription = if (isTimerRunning) "Pausar" else "Reanudar"
                                        )
                                    }
                                    IconButton(
                                        onClick = { 
                                            isTimerRunning = false
                                            timerSeconds = 0
                                        }
                                    ) {
                                        Icon(Icons.Default.Stop, contentDescription = "Detener")
                                    }
                                }
                            }
                        }
                    }
                }

                // Controles de navegación
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { if (currentStepIndex > 0) currentStepIndex-- },
                        enabled = currentStepIndex > 0
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Anterior")
                    }

                    Button(
                        onClick = {
                            if (currentStepIndex < recipe.steps.size - 1) {
                                currentStepIndex++
                            } else {
                                onFinish()
                            }
                        }
                    ) {
                        Text(
                            if (currentStepIndex < recipe.steps.size - 1) "Siguiente" 
                            else "Finalizar"
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            if (currentStepIndex < recipe.steps.size - 1) 
                                Icons.Default.ArrowForward
                            else Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
            }

            // Diálogo de confirmación para salir
            if (showConfirmExit) {
                AlertDialog(
                    onDismissRequest = { showConfirmExit = false },
                    title = { Text("¿Salir del modo cocina?") },
                    text = { Text("¿Estás seguro de que deseas salir? Perderás el progreso actual.") },
                    confirmButton = {
                        TextButton(
                            onClick = onFinish
                        ) {
                            Text("Salir")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showConfirmExit = false }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
} 