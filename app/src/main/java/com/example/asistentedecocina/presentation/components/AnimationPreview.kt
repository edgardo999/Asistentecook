package com.example.asistentedecocina.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.asistentedecocina.core.constants.AnimationConfig

@Composable
fun AnimationPreview(
    animationRes: String,
    title: String,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var speed by remember { mutableStateOf(1f) }
    var showDetails by remember { mutableStateOf(false) }
    var iterations by remember { mutableStateOf(-1) } // -1 para loop infinito
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        speed = speed,
        isPlaying = isPlaying
    )

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título y botón de detalles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = { showDetails = !showDetails }) {
                    Icon(
                        imageVector = if (showDetails) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (showDetails) "Ocultar detalles" else "Mostrar detalles"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Animación
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Controles básicos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Control de reproducción
                IconButton(onClick = { isPlaying = !isPlaying }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pausar" else "Reproducir"
                    )
                }

                // Control de velocidad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { speed = maxOf(0.25f, speed - 0.25f) },
                        enabled = speed > 0.25f
                    ) {
                        Icon(Icons.Default.Remove, "Reducir velocidad")
                    }
                    Text(
                        text = "${speed}x",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(
                        onClick = { speed = minOf(3f, speed + 0.25f) },
                        enabled = speed < 3f
                    ) {
                        Icon(Icons.Default.Add, "Aumentar velocidad")
                    }
                }

                // Control de repetición
                IconButton(
                    onClick = { iterations = if (iterations == -1) 1 else -1 }
                ) {
                    Icon(
                        imageVector = if (iterations == -1) Icons.Default.Loop else Icons.Default.PlayArrow,
                        contentDescription = if (iterations == -1) "Reproducción continua" else "Una vez"
                    )
                }
            }

            // Panel de detalles
            AnimatedVisibility(
                visible = showDetails,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    composition?.let { comp ->
                        DetailRow("Duración", "${comp.duration / 1000f} segundos")
                        DetailRow("Frames", "${comp.frameRate} fps")
                        DetailRow("Dimensiones", "${comp.bounds.width()} x ${comp.bounds.height()}")
                        DetailRow("Ruta", animationRes)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
} 