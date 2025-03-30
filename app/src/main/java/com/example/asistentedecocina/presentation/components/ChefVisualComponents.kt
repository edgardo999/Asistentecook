package com.example.asistentedecocina.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.asistentedecocina.core.model.ChefPreferences
import com.example.asistentedecocina.core.model.ChefStyle

@Composable
fun ChefBody(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    when (preferences.style) {
        ChefStyle.CLASSIC -> ClassicChefBody(preferences, modifier)
        ChefStyle.MODERN -> ModernChefBody(preferences, modifier)
        ChefStyle.CARTOON -> CartoonChefBody(preferences, modifier)
        ChefStyle.PIXEL -> PixelChefBody(preferences, modifier)
        ChefStyle.WATERCOLOR -> WatercolorChefBody(preferences, modifier)
    }
}

@Composable
private fun ClassicChefBody(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(200.dp)
    ) {
        // Cuerpo principal
        drawRoundRect(
            color = preferences.primaryColor,
            size = Size(size.width, size.height * 0.7f),
            cornerRadius = CornerRadius(20f, 20f),
            topLeft = Offset(0f, size.height * 0.3f)
        )

        // Delantal
        drawRoundRect(
            color = preferences.apronColor,
            size = Size(size.width * 0.6f, size.height * 0.5f),
            cornerRadius = CornerRadius(15f, 15f),
            topLeft = Offset(size.width * 0.2f, size.height * 0.4f)
        )

        // Detalles del delantal
        drawLine(
            color = preferences.secondaryColor,
            start = Offset(size.width * 0.4f, size.height * 0.4f),
            end = Offset(size.width * 0.4f, size.height * 0.9f),
            strokeWidth = 5f
        )
        drawLine(
            color = preferences.secondaryColor,
            start = Offset(size.width * 0.6f, size.height * 0.4f),
            end = Offset(size.width * 0.6f, size.height * 0.9f),
            strokeWidth = 5f
        )
    }
}

@Composable
private fun ModernChefBody(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(200.dp)
    ) {
        // Cuerpo minimalista
        drawRect(
            color = preferences.primaryColor,
            size = Size(size.width * 0.8f, size.height * 0.6f),
            topLeft = Offset(size.width * 0.1f, size.height * 0.4f)
        )

        // Delantal geométrico
        val path = Path().apply {
            moveTo(size.width * 0.3f, size.height * 0.4f)
            lineTo(size.width * 0.7f, size.height * 0.4f)
            lineTo(size.width * 0.6f, size.height)
            lineTo(size.width * 0.4f, size.height)
            close()
        }
        drawPath(
            path = path,
            color = preferences.apronColor
        )
    }
}

@Composable
private fun CartoonChefBody(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(200.dp)
    ) {
        // Cuerpo redondeado
        drawCircle(
            color = preferences.primaryColor,
            radius = size.width * 0.3f,
            center = Offset(size.width * 0.5f, size.height * 0.6f)
        )

        // Delantal divertido
        val path = Path().apply {
            moveTo(size.width * 0.3f, size.height * 0.4f)
            quadraticBezierTo(
                size.width * 0.5f, size.height * 0.3f,
                size.width * 0.7f, size.height * 0.4f
            )
            lineTo(size.width * 0.8f, size.height * 0.9f)
            quadraticBezierTo(
                size.width * 0.5f, size.height,
                size.width * 0.2f, size.height * 0.9f
            )
            close()
        }
        drawPath(
            path = path,
            color = preferences.apronColor
        )

        // Botones divertidos
        for (i in 0..2) {
            drawCircle(
                color = preferences.secondaryColor,
                radius = 10f,
                center = Offset(
                    size.width * 0.5f,
                    size.height * (0.5f + i * 0.15f)
                )
            )
        }
    }
}

@Composable
private fun PixelChefBody(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(200.dp)
    ) {
        val pixelSize = size.width / 16f

        // Dibujar cuerpo pixelado
        for (x in 4..11) {
            for (y in 6..15) {
                drawRect(
                    color = if (y < 10) preferences.primaryColor else preferences.apronColor,
                    topLeft = Offset(x * pixelSize, y * pixelSize),
                    size = Size(pixelSize, pixelSize)
                )
            }
        }

        // Detalles pixelados
        for (y in 10..14) {
            drawRect(
                color = preferences.secondaryColor,
                topLeft = Offset(7 * pixelSize, y * pixelSize),
                size = Size(pixelSize, pixelSize)
            )
            drawRect(
                color = preferences.secondaryColor,
                topLeft = Offset(9 * pixelSize, y * pixelSize),
                size = Size(pixelSize, pixelSize)
            )
        }
    }
}

