package com.example.asistentedecocina.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.asistentedecocina.core.animation.AnimationManager
import com.example.asistentedecocina.core.animation.ChefAnimationTransitions
import com.example.asistentedecocina.core.animation.ChefState
import com.example.asistentedecocina.core.model.ChefPreferences
import com.example.asistentedecocina.core.model.ChefStyle

@Composable
fun ChefAnimation(
    isListening: Boolean,
    isThinking: Boolean,
    preferences: ChefPreferences = ChefPreferences(),
    modifier: Modifier = Modifier
) {
    // Estado actual del chef
    var currentState by remember { mutableStateOf(ChefState.IDLE) }
    
    // Actualizar estado basado en props
    LaunchedEffect(isListening, isThinking) {
        currentState = when {
            isListening -> ChefState.LISTENING
            isThinking -> ChefState.THINKING
            else -> ChefState.IDLE
        }
    }

    // Animaciones base
    val transition = updateTransition(currentState, label = "ChefStateTransition")

    // Animación del cuerpo
    val bodyRotation by transition.animateFloat(
        transitionSpec = {
            when {
                targetState == ChefState.LISTENING -> tween(
                    durationMillis = (ChefAnimationTransitions.BASE_DURATION * preferences.expressiveness).toInt(),
                    easing = FastOutSlowInEasing
                )
                targetState == ChefState.THINKING -> spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
                else -> spring()
            }
        },
        label = "BodyRotation"
    ) { state ->
        when (state) {
            ChefState.IDLE -> 0f
            ChefState.LISTENING -> 5f
            ChefState.THINKING -> -5f
            ChefState.SPEAKING -> 2f
            ChefState.COOKING -> 3f
        }
    }

    // Animación del gorro
    val hatOffset by animateFloatAsState(
        targetValue = if (isListening) -10f else 0f,
        animationSpec = ChefAnimationTransitions.hatBounce
    )

    // Animación de los ojos
    val eyesScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = ChefAnimationTransitions.eyesBlink
    )

    // Animación del cuerpo ondulante
    val bodyWave by animateFloatAsState(
        targetValue = 0f,
        animationSpec = ChefAnimationTransitions.bodyWave
    )

    // Escala global basada en las preferencias
    val scale = preferences.size

    Box(
        modifier = modifier
            .fillMaxSize()
            .scale(scale)
    ) {
        // Contenedor principal del chef
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    rotationZ = bodyRotation + bodyWave
                    translationY = when (preferences.style) {
                        ChefStyle.CARTOON -> bodyWave * 2
                        ChefStyle.MODERN -> bodyWave * 0.5f
                        else -> bodyWave
                    }
                }
        ) {
            // Cuerpo del chef
            ChefBody(
                preferences = preferences,
                modifier = Modifier.align(Alignment.Center)
            )

            // Gorro del chef
            ChefHat(
                preferences = preferences,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = hatOffset.dp)
            )

            // Cara del chef
            ChefFace(
                preferences = preferences,
                eyesScale = eyesScale,
                isListening = isListening,
                isThinking = isThinking,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun ChefBody(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    // TODO: Implementar el cuerpo del chef según el estilo seleccionado
}

@Composable
private fun ChefHat(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    // TODO: Implementar el gorro del chef según el estilo seleccionado
}

@Composable
private fun ChefFace(
    preferences: ChefPreferences,
    eyesScale: Float,
    isListening: Boolean,
    isThinking: Boolean,
    modifier: Modifier = Modifier
) {
    // TODO: Implementar la cara del chef según el estilo seleccionado
}

@Composable
fun ChefAnimationWithControls(
    animationManager: AnimationManager,
    modifier: Modifier = Modifier,
    showControls: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChefAnimation(
            animationManager = animationManager,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        if (showControls) {
            Spacer(modifier = Modifier.height(8.dp))
            
            AnimationControls(
                animationManager = animationManager,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AnimationControls(
    animationManager: AnimationManager,
    modifier: Modifier = Modifier
) {
    val animationState by animationManager.animationState.collectAsState()

    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Reutilizamos los controles del AnimationPreview
        PlayPauseButton(
            isPlaying = animationState.isPlaying,
            onToggle = { animationManager.togglePlayback() }
        )

        SpeedControl(
            speed = animationState.speed,
            onSpeedChange = { animationManager.setPlaybackSpeed(it) }
        )

        LoopButton(
            iterations = animationState.iterations,
            onIterationsChange = { animationManager.setIterations(it) }
        )
    }
}

@Composable
private fun PlayPauseButton(
    isPlaying: Boolean,
    onToggle: () -> Unit
) {
    androidx.compose.material3.IconButton(onClick = onToggle) {
        androidx.compose.material3.Icon(
            imageVector = if (isPlaying) {
                androidx.compose.material.icons.Icons.Default.Pause
            } else {
                androidx.compose.material.icons.Icons.Default.PlayArrow
            },
            contentDescription = if (isPlaying) "Pausar" else "Reproducir"
        )
    }
}

@Composable
private fun SpeedControl(
    speed: Float,
    onSpeedChange: (Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        androidx.compose.material3.IconButton(
            onClick = { onSpeedChange(maxOf(0.25f, speed - 0.25f)) },
            enabled = speed > 0.25f
        ) {
            androidx.compose.material3.Icon(
                androidx.compose.material.icons.Icons.Default.Remove,
                "Reducir velocidad"
            )
        }

        androidx.compose.material3.Text(
            text = "${speed}x",
            style = MaterialTheme.typography.bodyMedium
        )

        androidx.compose.material3.IconButton(
            onClick = { onSpeedChange(minOf(3f, speed + 0.25f)) },
            enabled = speed < 3f
        ) {
            androidx.compose.material3.Icon(
                androidx.compose.material.icons.Icons.Default.Add,
                "Aumentar velocidad"
            )
        }
    }
}

@Composable
private fun LoopButton(
    iterations: Int,
    onIterationsChange: (Int) -> Unit
) {
    androidx.compose.material3.IconButton(
        onClick = { onIterationsChange(if (iterations == -1) 1 else -1) }
    ) {
        androidx.compose.material3.Icon(
            imageVector = if (iterations == -1) {
                androidx.compose.material.icons.Icons.Default.Loop
            } else {
                androidx.compose.material.icons.Icons.Default.PlayArrow
            },
            contentDescription = if (iterations == -1) {
                "Reproducción continua"
            } else {
                "Una vez"
            }
        )
    }
} 