package com.example.asistentedecocina.core.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

object ChefAnimationTransitions {
    // Duración base para las animaciones
    const val BASE_DURATION = 300

    // Curvas de animación personalizadas
    val bounceEasing = SpringSpec<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    val smoothEasing = SpringSpec<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )

    // Transición para cambios de estado
    @OptIn(ExperimentalAnimationApi::class)
    fun getStateTransition(
        expressiveness: Float = 1.0f
    ): AnimatedContentTransitionScope<ChefState>.() -> ContentTransform = {
        when (targetState) {
            ChefState.IDLE -> fadeIn() + slideIn(
                initialOffset = { IntOffset(0, -50) }
            ) with fadeOut() + slideOut(
                targetOffset = { IntOffset(0, 50) }
            )
            
            ChefState.LISTENING -> fadeIn() + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(
                    durationMillis = (BASE_DURATION * expressiveness).toInt(),
                    easing = FastOutSlowInEasing
                )
            ) with fadeOut() + scaleOut(
                targetScale = 1.2f
            )
            
            ChefState.THINKING -> fadeIn() + slideIn(
                initialOffset = { IntOffset(50, 0) },
                animationSpec = tween(
                    durationMillis = (BASE_DURATION * expressiveness).toInt(),
                    easing = FastOutSlowInEasing
                )
            ) with fadeOut() + slideOut(
                targetOffset = { IntOffset(-50, 0) }
            )
            
            ChefState.SPEAKING -> fadeIn() + scaleIn(
                initialScale = 1.2f,
                animationSpec = bounceEasing
            ) with fadeOut() + scaleOut(
                targetScale = 0.8f
            )
            
            ChefState.COOKING -> fadeIn() + slideIn(
                initialOffset = { IntOffset(0, 50) },
                animationSpec = smoothEasing
            ) with fadeOut() + slideOut(
                targetOffset = { IntOffset(0, -50) }
            )
        }
    }

    // Transiciones para elementos específicos
    val hatBounce = infiniteRepeatable(
        animation = keyframes {
            durationMillis = (1000 * 1.5f).toInt()
            0f at 0
            -10f at 500
            0f at 1000
        },
        repeatMode = RepeatMode.Restart
    )

    val bodyWave = infiniteRepeatable(
        animation = keyframes {
            durationMillis = 2000
            0f at 0
            5f at 500
            0f at 1000
            -5f at 1500
            0f at 2000
        },
        repeatMode = RepeatMode.Reverse
    )

    val eyesBlink = infiniteRepeatable(
        animation = keyframes {
            durationMillis = 3000
            1f at 0
            1f at 2700
            0.2f at 2800
            1f at 2900
        },
        repeatMode = RepeatMode.Restart
    )

    // Efectos de entrada/salida
    @Composable
    fun EnterTransition(content: @Composable AnimatedVisibilityScope.() -> Unit) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            content = content
        )
    }

    // Efectos de énfasis
    val emphasisEffect = infiniteRepeatable(
        animation = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        repeatMode = RepeatMode.Reverse
    )

    // Efectos de reacción
    val reactionEffect = keyframes {
        durationMillis = 500
        1f at 0
        1.2f at 250
        1f at 500
    }
} 