@Composable
private fun WatercolorChefBody(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(200.dp)
    ) {
        // Efecto acuarela para el cuerpo
        for (i in 0..5) {
            drawCircle(
                color = preferences.primaryColor.copy(alpha = 0.2f),
                radius = size.width * (0.3f - i * 0.02f),
                center = Offset(
                    size.width * (0.5f + (i * 0.02f)),
                    size.height * (0.6f + (i * 0.02f))
                )
            )
        }

        // Efecto acuarela para el delantal
        val path = Path().apply {
            moveTo(size.width * 0.3f, size.height * 0.4f)
            cubicTo(
                size.width * 0.4f, size.height * 0.3f,
                size.width * 0.6f, size.height * 0.3f,
                size.width * 0.7f, size.height * 0.4f
            )
            lineTo(size.width * 0.8f, size.height * 0.9f)
            cubicTo(
                size.width * 0.7f, size.height,
                size.width * 0.3f, size.height,
                size.width * 0.2f, size.height * 0.9f
            )
            close()
        }
        
        for (i in 0..3) {
            drawPath(
                path = path,
                color = preferences.apronColor.copy(alpha = 0.3f),
                style = Stroke(
                    width = (8 - i * 2).toFloat(),
                    pathEffect = PathEffect.cornerPathEffect(20f)
                )
            )
        }
    }
}

@Composable
fun ChefHat(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    when (preferences.style) {
        ChefStyle.CLASSIC -> ClassicChefHat(preferences, modifier)
        ChefStyle.MODERN -> ModernChefHat(preferences, modifier)
        ChefStyle.CARTOON -> CartoonChefHat(preferences, modifier)
        ChefStyle.PIXEL -> PixelChefHat(preferences, modifier)
        ChefStyle.WATERCOLOR -> WatercolorChefHat(preferences, modifier)
    }
}

@Composable
private fun ClassicChefHat(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(120.dp, 100.dp)
    ) {
        // Base del gorro
        drawRoundRect(
            color = preferences.hatColor,
            size = Size(size.width, size.height * 0.3f),
            cornerRadius = CornerRadius(10f, 10f),
            topLeft = Offset(0f, size.height * 0.7f)
        )

        // Parte superior del gorro
        drawOval(
            color = preferences.hatColor,
            size = Size(size.width * 0.8f, size.height * 0.8f),
            topLeft = Offset(size.width * 0.1f, 0f)
        )
    }
}

@Composable
private fun ModernChefHat(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(120.dp, 100.dp)
    ) {
        // Gorro minimalista
        drawRect(
            color = preferences.hatColor,
            size = Size(size.width * 0.8f, size.height * 0.2f),
            topLeft = Offset(size.width * 0.1f, size.height * 0.8f)
        )

        drawRect(
            color = preferences.hatColor,
            size = Size(size.width * 0.6f, size.height * 0.7f),
            topLeft = Offset(size.width * 0.2f, size.height * 0.1f)
        )
    }
}

@Composable
private fun CartoonChefHat(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(120.dp, 100.dp)
    ) {
        // Gorro divertido
        val path = Path().apply {
            moveTo(size.width * 0.1f, size.height * 0.8f)
            quadraticBezierTo(
                size.width * 0.5f, size.height * 0.2f,
                size.width * 0.9f, size.height * 0.8f
            )
            close()
        }
        drawPath(
            path = path,
            color = preferences.hatColor
        )

        // Detalles
        drawCircle(
            color = preferences.secondaryColor,
            radius = 10f,
            center = Offset(size.width * 0.5f, size.height * 0.4f)
        )
    }
}

@Composable
private fun PixelChefHat(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(120.dp, 100.dp)
    ) {
        val pixelSize = size.width / 12f

        // Dibujar gorro pixelado
        for (x in 2..9) {
            for (y in 1..8) {
                if ((x in 4..7 && y in 1..6) || y in 7..8) {
                    drawRect(
                        color = preferences.hatColor,
                        topLeft = Offset(x * pixelSize, y * pixelSize),
                        size = Size(pixelSize, pixelSize)
                    )
                }
            }
        }
    }
}

@Composable
private fun WatercolorChefHat(
    preferences: ChefPreferences,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(120.dp, 100.dp)
    ) {
        // Efecto acuarela para el gorro
        for (i in 0..4) {
            drawOval(
                color = preferences.hatColor.copy(alpha = 0.25f),
                size = Size(
                    size.width * (0.8f - i * 0.05f),
                    size.height * (0.7f - i * 0.05f)
                ),
                topLeft = Offset(
                    size.width * (0.1f + i * 0.025f),
                    size.height * (0.2f + i * 0.025f)
                )
            )
        }
    }
}

