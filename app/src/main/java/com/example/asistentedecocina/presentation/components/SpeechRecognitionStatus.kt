package com.example.asistentedecocina.presentation.components

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
import com.example.asistentedecocina.core.speech.SpeechState

@Composable
fun SpeechRecognitionStatus(
    state: SpeechState,
    onAlternativeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is SpeechState.Idle -> {
                // No mostrar nada en estado inactivo
            }
            is SpeechState.Starting -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Iniciando reconocimiento de voz...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            is SpeechState.Listening -> {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Escuchando",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Escuchando...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            is SpeechState.ProcessingInput -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Procesando...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            is SpeechState.PartialResult -> {
                Text(
                    text = state.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            is SpeechState.Success -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = state.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    if (state.alternatives.isNotEmpty()) {
                        Text(
                            text = "¿Quisiste decir?",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            state.alternatives.forEach { alternative ->
                                SuggestionChip(
                                    onClick = { onAlternativeSelected(alternative) },
                                    label = { Text(alternative) }
                                )
                            }
                        }
                    }
                }
            }
            is SpeechState.Error -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
} 