@Composable
fun ChefFace(
    preferences: ChefPreferences,
    eyesScale: Float,
    isListening: Boolean,
    isThinking: Boolean,
    modifier: Modifier = Modifier
) {
    when (preferences.style) {
        ChefStyle.CLASSIC -> ClassicChefFace(preferences, eyesScale, isListening, isThinking, modifier)
        ChefStyle.MODERN -> ModernChefFace(preferences, eyesScale, isListening, isThinking, modifier)
        ChefStyle.CARTOON -> CartoonChefFace(preferences, eyesScale, isListening, isThinking, modifier)
        ChefStyle.PIXEL -> PixelChefFace(preferences, eyesScale, isListening, isThinking, modifier)
        ChefStyle.WATERCOLOR -> WatercolorChefFace(preferences, eyesScale, isListening, isThinking, modifier)
    }
}

@Composable
private fun ClassicChefFace(
    preferences: ChefPreferences,
    eyesScale: Float,
    isListening: Boolean,
    isThinking: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(100.dp)
    ) {
        // Ojos
        val eyeSize = size.width * 0.15f * eyesScale
        drawCircle(
            color = preferences.primaryColor,
            radius = eyeSize,
            center = Offset(size.width * 0.35f, size.height * 0.4f)
        )
        drawCircle(
            color = preferences.primaryColor,
            radius = eyeSize,
            center = Offset(size.width * 0.65f, size.height * 0.4f)
        )

        // Boca
        val mouthPath = Path().apply {
            if (isListening) {
                // Boca abierta circular
                addOval(
                    Rect(
                        left = size.width * 0.4f,
                        top = size.height * 0.6f,
                        right = size.width * 0.6f,
                        bottom = size.height * 0.7f
                    )
                )
            } else if (isThinking) {
                // Boca pensativa
                moveTo(size.width * 0.3f, size.height * 0.65f)
                quadraticBezierTo(
                    size.width * 0.5f, size.height * 0.7f,
                    size.width * 0.7f, size.height * 0.65f
                )
            } else {
                // Boca sonriente
                moveTo(size.width * 0.3f, size.height * 0.6f)
                quadraticBezierTo(
                    size.width * 0.5f, size.height * 0.7f,
                    size.width * 0.7f, size.height * 0.6f
                )
            }
        }
        drawPath(
            path = mouthPath,
            color = preferences.primaryColor,
            style = if (isListening) Fill else Stroke(width = 5f)
        )
    }
}

@Composable
private fun ModernChefFace(
    preferences: ChefPreferences,
    eyesScale: Float,
    isListening: Boolean,
    isThinking: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(100.dp)
    ) {
        // Ojos minimalistas
        val eyeWidth = size.width * 0.15f * eyesScale
        val eyeHeight = size.height * 0.08f * eyesScale
        
        drawRect(
            color = preferences.primaryColor,
            size = Size(eyeWidth, eyeHeight),
            topLeft = Offset(size.width * 0.3f, size.height * 0.4f)
        )
        drawRect(
            color = preferences.primaryColor,
            size = Size(eyeWidth, eyeHeight),
            topLeft = Offset(size.width * 0.6f, size.height * 0.4f)
        )

        // Boca geométrica
        if (isListening) {
            drawRect(
                color = preferences.primaryColor,
                size = Size(size.width * 0.3f, size.height * 0.1f),
                topLeft = Offset(size.width * 0.35f, size.height * 0.6f)
            )
        } else if (isThinking) {
            drawLine(
                color = preferences.primaryColor,
                start = Offset(size.width * 0.35f, size.height * 0.65f),
                end = Offset(size.width * 0.65f, size.height * 0.65f),
                strokeWidth = 5f
            )
        } else {
            drawRect(
                color = preferences.primaryColor,
                size = Size(size.width * 0.3f, size.height * 0.05f),
                topLeft = Offset(size.width * 0.35f, size.height * 0.6f)
            )
        }
    }
}

@Composable
private fun CartoonChefFace(
    preferences: ChefPreferences,
    eyesScale: Float,
    isListening: Boolean,
    isThinking: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(100.dp)
    ) {
        // Ojos grandes y expresivos
        val eyeSize = size.width * 0.2f * eyesScale
        drawCircle(
            color = preferences.primaryColor,
            radius = eyeSize,
            center = Offset(size.width * 0.35f, size.height * 0.4f)
        )
        drawCircle(
            color = preferences.primaryColor,
            radius = eyeSize,
            center = Offset(size.width * 0.65f, size.height * 0.4f)
        )

        // Brillos en los ojos
        drawCircle(
            color = Color.White,
            radius = eyeSize * 0.3f,
            center = Offset(size.width * 0.32f, size.height * 0.37f)
        )
        drawCircle(
            color = Color.White,
            radius = eyeSize * 0.3f,
            center = Offset(size.width * 0.62f, size.height * 0.37f)
        )

        // Boca expresiva
        val mouthPath = Path().apply {
            if (isListening) {
                // Boca muy abierta
                addOval(
                    Rect(
                        left = size.width * 0.35f,
                        top = size.height * 0.55f,
                        right = size.width * 0.65f,
                        bottom = size.height * 0.75f
                    )
                )
            } else if (isThinking) {
                // Boca en forma de '3'
                moveTo(size.width * 0.35f, size.height * 0.6f)
                cubicTo(
                    size.width * 0.4f, size.height * 0.65f,
                    size.width * 0.5f, size.height * 0.6f,
                    size.width * 0.65f, size.height * 0.65f
                )
            } else {
                // Sonrisa exagerada
                moveTo(size.width * 0.25f, size.height * 0.55f)
                quadraticBezierTo(
                    size.width * 0.5f, size.height * 0.8f,
                    size.width * 0.75f, size.height * 0.55f
                )
            }
        }
        drawPath(
            path = mouthPath,
            color = preferences.primaryColor,
            style = if (isListening) Fill else Stroke(width = 8f)
        )
    }
}

@Composable
private fun PixelChefFace(
    preferences: ChefPreferences,
    eyesScale: Float,
    isListening: Boolean,
    isThinking: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(100.dp)
    ) {
        val pixelSize = size.width / 10f

        // Ojos pixelados
        val eyePixels = if (eyesScale > 0.5f) 2 else 1
        for (i in 0 until eyePixels) {
            drawRect(
                color = preferences.primaryColor,
                topLeft = Offset(3 * pixelSize, (4 + i) * pixelSize),
                size = Size(pixelSize, pixelSize)
            )
            drawRect(
                color = preferences.primaryColor,
                topLeft = Offset(6 * pixelSize, (4 + i) * pixelSize),
                size = Size(pixelSize, pixelSize)
            )
        }

        // Boca pixelada
        if (isListening) {
            // Boca abierta
            for (x in 3..6) {
                for (y in 6..7) {
                    drawRect(
                        color = preferences.primaryColor,
                        topLeft = Offset(x * pixelSize, y * pixelSize),
                        size = Size(pixelSize, pixelSize)
                    )
                }
            }
        } else if (isThinking) {
            // Boca pensativa
            for (x in 3..6) {
                drawRect(
                    color = preferences.primaryColor,
                    topLeft = Offset(x * pixelSize, 7 * pixelSize),
                    size = Size(pixelSize, pixelSize)
                )
            }
        } else {
            // Sonrisa
            drawRect(
                color = preferences.primaryColor,
                topLeft = Offset(3 * pixelSize, 6 * pixelSize),
                size = Size(4 * pixelSize, pixelSize)
            )
        }
    }
}

@Composable
private fun WatercolorChefFace(
    preferences: ChefPreferences,
    eyesScale: Float,
    isListening: Boolean,
    isThinking: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(100.dp)
    ) {
        // Ojos con efecto acuarela
        val eyeSize = size.width * 0.15f * eyesScale
        for (i in 0..3) {
            drawCircle(
                color = preferences.primaryColor.copy(alpha = 0.3f),
                radius = eyeSize - (i * 2),
                center = Offset(
                    size.width * (0.35f + i * 0.01f),
                    size.height * (0.4f + i * 0.01f)
                )
            )
            drawCircle(
                color = preferences.primaryColor.copy(alpha = 0.3f),
                radius = eyeSize - (i * 2),
                center = Offset(
                    size.width * (0.65f + i * 0.01f),
                    size.height * (0.4f + i * 0.01f)
                )
            )
        }

        // Boca con efecto acuarela
        val mouthPath = Path().apply {
            if (isListening) {
                addOval(
                    Rect(
                        left = size.width * 0.4f,
                        top = size.height * 0.6f,
                        right = size.width * 0.6f,
                        bottom = size.height * 0.7f
                    )
                )
            } else if (isThinking) {
                moveTo(size.width * 0.3f, size.height * 0.65f)
                quadraticBezierTo(
                    size.width * 0.5f, size.height * 0.7f,
                    size.width * 0.7f, size.height * 0.65f
                )
            } else {
                moveTo(size.width * 0.3f, size.height * 0.6f)
                quadraticBezierTo(
                    size.width * 0.5f, size.height * 0.7f,
                    size.width * 0.7f, size.height * 0.6f
                )
            }
        }

        for (i in 0..3) {
            drawPath(
                path = mouthPath,
                color = preferences.primaryColor.copy(alpha = 0.25f),
                style = Stroke(
                    width = (8 - i * 2).toFloat(),
                    pathEffect = PathEffect.cornerPathEffect(10f)
                )
            )
        }
    }
} 